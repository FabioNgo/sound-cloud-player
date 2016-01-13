package ngo.music.player.View;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import java.util.concurrent.TimeUnit;

import co.mobiwise.library.MaskProgressView;
import co.mobiwise.library.OnProgressDraggedListener;
import ngo.music.player.Controller.MenuController;
import ngo.music.player.Controller.MusicPlayerServiceController;
import ngo.music.player.Controller.WaveFormController;
import ngo.music.player.Model.Song;
import ngo.music.player.R;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;
import ngo.music.player.service.MusicPlayerService;


public class FullPlayerUI extends PlayerUI implements Constants.MusicService {

	private ImageView shuffle;
	private NetworkImageView songImage;
	private RelativeLayout artistInfo;
	private WaveFormView waveFormView;
	private TextView currentTimeText;
	private TextView durationText;
	MaskProgressView maskProgressView;
	private boolean draggingSeekbar = false;

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

		currentTimeText = (TextView) rootView
				.findViewById(R.id.full_player_current_time);
		durationText = (TextView) rootView
				.findViewById(R.id.full_player_duration);
		waveFormView = (WaveFormView)rootView.findViewById(R.id.wave_form_view);
		waveFormView.getLayoutParams().height = MusicPlayerMainActivity.screenWidth;
		setupSeekbar();
		//seekbar


		try {
			WaveFormController.getInstance().ReadFile(new File(MusicPlayerServiceController.getInstance().getCurrentSong().getLink()));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		updateShuffle();
		updateLoop();
		updateSongInfo(MusicPlayerServiceController.getInstance().getCurrentSong());
		return rootView;

	}

	private void setupSeekbar() {
		maskProgressView = (MaskProgressView) rootView.findViewById(R.id.seekbar);
		maskProgressView.getLayoutParams().height = MusicPlayerMainActivity.screenWidth;
		maskProgressView.setmMaxSeconds(Integer.parseInt((MusicPlayerServiceController.getInstance().getCurrentSong().getAttribute("duration"))) / 1000);
		maskProgressView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				MusicPlayerMainActivity.getActivity().disableSliding();
				v.onTouchEvent(event);
				if(event.getAction() == MotionEvent.ACTION_UP){
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
		// TODO Auto-generated method stub
			currentTimeText.setText(Helper.toFormatedTime(0));
			durationText.setText(Helper.toFormatedTime(0));


	}

	private void configToolbar() {
		// TODO Auto-generated method stub
		Toolbar toolbar = (Toolbar) rootView
				.findViewById(R.id.full_player_toolbar);
		toolbar.setLogo(R.drawable.logo);
		toolbar.inflateMenu(R.menu.full_player_menu);
		toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				switch (arg0.getItemId()) {
					case R.id.full_player_add_playlist:
						ArrayList<Song> songs = new ArrayList<>();
						songs.add(MusicPlayerServiceController.getInstance().getCurrentSong());
						MenuController.getInstance(songs).addToPlaylist();
						break;
					case R.id.full_player_share:
						/**
						 * TU dien
						 */
						break;
					case R.id.full_player_add_favorite:

					default:
						break;
				}
				return false;
			}
		});
	}

	/**
	 * Config rewind, fastforward, shuffle, loop button
	 */
	private void configButton() {
		/**
		 * Rewind button
		 */
		configRewindButton();
		/**
		 * Fast forward button
		 */
		configFastForwardButton();
		/**
		 * Shuffle button
		 */
		configShuffleButton();
		/**
		 * Loop button
		 */
		configLoopButton();
	}

	/**
	 * 
	 */
	private void configLoopButton() {
		ImageView loop = (ImageView) rootView
				.findViewById(R.id.full_player_loop);

		Helper.setImageViewSize(
				MusicPlayerMainActivity.screenWidth / 10,
				MusicPlayerMainActivity.screenWidth / 10, loop);
		updateLoop();
		loop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerServiceController.getInstance().changeLoopState();
				updateLoop();
			}
		});
	}

	/**
	 * 
	 */
	private void configShuffleButton() {
		shuffle = (ImageView) rootView.findViewById(R.id.full_player_shuffle);
		Helper.setImageViewSize(
				MusicPlayerMainActivity.screenWidth / 10,
				MusicPlayerMainActivity.screenWidth / 10, shuffle);

		shuffle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MusicPlayerServiceController.getInstance().setShuffle();
				updateShuffle();

			}
		});
	}

	/**
	 * 
	 */
	private void configFastForwardButton() {
		ImageView ff = (ImageView) rootView.findViewById(R.id.full_player_ff);
		Helper.setImageViewSize(
				MusicPlayerMainActivity.screenWidth / 10,
				MusicPlayerMainActivity.screenWidth / 10, ff);
		ff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerService.getInstance().forwardSong();
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
	}

	/**
	 * 
	 */
	private void configRewindButton() {
		ImageView rew = (ImageView) rootView.findViewById(R.id.full_player_rew);
		// Helper.ScaleImageViewW(MainActivity.screenWidth / 10, rew);
		Helper.setImageViewSize(
				MusicPlayerMainActivity.screenWidth / 10,
				MusicPlayerMainActivity.screenWidth / 10, rew);

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
		toolbar.setSubtitle(subtitle);
	}

	/**
	 * Display different icon of shuffle
	 */
	public void updateShuffle() {
		// TODO Auto-generated method stub
		if (MusicPlayerServiceController.getInstance().isShuffle()) {
			((ImageView)rootView.findViewById(R.id.full_player_shuffle)).setImageResource(R.drawable.ic_media_shuffle);
		} else {

			((ImageView)rootView.findViewById(R.id.full_player_shuffle)).setImageResource(R.drawable.ic_media_not_shuffle);
		}
	}

	public void updateLoop() {
		// TODO Auto-generated method stub
		if (MusicPlayerServiceController.getInstance().getLoopState() == MODE_LOOP_ONE) {
			((ImageView) rootView.findViewById(R.id.full_player_loop)).setImageResource(R.drawable.ic_media_loop_1);
		}
		if (MusicPlayerServiceController.getInstance().getLoopState() == MODE_LOOP_ALL) {
			((ImageView) rootView.findViewById(R.id.full_player_loop)).setImageResource(R.drawable.ic_media_loop);
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

	private void updateWaveForm(){
		Log.i("update wave", "update");
		waveFormView.updateWaveForm();
	}

	@Override
	public void pause() {

	}

	@Override
	public void play() {
		super.play();
		maskProgressView.setmMaxSeconds((int) (MusicPlayerServiceController.getInstance().getDuration())/1000);
	}


}
