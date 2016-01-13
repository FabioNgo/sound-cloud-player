package ngo.music.player.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.audiofx.Visualizer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ngo.music.player.Controller.HeadSetController;
import ngo.music.player.Controller.MusicPlayerServiceController;
import ngo.music.player.Controller.PlaybackActionController;
import ngo.music.player.Controller.WaveFormController;
import ngo.music.player.Model.OfflineSong;
import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.ModelManager.QueueManager;
import ngo.music.player.R;
import ngo.music.player.View.MusicPlayerMainActivity;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;
import ngo.music.player.helper.States;

public class MusicPlayerService extends Service implements OnErrorListener,
		OnCompletionListener, OnSeekCompleteListener, OnInfoListener,
		OnBufferingUpdateListener, Constants.MusicService,
		Constants.Models, Constants.Appplication, Constants.Data,
		OnPreparedListener,Observer {
	private static final int NOTIFICATION_ID = 1;
	public static boolean isLoaded;
	private static MusicPlayerService instance;
	public MediaPlayer mediaPlayer = null;
	int percent = -1;



	private int seekForwardTime = 5 * 1000;
	private int seekBackwardTime = 5 * 1000;

	public MusicPlayerService() {
		instance = this;
		MusicPlayerServiceController.getInstance().addObserver(this);

	}

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

		iniMediaPlayer();

		updateNotification();



		super.onStartCommand(intent, flags, startId);
		isLoaded = true;
		MusicPlayerServiceController.getInstance().notifyObservers(MUSIC_PAUSE,true);
		return START_STICKY;
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
//			mediaPlayer.seekTo(0);
			MusicPlayerServiceController.getInstance().startTimer();

			try {
				mediaPlayer.setDataSource(MusicPlayerServiceController.getInstance().getCurrentSong().getLink());

				mediaPlayer.prepareAsync();
//				WaveFormController.getInstance().setUpVisualizer();
				HeadSetController receiver = HeadSetController.getInstance();
				IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_HEADSET_PLUG);
				registerReceiver(receiver, intentFilter);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("ini media", e.getMessage());
			}
		}

	}

	/**
	 * Play next Song when click to next button or at the end of current song
	 */
	public void playNextSong() {
		MusicPlayerServiceController.getInstance().pushStackPlayed(MusicPlayerServiceController.getInstance().getCurrentSong());
		Song nextSong = MusicPlayerServiceController.getInstance().getNextSong();
		if(nextSong == null){
			MusicPlayerServiceController.getInstance().computeNextSong(); // try to compute next song to get new NextSong
			nextSong = MusicPlayerServiceController.getInstance().getNextSong();
			//no song to play
			if(nextSong == null){
				Helper.makeToastText("No song to play", MusicPlayerMainActivity.getActivity());
			}
		}
		MusicPlayerServiceController.getInstance().setCurrentSong(nextSong);

	}

	/**
	 * Back to preivious Song when click to prev button
	 */
	public void playPreviousSong() {
		// TODO Auto-generated method stub
		MusicPlayerServiceController.getInstance().setCurrentSong(MusicPlayerServiceController.getInstance().getPreviousSong());

	}

	/**
	 * start Media player.
	 * 
	 */
	private void playMedia() {
		// TODO Auto-generated method stub

		mediaPlayer.start();
//		WaveFormController.getInstance().visualizer.setEnabled(true);
		States.musicPlayerState = MUSIC_PLAYING;
		MusicPlayerServiceController.getInstance().notifyObservers(MUSIC_PLAYING,true);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
			Helper.makeToastText(
					"MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK",
					getApplicationContext());
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			Helper.makeToastText("MEDIA_ERROR_SERVER_DIED" + extra,
					getApplicationContext());
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			Helper.makeToastText("MEDIA_ERROR_UNKNOWN" + extra,
					getApplicationContext());
		}
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		MusicPlayerServiceController.getInstance().notifyObservers(MUSIC_STOPPED,true);
		if (States.musicPlayerState != MUSIC_STOPPED) {
			int loopState = MusicPlayerServiceController.getInstance().getLoopState();
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
//			WaveFormController.getInstance().visualizer.setEnabled(false);
			mediaPlayer.pause();

		}
		States.musicPlayerState = MUSIC_PAUSE;
		MusicPlayerServiceController.getInstance().notifyObservers(MUSIC_PAUSE,true);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		// this.percent = percent;
		// //Helper.makeToastText(""+percent,
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
		MusicPlayerServiceController.getInstance().notifyObservers(MUSIC_CUR_POINT_CHANGED,true);
	}

	/**
	 * Play the current song when press start button
	 */
	public void playCurrentSong() {
		if(MusicPlayerServiceController.getInstance().getCurrentSong()== null){
			MusicPlayerServiceController.getInstance().computeNextSong();
			playNextSong();

		}else{
			if (States.musicPlayerState == MUSIC_PAUSE|| States.musicPlayerState == MUSIC_HEADSET_UNPLUG || States.musicPlayerState == MUSIC_ON_PHONE) {
				playMedia();

			} else if (States.musicPlayerState == MUSIC_STOPPED) {

				mediaPlayer.seekTo(MusicPlayerServiceController.getInstance().getStoppedTime());
				playMedia();

			} else {

				playNewSong();

			}
		}

		updateNotification();
		return;
		// sendBroadcast(TAG_START);
	}


