/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;


/**
 * @author LEBAO_000
 *
 */
public class DeepHouseFragment extends SoundCloudExploreFragment {
	
	private static DeepHouseFragment instance = null; 

	private DeepHouseFragment(){
		super();
		category  = Constants.SoundCloudExploreConstant.DEEP_HOUSE;
		current_page = 1;
	}
	
	
	
	public static DeepHouseFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new DeepHouseFragment();
		}
		return instance;
	}

}
