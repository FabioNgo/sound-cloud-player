package ngo.music.soundcloudplayer.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Stack;

import org.xmlpull.v1.XmlPullParserException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.UpdateUiFromServiceController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.OfflineSongController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.format.Time;
import android.util.Log;
import android.util.Xml;

import com.facebook.LoginActivity;

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
	private ArrayList<Song> songsPlaying;
	private Stack<Integer> stackSongplayed;
	private final IBinder mBinder = new MusicPlayerServiceBinder();
	public MediaPlayer mediaPlayer = null;
	private static final int NOTIFICATION_ID = 1;
	private int currentSongPosition = -1;
	String filename = "lastPlayingSong";

	public Song getCurrentSong() {

		Song curSong = songsPlaying.get(currentSongPosition);
		return curSong;
	}

	private Notification notification;
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
		// TODO Auto-generated method stub
		if (instance == null) {
			instance = this;
		}

		return START_STICKY;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		if (instance == null) {
			instance = this;
		}
		musicState = MUSIC_STOP;

		try {
			getData();
		} catch (Exception e) {
			Log.w("getData", e.getMessage());
			currentSongPosition = 0;

		} finally {
			try {
				songsPlaying = OfflineSongController.getInstance().getSongs();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stackSongplayed = new Stack<Integer>();
		}
		UpdateUiFromServiceController.getInstance().updateUI(APP_START);
		iniMediaPlayer();
		iniNotification();
		updateNotification(false, R.drawable.ic_media_pause);
		try {
			OfflineSongController.getInstance().iniDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("inidata",e.getMessage());
		}
		// playCurrentSong();
		// pause();

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
		stackSongplayed.push(Integer.valueOf(currentSongPosition));	
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
		
		playNewSong(currentSongPosition);

	}

	public void playPreviousSong() {
		// TODO Auto-generated method stub
		
		if (stackSongplayed.empty()) {
			currentSongPosition--;
			int size = songsPlaying.size();
			currentSongPosition = currentSongPosition % size;
		} else {
			
			currentSongPosition = stackSongplayed.peek().intValue();
		}
		playNewSong(currentSongPosition);

	}

	private void iniNotification() {
		// TODO Auto-generated method stub
		notification = new Notification();

		Intent notificationIntent = new Intent(getApplicationContext(),
				LoginActivity.class);
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
		updateNotification(true, 0);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub

		mp.start();
		UpdateUiFromServiceController.getInstance().updateUI(MUSIC_START);
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
		mp.reset();
		if(loopState == MODE_LOOP_ONE){
			restartSong();
		}else{
			playNextSong();	
		}
		

		updateNotification(false, R.drawable.ic_media_play);
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

		if (musicState == MUSIC_PAUSE) {
			playMedia();

		} else {
			musicState = MUSIC_START;
			playNewSong(currentSongPosition);
		}
		// sendBroadcast(TAG_START);
	}

	public void playNewSong(int position, ArrayList<Song> listSong,
			boolean startNow) {
		if (startNow) {
			musicState = MUSIC_START;
		}
		
		songsPlaying = listSong;
		currentSongPosition = position;
		UpdateUiFromServiceController.getInstance().updateUI(MUSIC_NEW_SONG);
		updateNotification(false, R.drawable.ic_media_pause);
		if (musicState == MUSIC_START) {
			try {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.reset();
				}
				mediaPlayer.setDataSource(songsPlaying.get(position).getLink());
				// mediaPlayer.prepareAsync();

				mediaPlayer.prepare();
				storeData();
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
		}
		// Builder builder = new Builder(MainActivity.getActivity());
		// notification = builder.setContentTitle(currentSong.getTitle())
		// .setContentText(currentSong.getLink()).build();

	}

	public void playNewSong(int position) {

		playNewSong(position, songsPlaying, false);

		// Builder builder = new Builder(MainActivity.getActivity());
		// notification = builder.setContentTitle(currentSong.getTitle())
		// .setContentText(currentSong.getLink()).build();

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
			return mediaPlayer.getDuration();
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
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub

	}

	private void updateNotification(boolean cancel, int iconID) {
		if (cancel) {
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.cancelAll();
			return;
		}
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(iconID)
				.setContentTitle(getCurrentSong().getTitle())
				.setContentText(getCurrentSong().getArtist());
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this,
				ngo.music.soundcloudplayer.boundary.LoginActivity.class);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder
				.addParentStack(ngo.music.soundcloudplayer.boundary.LoginActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

	public void startPause() {
		// TODO Auto-generated method stub

		if (musicState != MUSIC_START) {

			playCurrentSong();
		} else {
			pause();
		}

	}

	private void storeData() {
		
//		ArrayList<String> data = new ArrayList<String>();
//		data.add(String.valueOf(currentSongPosition));
//		// for (Song song : songsPlaying) {
//		// data.add(song.getId());
//		// }
//		String rawData = "";
//		for (String string2 : data) {
//			rawData += string2 + ",";
//		}
//		BufferedWriter bufferedWriter;
//		try {
//			bufferedWriter = new BufferedWriter(new FileWriter(new File(
//					getApplicationContext().getExternalFilesDir(
//							ACCESSIBILITY_SERVICE), filename)));
//
//			bufferedWriter.write(rawData);
//			bufferedWriter.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	private void getData() throws Exception {
		BufferedReader bufferedReader;

		bufferedReader = new BufferedReader(new FileReader(new File(
				getApplicationContext().getExternalFilesDir(
						ACCESSIBILITY_SERVICE), filename)));
		String read;
		StringBuilder builder = new StringBuilder("");

		while ((read = bufferedReader.readLine()) != null) {
			builder.append(read);
		}
		bufferedReader.close();
		String[] data = builder.toString().split(",");

		currentSongPosition = Integer.parseInt(data[0]);

	}

	

	public ArrayList<Song> getSongs() {
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
		playNewSong(currentSongPosition);
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
}
