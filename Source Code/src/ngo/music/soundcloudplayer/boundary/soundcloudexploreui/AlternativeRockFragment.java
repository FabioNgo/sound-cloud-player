/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;


/**
 * @author LEBAO_000
 *
 */
public class AlternativeRockFragment extends SoundCloudExploreFragment {
	
	private static AlternativeRockFragment instance = null; 

	private AlternativeRockFragment(){
		super();
		category  = Constants.SoundCloudExploreConstant.ALTERNATIVE_ROCK;
		current_page = 1;
	}
	
	
	
	public static AlternativeRockFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new AlternativeRockFragment();
		}
		return instance;
	}

}
