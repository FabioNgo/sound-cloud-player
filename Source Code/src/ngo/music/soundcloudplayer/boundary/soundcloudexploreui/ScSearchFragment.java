package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ngo.music.soundcloudplayer.general.Constants;

public class ScSearchFragment extends SoundCloudExploreFragment {
	private static TrendingMusicFragment instance = null; 

	
	public ScSearchFragment(){
		super();
		//category  = Constants.SoundCloudExploreConstant.TRENDING_MUSIC;
		current_page = 1;
	
	}
	
	
	
	public static TrendingMusicFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new TrendingMusicFragment();
			
		
		}
		
		return instance;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
