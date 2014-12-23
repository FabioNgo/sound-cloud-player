package ngo.music.soundcloudplayer.service;

import java.io.IOException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.controller.MusicPlayerController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class MusicPlayerService extends IntentService implements
		OnPreparedListener, OnErrorListener, OnCompletionListener,
		OnSeekCompleteListener, OnInfoListener, OnBufferingUpdateListener,
		Constants.MusicService {
	public MusicPlayerService() {
		super("MusicPlayerService");
		// TODO Auto-generated constructor stub
		instance = this;
		iniMediaPlayer();
	}

	private final IBinder mBinder = new MusicPlayerServiceBinder();
	public MediaPlayer mediaPlayer = null;
	private static final int NOTIFICATION_ID = 1;
	private Song currentSong = null;

	public Song getCurrentSong() {
		return currentSong;
	}

	private SongController songController;

	private Notification notification;
	private static MusicPlayerService instance;

	public static MusicPlayerService getInstance() {
		if (instance == null) {
			new MusicPlayerService();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		iniMediaPlayer();
		iniNotification();
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
	}

	private void iniNotification() {
		// TODO Auto-generated method stub
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager) getSystemService(ns);
		notification = new Notification();

		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, notificationIntent, 0);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				getApplicationContext());
		notification = builder.setContentIntent(contentIntent)
				.setSmallIcon(R.drawable.ic_media_play).setTicker("playing")
				.setWhen(System.currentTimeMillis()).setAutoCancel(true)
				.build();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		startForeground(NOTIFICATION_ID, notification);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
		}
		// cancelNotification();
	}

	private void cancelNotification() {
		// TODO Auto-generated method stub
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManagerCompat notificationManagerCompat = (NotificationManagerCompat) getSystemService(ns);
		notificationManagerCompat.cancel(NOTIFICATION_ID);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		playMedia();

	}

	private void playMedia() {
		// TODO Auto-generated method stub
		MusicPlayerController.getInstance().updateUI(MUSIC_START);
		mediaPlayer.start();
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
		pauseMedia();
	}

	private void pauseMedia() {
		// TODO Auto-generated method stub
		if (mediaPlayer.isPlaying()) {

			mediaPlayer.pause();
			MusicPlayerController.getInstance().updateUI(MUSIC_PAUSE);
		}
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

	}

	public class MusicPlayerServiceBinder extends Binder {
		public MusicPlayerService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return MusicPlayerService.this;
		}
	}

	public String getSongID() {
		return currentSong.getId();
	}

	public void playCurrentSong() {
		if (currentSong == null) {
			songController = SongController.getInstance();
			currentSong = songController.getSongs().get(0);
			playNewSong(currentSong);
		} else {
			playMedia();
		}
		// sendBroadcast(TAG_START);
	}

	public void playNewSong(Song song) {
		currentSong = song;

		try {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.reset();
			}
			mediaPlayer.setDataSource(song.getLink());
			// mediaPlayer.prepareAsync();
			mediaPlayer.prepare();

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

		// Builder builder = new Builder(MainActivity.getActivity());
		// notification = builder.setContentTitle(currentSong.getTitle())
		// .setContentText(currentSong.getLink()).build();

	}

	public void pause() {
		pauseMedia();
	}

	public int getCurrentTime() {
		if (mediaPlayer != null) {
			return mediaPlayer.getCurrentPosition();
		}
		return -1;
	}

	public long getDuration() {
		if (mediaPlayer != null) {
			return mediaPlayer.getDuration();
		}
		return -1;
	}

	public boolean isPlaying() {
		boolean a = mediaPlayer.isPlaying();
		return mediaPlayer.isPlaying();
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub

	}

}
