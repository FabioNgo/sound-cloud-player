package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.MusicPlayerController;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.todddavies.components.progressbar.ProgressWheel;

public class FullPlayerUI extends PlayerUI {

	private static FullPlayerUI instance = null;
	private View rootView = null;

	public FullPlayerUI() {
		// TODO Auto-generated constructor stub
		super();
	}

	public static FullPlayerUI getInstance() {
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		instance = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.fullplayer, container, false);

		BasicFunctions.ResizeImageView(MainActivity.screenWidth,
				(ImageView) rootView.findViewById(R.id.full_player_song_image));

		musicProgressBar = (ProgressWheel) rootView
				.findViewById(R.id.full_player_progress_bar);

		musicProgressBar.setBackgroundResource(R.drawable.ic_media_play);
		musicProgressBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerController.getInstance().startPause();
			}
		});
		MusicPlayerController.getInstance().addUiFragment(this);
		try {
			title = MusicPlayerService.getInstance().getCurrentSong()
					.getTitle();
			subtitle = MusicPlayerService.getInstance().getCurrentSong()
					.getArtist();
		} catch (Exception e) {

		}
		// MusicPlayerController.getInstance().
		return rootView;
	}

	@Override
	public void updateTitle(String title) {
		// TODO Auto-generated method stub
		this.title =title; 
		Toolbar toolbar = (Toolbar) rootView
				.findViewById(R.id.full_player_toolbar);
		toolbar.setTitle(title);
	}

	@Override
	public void updateSubTitle(String subTitle) {
		// TODO Auto-generated method stub
		this.subtitle = subTitle;
		Toolbar toolbar = (Toolbar) rootView
				.findViewById(R.id.full_player_toolbar);
		toolbar.setSubtitle(subTitle);
	}

}
