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

	
	
	
	
	public static AmbientFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new AmbientFragment();
		}
		return instance;
	}





	@Override
	protected int setCategory() {
		// TODO Auto-generated method stub
		return AMBIENT;
	}





	@Override
	protected int setCurrentPage() {
		// TODO Auto-generated method stub
		return 1;
	}

}
