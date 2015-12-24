package ngo.music.player.boundary.fragment.real;

import ngo.music.player.Model.Song;
import ngo.music.player.boundary.fragment.abstracts.ListItemsInCompositionListFragment;

public class ListItemInAlbumFragment extends
		ListItemsInCompositionListFragment {


	public ListItemInAlbumFragment(Song[] songs, String cat) {
		super(songs, cat);
	}

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return ALBUM;
	}


}
