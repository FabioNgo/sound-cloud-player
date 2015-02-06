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

	
	
	
	public static ElectronicFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new ElectronicFragment();
		}
		return instance;
	}




	@Override
	protected int setCategory() {
		// TODO Auto-generated method stub
		return ELECTRONIC;
	}




	@Override
	protected int setCurrentPage() {
		// TODO Auto-generated method stub
		return 1;
	}

}
