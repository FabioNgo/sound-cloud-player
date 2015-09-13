package ngo.music.player.boundary.fragment.real;

import ngo.music.player.boundary.fragment.abstracts.CategoryListContentFragment;

public class ArtistsFragment extends CategoryListContentFragment  {
	public static ArtistsFragment instance = null;
	
	

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return ARTIST;
	}
}
