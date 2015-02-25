/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.fragment.real;

import ngo.music.soundcloudplayer.boundary.fragment.abstracts.SoundCloudExploreFragment;
import ngo.music.soundcloudplayer.general.Constants;

/**
 * @author LEBAO_000
 *
 */
public class TrendingAudioFragment extends SoundCloudExploreFragment {

	private static TrendingAudioFragment instance = null;

	public static TrendingAudioFragment getInstance() {
		// TODO Auto-generated method stub
		if (instance == null) {
			instance = new TrendingAudioFragment();
		}
		return instance;
	}

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return TRENDING_AUDIO;
	}


}
