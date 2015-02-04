package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Song;

public class SCPlaylistAddingFragment extends CategoryAddingFragment {

	public SCPlaylistAddingFragment(ArrayList<Song> songs) {
		super(songs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return SC_PLAYLIST;
	}

}
