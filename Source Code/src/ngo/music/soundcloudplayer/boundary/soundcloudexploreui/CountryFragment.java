/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;


/**
 * @author LEBAO_000
 *
 */
public class CountryFragment extends SoundCloudExploreFragment {
	
	private static CountryFragment instance = null; 

	private CountryFragment(){
		super();
		category  = Constants.SoundCloudExploreConstant.COUNTRY;
		current_page = 1;
	}
	
	
	
	public static CountryFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new CountryFragment();
		}
		return instance;
	}

}
