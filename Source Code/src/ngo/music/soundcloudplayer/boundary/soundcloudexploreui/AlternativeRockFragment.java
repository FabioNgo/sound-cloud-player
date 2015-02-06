/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;


/**
 * @author LEBAO_000
 *
 */
public class AlternativeRockFragment extends SoundCloudExploreFragment {
	
	private static AlternativeRockFragment instance = null; 

	
	
	
	
	public static AlternativeRockFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new AlternativeRockFragment();
		}
		return instance;
	}





	@Override
	protected int setCategory() {
		// TODO Auto-generated method stub
		return ALTERNATIVE_ROCK;
	}





	@Override
	protected int setCurrentPage() {
		// TODO Auto-generated method stub
		return 1;
	}

}
