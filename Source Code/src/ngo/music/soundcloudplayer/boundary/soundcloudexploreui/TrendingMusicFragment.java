/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;

import com.facebook.FacebookRequestError.Category;


/**
 * @author LEBAO_000
 *
 */
public class TrendingMusicFragment extends SoundCloudExploreFragment {
	
	private static TrendingMusicFragment instance = null; 

	
	private TrendingMusicFragment(){
		super();
		category  = Constants.SoundCloudExploreConstant.TRENDING_MUSIC;
		current_page = 1;
	}
	
	
	
	public static TrendingMusicFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new TrendingMusicFragment();
			
		
		}
		return instance;
	}

}
