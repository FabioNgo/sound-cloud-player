/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import ngo.music.soundcloudplayer.general.Constants;


/**
 * @author LEBAO_000
 *
 */
public class DrumBassFragment extends SoundCloudExploreFragment {
	
	private static DrumBassFragment instance = null; 

	private DrumBassFragment(){
		super();
		category  = Constants.SoundCloudExploreConstant.DRUM_BASS;
		current_page = 1;
	}
	
	
	
	public static DrumBassFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new DrumBassFragment();
		}
		return instance;
	}

}
