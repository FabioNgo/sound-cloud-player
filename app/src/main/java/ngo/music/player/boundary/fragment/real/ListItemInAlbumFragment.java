package ngo.music.player.boundary.fragment.real;

import android.annotation.SuppressLint;

import ngo.music.player.Model.Song;
import ngo.music.player.boundary.fragment.abstracts.ListItemsInCompositionListFragment;

@SuppressLint("ValidFragment")
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
