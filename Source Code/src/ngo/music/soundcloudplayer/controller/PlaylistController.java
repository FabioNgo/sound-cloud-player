package ngo.music.soundcloudplayer.controller;

import ngo.music.soundcloudplayer.general.Constants;

public class PlaylistController extends CategoryController implements Constants.Data, Constants {

	private static PlaylistController instance = null;
	
	
	PlaylistController() {
		// TODO Auto-generated constructor stub
		// playlists = new ArrayMap<String, ArrayList<Song>>();
		
		instance = this;
		filename = "playlists";
		categories = getCategories();
		TAG_DATA_CHANGED = PLAYLIST_CHANGED;
		TAG_ITEM_CHANGED = ITEM_IN_PLAYLIST_CHANGED;
	}

	public static PlaylistController getInstance() {
		if (instance == null) {
			new PlaylistController();
		}

		return instance;

	}

	

}
