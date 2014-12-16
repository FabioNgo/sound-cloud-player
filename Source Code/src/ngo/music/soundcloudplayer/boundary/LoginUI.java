package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class LoginUI extends Fragment {

	public LoginUI() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.login, container, false);
		
		Button loginFacebook =  (Button)rootView.findViewById(R.id.login_facebook_button);
		loginFacebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
			}
		});
		
		Button loginGooglePlus = (Button) rootView.findViewById(R.id.login_google_plus_button);
		
		Button loginSoundCloud = (Button) rootView.findViewById(R.id.login_soundcloud_button);
		
		return rootView;
	}

}
