package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Song;

public class ListItemInArtistFragment extends
		ListItemsInCompositionListFragment {

	public ListItemInArtistFragment(ArrayList<Song> songs, String cat) {
		super(songs, cat);
		type = ARTIST;
		// TODO Auto-generated constructor stub
	}

}
