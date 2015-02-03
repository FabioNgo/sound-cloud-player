package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Song;

public class ListItemInAlbumFragment extends
		ListItemsInCompositionListFragment {

	ListItemInAlbumFragment(ArrayList<Song> songs, String cat) {
		super(songs, cat);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return ALBUM;
	}


}
