package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.MusicPlayerController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.todddavies.components.progressbar.ProgressWheel;

public class FullPlayerUI extends Fragment {
	private Toolbar toolbar;
	private ProgressWheel musicProgressBar;
	private SongController songController = null;

	private Intent intent;

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

		BasicFunctions.ResizeImageView(MainActivity.screenWidth,
				(ImageView) rootView.findViewById(R.id.full_player_song_image));
		

		musicProgressBar = (ProgressWheel) rootView
				.findViewById(R.id.full_player_progress_bar);

		musicProgressBar.setBackgroundResource(R.drawable.ic_media_play);
		MusicPlayerController.getInstance().addMusicProgressBar(
				musicProgressBar);

		

		return rootView;
	}

}
