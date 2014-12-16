package ngo.music.soundcloudplayer.boundary;

import java.util.zip.Inflater;

import ngo.music.soundcloudplayer.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Login UI of SoundCloud
 * @author LEBAO_000
 *
 */
public class SoundCloudLoginUI extends Fragment {

	public SoundCloudLoginUI() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.login_soundcloud_layout,container);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
