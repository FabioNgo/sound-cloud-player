/**
 * 
 */
package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.UserControllerFactory;
import ngo.music.soundcloudplayer.general.Contants;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author LEBAO_000
 *
 */
public class LoginActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.login_layout);
	
	    Button loginFacebook =  (Button)findViewById(R.id.login_facebook_button);
		loginFacebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UserControllerFactory.createUserController(Contants.FACEBOOK_USER).login();
				// TODO Auto-generated method stub
				
				
			}
		});
		
		Button loginGooglePlus = (Button) findViewById(R.id.login_google_plus_button);
		loginGooglePlus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserControllerFactory.createUserController(Contants.GOOGLE_PLUS_USER).login();
			}
		});
		
		Button loginSoundCloud = (Button) findViewById(R.id.login_soundcloud_button);
		loginSoundCloud.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SoundCloudLoginUI soundCloudLoginUI =  new SoundCloudLoginUI();
				FragmentManager fragmentManager = getFragmentManager();
				int fragmentTransaction = fragmentManager.beginTransaction()
														.replace(R.id.login_layout, soundCloudLoginUI)
														.commit();
				// TODO Auto-generated method stub

			}
		});
	    // TODO Auto-generated method stub
	}

}
