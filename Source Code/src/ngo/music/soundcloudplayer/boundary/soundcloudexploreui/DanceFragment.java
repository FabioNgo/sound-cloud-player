/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;


/**
 * @author LEBAO_000
 *
 */
public class DanceFragment extends SoundCloudExploreFragment {
	
	private static DanceFragment instance = null; 

	private DanceFragment(){
		super();
		category  = Constants.SoundCloudExploreConstant.DANCE;
		current_page = 1;
	}
	
	
	
	public static DanceFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new DanceFragment();
		}
		return instance;
	}

}
