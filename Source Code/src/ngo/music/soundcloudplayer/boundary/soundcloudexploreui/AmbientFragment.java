/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;


/**
 * @author LEBAO_000
 *
 */
public class AmbientFragment extends SoundCloudExploreFragment {
	
	private static AmbientFragment instance = null; 

	public AmbientFragment(){
		super();
		category  = Constants.SoundCloudExploreConstant.AMBIENT;
		current_page = 1;
	}
	
	
	
	public static AmbientFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new AmbientFragment();
		}
		return instance;
	}

}
