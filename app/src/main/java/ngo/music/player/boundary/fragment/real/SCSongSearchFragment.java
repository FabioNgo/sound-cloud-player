package ngo.music.player.boundary.fragment.real;

import ngo.music.player.boundary.MusicPlayerMainActivity;
import ngo.music.player.boundary.fragment.abstracts.SoundCloudExploreFragment;

public class SCSongSearchFragment extends SoundCloudExploreFragment {

	public SCSongSearchFragment() {
		// TODO Auto-generated constructor stub
		super();
		query = MusicPlayerMainActivity.query;
	}

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return SEARCH;
	}

	
}
