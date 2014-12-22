package ngo.music.soundcloudplayer.boundary;

import java.io.IOException;

import com.todddavies.components.progressbar.ProgressWheel;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import ngo.music.soundcloudplayer.service.MusicPlayerService.MusicPlayerServiceBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.Toast;

public class FullPlayerUI extends Fragment implements OnClickListener {
	private Toolbar toolbar;
	private ProgressWheel musicProgressBar;
	private SongController songController = null;
	private Song song = null;
	private boolean isPlaying = false;
	private MusicPlayerService musicPlayerService;
	private boolean mBound = false;
	private Intent intent;
	private CountDownTimer timer;
	private final Runnable runnable;

	public FullPlayerUI() {
		// TODO Auto-generated constructor stub
		intent = new Intent(MainActivity.getActivity().getApplicationContext(),
				MusicPlayerService.class);
		runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}
		};
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.fullplayer, container, false);
		songController = SongController.getInstance();
		song = songController.getSongs().get(0);
		BasicFunctions.ResizeImageView(MainActivity.screenWidth,
				(ImageView) rootView.findViewById(R.id.full_player_song_image));
		;
		musicPlayerService = new MusicPlayerService();

		musicProgressBar = (ProgressWheel) rootView
				.findViewById(R.id.full_player_progress_bar);
		musicProgressBar.setBackgroundResource(R.drawable.ic_media_play);
		musicProgressBar.setOnClickListener(this);
		intent.putExtra("LINK", song.getLink());
		getActivity().startService(intent);

		return rootView;
	}

	private void pause() {
		// TODO Auto-generated method stub

		musicProgressBar.setBackgroundResource(R.drawable.ic_media_play);
		musicPlayerService.pause();
		timer.cancel();

		// double percentage = mediaPlayer.getCurrentPosition()
		// / mediaPlayer.getDuration();
		// musicProgressBar.setProgressPercentage(percentage);
	}

	private void play() {
		// TODO Auto-generated method stub

		musicPlayerService.playCurrentSong();

		musicProgressBar
				.setBackgroundResource(android.R.drawable.ic_media_pause);
		timer = new CountDownTimer(musicPlayerService.getDuration()
				- musicPlayerService.getCurrentTime(), 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

				musicProgressBar
						.setProgressPercentage((double) musicPlayerService
								.getCurrentTime()
								/ musicPlayerService.getDuration());

				Log.i("Time", musicPlayerService.getCurrentTime() + " of "
						+ musicPlayerService.getDuration());
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}
		};
		timer.start();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getActivity().getApplicationContext().bindService(intent, mConnection,
				0);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mBound) {
			getActivity().getApplicationContext().unbindService(mConnection);
			mBound = false;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v instanceof ProgressWheel) {
			if (!musicPlayerService.isPlaying()) {
				play();
			} else {
				pause();
			}
		}
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			MusicPlayerServiceBinder binder = (MusicPlayerServiceBinder) service;
			musicPlayerService = binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

}
