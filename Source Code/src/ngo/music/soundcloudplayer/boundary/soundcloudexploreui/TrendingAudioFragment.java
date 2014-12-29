/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;


/**
 * @author LEBAO_000
 *
 */
public class TrendingAudioFragment extends SoundCloudExploreFragment {
	
	private static TrendingAudioFragment instance = null; 

	public TrendingAudioFragment(){
		super();
		category  = Constants.SoundCloudExploreConstant.TRENDING_AUDIO;
		current_page = 1;
	}
	
	
	
	public static TrendingAudioFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new TrendingAudioFragment();
		}
		return instance;
	}

}
