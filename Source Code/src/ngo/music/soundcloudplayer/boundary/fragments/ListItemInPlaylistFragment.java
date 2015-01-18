package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Song;

public class ListItemInPlaylistFragment extends
		ListItemsInCompositionListFragment {

	public ListItemInPlaylistFragment(ArrayList<Song> songs, String cat) {
		super(songs, cat);
		type = PLAYLIST;
		// TODO Auto-generated constructor stub
	}

}
