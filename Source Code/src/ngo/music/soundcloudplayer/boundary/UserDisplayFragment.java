package ngo.music.soundcloudplayer.boundary;

import java.io.IOException;

import org.apache.http.HttpResponse;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Http;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.SoundCloudExploreFragment;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import ngo.music.soundcloudplayer.database.DatabaseHandler;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.CircularImageView;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.imageloader.ImageLoader;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserDisplayFragment extends Fragment implements Constants,Constants.UserContant, Constants.UIContant, Constants.TabContant {

	int layoutWidth;
	int layoutHeight;
	View rootView;
	
	private static String avatarUrl;
	private static String fullNameString;
	private static int numFollowers;
	public UserDisplayFragment(){
		super();
		layoutWidth = MainActivity.screenWidth;
		layoutHeight = MainActivity.screenHeight;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.user_profile_layout, container,false);
		
		/*
		 * Get info from actvity
		 */
		Bundle bundle  = getActivity().getIntent().getBundleExtra(USER);
		
		
	
		//int numFollowers = bundle.getInt(FOLLOWERS_COUNT);

		if (bundle != null ){
			avatarUrl = bundle.getString(AVATAR_URL);
			fullNameString = bundle.getString(FULLNAME);
			numFollowers = bundle.getInt(FOLLOWERS_COUNT);
			configUserLayout(avatarUrl, fullNameString, numFollowers);
			
		}else{
			configUserLayout(null,null, -1);
		}
		
		configButton();
		
		return rootView;
		
	
		
	}

	/**
	 * Config buttons
	 */
	private void configButton() {
		
		int constantLayoutHeight = MainActivity.screenHeight/15;
		/*
		 * My Music
		 */
		RelativeLayout myMusic = (RelativeLayout) rootView.findViewById(R.id.my_music_field);
		myMusic.getLayoutParams().height = constantLayoutHeight;
		myMusic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.isExplore = false;
				Intent i = new Intent(getActivity(), MainActivity.class);
				i.putExtra(DEFAULT_ID, SONGS);
				SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
				Bundle bundle = soundCloudUserController.getBundle(soundCloudUserController.getCurrentUser());
				SongController songController = SongController.getInstance();
				songController.loadFavoriteSong();
				i.putExtra(USER, bundle);
				
				startActivity(i);
				
				
			}
		});
		/*
		 * Soundcloud explore
		 */
		RelativeLayout soundCloudExplore = (RelativeLayout) rootView.findViewById(R.id.soundcloud_explore_field);
		soundCloudExplore.getLayoutParams().height =constantLayoutHeight;
		soundCloudExplore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new loadSongBackground().execute();
				
				
				
				
			}
		});
		
		/*
		 * Login/Logut
		 */
		RelativeLayout logOut = (RelativeLayout) rootView.findViewById(R.id.log_out_field);
		logOut.getLayoutParams().height =constantLayoutHeight;
		SoundCloudUserController userController = SoundCloudUserController.getInstance();
		
		if (!userController.isLogin()){
			((TextView) rootView.findViewById(R.id.logout_id)).setText("Log in");
			
		}
		logOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
				soundCloudUserController.logout();
				DatabaseHandler databaseHandler = DatabaseHandler.getInstance(getActivity());
				databaseHandler.refreshDatabase();
				Intent loginAct = new Intent(getActivity(), LoginActivity.class);
				startActivity(loginAct);
			}
		});
		
		/*
		 * Upload Button
		 */
		RelativeLayout uploadButton = (RelativeLayout) rootView.findViewById(R.id.upload_soundcloud_field);
		uploadButton.getLayoutParams().height = constantLayoutHeight;
		if (!userController.isLogin()){
			uploadButton.setVisibility(View.GONE);
		}else{
			uploadButton.setVisibility(View.VISIBLE);
		}
		uploadButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent uploadActivity = new Intent(getActivity(), UploadSongActivity.class);
				startActivity(uploadActivity);
			}
		});
	}

	/**
	 * @param background.rootView
	 * @param avatarUrl
	 * @param fullNameString
	 * @param numFollowers
	 */
	private void configUserLayout(String avatarUrl,
			String fullNameString, int numFollowers) {
		
		
		/*
		 * Config FrameLayout
		 */
		RelativeLayout frame =  (RelativeLayout) rootView.findViewById(R.id.frame_avatar);
		// Get the width and length of the screen
		
		
		frame.getLayoutParams().width = layoutWidth;
		frame.getLayoutParams().height = (int)(layoutHeight * 0.2);
		
		/*
		 * load avatar
		 */
		CircularImageView avatar = (CircularImageView)rootView.findViewById(R.id.avatar_image_id);
		
		if (avatarUrl == null) avatar.setVisibility(View.INVISIBLE);
		//avatar.setPadding(10, frame.getLayoutParams().height * 0.2,null,null);
		
		//BasicFunctions.ResizeImageView(layoutWidth, height, imageView);
		avatar.getLayoutParams().height = (int) (frame.getLayoutParams().height *0.5 );
		avatar.getLayoutParams().width = avatar.getLayoutParams().height;
		
		ImageLoader imgLoader = new ImageLoader(getActivity().getApplicationContext());

		// Loader image - will be shown before loading image
		int loader = R.drawable.image_not_found;
		
		imgLoader.DisplayImage(avatarUrl, loader,avatar);

		/*
		 * load fullname
		 * 
		 */
		TextView fullName = (TextView) rootView.findViewById(R.id.fullname);
		fullName.setText(fullNameString);
		
		
		/*
		 * load follower
		 */
		TextView follower = (TextView) rootView.findViewById(R.id.follower);
		if (numFollowers < 0 ) follower.setVisibility(View.INVISIBLE);
		
		follower.setText(String.valueOf(numFollowers)+ " followers");
	}

	private class loadSongBackground extends AsyncTask<String, String, String>{

		ProgressDialog pDialog =  new ProgressDialog(getActivity().getApplicationContext());
		String stringResponse;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			//super.onPreExecute();
			
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Downloading Song......");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			
			pDialog.show();
			
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			SongController songController = SongController.getInstance();
			songController.initialSongCategory();
			
			SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
			ApiWrapper wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null, soundCloudUserController.getToken());
			try {
				HttpResponse resp = wrapper.get(Request.to(ME_FAVORITES));
				stringResponse = Http.getString(resp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			MainActivity.isExplore = true;
			Intent i = new Intent(getActivity(), MainActivity.class);
			SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
			soundCloudUserController.setResponseString(stringResponse);
			Bundle bundle = soundCloudUserController.getBundle(soundCloudUserController.getCurrentUser());
			i.putExtra(USER, bundle);
			i.putExtra(ME_FAVORITES,stringResponse);
			
			startActivity(i);
			
	
			 
			
			pDialog.dismiss();
		}
		
	}
	
	
}
