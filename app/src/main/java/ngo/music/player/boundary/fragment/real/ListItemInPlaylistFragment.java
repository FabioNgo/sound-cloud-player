package ngo.music.player.boundary.fragment.real;

import ngo.music.player.Model.Song;
import ngo.music.player.boundary.fragment.abstracts.ListItemsInCompositionListFragment;

public class ListItemInPlaylistFragment extends
		ListItemsInCompositionListFragment {

	public ListItemInPlaylistFragment(Song[] songs, String cat) {
		super(songs, cat);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return PLAYLIST;
	}

}
