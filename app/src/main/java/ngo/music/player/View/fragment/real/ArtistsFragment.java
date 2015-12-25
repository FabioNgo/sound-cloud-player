package ngo.music.player.View.fragment.real;

import java.util.Observable;

import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.View.fragment.abstracts.CategoryListContentFragment;

public class ArtistsFragment extends CategoryListContentFragment  {
	public static ArtistsFragment instance = null;
	
	

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return ARTIST;
	}



	@Override
	public void update(Observable observable, Object data) {

	}
}
