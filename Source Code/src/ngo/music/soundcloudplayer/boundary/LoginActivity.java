/**
 * 
 */
package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.SoundCloudLoginUI.Background;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import ngo.music.soundcloudplayer.controller.UserController;
import ngo.music.soundcloudplayer.controller.UserControllerFactory;
import ngo.music.soundcloudplayer.database.DatabaseHandler;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.Constants;
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

/**
 * @author LEBAO_000
 *
 */
public class LoginActivity extends FragmentActivity implements Constants.UserContant {
	SoundCloudLoginUI soundcloudLoginUI = null;
	GoogleLoginUI googleLoginUI = null;
	FacebookLoginUI facebookLoginUI = null;
	GeneralLoginUI generalLoginUI = null;
	public LoginActivity activity = this;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
		System.out.println(databaseHandler.isUserLoggedIn());
		if (databaseHandler.isUserLoggedIn()){
			String[] userInfo = databaseHandler.getUserInfo();
			new Background(userInfo[0], userInfo[1]).execute();
			
			return;
		}else{
			setContentView(R.layout.login_layout);
			changeFragment(new GeneralLoginUI());
		}
		// TODO Auto-generated method stub
	}

	public void changeFragment(Fragment fragment) {
		if (fragment instanceof SoundCloudLoginUI) {
			if (soundcloudLoginUI == null) {
				soundcloudLoginUI = new SoundCloudLoginUI();
				
			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.login_layout, soundcloudLoginUI).addToBackStack("soundcloudLogin").commit();
		}
		if (fragment instanceof GoogleLoginUI) {
			if (googleLoginUI == null) {
				googleLoginUI = new GoogleLoginUI();
				
			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.login_layout, googleLoginUI).addToBackStack("googleLogin").commit();
		}
		if (fragment instanceof FacebookLoginUI) {
			if (facebookLoginUI == null) {
				facebookLoginUI = new FacebookLoginUI();
				
			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.login_layout, facebookLoginUI).addToBackStack("facebookLogin").commit();
		}
		if (fragment instanceof GeneralLoginUI) {
			if (generalLoginUI == null) {
				generalLoginUI = new GeneralLoginUI();
				
			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.login_layout, generalLoginUI).addToBackStack("generalLogin").commit();
		}

	}
	
	private class Background extends AsyncTask<String, String, String>{

		private static final String USERNAME_LOGIN = "baoloc1403@gmail.com";
		private static final String PASSWORD_LOGIN = "ngolebaoloc";
		
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
					SoundCloudUserController userController = SoundCloudUserController.getInstance();
					username = USERNAME_LOGIN;
					password = PASSWORD_LOGIN;
					User currentUser = userController.validateLogin(username, password);
					
					//Cannot login
					if (currentUser == null){
						pDialog.dismiss();
						isLogin = false;
					}else{
						DatabaseHandler databaseHandler = DatabaseHandler.getInstance(getApplicationContext());
						databaseHandler.addLoginInfo(username, password);
						Bundle bundle  = userController.getBundle(currentUser);
						Intent goToMainActivity  =  new Intent(getApplicationContext(), MainActivity.class);
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
	

}
