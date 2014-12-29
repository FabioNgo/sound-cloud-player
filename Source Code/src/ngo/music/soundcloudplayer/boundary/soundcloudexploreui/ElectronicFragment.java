/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;


/**
 * @author LEBAO_000
 *
 */
public class ElectronicFragment extends SoundCloudExploreFragment {
	
	private static ElectronicFragment instance = null; 

	public ElectronicFragment(){
		super();
		category  = Constants.SoundCloudExploreConstant.ELECTRONIC;
		current_page = 1;
	}
	
	
	
	public static ElectronicFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new ElectronicFragment();
		}
		return instance;
	}

}
