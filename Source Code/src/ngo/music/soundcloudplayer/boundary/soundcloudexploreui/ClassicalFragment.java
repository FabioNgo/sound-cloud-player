/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;


/**
 * @author LEBAO_000
 *
 */
public class ClassicalFragment extends SoundCloudExploreFragment {
	
	private static ClassicalFragment instance = null; 

	public ClassicalFragment(){
		super();
		category  = Constants.SoundCloudExploreConstant.CLASSICAL;
		current_page = 1;
	}
	
	
	
	public static ClassicalFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new ClassicalFragment();
		}
		return instance;
	}

}
