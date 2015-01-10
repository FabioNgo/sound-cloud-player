package ngo.music.soundcloudplayer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Stack;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.QueueSongAdapter;
import ngo.music.soundcloudplayer.boundary.LoginActivity;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.OnlineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.general.MusicPlayerBroadcastReceiver;
import ngo.music.soundcloudplayer.general.States;
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
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.util.Log;
import android.widget.RemoteViews;

public class MusicPlayerService extends Service implements OnErrorListener,
		OnCompletionListener, OnSeekCompleteListener, OnInfoListener,
		OnBufferingUpdateListener, Constants.MusicService,
		Constants.Appplication {
	public MusicPlayerService() {
		instance = this;

	}

	private int loopState = 0;
	private int explorecategory = -1;
	private boolean isShuffle = false;
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

	public static MusicPlayerService getInstance() {
		if (instance == null) {
			instance = new MusicPlayerService();
		}

		return instance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		if (instance == null) {
			instance = this;
		}

		super.onCreate();
		States.musicPlayerState = MUSIC_STOPPED;
		try {
			// get song queue, last song play
			getData();
		} catch (Exception e) {
			Log.w("getData", e.toString());

		} finally {
			iniMediaPlayer();

			try {
				updateNotification();
			} catch (Exception e) {

			}

		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		super.onStartCommand(intent, flags, startId);
		UIController.getInstance().updateUI(APP_RUNNING);
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
			return (Song) songQueue.get(currentSongPosition);
		}

	}

	public ArrayList<Song> getQueue() {
		return songQueue;
	}

	public void addSongToQueue(Song song) {
		for (Song mSong : songQueue) {
			if (mSong.getId().equals(song.getId())) {
				return;
			}
		}
		songQueue.add(song);
		UIController.getInstance().updateUI(QUEUE_CHANGED);
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
			mediaPlayer.reset();

		}
		/**
		 * get song played last time
		 */
		String link = "";
		try {
			link = getCurrentSong().getLink();
			mediaPlayer.setDataSource(link);
			mediaPlayer.prepare();

		} catch (Exception e) {

		}
	}

	/**
	 * Play next Song when click to next button or at the end of current song
	 */
	public void playNextSong() {
		mediaPlayer.stop();
		stackSongplayed.push(nextSongId);
		for (int i = 0; i < songQueue.size(); i++) {
			if (songQueue.get(i).getId().equals(nextSongId)) {
				currentSongPosition = i;
				break;
			}
		}

		playNewSong(false);
		computeNextSong();
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
		}else{
			for (Song song : songQueue) {
				if(song.getId().equals(stackSongplayed.peek())){
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
		UIController.getInstance().updateUI(MUSIC_PLAYING);
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
		if (States.musicPlayerState != MUSIC_STOPPED) {
			mp.reset();
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
		UIController.getInstance().updateUI(MUSIC_PAUSE);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// TODO Auto-generated method stub
		UIController.getInstance().updateUI(MUSIC_CUR_POINT_CHANGED);
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
	public void playNewSong(int position, ArrayList<Song> queue) {
		// incase of the queue in put is songQueue itself
		if (!this.songQueue.equals(queue)) {
			this.songQueue.clear();
			songQueue.addAll(queue);
		}
		currentSongPosition = position;
		playNewSong(true);

	}

	public void playNewExploreSong(int position, int category,
			ArrayList<Song> listSongs) {
		explorecategory = category;
		playNewSong(position, listSongs);

	}

	public void updateQueue(int category) {

		if (category == explorecategory) {
			this.songQueue.clear();
			songQueue.addAll(SongController.getInstance().getOnlineSongs(
					explorecategory));

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
		if (startNow) {

			States.musicPlayerState = MUSIC_PLAYING;
		}

		UIController.getInstance().updateUI(MUSIC_NEW_SONG);
		if (States.musicPlayerState == MUSIC_PLAYING) {
			Song song = getCurrentSong();
			new playNewSongBackground().execute(song);

		}
		updateNotification();

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
	public int getCurrentTime() {
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
		if (mediaPlayer != null) {
			int a = mediaPlayer.getDuration();
			return a;
		}
		return -1;
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
	private void updateNotification() throws NullPointerException {

		Song song = getCurrentSong();
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
		bigView.setTextViewText(R.id.noti_title, song.getTitle());
		bigView.setTextViewText(R.id.noti_content, song.getArtist());

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
		if (song instanceof OnlineSong) {
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
		smallView.setTextViewText(R.id.noti_title, song.getTitle());
		smallView.setTextViewText(R.id.noti_content, song.getArtist());
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
		if (song instanceof OnlineSong) {
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
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = builder.build();
		notification.flags |= NotificationCompat.FLAG_ONGOING_EVENT;
		notification.flags |= NotificationCompat.FLAG_FOREGROUND_SERVICE;
		notification.bigContentView = bigView;
		notificationManager.notify(NOTIFICATION_ID, notification);
		startForeground(NOTIFICATION_ID, notification);
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

			Song song = songQueue.get(currentSongPosition);
			String id = song.getId();
			return id;
		} catch (Exception e) {
			Log.e("getCurrentSongID", e.toString());
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
		UIController.getInstance().updateUI(-1);

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
		loopState = loopState % 3;
		UIController.getInstance().updateUI(-1);
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

	private class playNewSongBackground extends AsyncTask<Song, String, String> {

		Song song;

		@Override
		protected String doInBackground(Song... params) {
			// TODO Auto-generated method stub
			String link = "";
			song = params[0];
			try {

				link = song.getLink();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("getLink", e.toString());
			}

			return link;
		}

		@Override
		protected void onPostExecute(String link) {
			// TODO Auto-generated method stub
			try {

				mediaPlayer.reset();

				mediaPlayer.setDataSource(link);
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.prepare();
				playMedia();
				if (song instanceof OfflineSong) {
					SongController.getInstance().storePlayingSong();
				}

			} catch (Exception e) {
				Log.e("playsong", e.toString());

			}
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
		UIController.getInstance().updateUI(MUSIC_STOPPED);

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
		if (isShuffle) {
			// TODO Auto-generated method stub
			Random random = new Random(System.currentTimeMillis());

			size = songQueue.size();

			nextPosition = currentSongPosition
					+ (Math.abs(random.nextInt()) % size) - 1;
			nextPosition = nextPosition % size;

		} else {
			nextPosition = currentSongPosition + 1;

			nextPosition = nextPosition % size;

		}
		nextSongId = songQueue.get(nextPosition).getId();
	}

	public void removeFromQueue(Song song) {
		// TODO Auto-generated method stub
		for (int i = 0; i < songQueue.size(); i++) {
			if (songQueue.get(i).getId().equals(song.getId())) {
				if (nextSongId.equals(song.getId())) {
					computeNextSong();
				}
				songQueue.remove(i);
				break;
			}
		}
		computeNextSong();
		UIController.getInstance().updateUI(QUEUE_CHANGED);
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
		UIController.getInstance().updateUI(QUEUE_CHANGED);
		
	}
}