//	public void playSongInQueue(int position) {
//		currentSongPosition = position;
//		playNewSong(true);
//	}

	/**
	 * Play a list of online song
	 *  @param position
	 *            : position of first song in the list
	 * @param queue
	 */
	public void playNewSong(int position, ArrayList<Song> queue) {
		if(queue.size() == 0){
			Helper.makeToastText("No song to play",MusicPlayerMainActivity.getActivity());
			return;
		}

		MusicPlayerServiceController.getInstance().clearStack();
		States.musicPlayerState = MUSIC_PLAYING;
		((QueueManager)ModelManager.getInstance(QUEUE)).replaceQueue(queue);
		MusicPlayerServiceController.getInstance().setCurrentSong(queue.get(position));
	}
	/**
	 * Play song from queue
	 *
	 * @param position
	 *            : position in queue
	 */
	public void playSongInQueue(int position){
		ArrayList<Song> queue = ((QueueManager) ModelManager.getInstance(QUEUE)).getAllSong();
		MusicPlayerServiceController.getInstance().pushStackPlayed(MusicPlayerServiceController.getInstance().getCurrentSong());
		MusicPlayerServiceController.getInstance().setCurrentSong(queue.get(position));
	}
	/**
	 * Play new song
	 *
	 */
	private void playNewSong() {
		mediaPlayer.stop();
		// System.out.println("PLAY NEW SONG 2");
		Song song = MusicPlayerServiceController.getInstance().getCurrentSong();

		if (song == null) {
			Helper.makeToastText("No song to play",
					getApplicationContext());
			return;
		}
		updateNotification();
		playSong(song);




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
			return MusicPlayerServiceController.getInstance().getStoppedTime();
		}
		if (mediaPlayer != null) {
			int currentPosition = mediaPlayer.getCurrentPosition();
			return currentPosition;
		}
		return -1;
	}



	/**
	 * Check if media is playing or not
	 *
	 * @return
	 */
	public boolean isPlaying() {
		return States.musicPlayerState == MUSIC_PLAYING;
	}

	private void updateNotification() {
		// System.out.println("NOTIFICATION");
		String title = "";
		String subTitle = "";
		Song song = MusicPlayerServiceController.getInstance().getCurrentSong();
		if (song != null) {
			title = song.getName();
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
		Intent intent = new Intent(this, PlaybackActionController.class);
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



	public void restartSong() {
		mediaPlayer.reset();
		playNewSong();
	}







	/**
	 * Remove notification
	 */
	public void cancelNoti() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
	}



	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void playSong(Song song) {

		try {
			String link = song.getLink();
			mediaPlayer.reset();
//			mediaPlayer.seekTo(0);

			mediaPlayer.setDataSource(link);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.prepareAsync();

		} catch (Exception e) {
			iniMediaPlayer();
			playNextSong();

		}
	}


	public void release() {
		// TODO Auto-generated method stub
		States.musicPlayerState = MUSIC_STOPPED;

		MusicPlayerServiceController.getInstance().notifyObservers(MUSIC_STOPPED,true);
		MusicPlayerServiceController.getInstance().stopTimer();
		cancelNoti();
		mediaPlayer.stop();
		mediaPlayer.release();
		MusicPlayerMainActivity.getActivity().finish();

		stopSelf();
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
//		WaveFormController.getInstance().setDuration(mp.getDuration());
	}

	@Override
	public void update(Observable observable, Object data) {
		if(observable instanceof MusicPlayerServiceController){
			if(data instanceof Song){
				playNewSong();
			}
		}
	}

	public class MusicPlayerServiceBinder extends Binder {
		public MusicPlayerService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return MusicPlayerService.this;
		}
	}

	public class LocalBinder extends Binder {
		public MusicPlayerService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return MusicPlayerService.this;
		}
	}
}
