package ngo.music.player.View.fragment.real;

import java.util.Observable;

import ngo.music.player.View.fragment.abstracts.CategoryListContentFragment;

public class PlaylistFragment extends CategoryListContentFragment {
	public static PlaylistFragment instance;

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return PLAYLIST;
	}



	@Override
	public void update(Observable observable, Object data) {

	}
}
