package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Song;

public class ListItemInSCPlaylistSearchFragment extends
		ListItemsInCompositionListFragment {

	public ListItemInSCPlaylistSearchFragment(ArrayList<Song> songs, String cat) {
		super(songs, cat);
		type = SC_SEARCH_PLAYLIST;
		// TODO Auto-generated constructor stub
	}

}
