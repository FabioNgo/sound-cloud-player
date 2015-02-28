package ngo.music.soundcloudplayer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Stack;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.QueueSongAdapter;
import ngo.music.soundcloudplayer.api.Stream;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.SCSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.general.MusicPlayerBroadcastReceiver;
import ngo.music.soundcloudplayer.general.States;
import ngo.music.soundcloudplayer.imageloader.ImageLoader;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MusicPlayerService extends Service implements OnErrorListener,
		OnCompletionListener, OnSeekCompleteListener, OnInfoListener,
		OnBufferingUpdateListener, Constants.MusicService,
		Constants.Categories, Constants.Appplication, Constants.Data,
		OnPreparedListener {
	public MusicPlayerService() {
		instance = this;

	}

	public static boolean isLoaded;
	private int loopState = 0;
	public int explorecategory = -1;
	private boolean isShuffle = true;
	private ArrayList<Song> songQueue = new ArrayList<Song>();
	private Stack<String> stackSongplayed;
	public MediaPlayer mediaPlayer = null;
	private static final int NOTIFICATION_ID = 1;
	private int currentSongPosition;
	int timeLastStop = -1; // when the music stopped before
	private int seekForwardTime = 5 * 1000;
	private int seekBackwardTime = 5 * 1000;
	private static MusicPlayerService instance;
	private String nextSongId = ""; // assign specific song played next
	int percent = -1;

	public static MusicPlayerService getInstance() {
		if (instance == null) {
			instance = new MusicPlayerService();
		}

		return instance;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		States.musicPlayerState = MUSIC_STOPPED;
		isLoaded = false;
		try {
			// get song queue, last song play
			getData();
		} catch (Exception e) {
			// Log.w("getData", e.toString());

		} finally {
			iniMediaPlayer();

			updateNotification();

		}

		super.onStartCommand(intent, flags, startId);
		isLoaded = true;
		UIController.getInstance().updateUiAppChanged(APP_RUNNING);
		return START_STICKY;
	}

	/**
	 * Get current song is playing
	 * 
	 * @return
	 */
	public Song getCurrentSong() {

		if (songQueue == null) {
			return null;
		} else if (songQueue.isEmpty()) {
			return null;
		} else {
			try {
				return (Song) songQueue.get(currentSongPosition);
			} catch (Exception e) {
				return null;
			}
		}

	}

	/**
	 * Get next song
	 * 
	 * @return
	 */
	public Song getNextSong() {

		if (nextSongId.equals("") || loopState == MODE_LOOP_ONE) {
			return getCurrentSong();
		} else {
			return SongController.getInstance().getSong(nextSongId);
		}

	}

	public ArrayList<Song> getQueue() {
		if (songQueue == null) {
			return new ArrayList<Song>();
		} else {
			return songQueue;
		}

	}

	public void addSongToQueue(Song song) {
		for (Song mSong : songQueue) {
			if (mSong.getId().equals(song.getId())) {
				return;
			}
		}
		songQueue.add(song);
		UIController.getInstance().updateUiWhenDataChanged(QUEUE_CHANGED);
	}

	public void addSongsToQueue(ArrayList<Song> songs) {
		for (Song iSong : songs) {

			for (Song mSong : songQueue) {
				if (mSong.getId().equals(iSong.getId())) {
					return;
				}
			}
			songQueue.add(iSong);
		}

		UIController.getInstance().updateUiWhenDataChanged(QUEUE_CHANGED);
	}

	/**
	 * Initialize media player object
	 */
	private void iniMediaPlayer() {
		// TODO Auto-generated method stub
		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setOnErrorListener(this);
			mediaPlayer.setOnSeekCompleteListener(this);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnInfoListener(this);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.reset();
			try {
				mediaPlayer.setDataSource(getCurrentSong().getLink());
				mediaPlayer.prepareAsync();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * Play next Song when click to next button or at the end of current song
	 */
	public void playNextSong() {

		if (!nextSongId.equals("")) {
			stackSongplayed.push(nextSongId);
			for (int i = 0; i < songQueue.size(); i++) {
				if (songQueue.get(i).getId().equals(nextSongId)) {
					currentSongPosition = i;
					break;
				}
			}

			playNewSong(false);
			computeNextSong();
		} else {
			currentSongPosition = -1;
		}
	}

	/**
	 * Back to preivious Song when click to prev button
	 */
	public void playPreviousSong() {
		// TODO Auto-generated method stub

		stackSongplayed.pop();

		if (stackSongplayed.isEmpty()) {

			int size = songQueue.size();
			currentSongPosition = currentSongPosition + size - 1;
			currentSongPosition = currentSongPosition % size;
			stackSongplayed.push(getCurrentSongId());
			playNewSong(false);
			return;
		} else {
			for (Song song : songQueue) {
				if (song.getId().equals(stackSongplayed.peek())) {
					currentSongPosition = songQueue.indexOf(song);
					playNewSong(false);
					return;
				}
			}
			playPreviousSong();
		}

	}

	/**
	 * start Media player.
	 * 
	 */
	private void playMedia() {
		// TODO Auto-generated method stub

		mediaPlayer.start();
		States.musicPlayerState = MUSIC_PLAYING;
		UIController.getInstance().updateUiWhilePlayingMusic(MUSIC_PLAYING);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
			BasicFunctions.makeToastTake(
					"MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK",
					getApplicationContext());
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			BasicFunctions.makeToastTake("MEDIA_ERROR_SERVER_DIED" + extra,
					getApplicationContext());
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			BasicFunctions.makeToastTake("MEDIA_ERROR_UNKNOWN" + extra,
					getApplicationContext());
		}
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub

		UIController.getInstance().updateUiWhilePlayingMusic(MUSIC_STOPPED);
		if (States.musicPlayerState != MUSIC_STOPPED) {

			if (loopState == MODE_LOOP_ONE) {
				restartSong();
			} else {
				playNextSong();
			}

			updateNotification();
		}
	}

	/**
	 * Pause . When click to pause button
	 */
	private void pauseMedia() {
		// TODO Auto-generated method stub
		if (mediaPlayer.isPlaying()) {

			mediaPlayer.pause();

		}
		States.musicPlayerState = MUSIC_PAUSE;
		UIController.getInstance().updateUiWhilePlayingMusic(MUSIC_PAUSE);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		// this.percent = percent;
		// //BasicFunctions.makeToastTake(""+percent,
		// MusicPlayerService.getInstance());
		// if(mp.getDuration()*percent/100<mp.getCurrentPosition()+5000){
		// pause();
		// }else{
		// playMedia();
		// }
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// TODO Auto-generated method stub
		UIController.getInstance().updateUiWhilePlayingMusic(
				MUSIC_CUR_POINT_CHANGED);
	}

	public class MusicPlayerServiceBinder extends Binder {
		public MusicPlayerService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return MusicPlayerService.this;
		}
	}

	/**
	 * Play the current song when press start button
	 */
	public void playCurrentSong() {

		if (States.musicPlayerState == MUSIC_PAUSE) {
			playMedia();

		} else if (States.musicPlayerState == MUSIC_STOPPED) {
			mediaPlayer.seekTo(timeLastStop);
			playMedia();

		} else {

			playNewSong(true);

		}
		updateNotification();
		return;
		// sendBroadcast(TAG_START);
	}

	/**
	 * Play song from queue
	 * 
	 * @param position
	 *            : position in queue
	 */
	public void playSongInQueue(int position) {
		currentSongPosition = position;
		playNewSong(true);
	}

	/**
	 * Play a list of online song
	 * 
	 * @param position
	 *            : position of first song in the list
	 * @param queue
	 *            : list of the songs want to play
	 */
	public void playNewSong(int position, int category, ArrayList<Song> queue) {
		// incase of the queue in put is songQueue itself
		if (category != -1) {
			explorecategory = category;
		}
		if (category != QUEUE) {
			if (!this.songQueue.equals(queue)) {
				this.songQueue.clear();
				songQueue.addAll(queue);
			}
		}
		currentSongPosition = position;
		isShuffle = true;
		playNewSong(true);
		computeNextSong();
		UIController.getInstance().updateUiWhenDataChanged(QUEUE_CHANGED);
	}

	public void updateQueue(int category) {

		if (category == explorecategory) {
			ArrayList<Song> onlineSongs = SongController.getInstance()
					.getOnlineSongs(explorecategory);
			addSongsToQueue(onlineSongs);

		}
	}

	/**
	 * Play new song
	 * 
	 * @param startNow
	 *            : true if want to play immediately, False: play when user
	 *            press start
	 */
	private void playNewSong(boolean startNow) {
		mediaPlayer.stop();
		// System.out.println("PLAY NEW SONG 2");
		Song song = getCurrentSong();

		if (song == null) {
			BasicFunctions.makeToastTake("No song to play",
					getApplicationContext());
			return;
		}
		// System.out.println ("PLAY NEW SONG = " + song.getId());
		if (startNow) {

			States.musicPlayerState = MUSIC_PLAYING;
		}
		updateNotification();
		UIController.getInstance().updateUiWhilePlayingMusic(MUSIC_NEW_SONG);
		if (States.musicPlayerState == MUSIC_PLAYING) {

			if (song instanceof SCSong) {
				// String link = ((OnlineSong) song).getLink();

				new playNewSongBackground().execute((SCSong) song);

			} else {
				playSong(song, ((OfflineSong) song).getLink());

			}

		}

	}

	/**
	 * pause media. Update notification when pause
	 */
	public void pause() {

		pauseMedia();
		updateNotification();

	}

	/**
	 * get current time of playing song
	 * 
	 * @return current time in milisecond
	 */
	public long getCurrentTime() {
		if (States.musicPlayerState == MUSIC_STOPPED) {
			return timeLastStop;
		}
		if (mediaPlayer != null) {
			return mediaPlayer.getCurrentPosition();
		}
		return -1;
	}

	/**
	 * Get Duration of playingSong
	 * 
	 * @return
	 */
	public long getDuration() {
		Song song = getCurrentSong();
		if (song != null) {
			return getCurrentSong().getDuration();
		} else {
			return 0;
		}
	}

	/**
	 * Check if media is playing or not
	 * 
	 * @return
	 */
	public boolean isPlaying() {
		if (States.musicPlayerState == MUSIC_PLAYING) {
			return true;
		}
		return false;
	}

	/**
	 * Update notification
	 * 
	 * @param cancel
	 *            : singal to cancel notification or not
	 * @param iconID
	 *            : the id of icon
	 */
	private void updateNotification() {
		// System.out.println("NOTIFICATION");
		String title = "";
		String subTitle = "";
		Song song = getCurrentSong();
		if (song != null) {
			title = song.getTitle();
			subTitle = song.getArtist();
		}
		/**
		 * Big view
		 */
		RemoteViews bigView = new RemoteViews(getPackageName(),
				R.layout.big_noti_layout);

		bigView.setOnClickPendingIntent(R.id.noti_play_pause,
				createPendingIntent(NOTI_ACTION_PLAY_PAUSE));
		bigView.setOnClickPendingIntent(R.id.noti_prev,
				createPendingIntent(NOTI_ACTION_PREV));
		bigView.setOnClickPendingIntent(R.id.noti_next,
				createPendingIntent(NOTI_ACTION_NEXT));
		bigView.setOnClickPendingIntent(R.id.noti_cancel,
				createPendingIntent(NOTI_ACTION_CANCEL));

		bigView.setTextViewText(R.id.noti_title, title);
		bigView.setTextViewText(R.id.noti_content, subTitle);

		if (States.musicPlayerState == MUSIC_PLAYING) {
			bigView.setImageViewResource(R.id.noti_play_pause,
					R.drawable.ic_media_pause);
		} else {
			bigView.setImageViewResource(R.id.noti_play_pause,
					R.drawable.ic_media_play);
		}
		if (song instanceof OfflineSong) {
			bigView.setImageViewResource(R.id.noti_icon, R.drawable.ic_launcher);
		}
		if (song instanceof SCSong) {
			bigView.setImageViewUri(R.id.noti_icon,
					Uri.parse(song.getArtworkUrl()));
		}
		/**
		 * Small View
		 */
		RemoteViews smallView = new RemoteViews(getPackageName(),
				R.layout.small_noti_layout);
		smallView.setOnClickPendingIntent(R.id.noti_play_pause,
				createPendingIntent(NOTI_ACTION_PLAY_PAUSE));
		smallView.setOnClickPendingIntent(R.id.noti_cancel,
				createPendingIntent(NOTI_ACTION_CANCEL));
		smallView.setTextViewText(R.id.noti_title, title);
		smallView.setTextViewText(R.id.noti_content, subTitle);
		if (States.musicPlayerState == MUSIC_PLAYING) {
			smallView.setImageViewResource(R.id.noti_play_pause,
					R.drawable.ic_media_pause);
		} else {
			smallView.setImageViewResource(R.id.noti_play_pause,
					R.drawable.ic_media_play);
		}
		if (song instanceof OfflineSong) {
			smallView.setImageViewResource(R.id.noti_icon,
					R.drawable.ic_launcher);
		}

		if (song instanceof SCSong) {
			// ImageLoader imageLoader = new ImageLoader(this);
			// imageLoader.DisplayImage(url, loader, imageView);

			smallView.setImageViewUri(R.id.noti_icon,
					Uri.parse(song.getArtworkUrl()));
		}
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContent(smallView);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, MusicPlayerMainActivity.class);
		resultIntent.setAction("CallFromNoti");

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		// TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// // Adds the back stack for the Intent (but not the Intent itself)
		// stackBuilder.addParentStack(MainActivity.class);
		// // Adds the Intent that starts the Activity to the top of the stack
		// stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, resultIntent, 0);
		builder.setContentIntent(resultPendingIntent);
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
			builder.setVisibility(Notification.VISIBILITY_PUBLIC);
		}
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = builder.build();
		notification.flags |= NotificationCompat.FLAG_ONGOING_EVENT;
		notification.flags |= NotificationCompat.FLAG_FOREGROUND_SERVICE;
		notification.bigContentView = bigView;
		notificationManager.notify(NOTIFICATION_ID, notification);
		startForeground(NOTIFICATION_ID, notification);

		// System.out.println("END NOTIFICATION");
	}

	private PendingIntent createPendingIntent(String action) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, MusicPlayerBroadcastReceiver.class);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, intent, 0);
		return pendingIntent;
	}

	/**
	 * function when click to start/pause button
	 */
	public void startPause() {
		// TODO Auto-generated method stub

		if (States.musicPlayerState != MUSIC_PLAYING) {
			playCurrentSong();
		} else {
			pause();
		}

	}

	private void getData() {
		currentSongPosition = -1;
		songQueue = new ArrayList<Song>();
		stackSongplayed = new Stack<String>();
		ArrayList<Object[]> songsPLayed = SongController.getInstance()
				.getSongsPlayed();

		for (int i = 0; i < songsPLayed.size(); i++) {

			songQueue.add((Song) songsPLayed.get(i)[0]);
			int time = ((Integer) songsPLayed.get(i)[1]).intValue();
			if (time != 0) {
				timeLastStop = time;
				currentSongPosition = i;
			}
		}
		/**
		 * If there is no song in queue, add the first song in the device to the
		 * queue
		 */
		if (songQueue.size() == 0) {
			ArrayList<Song> songs = SongController.getInstance()
					.getOfflineSongs(true);
			if (songs.size() != 0) {
				songQueue.add(songs.get(0));

				currentSongPosition = 0;

			}

		} else {
			if (currentSongPosition == -1) {
				currentSongPosition = 0;
			}
		}

		stackSongplayed.push(getCurrentSongId());
		computeNextSong();
	}

	/**
	 * Fast forward of playing song
	 */
	public void forwardSong() {
		if (mediaPlayer != null) {
			if (States.musicPlayerState != MUSIC_PLAYING) {
				playCurrentSong();
			}
			int currentPosition = mediaPlayer.getCurrentPosition();
			if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
				mediaPlayer.seekTo(currentPosition + seekForwardTime);

			} else {
				playNextSong();
			}
		}
	}

	/**
	 * rewind of playing song
	 */
	public void rewindSong() {
		if (mediaPlayer != null) {
			if (States.musicPlayerState != MUSIC_PLAYING) {
				playCurrentSong();
			}
			int currentPosition = mediaPlayer.getCurrentPosition();
			if (currentPosition - seekBackwardTime >= 0) {
				mediaPlayer.seekTo(currentPosition - seekBackwardTime);

			} else {
				playPreviousSong();
			}
		}
	}

	public String getCurrentSongId() {
		try {

			Song song = getCurrentSong();
			String id = song.getId();
			return id;
		} catch (Exception e) {
			// Log.e("getCurrentSongID", e.toString());
			return "";
		}

	}

	public void restartSong() {
		mediaPlayer.reset();
		playNewSong(false);
	}

	/**
	 * set playing suffle when play a list
	 */
	public void setShuffle() {
		// TODO Auto-generated method stub
		isShuffle = !isShuffle;
		UIController.getInstance().updateUiWhilePlayingMusic(-1);

	}

	public boolean isShuffle() {
		return isShuffle;
	}

	/**
	 * @return the loopState
	 */
	public int getLoopState() {
		return loopState;
	}

	/**
	 * change Loop State
	 */
	public void changeLoopState() {
		loopState++;
		loopState = loopState % 2;
		UIController.getInstance().updateUiWhilePlayingMusic(-1);
	}

	/**
	 * Remove notification
	 */
	public void cancelNoti() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
	}

	public Stack<String> getStackSongIds() {
		// TODO Auto-generated method stub
		return stackSongplayed;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class playNewSongBackground extends
			AsyncTask<SCSong, String, String> {

		SCSong onlineSong;

		@Override
		protected String doInBackground(SCSong... params) {
			// TODO Auto-generated method stub
			// System.out.println ("PLAY NEW SONG BG");
			String link = "";
			onlineSong = params[0];
			try {
				// System.out.println ("ONLINE SONG GET LINK = " +
				// onlineSong.getId());
				link = onlineSong.getLink();
			} catch (Exception e) {
				e.printStackTrace();
				link = null;
			}

			return link;
		}

		@Override
		protected void onPostExecute(String link) {
			// TODO Auto-generated method stub
			if (link == null) {
				playNextSong();
				Toast.makeText(getApplicationContext(),
						"Cannot play this song", Toast.LENGTH_LONG).show();
				return;
			} else {
				playSong(onlineSong, link);
				updateNotification();
			}

		}

	}

	/**
	 * @param link
	 *            A part of playNewSong function. Called after get link of song
	 */
	private void playSong(Song song, String link) {

		try {

			mediaPlayer.reset();

			mediaPlayer.setDataSource(link);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.prepareAsync();

			if (song instanceof OfflineSong) {
				SongController.getInstance().storePlayingSong();
			}

		} catch (Exception e) {
			// Log.e("playsong", e.toString());
			// stopSelf();
			iniMediaPlayer();
			playNextSong();

		}
	}

	public boolean queueChanged() {
		// TODO Auto-generated method stub
		return false;
	}

	public class LocalBinder extends Binder {
		public MusicPlayerService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return MusicPlayerService.this;
		}
	}

	public void release() {
		// TODO Auto-generated method stub
		States.musicPlayerState = MUSIC_STOPPED;
		UIController.getInstance().updateUiWhilePlayingMusic(MUSIC_STOPPED);

		cancelNoti();
		mediaPlayer.stop();
		mediaPlayer.release();
		MusicPlayerMainActivity.getActivity().finish();

		stopSelf();
	}

	/**
	 * Set song be the next song to be played
	 * 
	 * @param song
	 */
	public void addToNext(Song song) {
		// TODO Auto-generated method stub
		addSongToQueue(song);
		nextSongId = song.getId();

	}

	private void computeNextSong() {
		int nextPosition;
		int size = songQueue.size();
		if (size > 2) {
			if (isShuffle) {
				// TODO Auto-generated method stub
				Random random = new Random(System.currentTimeMillis());

				size = songQueue.size();

				nextPosition = currentSongPosition
						+ (Math.abs(random.nextInt()) % (size - 2)) + 1;
				nextPosition = nextPosition % size;

			} else {
				nextPosition = currentSongPosition + 1;

				nextPosition = nextPosition % size;

			}

			nextSongId = songQueue.get(nextPosition).getId();
		} else {
			System.out.println("COMPUT NEXT SONG: " + size);
			if (size == 2) {
				nextSongId = songQueue.get((currentSongPosition + 1) % 2)
						.getId();
			}
			if (size == 1) {
				nextSongId = getCurrentSongId();
			}
			if (size == 0) {
				nextSongId = "";
			}
		}
	}

	public void removeFromQueue(Song song, boolean forceStop) {
		// TODO Auto-generated method stub
		if (forceStop) {
			if (song.getId().equals(getCurrentSongId())) {
				playNextSong();
			}

		}
		for (int i = 0; i < songQueue.size(); i++) {
			if (songQueue.get(i).getId().equals(song.getId())) {
				if (nextSongId.equals(song.getId())) {
					computeNextSong();
				}
				songQueue.remove(i);
				if (i < currentSongPosition) {
					currentSongPosition--;
				}
				break;
			}
		}

		UIController.getInstance().updateUiWhenDataChanged(QUEUE_CHANGED);
	}

	public int getQueueSize() {
		// TODO Auto-generated method stub
		return songQueue.size();
	}

	public void clearQueue() {
		// TODO Auto-generated method stub
		Song song = songQueue.get(currentSongPosition);
		songQueue.clear();
		songQueue.add(song);
		currentSongPosition = 0;
		computeNextSong();
		UIController.getInstance().updateUiWhenDataChanged(QUEUE_CHANGED);

	}

	public int getPercent() {
		return percent;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		if (States.musicPlayerState == MUSIC_PLAYING) {
			playMedia();
		}
	}
}
