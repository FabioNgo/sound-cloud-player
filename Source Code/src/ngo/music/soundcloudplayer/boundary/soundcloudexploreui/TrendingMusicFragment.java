/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;




/**
 * @author LEBAO_000
 *
 */
public class TrendingMusicFragment extends SoundCloudExploreFragment {
	
	private static TrendingMusicFragment instance = null; 

	
	
	
	
	
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
	@Override
	protected int setCategory() {
		// TODO Auto-generated method stub
		return TRENDING_MUSIC;
	}
	@Override
	protected int setCurrentPage() {
		// TODO Auto-generated method stub
		return 0;
	}

}
