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

	
	
	
	
	public static CountryFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new CountryFragment();
		}
		return instance;
	}





	@Override
	protected int setCategory() {
		// TODO Auto-generated method stub
		return COUNTRY;
	}





	@Override
	protected int setCurrentPage() {
		// TODO Auto-generated method stub
		return 1;
	}

}
