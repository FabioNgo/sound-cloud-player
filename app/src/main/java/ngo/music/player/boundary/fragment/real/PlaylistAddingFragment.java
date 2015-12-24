package ngo.music.player.boundary.fragment.real;

import ngo.music.player.Model.Song;
import ngo.music.player.boundary.fragment.abstracts.CategoryAddingFragment;

public class PlaylistAddingFragment extends CategoryAddingFragment {

	public PlaylistAddingFragment(Song[] songs) {
		super(songs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return PLAYLIST;
	}

}
