package ngo.music.soundcloudplayer.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Stack;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.LoginActivity;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.controller.OfflineSongController;
import ngo.music.soundcloudplayer.controller.UpdateUiFromServiceController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.general.MusicPlayerBroadcastReceiver;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Binder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.text.style.BulletSpan;
import android.util.Log;

public class MusicPlayerService extends IntentService implements
		OnPreparedListener, OnErrorListener, OnCompletionListener,
		OnSeekCompleteListener, OnInfoListener, OnBufferingUpdateListener,
		Constants.MusicService {
	public MusicPlayerService() {
		super("MusicPlayerService");
		// TODO Auto-generated constructor stub
		instance = this;

	}

	// public MusicPlayerService(String name) {
	// super(name);
	// // TODO Auto-generated constructor stub
	// }
	private int loopState = 0;
	private boolean isShuffle = false;
	private int musicState;
	private ArrayList<String> songsPlaying;
	private Stack<String> stackSongplayed;
	public MediaPlayer mediaPlayer = null;
	private static final int NOTIFICATION_ID = 1;
	private String currentSongId = "";
	int timeLastStop = -1; // when the music stopped before

	public Song getCurrentSong() {

		Song curSong = null;
		if (currentSongId.equals("")) {
			currentSongId = stackSongplayed.peek();
		}
		try {
			curSong = OfflineSongController.getInstance().getSongbyId(
					currentSongId);
			return curSong;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;

		}

	}

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

			iniMediaPlayer();
			iniNotification();
			updateNotification(false, R.drawable.ic_media_pause);
			UpdateUiFromServiceController.getInstance().updateUI(APP_START);

		}
		super.onStartCommand(intent, flags, startId);

		return START_STICKY;
	}

	public ArrayList<String> getSongsPlaying() {
		return songsPlaying;
	}

	public void setSongsPlaying(ArrayList<String> songsPlaying) {
		this.songsPlaying = songsPlaying;
	}

	private void iniMediaPlayer() {
		// TODO Auto-generated method stub
		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnErrorListener(this);
			mediaPlayer.setOnSeekCompleteListener(this);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnInfoListener(this);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.reset();

		}
		
			String link;
			
			try {
				link = OfflineSongController.getInstance().getSongbyId(currentSongId).getLink();
				mediaPlayer.setDataSource(link);
				mediaPlayer.prepareAsync();    
				
				                             
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// fplayNewSong(id);
		
	}

	public void playNextSong() {
		int currentSongPosition = getCurrentPosition(getCurrentSongId());
		stackSongplayed.push(getCurrentSongId());
		if (isShuffle) {
			// TODO Auto-generated method stub
			Random random = new Random(Calendar.getInstance().getTimeInMillis());

			int size = songsPlaying.size();

			currentSongPosition = currentSongPosition
					+ (Math.abs(random.nextInt()) % size) + 1;
			currentSongPosition = currentSongPosition % size;

		} else {
			currentSongPosition++;
			int size = songsPlaying.size();
			currentSongPosition = currentSongPosition % size;

		}
		playNewSong(songsPlaying.get(currentSongPosition));

	}

	public void playPreviousSong() {
		// TODO Auto-generated method stub
		int currentSongPosition = getCurrentPosition(stackSongplayed.peek());
		stackSongplayed.pop();

		if (stackSongplayed.isEmpty()) {

			int size = songsPlaying.size();
			currentSongPosition = currentSongPosition + size - 1;
			currentSongPosition = currentSongPosition % size;
			stackSongplayed.push(songsPlaying.get(currentSongPosition));
		} else {

			currentSongPosition = getCurrentPosition(stackSongplayed.peek());
		}
		playNewSong(songsPlaying.get(currentSongPosition));

	}

	private int getCurrentPosition(String input) {
		// TODO Auto-generated method stub
		for (int i = 0; i < songsPlaying.size(); i++)
			if (songsPlaying.get(i).equals(input)) {
				return i;
			}
		return -1;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		if (musicState != APP_START) {
			mp.start();
			UpdateUiFromServiceController.getInstance().updateUI(MUSIC_START);
		}
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
		if(musicState == APP_START){
			mediaPlayer.seekTo(timeLastStop);
			playMedia();
		}
		else {
			musicState = MUSIC_START;

			playNewSong(stackSongplayed.peek());
		}
		// sendBroadcast(TAG_START);
	}

	public void playNewSong(String songId, boolean startNow) {
		if (startNow) {
			musicState = MUSIC_START;
			stackSongplayed.push(songId);
		}
		currentSongId = songId;
		UpdateUiFromServiceController.getInstance().updateUI(MUSIC_NEW_SONG);
		updateNotification(false, R.drawable.ic_media_play);
		if (musicState == MUSIC_START) {
			try {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.reset();
				}
				String link = OfflineSongController.getInstance()
						.getSongbyId(songId).getLink();
				mediaPlayer.setDataSource(link);

				mediaPlayer.prepareAsync();
			} catch ( Exception e){
				Log.e("PlayNewSong",e.getMessage());
			}
		}
		// Builder builder = new Builder(MainActivity.getActivity());
		// notification = builder.setContentTitle(currentSong.getTitle())
		// .setContentText(currentSong.getLink()).build();

	}

	public void playNewSong(String id) {

		playNewSong(id, false);

	}

	public void pause() {

		pauseMedia();
		updateNotification(false, R.drawable.ic_media_pause);

	}

	public int getCurrentTime() {
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

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		if ("dismiss".equals(intent.getAction())) {
			BasicFunctions.makeToastTake("asd", this);
			instance.dismissService();
			stopService(intent);

		}
	}

	private void dismissService() {
		// TODO Auto-generated method stub
		UpdateUiFromServiceController.getInstance().updateUI(MUSIC_PAUSE);
		mediaPlayer.release();
		MainActivity.getActivity().finish();
		stopSelf();
	}

	private void updateNotification(boolean cancel, int iconID) {
		if (cancel) {
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.cancelAll();
			return;
		}
		Intent switchIntent = new Intent(this,
				MusicPlayerBroadcastReceiver.class);
		switchIntent.setAction("dismiss");
		PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 0,
				switchIntent, 0);

		Action cancelAction = new Action(R.drawable.ic_action_github, "cancel",
				pendingSwitchIntent);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this);
		builder.setContentTitle(getCurrentSong().getTitle());
		builder.setContentText(getCurrentSong().getArtist());
		// builder.setNumber(count);
		builder.setSmallIcon(iconID);
		builder.addAction(cancelAction);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// notification.setLatestEventInfo(getApplicationContext(), "hello",
		// "hello", contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, builder.build());

	}

	private void iniNotification() {

		Intent switchIntent = new Intent(this,
				MusicPlayerBroadcastReceiver.class);
		switchIntent.setAction("dismiss");
		PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 0,
				switchIntent, 0);

		Action cancelAction = new Action(R.drawable.ic_action_github, "cancel",
				pendingSwitchIntent);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				getApplicationContext());
		builder.setContentTitle(getCurrentSong().getTitle());
		builder.setContentText(getCurrentSong().getArtist());
		// builder.setNumber(count);
		builder.setSmallIcon(R.drawable.ic_media_pause);
		builder.addAction(cancelAction);
		Intent notificationIntent = new Intent(this, LoginActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		builder.setContentIntent(contentIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// notification.setLatestEventInfo(getApplicationContext(), "hello",
		// "hello", contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, builder.build());
	}

	public void startPause() {
		// TODO Auto-generated method stub

		if (musicState != MUSIC_START) {

			playCurrentSong();
		} else {
			pause();
		}

	}

	private void getData() {
		ArrayList<Song> songs = null;
		try {
			songs = OfflineSongController.getInstance().getSongs();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		songsPlaying = new ArrayList<String>();
		for (Song song : songs) {
			songsPlaying.add(song.getId());
		}
		Stack<Object[]> temp = OfflineSongController.getInstance()
				.getSongsPlayed();
		stackSongplayed = new Stack<String>();
		while (!temp.isEmpty()) {
			Object[] temp1 = temp.pop();
			stackSongplayed.push((String) temp1[0]);
			int temp2 = ((Integer) temp1[1]).intValue();
			if (temp2 != 0) {
				timeLastStop = temp2;
			}
		}
		if (stackSongplayed.isEmpty()) {
			stackSongplayed.add(songsPlaying.get(0));
		}
		currentSongId = stackSongplayed.peek();
	}

	public ArrayList<String> getSongs() {
		// TODO Auto-generated method stub
		return songsPlaying;
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
		return getCurrentSong().getId();
	}

	public void restartSong() {
		mediaPlayer.reset();
		playNewSong(currentSongId);
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

	public Stack<String> getStackSongs() {
		// TODO Auto-generated method stub
		return stackSongplayed;
	}

	public int getTimeLastStop() {
		return timeLastStop;
	}

}
