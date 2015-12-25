package ngo.music.player.View.fragment.real;

import java.util.Observable;

import ngo.music.player.View.fragment.abstracts.CategoryListContentFragment;

public class AlbumsFragment extends CategoryListContentFragment  {
	public static AlbumsFragment instance = null;
	
	
	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return ALBUM;
	}



	@Override
	public void update(Observable observable, Object data) {

	}
}
