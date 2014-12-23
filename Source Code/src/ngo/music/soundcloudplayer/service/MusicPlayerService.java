package ngo.music.soundcloudplayer.service;

import java.io.IOException;
import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.UpdateUiFromServiceController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import android.app.IntentService;
import android.app.Notification;
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
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.facebook.LoginActivity;

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

	private ArrayList<Song> songsPlaying;
	private final IBinder mBinder = new MusicPlayerServiceBinder();
	public MediaPlayer mediaPlayer = null;
	private static final int NOTIFICATION_ID = 1;
	private int currentSongPosition = -1;

	public Song getCurrentSong() {
		return songsPlaying.get(currentSongPosition);
	}

	private Notification notification;
	private static MusicPlayerService instance;

	public static MusicPlayerService getInstance() {
		if (instance == null) {
			instance = new MusicPlayerService();
		}
		return instance;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (instance == null) {
			instance = this;
		}
		iniMediaPlayer();
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		if (instance == null) {
			instance = this;
		}
		iniMediaPlayer();
		iniNotification();
	}

	public ArrayList<Song> getSongsPlaying() {
		return songsPlaying;
	}

	public void setSongsPlaying(ArrayList<Song> songsPlaying) {
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
	}

	public void playNextSong() {
		try {
			playNewSong(currentSongPosition + 1);
		} catch (Exception e) {
			playNewSong(0);
		}
	}

	public void playPreviousSong() {
		// TODO Auto-generated method stub
		try {
			playNewSong(currentSongPosition - 1);
		} catch (Exception e) {
			playNewSong(songsPlaying.size() - 1);
		}
	}

	private void iniNotification() {
		// TODO Auto-generated method stub
		notification = new Notification();

		Intent notificationIntent = new Intent(this, LoginActivity.class);
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
		UpdateUiFromServiceController.getInstance().updateUI(MUSIC_START);
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
		if (mediaPlayer.isPlaying()) {
			playNextSong();
		}
	}

	private void pauseMedia() {
		// TODO Auto-generated method stub
		if (mediaPlayer.isPlaying()) {

			mediaPlayer.pause();
			UpdateUiFromServiceController.getInstance().updateUI(MUSIC_PAUSE);
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

	public void playCurrentSong() {
		if (currentSongPosition == -1) {

			if (songsPlaying == null) {
				songsPlaying = SongController.getInstance().getSongs();
			}

			playNewSong(0);
		} else {
			playMedia();
		}
		// sendBroadcast(TAG_START);
	}

	public void playNewSong(int position, ArrayList<Song> listSong) {
		songsPlaying = listSong;
		currentSongPosition = position;

		try {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.reset();
			}
			mediaPlayer.setDataSource(songsPlaying.get(position).getLink());
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

	public void playNewSong(int position) {
		currentSongPosition = position;

		playNewSong(position, songsPlaying);

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
