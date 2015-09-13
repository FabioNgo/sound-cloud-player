package ngo.music.player.boundary.fragment.real;

import ngo.music.player.boundary.fragment.abstracts.CategoryListContentFragment;

public class SCPlaylistFragment extends CategoryListContentFragment  {
	public static SCPlaylistFragment instance = null;
	
	

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return SC_PLAYLIST;
	}
}
