package ngo.music.player.boundary.fragment.real;

import ngo.music.player.boundary.fragment.abstracts.CategoryListContentFragment;

public class AlbumsFragment extends CategoryListContentFragment  {
	public static AlbumsFragment instance = null;
	
	
	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return ALBUM;
	}


	
}
