package ngo.music.soundcloudplayer.controller;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.CategoryAddingFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.PlaylistAddingFragment;
import ngo.music.soundcloudplayer.entity.Song;

public class MenuController {
	private static MenuController instance = null;
	public static MenuController getInstance(){
		if(instance== null){
			instance = new MenuController();
		}
		return instance;
	}
	public void addToPlaylist(ArrayList<Song> songs){
		CategoryAddingFragment playlistAddingFragment = new PlaylistAddingFragment(songs);
		playlistAddingFragment.show(MusicPlayerMainActivity.getActivity().getSupportFragmentManager(), "New Playlist");
	}
}
