/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;


/**
 * @author LEBAO_000
 *
 */
public class DiscoFragment extends SoundCloudExploreFragment {
	
	private static DiscoFragment instance = null; 

	
	
	
	
	public static DiscoFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new DiscoFragment();
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
