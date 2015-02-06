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

	
	
	public static ElectroFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new ElectroFragment();
		}
		return instance;
	}



	@Override
	protected int setCategory() {
		// TODO Auto-generated method stub
		return ELECTRO;
	}



	@Override
	protected int setCurrentPage() {
		// TODO Auto-generated method stub
		return 1;
	}

}
