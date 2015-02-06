/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;


/**
 * @author LEBAO_000
 *
 */
public class DubstepFragment extends SoundCloudExploreFragment {
	
	private static DubstepFragment instance = null; 

	
	
	
	
	public static DubstepFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new DubstepFragment();
		}
		return instance;
	}





	@Override
	protected int setCategory() {
		// TODO Auto-generated method stub
		return DUBSTEP;
	}





	@Override
	protected int setCurrentPage() {
		// TODO Auto-generated method stub
		return 1;
	}

}
