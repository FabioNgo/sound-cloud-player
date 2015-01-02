package ngo.music.soundcloudplayer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Stack;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.controller.OfflineSongController;
import ngo.music.soundcloudplayer.controller.UpdateUiFromServiceController;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.OnlineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.general.MusicPlayerBroadcastReceiver;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.app.NotificationCompat.Style;
import android.util.Log;

public class MusicPlayerService extends Service implements OnErrorListener,
		OnCompletionListener, OnSeekCompleteListener, OnInfoListener,
		OnBufferingUpdateListener, Constants.MusicService {
	public MusicPlayerService() {
		instance = this;

	}

	// public MusicPlayerService(String name) {
	// super(name);
	// // TODO Auto-generated constructor stub
	// }
	private int loopState = 0;
	private boolean isShuffle = false;
	private int musicState;
	private ArrayList<Song> songQueue;
	private Stack<Integer> stackSongplayed;
	public MediaPlayer mediaPlayer = null;
	private static final int NOTIFICATION_ID = 1;
	private String currentSongId = "";
	private int currentSongPosition;
	int timeLastStop = -1; // when the music stopped before
	Action[] actions;

	private int seekForwardTime = 5 * 1000;
	private int seekBackwardTime = 5 * 1000;
	private static MusicPlayerService instance;

	public static MusicPlayerService getInstance() {
		if (instance == null) {
			instance = new MusicPlayerService();
		}

		return instance;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (instance == null) {
			instance = this;
		}
		musicState = APP_START;
		try {
			getData();

		} catch (Exception e) {
			Log.w("getData", e.getMessage());
			// currentSongPosition = 0;

		} finally {
			stackSongplayed = new Stack<Integer>();
			iniMediaPlayer();
			updateNotification(false, R.drawable.ic_media_pause);
			UpdateUiFromServiceController.getInstance().updateUI(APP_START);

		}
		super.onStartCommand(intent, flags, startId);

		return START_STICKY;
	}

	public Song getCurrentSong() {

		return songQueue.get(currentSongPosition);

	}

	public ArrayList<Song> getQueue() {
		return songQueue;
	}

	public void addSongToQueue(Song song) {
		songQueue.add(song);
	}

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

		String link;

		try {
			link = getCurrentSong().getLink();
			mediaPlayer.setDataSource(link);
			mediaPlayer.prepare();

		} catch (Exception e) {
			Log.e("iniMedia", e.toString());
			try {
				Log.e("iniMedia", e.getMessage());
			} catch (Exception e1) {

			}
		}
	}

	public void playNextSong() {

		stackSongplayed.push(currentSongPosition);
		if (isShuffle) {
			// TODO Auto-generated method stub
			Random random = new Random(Calendar.getInstance().getTimeInMillis());

			int size = songQueue.size();

			currentSongPosition = currentSongPosition
					+ (Math.abs(random.nextInt()) % size) + 1;
			currentSongPosition = currentSongPosition % size;

		} else {
			currentSongPosition++;
			int size = songQueue.size();
			currentSongPosition = currentSongPosition % size;

		}
		playNewSong(false);

	}

	public void playPreviousSong() {
		// TODO Auto-generated method stub

		stackSongplayed.pop();

		if (stackSongplayed.isEmpty()) {

			int size = songQueue.size();
			currentSongPosition = currentSongPosition + size - 1;
			currentSongPosition = currentSongPosition % size;
			stackSongplayed.push(currentSongPosition);
		}
		playNewSong(false);

	}

	private void playMedia() {
		// TODO Auto-generated method stub

		mediaPlayer.start();
		musicState = MUSIC_START;
		UpdateUiFromServiceController.getInstance().updateUI(MUSIC_START);
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
		if (musicState != APP_START) {
			mp.reset();
			if (loopState == MODE_LOOP_ONE) {
				restartSong();
			} else {
				playNextSong();
			}

			updateNotification(false, R.drawable.ic_media_play);
		}
	}

	private void pauseMedia() {
		// TODO Auto-generated method stub
		if (mediaPlayer.isPlaying()) {

			mediaPlayer.pause();

		}
		musicState = MUSIC_PAUSE;
		UpdateUiFromServiceController.getInstance().updateUI(MUSIC_PAUSE);
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
		UpdateUiFromServiceController.getInstance().updateUI(
				MUSIC_CUR_POINT_CHANGED);
	}

	public class MusicPlayerServiceBinder extends Binder {
		public MusicPlayerService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return MusicPlayerService.this;
		}
	}

	public void playCurrentSong() {
		updateNotification(false, R.drawable.ic_media_play);
		if (musicState == MUSIC_PAUSE) {
			playMedia();

		}
		if (musicState == APP_START) {
			mediaPlayer.seekTo(timeLastStop);
			playMedia();
		} else {
			musicState = MUSIC_START;
			currentSongPosition = stackSongplayed.peek();
			playNewSong(false);
		}
		// sendBroadcast(TAG_START);
	}

	public void playNewOfflineSong(int position, ArrayList<OfflineSong> queue) {
		this.songQueue.clear();
		songQueue.addAll(queue);
		currentSongPosition = position;
		playNewSong(true);

	}
	
	public void playNewOnlineSong(int position, ArrayList<OnlineSong> queue) {
		this.songQueue.clear();
		songQueue.addAll(queue);
		currentSongPosition = position;
		playNewSong(true);

	}

	private void playNewSong(boolean startNow) {
		if (startNow) {

			musicState = MUSIC_START;
		}

		UpdateUiFromServiceController.getInstance().updateUI(MUSIC_NEW_SONG);
		updateNotification(false, R.drawable.ic_media_play);
		if (musicState == MUSIC_START) {
			new playNewSongBackground().execute();
//			try {
//
//				mediaPlayer.reset();
//				String link = songQueue.get(currentSongPosition).getLink();
//
//				mediaPlayer.setDataSource(link);
//				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//				mediaPlayer.prepare();
//				playMedia();
//
//			} catch (Exception e) {
//				Log.e("iniMedia", e.toString());
//				try {
//					Log.e("iniMedia", e.getMessage());
//				} catch (Exception e1) {
//
//				}
//			}

		}
		// Builder builder = new Builder(MainActivity.getActivity());
		// notification = builder.setContentTitle(currentSong.getTitle())
		// .setContentText(currentSong.getLink()).build();

	}

	public void pause() {

		pauseMedia();
		updateNotification(false, R.drawable.ic_media_pause);

	}

	public int getCurrentTime() {
		if (musicState == APP_START) {
			return timeLastStop;
		}
		if (mediaPlayer != null) {
			return mediaPlayer.getCurrentPosition();
		}
		return -1;
	}

	public long getDuration() {
		if (mediaPlayer != null) {
			int a = mediaPlayer.getDuration();
			return a;
		}
		return -1;
	}

	public boolean isPlaying() {
		if (musicState == MUSIC_START) {
			return true;
		}
		return false;
	}

	private void updateNotification(boolean cancel, int iconID) {

		Intent intent = new Intent(getApplicationContext(),
				MusicPlayerService.class);
		intent.setAction(NOTI_ACTION_CANCEL);
		PendingIntent pendingIntent = PendingIntent.getService(
				getApplicationContext(), 1, intent, 0);
		Style style = new Style() {
		};
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this).setSmallIcon(iconID)
				.setContentTitle(getCurrentSong().getTitle())
				.setContentText(getCurrentSong().getArtist())
				.setDeleteIntent(pendingIntent).setStyle(style);

		builder.addAction(createAction(R.drawable.ic_media_previous,
				NOTI_ACTION_PREV));
		builder.addAction(createAction(R.drawable.play_button,
				NOTI_ACTION_PLAY_PAUSE));
		builder.addAction(createAction(R.drawable.ic_media_next,
				NOTI_ACTION_NEXT));

		// style.
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, MainActivity.class);

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

		notificationManager.notify(NOTIFICATION_ID, notification);
		startForeground(NOTIFICATION_ID, notification);
	}

	public void startPause() {
		// TODO Auto-generated method stub

		if (musicState != MUSIC_START) {

			playCurrentSong();
			// playSampleSong();
		} else {
			pause();
		}

	}

	private void getData() {
		currentSongPosition = -1;
		songQueue = new ArrayList<Song>();

		ArrayList<Object[]> songsPLayed = OfflineSongController.getInstance()
				.getSongsPlayed();

		for (int i = 0; i < songsPLayed.size(); i++) {

			songQueue.add((Song) songsPLayed.get(i)[0]);
			int time = ((Integer) songsPLayed.get(i)[1]).intValue();
			if (time != 0) {
				timeLastStop = time;
				currentSongPosition = i;
			}
		}
		if(songQueue.size()==0){
			try {
				songQueue.add(OfflineSongController.getInstance().getSongs().get(0));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (currentSongPosition == -1) {
			currentSongPosition = 0;
		}

	}

	public ArrayList<Song> getSongs() {
		// TODO Auto-generated method stub
		return songQueue;
	}

	public void forwardSong() {
		if (mediaPlayer != null) {
			if (musicState != MUSIC_START) {
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

	public void rewindSong() {
		if (mediaPlayer != null) {
			if (musicState != MUSIC_START) {
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
		return songQueue.get(currentSongPosition).getId();
	}

	public void restartSong() {
		mediaPlayer.reset();
		playNewSong(false);
	}

	public void setShuffle() {
		// TODO Auto-generated method stub
		isShuffle = !isShuffle;
		UpdateUiFromServiceController.getInstance().updateUI(-1);

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
		UpdateUiFromServiceController.getInstance().updateUI(-1);
	}

	public void cancelNoti() {
		updateNotification(true, -1);
	}

	public Stack<String> getStackSongIds() {
		// TODO Auto-generated method stub
		Stack<String> ids = new Stack<String>();
		for (Integer position : stackSongplayed) {
			ids.add(songQueue.get(position).getId());
		}
		return ids;
	}

	private Action createAction(int iconID, String action) {
		Intent switchIntent = new Intent(this,
				MusicPlayerBroadcastReceiver.class);
		switchIntent.setAction(action);
		PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 0,
				switchIntent, 0);

		Action result = new Action(iconID, "", pendingSwitchIntent);
		return result;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class playNewSongBackground extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {

				mediaPlayer.reset();
				String link = songQueue.get(currentSongPosition).getLink();

				mediaPlayer.setDataSource(link);
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.prepare();
				playMedia();

			} catch (Exception e) {
				Log.e("iniMedia", e.toString());
				try {
					Log.e("iniMedia", e.getMessage());
				} catch (Exception e1) {

				}
			}
			return null;
		}

		
		
	}
}
