package ngo.music.soundcloudplayer.boundary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.UpdateUiFromServiceController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.todddavies.components.progressbar.ProgressWheel;

public class FullPlayerUI extends PlayerUI implements Constants.MusicService {

	private static FullPlayerUI instance = null;
	private ImageView shuffle;


	public static FullPlayerUI getInstance() {
		if (instance == null) {
			instance = new FullPlayerUI();
		}
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		instance = this;
		hasTextTime = true;
		musicProgressBar_id = R.id.full_player_progress_bar;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// super.onCreateView(inflater, container, savedInstanceState);
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.fullplayer, container, false);
		iniMusicProgressBar();
		BasicFunctions.ScaleImageViewW(MainActivity.screenWidth,
				(ImageView) rootView.findViewById(R.id.full_player_song_image));

		currentTimeText = (TextView) rootView
				.findViewById(R.id.full_player_current_time);
		durationText = (TextView) rootView
				.findViewById(R.id.full_player_duration);
		/**
		 * Rewind button
		 */
		ImageView rew = (ImageView) rootView.findViewById(R.id.full_player_rew);
		BasicFunctions.ScaleImageViewW(MainActivity.screenWidth / 10, rew);
		rew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerService.getInstance().rewindSong();
			}
		});
		rew.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if (MusicPlayerService.getInstance().getCurrentTime() < 5000) {
					MusicPlayerService.getInstance().playPreviousSong();
				} else {
					MusicPlayerService.getInstance().restartSong();
				}
				return true;
			}
		});
		/**
		 * Fast forward button
		 */
		ImageView ff = (ImageView) rootView.findViewById(R.id.full_player_ff);
		BasicFunctions.ScaleImageViewW(MainActivity.screenWidth / 10, ff);
		ff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerService.getInstance().forwardSong();
				;
			}
		});
		ff.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerService.getInstance().playNextSong();
				return true;
			}
		});
		/**
		 * Shuffle button
		 */
		shuffle = (ImageView) rootView.findViewById(R.id.full_player_shuffle);
		BasicFunctions.ScaleImageViewW(MainActivity.screenWidth / 10, shuffle);

		shuffle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MusicPlayerService.getInstance().setShuffle();
				

			}
		});
		/**
		 * Loop button
		 */
		ImageView loop = (ImageView) rootView
				.findViewById(R.id.full_player_loop);
		BasicFunctions.ScaleImageViewW(MainActivity.screenWidth / 10, loop);
		updateLoop();
		loop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerService.getInstance().changeLoopState();
			}
		});
		/**
		 * updateUI
		 */
		updateShuffle();
		updateLoop();
		UpdateUiFromServiceController.getInstance().addUiFragment(this);
		return rootView;
	}

	@Override
	public void updateTitle(Song song) {
		// TODO Auto-generated method stub
		String title = song.getTitle();
		Toolbar toolbar = (Toolbar) rootView
				.findViewById(R.id.full_player_toolbar);
		toolbar.setTitle(title);
	}

	@Override
	public void updateSubtitle(Song song) {
		// TODO Auto-generated method stub
		String subtitle = song.getArtist() + " | " + song.getAlbum();
		Toolbar toolbar = (Toolbar) rootView
				.findViewById(R.id.full_player_toolbar);
		toolbar.setSubtitle(subtitle);
	}

	public void updateShuffle() {
		// TODO Auto-generated method stub
		if (MusicPlayerService.getInstance().isShuffle()) {
			rootView.findViewById(R.id.full_player_shuffle).setBackgroundColor(Color.CYAN);
		} else {

			rootView.findViewById(R.id.full_player_shuffle).setBackgroundColor(Color.TRANSPARENT);
		}
	}

	public void updateLoop() {
		// TODO Auto-generated method stub
		if (MusicPlayerService.getInstance().getLoopState() == MODE_LOOP_NONE) {
			((ImageView) rootView.findViewById(R.id.full_player_loop))
					.setBackgroundColor(Color.TRANSPARENT);
		}
		if (MusicPlayerService.getInstance().getLoopState() == MODE_LOOP_ONE) {
			((ImageView) rootView.findViewById(R.id.full_player_loop))
					.setBackgroundColor(Color.CYAN);
		}
		if (MusicPlayerService.getInstance().getLoopState() == MODE_LOOP_ALL) {
			((ImageView) rootView.findViewById(R.id.full_player_loop))
					.setBackgroundColor(Color.YELLOW);
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		updateShuffle();
		updateLoop();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
