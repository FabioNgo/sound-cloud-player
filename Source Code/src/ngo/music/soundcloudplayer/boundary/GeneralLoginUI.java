package ngo.music.soundcloudplayer.boundary;

import java.io.IOException;
import java.util.zip.Inflater;

import org.json.JSONException;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.SharedPreferencesTokenCachingStrategy;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.controller.UserControllerFactory;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.TextView;
import android.widget.Toast;


/**
 * Login UI of SoundCloud
 * @author LEBAO_000
 *
 */
public class GeneralLoginUI extends Fragment implements Constants.UserContant {

	
	private LoginButton loginFacebookButton;
	private ProfilePictureView profilePictureView;
	private TextView userNameView;
	private GraphUser pendingUpdateForUser;
	private StatusCallback sessionStatusCallback;
	private Session currentSession;



	public GeneralLoginUI() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View rootView = inflater.inflate(R.layout.login_general_layout,container,false);
		
		/*
		 * Login Google Plus
		 */
		Button loginGooglePlus = (Button) rootView.findViewById(R.id.login_google_plus_button);
		loginGooglePlus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (BasicFunctions.isConnectingToInternet(getActivity())){
					try {
						UserControllerFactory.createUserController(Constants.GOOGLE_PLUS_USER).login();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
					return;
				}
				
			}
		});
		
		/*
		 * Login Sound Cloud
		 */
//		Button loginSoundCloud = (Button) rootView.findViewById(R.id.login_soundcloud_button);
//		loginSoundCloud.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if (BasicFunctions.isConnectingToInternet(getActivity())){
//					SCLoginUI soundCloudLoginUI =  new SCLoginUI();
//					((UserLoginActivity)getActivity()).changeFragment(soundCloudLoginUI);
//				}else{
//					Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
//					return;
//				}
//				
//				// TODO Auto-generated method stub
//
//			}
//		});
		
		/*
		 * Login Facebook
		 */
		loginFacebookButton = (LoginButton) rootView.findViewById(R.id.login_facebook_button);
      //  loginFacebookButton.setReadPermissions("user_friends");
      // handleLoginFacebook();
//        if (pendingUpdateForUser != null) {
//            updateViewForUser(pendingUpdateForUser);
//            pendingUpdateForUser = null;
//        }
		loginFacebookButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (BasicFunctions.isConnectingToInternet(getActivity())){
					//UserControllerFactory.createUserController(Constants.FACEBOOK_USER).login();
					
					((UserLoginActivity) getActivity()).changeFragment(new FacebookLoginUI());
				}else{
					Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
					return;
				}
				
				// TODO Auto-generated method stub
				
				
			}
		});
		
		/*
		 * Without login
		 */
		Button withoutLogin = (Button)rootView.findViewById(R.id.continue_not_login_button);
		withoutLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mainActivity = new Intent(getActivity(), MusicPlayerMainActivity.class);
				startActivity(mainActivity);
				// TODO Auto-generated method stub
				
			}
		});
		return rootView;
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
		bundle.putString(URI_LINK, user.getUri());
		bundle.putInt(TRACK_COUNT, user.getTrackCount());
		
		
		return bundle;
	}
	
	 public void updateViewForUser(GraphUser user) {
	        if (userNameView == null || profilePictureView == null || !isAdded()) {
	            // Fragment not yet added to the view. So let's store which user was intended
	            // for display.
	            pendingUpdateForUser = user;
	            return;
	        }

	        if (user == null) {
	            profilePictureView.setProfileId(null);
	            userNameView.setText("No user");
	        } else {
	            profilePictureView.setProfileId(user.getId());
	            System.out.println(user.getFirstName());
	        }
	  }
	 
	  private void fetchUserInfo() {
	        if (currentSession != null && currentSession.isOpened()) {
	            Request request = Request.newMeRequest(currentSession, new Request.GraphUserCallback() {
	                @Override
	                public void onCompleted(GraphUser me, Response response) {
	                	System.out.println (me.getFirstName());
	                    if (response.getRequest().getSession() == currentSession) {
	                       // updateFragments(me);
	                    }
	                }
	            });
	            request.executeAsync();
	        }
	    }
	  
	  
	  private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	        if (session != currentSession) {
	            return;
	        }

	        if (state.isOpened()) {
	            // Log in just happened.
	            fetchUserInfo();
	          //  showProfile();
	        } else if (state.isClosed()) {
	            // Log out just happened. Update the UI.
	           // updateFragments(null);
	        }
	    }
	  
	  
	  private void handleLoginFacebook(){
		  sessionStatusCallback = new Session.StatusCallback() {
	          @Override
	          public void call(Session session, SessionState state, Exception exception) {
	              onSessionStateChange(session, state, exception);
	          }
	      };

	          
//
//	          SharedPreferencesTokenCachingStrategy restoredCache = new SharedPreferencesTokenCachingStrategy(
//	                  this,
//	                  savedInstanceState.getString(TOKEN_CACHE_NAME_KEY));
//	          			currentSession = Session.restoreSession(
//	                  this,
//	                  restoredCache,
//	                  sessionStatusCallback,
//	                  savedInstanceState);
//	      
//		  
	  }
	  
	
	
}
