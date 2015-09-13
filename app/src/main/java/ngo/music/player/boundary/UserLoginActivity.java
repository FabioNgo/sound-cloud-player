/**
 * 
 */
package ngo.music.player.boundary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import org.json.JSONException;

import java.io.IOException;

import ngo.music.player.controller.SCUserController;
import ngo.music.player.database.DatabaseCreate;
import ngo.music.player.database.SCLoginDatabaseTable;
import ngo.music.player.entity.User;
import ngo.music.player.helper.BasicFunctions;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.States;
import ngo.music.player.service.MusicPlayerService;
import ngo.music.player.R;

/**
 * @author LEBAO_000
 *
 */
public class UserLoginActivity extends FragmentActivity implements
		Constants.UserContant {
	SCLoginUI soundcloudLoginUI = null;
	GoogleLoginUI googleLoginUI = null;
	FacebookLoginUI facebookLoginUI = null;
	GeneralLoginUI generalLoginUI = null;
	private static UserLoginActivity activity;
	
	public static UserLoginActivity getActivity() {
		return activity;
	}
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		//AppController.printKeyHash(this);
		//System.out.println ("USER LOGIN ACTIVITY ON CREATE");
		DatabaseCreate databaseCreate =  new DatabaseCreate(getActivity());
		databaseCreate.createTables();
		MusicPlayerService.getInstance();
		SCLoginDatabaseTable databaseHandler = SCLoginDatabaseTable.getInstance(getActivity());
		if (BasicFunctions.isConnectingToInternet(activity)) {
			System.out.println ("LOGIN STAT " + States.loginState);
			if (databaseHandler.isLoggedIn() && States.loginState == LOGGED_IN) {
				String token = databaseHandler.getToken();
				
				new Background(token).execute();

				return;
			}
			
		}else{
			
			//System.out.println ("NO INTERNET");
			States.loginState = NOT_LOGGED_IN;
			//Bundle bundle = userController.getBundle(currentUser);
			Intent goToMainActivity = new Intent(
					getApplicationContext(), MusicPlayerMainActivity.class);
			//goToMainActivity.putExtra(USER, bundle);
			
			startActivity(goToMainActivity);
			finish();
		}
		
		/**
		 * Music Player Service
		 */
	//	private void configMusicPlayerService() {
//			if (!isMyServiceRunning()) {
			//Intent musicPlayerServiceIntent = new Intent(this, MusicPlayerService.class);
//	        bindService(musicPlayerServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
			//	startService(musicPlayerServiceIntent);
//			} else {
				
//			}
		//}
		setContentView(R.layout.login_layout);

		changeFragment(new GeneralLoginUI());


		// TODO Auto-generated method stub
	}

	public void changeFragment(Fragment fragment) {

		// Not connect to internet
		// if (!BasicFunctions.isConnectingToInternet(activity)){
		// Toast.makeText(activity, "No internet connection",
		// Toast.LENGTH_LONG).show();
		// return;
		// }
		if (fragment instanceof SCLoginUI) {
			if (soundcloudLoginUI == null) {
				soundcloudLoginUI = new SCLoginUI();

			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.login_layout, soundcloudLoginUI)
					.addToBackStack("soundcloudLogin").commit();
		}
		if (fragment instanceof GoogleLoginUI) {
			if (googleLoginUI == null) {
				googleLoginUI = new GoogleLoginUI();

			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.login_layout, googleLoginUI)
					.addToBackStack("googleLogin").commit();
		}
		if (fragment instanceof FacebookLoginUI) {
			if (facebookLoginUI == null) {
				facebookLoginUI = new FacebookLoginUI();

			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.login_layout, facebookLoginUI)
					.addToBackStack("facebookLogin").commit();
		}
		if (fragment instanceof GeneralLoginUI) {
			if (generalLoginUI == null) {
				generalLoginUI = new GeneralLoginUI();

			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.login_layout, generalLoginUI)
					.addToBackStack("generalLogin").commit();
		}

	}

	private class Background extends AsyncTask<String, String, String> {

		private static final String USERNAME_LOGIN = "baoloc1403@gmail.com";
		private static final String PASSWORD_LOGIN = "ngolebaoloc";

		private ProgressDialog pDialog;
		String token;
		String username;
		String password;
		boolean isLogin = false;

		public Background(String token) {
			this.token = token;
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pDialog = new ProgressDialog(UserLoginActivity.this);
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
					SCUserController userController = SCUserController
							.getInstance();
					User currentUser = null;
					try {
						System.out.println (token);
//						userController.setToken(new Token(token, "refresh-token"));
//						userController.login();
						
						
						 currentUser = userController.getCurrentUser();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					
//					username = USERNAME_LOGIN;
//					password = PASSWORD_LOGIN;
//					User currentUser = userController.validateLogin(username,
//							password);

					// Cannot login
					if (currentUser == null) {
						pDialog.dismiss();
						isLogin = false;
					} else {
						
						Bundle bundle = userController.getBundle(currentUser);
						Intent goToMainActivity = new Intent(
								getApplicationContext(), MusicPlayerMainActivity.class);
						goToMainActivity.putExtra(USER, bundle);
						States.loginState = LOGGED_IN;
						startActivity(goToMainActivity);
						finish();
					}

				}
			});

			background.start();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pDialog.dismiss();

		}

	}

}
