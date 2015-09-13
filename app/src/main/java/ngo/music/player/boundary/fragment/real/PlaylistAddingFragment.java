package ngo.music.player.boundary.fragment.real;

import java.util.ArrayList;

import ngo.music.player.boundary.fragment.abstracts.CategoryAddingFragment;
import ngo.music.player.entity.Song;

public class PlaylistAddingFragment extends CategoryAddingFragment {

	public PlaylistAddingFragment(ArrayList<Song> songs) {
		super(songs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return PLAYLIST;
	}

}
