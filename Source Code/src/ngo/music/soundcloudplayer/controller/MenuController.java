package ngo.music.soundcloudplayer.controller;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.boundary.fragments.CategoryAddingFragment;
import ngo.music.soundcloudplayer.boundary.fragments.PlaylistAddingFragment;
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
