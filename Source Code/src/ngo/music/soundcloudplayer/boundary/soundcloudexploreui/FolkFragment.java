/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;


/**
 * @author LEBAO_000
 *
 */
public class FolkFragment extends SoundCloudExploreFragment {
	
	private static FolkFragment instance = null; 

	
	
	
	public static FolkFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new FolkFragment();
		}
		return instance;
	}




	@Override
	protected int setCategory() {
		// TODO Auto-generated method stub
		return FOLK;
	}




	@Override
	protected int setCurrentPage() {
		// TODO Auto-generated method stub
		return 1;
	}

}
