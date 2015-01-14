package ngo.music.soundcloudplayer.boundary.fragments;

import android.support.v4.app.Fragment;

public class AlbumsFragment extends Fragment {
	public static AlbumsFragment instance = null;
	private AlbumsFragment() {
		
	}
	public static AlbumsFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new AlbumsFragment();
		}
		return instance;
	}
}
