/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;


/**
 * @author LEBAO_000
 *
 */
public class ElectroFragment extends SoundCloudExploreFragment {
	
	private static ElectroFragment instance = null; 

	public ElectroFragment(){
		super();
		category  = Constants.SoundCloudExploreConstant.ELECTRO;
		current_page = 1;
	}
	
	
	
	public static ElectroFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new ElectroFragment();
		}
		return instance;
	}

}
