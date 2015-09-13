package ngo.music.player.boundary.fragment.real;

import ngo.music.player.boundary.fragment.abstracts.CategoryListContentFragment;

public class PlaylistFragment extends CategoryListContentFragment {
	public static PlaylistFragment instance;

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return PLAYLIST;
	}

}
