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

	
	
	
	
	public static ClassicalFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new ClassicalFragment();
		}
		return instance;
	}





	@Override
	protected int setCategory() {
		// TODO Auto-generated method stub
		return CLASSICAL;
	}





	@Override
	protected int setCurrentPage() {
		// TODO Auto-generated method stub
		return 1;
	}

}
