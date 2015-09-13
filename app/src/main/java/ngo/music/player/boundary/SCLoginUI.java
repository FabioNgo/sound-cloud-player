package ngo.music.player.boundary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ngo.music.player.controller.SCUserController;
import ngo.music.player.database.DatabaseCreate;
import ngo.music.player.entity.User;
import ngo.music.player.helper.Constants;
import ngo.music.player.R;


/**
 * Login UI of SoundCloud
 * @author LEBAO_000
 *
 */
public class SCLoginUI extends Fragment implements Constants.UserContant {


	
	private static final String USERNAME_LOGIN = "baoloc1403@gmail.com";
	private static final String PASSWORD_LOGIN = "ngolebaoloc";
	View rootView;
	public SCLoginUI() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		rootView = inflater.inflate(R.layout.login_soundcloud_layout,container,false);
		configLayout();
		
		return rootView;
	}

	/**
	 * @param
	 */
	private void configLayout() {
		final EditText username = (EditText)rootView.findViewById(R.id.username_soundcloud);
		final EditText password = (EditText) rootView.findViewById(R.id.password_soundcloud);
		
		
		Button loginButton = (Button) rootView.findViewById(R.id.login_button);
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String usernameStr = username.getText().toString();
				String passwordStr = password.getText().toString();
				//Background background = new  Background(usernameStr, passwordStr);
				//background.execute();

				
			}
		});
	}
	
	public class Background extends AsyncTask<String, String, String>{

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
					SCUserController userController = SCUserController.getInstance();
					username = USERNAME_LOGIN;
					password = PASSWORD_LOGIN;
					User currentUser = userController.validateLogin(username, password);
					
					//Cannot login
					if (currentUser == null){
						
						isLogin = false;
					}else{
						DatabaseCreate databaseHandler = DatabaseCreate.getInstance(getActivity());
						//databaseHandler.addLoginInfo(token);
						Bundle bundle  = userController.getBundle(currentUser);
						Intent goToMainActivity  =  new Intent(getActivity(), MusicPlayerMainActivity.class);
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
			
			pDialog.dismiss();
		}
		
	}
	
	
	
	
}
