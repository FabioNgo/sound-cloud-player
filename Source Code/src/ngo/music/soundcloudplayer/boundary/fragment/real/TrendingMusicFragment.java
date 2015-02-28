/**
 * 
 */
package ngo.music.soundcloudplayer.boundary.fragment.real;

import ngo.music.soundcloudplayer.boundary.fragment.abstracts.SoundCloudExploreFragment;

/**
 * @author LEBAO_000
 *
 */
public class TrendingMusicFragment extends SoundCloudExploreFragment {

	private static TrendingMusicFragment instance = null;

	public static TrendingMusicFragment getInstance() {
		// TODO Auto-generated method stub
		if (instance == null) {
			instance = new TrendingMusicFragment();

		}

		return instance;
	}

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return TRENDING_MUSIC;
	}

}
