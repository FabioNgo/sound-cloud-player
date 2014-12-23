package ngo.music.soundcloudplayer.boundary;

import android.support.v4.app.Fragment;

public class ArtistsFragment extends Fragment {
	public static ArtistsFragment instance = null;
	private ArtistsFragment() {
		
	}
	public static ArtistsFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new ArtistsFragment();
		}
		return instance;
	}

}
