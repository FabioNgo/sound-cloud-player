package ngo.music.soundcloudplayer.boundary;

import java.io.IOException;

import com.todddavies.components.progressbar.ProgressWheel;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class FullPlayerUI extends Fragment {
	Toolbar toolbar;
	ProgressWheel musicProgressBar;
	SongController songController = null;
	Song song = null;
	MediaPlayer mediaPlayer = null;
	boolean ableToPlay = false;

	public FullPlayerUI() {
		// TODO Auto-generated constructor stub

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
		mediaPlayer = new MediaPlayer();

		
		AudioManager am = (AudioManager) getActivity().getSystemService(
				Context.AUDIO_SERVICE);
		am.setStreamVolume(AudioManager.STREAM_MUSIC,
				am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
		toolbar = (Toolbar) rootView.findViewById(R.id.full_player_toolbar);

		toolbar.setTitle("Title");
		toolbar.setSubtitle("subtitle");
		toolbar.inflateMenu(R.menu.global);
		ImageView full_player_song_image = (ImageView) rootView
				.findViewById(R.id.full_player_song_image);
		BasicFunctions.ResizeImageView(MainActivity.screenWidth,
				full_player_song_image);
		musicProgressBar = (ProgressWheel) rootView
				.findViewById(R.id.full_player_progress_bar);
		musicProgressBar.setProgressPercentage(20);
		
		mediaPlayer.stop();
		play();
//		mediaPlayer.seekTo(10000);
		musicProgressBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mediaPlayer.isPlaying()) {
					pause();
				} else {
					if (ableToPlay) {
						play();
					}
				}
			}
		});
		return rootView;
	}

	private void pause() {
		// TODO Auto-generated method stub
		musicProgressBar.setText("Paused");
		musicProgressBar.setBackgroundResource(R.drawable.ic_action_github);
		mediaPlayer.pause();
//		double percentage = mediaPlayer.getCurrentPosition()
//				/ mediaPlayer.getDuration();
//		musicProgressBar.setProgressPercentage(percentage);
	}

	private void play() {
		// TODO Auto-generated method stub
		musicProgressBar.setText("Playing...");
		musicProgressBar.setBackgroundResource(R.drawable.ic_launcher);
		mediaPlayer.start();
//		double percentage = mediaPlayer.getCurrentPosition()
//				/ mediaPlayer.getDuration();
//		musicProgressBar.setProgressPercentage(percentage);
	}

}
