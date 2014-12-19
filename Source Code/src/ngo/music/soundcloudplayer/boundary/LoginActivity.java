/**
 * 
 */
package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.UserControllerFactory;
import ngo.music.soundcloudplayer.general.Contants;
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
public class LoginActivity extends FragmentActivity {
	SoundCloudLoginUI soundcloudLoginUI = null;
	GoogleLoginUI googleLoginUI = null;
	FacebookLoginUI facebookLoginUI = null;
	GeneralLoginUI generalLoginUI = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		changeFragment(new GeneralLoginUI());
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
	

}
