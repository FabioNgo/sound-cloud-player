package ngo.music.player.View.fragment.real;

import java.util.ArrayList;

import ngo.music.player.Model.Song;
import ngo.music.player.View.fragment.abstracts.CategoryAddingFragment;

public class PlaylistAddingFragment extends CategoryAddingFragment {

	public PlaylistAddingFragment(){
		super();
	}
	/**
	 * @param songs
	 */
	public PlaylistAddingFragment(ArrayList<Song> songs) {
		super(songs);
	}

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return PLAYLIST;
	}

}
