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

	private FolkFragment(){
		super();
		category  = Constants.SoundCloudExploreConstant.FOLK;
		current_page = 1;
	}
	
	
	
	public static FolkFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new FolkFragment();
		}
		return instance;
	}

}
