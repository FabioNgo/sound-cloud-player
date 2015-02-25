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

	
	
	
	
	public static DanceFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new DanceFragment();
		}
		return instance;
	}





	@Override
	protected int setCategory() {
		// TODO Auto-generated method stub
		return DANCE_EDM;
	}





	@Override
	protected int setCurrentPage() {
		// TODO Auto-generated method stub
		return 1;
	}

}
