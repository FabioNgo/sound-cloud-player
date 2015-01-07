package ngo.music.soundcloudplayer.boundary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import ngo.music.soundcloudplayer.R;

import ngo.music.soundcloudplayer.api.Http;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;

import ngo.music.soundcloudplayer.entity.OnlineSong;

import ngo.music.soundcloudplayer.controller.UIController;

import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.entity.SoundCloudAccount;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import com.todddavies.components.progressbar.ProgressWheel;
import com.volley.api.AppController;

public class FullPlayerUI extends PlayerUI implements Constants.MusicService {

	private static FullPlayerUI instance = null;
	private ImageView shuffle;
	private NetworkImageView songImage;
	private RelativeLayout artistInfo;
	
	SoundCloudAccount soundCloudAccount = null;
	


//	public static FullPlayerUI getInstance() {
//		if (instance == null) {
//			instance = new FullPlayerUI();
//		}
//		return instance;
//	}
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	//	instance = this;
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
		
	
		
		
		songImage = (NetworkImageView) rootView.findViewById(R.id.full_player_song_image);
		BasicFunctions.setImageViewSize(MainActivity.screenWidth, MainActivity.screenWidth, songImage);

		//mainImage.setImageUrl(currentSong.getArtworkUrl(),);
		currentTimeText = (TextView) rootView
				.findViewById(R.id.full_player_current_time);
		durationText = (TextView) rootView
				.findViewById(R.id.full_player_duration);
		
		/*
		 * Config buttons in UI
		 */
		configButton();
		
		/**
		 * updateUI
		 */
		updateShuffle();
		updateLoop();
		UIController.getInstance().addUiFragment(this);
		return rootView;

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
		
		BasicFunctions.setImageViewSize(MainActivity.screenWidth/10, MainActivity.screenWidth/10, loop);
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
		BasicFunctions.setImageViewSize(MainActivity.screenWidth/10, MainActivity.screenWidth/10, shuffle);

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
		BasicFunctions.setImageViewSize(MainActivity.screenWidth/10, MainActivity.screenWidth/10, ff);
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
	}

	/**
	 * 
	 */
	private void configRewindButton() {
		ImageView rew = (ImageView) rootView.findViewById(R.id.full_player_rew);
		//BasicFunctions.ScaleImageViewW(MainActivity.screenWidth / 10, rew);
		BasicFunctions.setImageViewSize(MainActivity.screenWidth/10, MainActivity.screenWidth/10, rew);
		
		
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
		if (song == null) return;
		String title = song.getTitle();
		Toolbar toolbar = (Toolbar) rootView
				.findViewById(R.id.full_player_toolbar);
		toolbar.setTitle(title);
	}

	@Override
	public void updateSubtitle(Song song) {
		// TODO Auto-generated method stub
		if (song == null) return;
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

	@Override
	public void updateSongInfo(Song currentSong) {
		// TODO Auto-generated method stub
		
		/*
		 * Config Avatar of Song 
		 */
		ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
		songImage.setDefaultImageResId(R.drawable.ic_launcher);
		
		//System.out.println (currentSong);
		if (currentSong != null){
			//System.out.println (currentSong.getArtworkUrl());
			songImage.setImageUrl(currentSong.getArtworkUrl(), mImageLoader);
			
		}
		
		///songImage.setVisibility(View.GONE);
		/*
		 * Config artist of Song
		 */
		
		FrameLayout songInfo  = (FrameLayout) rootView.findViewById(R.id.song_info_field);
		
		songInfo.getLayoutParams().width = MainActivity.screenWidth;
		songInfo.getLayoutParams().height = songInfo.getLayoutParams().width;
		
		
		artistInfo = (RelativeLayout) rootView.findViewById(R.id.artist_info);
		
		
		/*
		 * If play online, load avatar of artist 
		 */
		OnlineSong onlineSong = null;
		 
		if (currentSong instanceof OnlineSong){
		
			onlineSong = (OnlineSong) currentSong;
			//artistInfo.setAlpha((float) 0.6);
			try {
				soundCloudAccount = new getUserbyIdBackground().execute(String.valueOf(onlineSong.getUser().getId())).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}else{
			//	artistInfo.setAlpha((float) 1);
			songImage.setImageResource(R.drawable.ic_launcher);
			artistInfo.setVisibility(View.INVISIBLE);
		}
		
		artistInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
				System.out.println ("ARTIST " +  soundCloudAccount);
				soundCloudUserController.setGuest(soundCloudAccount);
				Intent i = new Intent(getActivity(), MainActivity.class);
				Bundle bundle = soundCloudUserController.getBundle(soundCloudUserController.getCurrentUser());
				i.putExtra(Constants.UserContant.USER, bundle);
				MainActivity.getActivity().finish();
				startActivity(i);
			}
		});
		
		
			
	}
	
	private class getUserbyIdBackground extends AsyncTask<String, String, SoundCloudAccount>{

		SoundCloudAccount soundCloudAccount = null;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected SoundCloudAccount doInBackground(String... params) {
			// TODO Auto-generated method stub
			SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
			soundCloudAccount = soundCloudUserController.getUserbyId(params[0]);
			return soundCloudAccount;
		}
		
		@Override
		protected void onPostExecute(
				ngo.music.soundcloudplayer.entity.SoundCloudAccount result) {
			// TODO Auto-generated method stub
			TextView artistFullname = (TextView) rootView.findViewById(R.id.artist_fullname);
			NetworkImageView artistAvatar = (NetworkImageView) rootView.findViewById(R.id.artist_image);
			
			
			artistFullname.setText(soundCloudAccount.getFullName());
			artistAvatar.setImageUrl(soundCloudAccount.getAvatarUrl(), AppController.getInstance().getImageLoader());
			BasicFunctions.setImageViewSize(MainActivity.screenHeight/10, MainActivity.screenHeight/10,artistAvatar);
			 
			artistAvatar.setDefaultImageResId(R.drawable.ic_launcher);
			
			artistAvatar.setImageUrl(soundCloudAccount.getAvatarUrl(),AppController.getInstance().getImageLoader() );
			
			
			
		}
		
	}
	

}
