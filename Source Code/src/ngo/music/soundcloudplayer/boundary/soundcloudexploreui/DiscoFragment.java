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

	public DiscoFragment(){
		super();
		category  = Constants.SoundCloudExploreConstant.DISCO;
		current_page = 1;
	}
	
	
	
	public static DiscoFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new DiscoFragment();
		}
		return instance;
	}

}
