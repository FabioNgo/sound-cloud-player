package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Song;

public class ListItemInAlbumFragment extends
		ListItemsInCompositionListFragment {

	public ListItemInAlbumFragment(ArrayList<Song> songs, String cat) {
		super(songs, cat);
		type = ALBUM;
		// TODO Auto-generated constructor stub
	}

}
