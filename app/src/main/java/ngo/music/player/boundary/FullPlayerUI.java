package ngo.music.player.boundary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.volley.api.AppController;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ngo.music.player.controller.MenuController;
import ngo.music.player.controller.SCUserController;
import ngo.music.player.controller.UIController;
import ngo.music.player.entity.SCAccount;
import ngo.music.player.entity.SCSong;
import ngo.music.player.entity.Song;
import ngo.music.player.helper.BasicFunctions;
import ngo.music.player.helper.Constants;
import ngo.music.player.service.MusicPlayerService;
import ngo.music.player.R;


public class FullPlayerUI extends PlayerUI implements Constants.MusicService {

	private ImageView shuffle;
	private NetworkImageView songImage;
	private RelativeLayout artistInfo;

	SCAccount soundCloudAccount = null;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		numberPlayerLoading++;
		rootView = inflater.inflate(R.layout.fullplayer, container, false);
		iniMusicProgressBar();
		songImage = (NetworkImageView) rootView
				.findViewById(R.id.full_player_song_image);
		BasicFunctions.setImageViewSize(MusicPlayerMainActivity.screenWidth,
				MusicPlayerMainActivity.screenWidth, songImage);

		currentTimeText = (TextView) rootView
				.findViewById(R.id.full_player_current_time);
		durationText = (TextView) rootView
				.findViewById(R.id.full_player_duration);
		
		
		/**
		 * Config Tool Bar
		 * 
		 */
		configToolbar();
		/*
		 * Config buttons in UI
		 */
		configButton();

		/**
		 * updateUI
		 */
		updateShuffle();
		updateLoop();
		UIController.getInstance().addPlayerUiFragment(this);
		numberPlayerLoading--;
		return rootView;

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
					ArrayList<Song> songs = new ArrayList<Song>();
					songs.add(MusicPlayerService.getInstance().getCurrentSong());
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

		BasicFunctions.setImageViewSize(
				MusicPlayerMainActivity.screenWidth / 10,
				MusicPlayerMainActivity.screenWidth / 10, loop);
		updateLoop();
		loop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerService.getInstance().changeLoopState();
			}
		});
	}

	/**
	 * 
	 */
	private void configShuffleButton() {
		shuffle = (ImageView) rootView.findViewById(R.id.full_player_shuffle);
		BasicFunctions.setImageViewSize(
				MusicPlayerMainActivity.screenWidth / 10,
				MusicPlayerMainActivity.screenWidth / 10, shuffle);

		shuffle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MusicPlayerService.getInstance().setShuffle();

			}
		});
	}

	/**
	 * 
	 */
	private void configFastForwardButton() {
		ImageView ff = (ImageView) rootView.findViewById(R.id.full_player_ff);
		BasicFunctions.setImageViewSize(
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
		// BasicFunctions.ScaleImageViewW(MainActivity.screenWidth / 10, rew);
		BasicFunctions.setImageViewSize(
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
		String title = song.getTitle();
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
		if (MusicPlayerService.getInstance().isShuffle()) {
			((ImageView)rootView.findViewById(R.id.full_player_shuffle)).setImageResource(R.drawable.ic_media_shuffle);
		} else {

			((ImageView)rootView.findViewById(R.id.full_player_shuffle)).setImageResource(R.drawable.ic_media_not_shuffle);
		}
	}

	public void updateLoop() {
		// TODO Auto-generated method stub
		if (MusicPlayerService.getInstance().getLoopState() == MODE_LOOP_ONE) {
			((ImageView) rootView.findViewById(R.id.full_player_loop)).setImageResource(R.drawable.ic_media_loop_1);
		}
		if (MusicPlayerService.getInstance().getLoopState() == MODE_LOOP_ALL) {
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
		// TODO Auto-generated method stub
		ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
		songImage.setDefaultImageResId(R.drawable.ic_launcher);

		if (currentSong != null) {
			// System.out.println (currentSong.getArtworkUrl());
			songImage.setImageUrl(currentSong.getArtworkUrl(), mImageLoader);

		}

		// /songImage.setVisibility(View.GONE);

	}

	private class getUserbyIdBackground extends
			AsyncTask<String, String, SCAccount> {

		SCAccount soundCloudAccount = null;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected SCAccount doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			SCUserController soundCloudUserController = SCUserController
					.getInstance();
			
			try {
				
				soundCloudAccount = soundCloudUserController.getUserbyId(params[0]);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return null;
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return null;
				
			}
			return soundCloudAccount;
		}

		@Override
		protected void onPostExecute(SCAccount result) {
			// TODO Auto-generated method stub
			TextView artistFullname = (TextView) rootView
					.findViewById(R.id.artist_fullname);
			NetworkImageView artistAvatar = (NetworkImageView) rootView
					.findViewById(R.id.artist_image);
			RelativeLayout artisInfo = (RelativeLayout) rootView.findViewById(R.id.artist_info);
			

			if (soundCloudAccount != null){
				artistFullname.setText(soundCloudAccount.getFullName());
			
				artistAvatar.setImageUrl(soundCloudAccount.getAvatarUrl(),
					
					AppController.getInstance().getImageLoader());
//			BasicFunctions.setImageViewSize(
//					MusicPlayerMainActivity.screenHeight / 10,
//					MusicPlayerMainActivity.screenHeight / 10, artistAvatar);

			artistAvatar.setDefaultImageResId(R.drawable.ic_launcher);
//
			}
//			artistAvatar.setImageUrl(soundCloudAccount.getAvatarUrl(),
//					AppController.getInstance().getImageLoader());

		}

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
		SCSong onlineSong = null;

		if (song instanceof SCSong) {
			
			artistInfo.setVisibility(View.VISIBLE);
			onlineSong = (SCSong) song;
			// artistInfo.setAlpha((float) 0.6);
			try {
				String ID = String.valueOf(onlineSong.getUser().getId());
				
				soundCloudAccount = new getUserbyIdBackground().execute(ID).get();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// artistInfo.setAlpha((float) 1);
			songImage.setImageResource(R.drawable.ic_launcher);
			artistInfo.setVisibility(View.INVISIBLE);
		}

		artistInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SCUserController soundCloudUserController = SCUserController
						.getInstance();

				soundCloudUserController.setGuest(soundCloudAccount);
				Intent i = new Intent(getActivity(),
						MusicPlayerMainActivity.class);
				Bundle bundle;
				try {
					bundle = soundCloudUserController.getBundle(soundCloudUserController.getCurrentUser());
					i.putExtra(Constants.UserContant.USER, bundle);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				MusicPlayerMainActivity.getActivity().finish();
				startActivity(i);
			}
		});
	}

	public void resetProgress() {

	}

	@Override
	protected boolean hasTextTime() {
		// TODO Auto-generated method stub
		return true;
	}

	
}
