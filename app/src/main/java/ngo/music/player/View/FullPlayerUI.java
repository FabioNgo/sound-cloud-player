package ngo.music.player.View;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import co.mobiwise.library.MaskProgressView;
import co.mobiwise.library.OnProgressDraggedListener;
import ngo.music.player.Controller.MenuController;
import ngo.music.player.Controller.MusicPlayerServiceController;
import ngo.music.player.Controller.WaveFormController;
import ngo.music.player.Model.Song;
import ngo.music.player.R;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;
import ngo.music.player.helper.States;
import ngo.music.player.service.MusicPlayerService;


public class FullPlayerUI extends PlayerUI implements Constants.MusicService {

	private FloatingActionButton playerMode;
	private ImageView addToPlaylist;
	private NetworkImageView songImage;
	private RelativeLayout artistInfo;
	private WaveFormView waveFormView;
	private RelativeLayout functionalButtonsContainer;
	private MaskProgressView maskProgressView;
	private boolean draggingSeekbar = false;
	private ImageView playPauseButton;


	public FullPlayerUI(){
		super();
		WaveFormController.getInstance().addObserver(this);
	}

	@Override
	protected Runnable setRunnable() {
		return new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
//				maskProgressView.setmCurrentSeconds((int) (MusicPlayerService.getInstance().getCurrentTime()/1000));
				float index = MusicPlayerService.getInstance().getCurrentTime()/(float)MusicPlayerServiceController.getInstance().getDuration();
				if(!draggingSeekbar){
					maskProgressView.setIndexY(index);
				}


			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fullplayer, container, false);

//		iniMusicProgressBar();
		songImage = (NetworkImageView) rootView
				.findViewById(R.id.full_player_song_image);
		Helper.setImageViewSize(MusicPlayerMainActivity.screenWidth,
				MusicPlayerMainActivity.screenWidth, songImage);
		waveFormView = (WaveFormView)rootView.findViewById(R.id.wave_form_view);
		waveFormView.getLayoutParams().height = MusicPlayerMainActivity.screenWidth;
		setupSeekbar();
		//seekbar


		WaveFormController.getInstance().ReadFile(new File(MusicPlayerServiceController.getInstance().getCurrentSong().getLink()));
		//		WaveFormController.getInstance().addObserver(waveFormView);
		/**
		 * Config Tool Bar
		 * 
		 */
		configToolbar();
		/**
		 * Config buttons in UI
		 */
		configButton();
		/**
		 * updateUI
		 */
		updatePlayerMode();

