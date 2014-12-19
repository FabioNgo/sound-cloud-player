package ngo.music.soundcloudplayer.boundary;

import java.io.IOException;
import java.util.zip.Inflater;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.Contants;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.YuvImage;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Login UI of SoundCloud
 * @author LEBAO_000
 *
 */
public class SoundCloudLoginUI extends Fragment implements Contants.UserContant {

	public SoundCloudLoginUI() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View rootView = inflater.inflate(R.layout.login_soundcloud_layout,container,false);
		final EditText username = (EditText)rootView.findViewById(R.id.username_soundcloud);
		final EditText password = (EditText) rootView.findViewById(R.id.password_soundcloud);
		
		
		Button loginButton = (Button) rootView.findViewById(R.id.login_button);
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String usernameStr = username.getText().toString();
				String passwordStr = password.getText().toString();
				Background background = new  Background(usernameStr, passwordStr);
				background.execute();
				
				
//				SoundCloudUserController userController = new SoundCloudUserController();
//				Toast.makeText(getActivity(), userController.validateLogin(usernameStr, passwordStr), Toast.LENGTH_LONG).show();
				
			}
		});
		
		return rootView;
	}
	
	private class Background extends AsyncTask<String, String, String>{

		private ProgressDialog pDialog;
		String username;
		String password;
		boolean isLogin = false;
		
		public Background(String username, String password){
			this.username = username;
			this.password = password;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... arg) {
			// TODO Auto-generated method stub
			Thread background = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					SoundCloudUserController userController = new SoundCloudUserController();
					User currentUser = userController.validateLogin(username, password);
					
					//Cannot login
					if (currentUser == null){
						pDialog.dismiss();
						isLogin = false;
					}else{
						Bundle bundle  = getBundle(currentUser);
						isLogin = true;
						Intent goToMainActivity  =  new Intent(getActivity(), MainActivity.class);
						goToMainActivity.putExtra(USER, bundle);
						startActivity(goToMainActivity);
					}
				
					
				}
			});
			
			background.start();
			return null;
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			
		}
		
	}
	
	public Bundle getBundle (User user){
		Bundle bundle = new Bundle();
		
		bundle.putInt(ID, user.getId());
		bundle.putString(USERNAME, user.getUsername());
		bundle.putString(AVATAR_URL, user.getAvatarUrl());
		bundle.putString(CITY, user.getCity());
		bundle.putString(COUNTRY, user.getCountry());
		bundle.putString(DESCRIPTION, user.getDescription());
		bundle.putInt(FOLLOWERS_COUNT, user.getFollowersCount());
		bundle.putInt(FOLLOWINGS_COUNT, user.getFollowingCount());
		bundle.putString(FULLNAME, user.getFullName());
		bundle.putBoolean(ONLINE, user.isOnline());
		bundle.putInt(PLAYLIST_COUNT, user.getPlaylistCount());
		bundle.putString(PERMALINK, user.getPermalink());
		bundle.putString(PERMALINK_URL, user.getPermalinkUrl());
		bundle.putBoolean(PRIMARY_EMAIL_CONFIRMED, user.isPrimaryEmailConfirmed());
		bundle.putInt(PRIVATE_PLAYLISTS_COUNT, user.getPlaylistCount());
		bundle.putInt(PRIVATE_TRACK_COUNT, user.getPrivateTracksCount());
		bundle.putInt(PUBLIC_FAVORITES_COUNT,user.getPublicFavoriteCount());
		bundle.putString(URI, user.getUri());
		bundle.putInt(TRACK_COUNT, user.getTrackCount());
		
		
		return bundle;
	}
	
	
}
