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

	
	
	
	
	public static DeepHouseFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new DeepHouseFragment();
		}
		return instance;
	}





	@Override
	protected int setCategory() {
		// TODO Auto-generated method stub
		return DEEP_HOUSE;
	}





	@Override
	protected int setCurrentPage() {
		// TODO Auto-generated method stub
		return 1;
	}

}
