/**
 * 
 */
package ngo.music.soundcloudplayer.boundary;

import java.io.IOException;

import org.json.JSONException;

import com.volley.api.AppController;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.SCLoginUI.Background;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.controller.UserController;
import ngo.music.soundcloudplayer.controller.UserControllerFactory;
import ngo.music.soundcloudplayer.database.DatabaseHandler;
import ngo.music.soundcloudplayer.database.DatabaseTable;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.general.States;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author LEBAO_000
 *
 */
public class LoginActivity extends FragmentActivity implements
		Constants.UserContant {
	SCLoginUI soundcloudLoginUI = null;
	GoogleLoginUI googleLoginUI = null;
	FacebookLoginUI facebookLoginUI = null;
	GeneralLoginUI generalLoginUI = null;
	private static LoginActivity activity ;

	public static Activity getActivity(){
		return activity;
	}
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		//AppController.printKeyHash(this);
//		MusicPlayerService.getInstance();
		DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
		if (BasicFunctions.isConnectingToInternet(activity)) {
			if (databaseHandler.isUserLoggedIn()) {
				String token = databaseHandler.getUserInfo();
				new Background(token).execute();

				return;
			}
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
			pDialog = new ProgressDialog(LoginActivity.this);
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
//					SCUserController userController = SCUserController
//							.getInstance();
//					username = USERNAME_LOGIN;
//					password = PASSWORD_LOGIN;
//					User currentUser = userController.validateLogin(username,password);

					// Cannot login
//					if (currentUser == null) {
//						pDialog.dismiss();
//						isLogin = false;
//					} else {
//						DatabaseTable databaseHandler = DatabaseTable
//								.getInstance(getApplicationContext());
//						String token = databaseHandler.getToken();
						if (token != null)
							SCUserController.getInstance().setToken(new Token(token, "refresh-token"));
							
							try {
								SCUserController.getInstance().login();
								Bundle bundle = SCUserController.getInstance().getBundle(SCUserController.getInstance().getCurrentUser());
								
								Intent goToMainActivity = new Intent(
										getApplicationContext(), MusicPlayerMainActivity.class);
								goToMainActivity.putExtra(USER, bundle);
								States.loginState = LOGGED_IN;
								startActivity(goToMainActivity);
								finish();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								
							}
							
							
						}
						
//					}

//				}
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
