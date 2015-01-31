package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Song;

public class ListItemInSCPlaylistFragment extends
		ListItemsInCompositionListFragment {

	public ListItemInSCPlaylistFragment(ArrayList<Song> songs, String cat) {
		super(songs, cat);
		type = SC_PLAYLIST;
		// TODO Auto-generated constructor stub
	}

}
