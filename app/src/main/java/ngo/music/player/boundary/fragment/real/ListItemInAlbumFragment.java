package ngo.music.player.boundary.fragment.real;

import java.util.ArrayList;

import ngo.music.player.boundary.fragment.abstracts.ListItemsInCompositionListFragment;
import ngo.music.player.entity.Song;

public class ListItemInAlbumFragment extends
		ListItemsInCompositionListFragment {

	public ListItemInAlbumFragment(ArrayList<Song> songs, String cat) {
		super(songs, cat);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return ALBUM;
	}


}
