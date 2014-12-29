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

	public DubstepFragment(){
		super();
		category  = Constants.SoundCloudExploreConstant.DUBSTEP;
		current_page = 1;
	}
	
	
	
	public static DubstepFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new DubstepFragment();
		}
		return instance;
	}

}