		updateSongInfo(MusicPlayerServiceController.getInstance().getCurrentSong());
		return rootView;

	}

	private void setupSeekbar() {
		maskProgressView = (MaskProgressView) rootView.findViewById(R.id.seekbar);
		maskProgressView.getLayoutParams().height = MusicPlayerMainActivity.screenWidth;
		maskProgressView.setmMaxSeconds((MusicPlayerServiceController.getInstance().getCurrentSong().getDuration() / 1000));
		maskProgressView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				MusicPlayerMainActivity.getActivity().disableSliding();
				v.onTouchEvent(event);
				if (event.getAction() == MotionEvent.ACTION_UP) {
					MusicPlayerMainActivity.getActivity().enableSliding();
					return true;
				}
				return true;
			}
		});
		maskProgressView.setOnProgressDraggedListener(new OnProgressDraggedListener() {
			@Override
			public void onProgressDragged(int position) {
				MusicPlayerService.getInstance().seekTo(position);
				draggingSeekbar = false;
			}

			@Override
			public void onProgressDragging(int position) {
				draggingSeekbar = true;

			}
		});


	}

	@Override
	public void stop() {
		playPauseButton.setImageResource(android.R.drawable.ic_media_play);
	}

	private void configToolbar() {
		// TODO Auto-generated method stub
		Toolbar toolbar = (Toolbar) rootView
				.findViewById(R.id.full_player_toolbar);
		toolbar.setLogo(R.drawable.logo);
//		toolbar.inflateMenu(R.menu.full_player_menu);
//		toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//
//			@Override
//			public boolean onMenuItemClick(MenuItem arg0) {
//				// TODO Auto-generated method stub
//				switch (arg0.getItemId()) {
//					case R.id.full_player_add_playlist:
//						ArrayList<Song> songs = new ArrayList<>();
//						songs.add(MusicPlayerServiceController.getInstance().getCurrentSong());
//						MenuController.getInstance(songs).addToPlaylist();
//						break;
//					case R.id.full_player_share:
//						/**
//						 * TU dien
//						 */
//						break;
//					case R.id.full_player_add_favorite:
//
//					default:
//						break;
//				}
//				return false;
//			}
//		});
	}

	/**
	 * Config rewind, fastforward, shuffle, loop button
	 */
	private void configButton() {

		configPrevSongButton();

		configNextSongButton();
		configPlayPauseButton();
		configFunctionalButtons();

	}

	private void configPlayPauseButton() {
		playPauseButton = (ImageView) rootView.findViewById(R.id.play_pause_button);
		Helper.setImageViewSize(
				Helper.getWidthInPercent(8.3),
				Helper.getWidthInPercent(8.3), playPauseButton);
		if(States.musicPlayerState == MUSIC_RESUME || States.musicPlayerState == MUSIC_NEW_SONG) {
			playPauseButton.setImageResource(android.R.drawable.ic_media_play);
		}else{
			playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
		}
		playPauseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerService.getInstance().startPause();
			}
		});
	}

	/**
	 * Functional button ( player mode, share,favortite,....)
	 */
	private void configFunctionalButtons() {
//		functionalButtonsContainer = (RelativeLayout)rootView.findViewById(R.id.full_player_func_btn_container);
//		functionalButtonsContainer.getLayoutParams().height = Helper.getHeightInPercent(8.3);
		/**
		 * Buttons
		 */
		playerMode = (FloatingActionButton) rootView.findViewById(R.id.full_player_mode);
//		playerMode.getLayoutParams().width = Helper.getWidthInPercent(25);

		Helper.setImageViewSize(
				Helper.getWidthInPercent(8.3),
				Helper.getWidthInPercent(8.3), playerMode);
		playerMode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerServiceController.getInstance().setPlayerMode();
				updatePlayerMode();
			}
		});
		addToPlaylist = (ImageView) rootView.findViewById(R.id.full_player_add_playlist);
		addToPlaylist.getLayoutParams().width = Helper.getWidthInPercent(25);
		Helper.setImageViewSize(
				Helper.getWidthInPercent(8.3),
				Helper.getWidthInPercent(8.3), addToPlaylist);
		addToPlaylist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<Song> songs = new ArrayList<>();
				songs.add(MusicPlayerServiceController.getInstance().getCurrentSong());
				MenuController.getInstance(songs).addToPlaylist();
			}
		});

	}
	public void updatePlayerMode(){
		switch (MusicPlayerServiceController.getInstance().getPlayerMode()){
			case MODE_IN_ORDER:
				playerMode.setImageResource(R.drawable.ic_media_loop);
				break;
			case MODE_LOOP_ONE:
				playerMode.setImageResource(R.drawable.ic_media_loop_1);
				break;
			case MODE_SHUFFLE:
				playerMode.setImageResource(R.drawable.ic_media_shuffle);
				break;
			default:
				return;
		}
	}
	/**
	 * Next song button
	 */
	private void configNextSongButton() {
		ImageView prevSong = (ImageView) rootView.findViewById(R.id.full_player_next_song);
		Helper.setImageViewSize(
				Helper.getWidthInPercent(8.3),
				Helper.getWidthInPercent(8.3), prevSong);

		prevSong.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerService.getInstance().playNextSong();
			}
		});
	}

	/**
	 * Previous song button
	 */
	private void configPrevSongButton() {
		ImageView prevSong = (ImageView) rootView.findViewById(R.id.full_player_prev_song);
		Helper.setImageViewSize(
				Helper.getWidthInPercent(8.3),
				Helper.getWidthInPercent(8.3), prevSong);

		prevSong.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerService.getInstance().playPreviousSong();
			}
		});

	}

	
	@Override
	public void updateTitle(Song song) {
		// TODO Auto-generated method stub
		if (song == null)
			return;
		String title = song.getName();
		Toolbar toolbar = (Toolbar) rootView
				.findViewById(R.id.full_player_toolbar);
		toolbar.setTitle(title);
	}

	@Override
	public void updateSubtitle(Song song) {
		// TODO Auto-generated method stub
		if (song == null)
			return;

		String subtitle = song.getArtist() + " | " + song.getAlbum();
		Toolbar toolbar = (Toolbar) rootView
				.findViewById(R.id.full_player_toolbar);
		if(subtitle.equals(" | ")){
			subtitle = "";
		}
		toolbar.setSubtitle(subtitle);
	}




	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void updateImage(Song currentSong) {
//		// TODO Auto-generated method stub
//		ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
//		songImage.setDefaultImageResId(R.drawable.ic_launcher);
//
//		if (currentSong != null) {
//			// System.out.println (currentSong.getArtworkUrl());
//			songImage.setImageUrl(currentSong.getArtworkUrl(), mImageLoader);
//
//		}
//
//		// /songImage.setVisibility(View.GONE);

	}



	@Override
	protected void updateOtherInfo(Song song) {
		// TODO Auto-generated method stub
		/*
		 * Config artist of Song
		 */
		
		if(song == null){
			
			return;
		}
		FrameLayout songInfo = (FrameLayout) rootView
				.findViewById(R.id.song_info_field);

		songInfo.getLayoutParams().width = MusicPlayerMainActivity.screenWidth;
		songInfo.getLayoutParams().height = songInfo.getLayoutParams().width;

		artistInfo = (RelativeLayout) rootView.findViewById(R.id.artist_info);
		artistInfo.getLayoutParams().height =MusicPlayerMainActivity.screenHeight / 10; 

		/*
		 * If play online, load avatar of artist
		 */


			// artistInfo.setAlpha((float) 1);
		songImage.setImageResource(R.drawable.ic_launcher);
		artistInfo.setVisibility(View.INVISIBLE);



	}


	@Override
	public void update(Observable observable, Object data) {
		super.update(observable, data);
		if(observable instanceof MusicPlayerServiceController){
			if(!(data instanceof Song)){
				int TAG = (int) data;
				switch (TAG) {
					case MUSIC_PROGRESS:
//						updateWaveForm();
						break;
				}
			}
		}
		if(observable instanceof WaveFormController){
			updateWaveForm();
		}
	}

	/**
	 * resume player from pause
	 */
	@Override
	public void resume() {
		super.resume();
		playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
	}

	private void updateWaveForm(){
		waveFormView.updateWaveForm();
	}

	@Override
	public void pause() {
		playPauseButton.setImageResource(android.R.drawable.ic_media_play);
	}

	@Override
	public void play() {
		super.play();
		maskProgressView.setmMaxSeconds((int) (MusicPlayerServiceController.getInstance().getDuration()) / 1000);

		playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
	}



}
