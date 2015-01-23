package ngo.music.soundcloudplayer.controller;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.general.Constants;

public class SoundCloudPlaylistController extends CategoryController implements Constants.Data, Constants {

	private static SoundCloudPlaylistController instance = null;
	
	
	SoundCloudPlaylistController() {
		// TODO Auto-generated constructor stub
		// playlists = new ArrayMap<String, ArrayList<Song>>();
		
		instance = this;
		filename = "playlists";
		categories = getCategories();
		TAG_DATA_CHANGED = PLAYLIST_CHANGED;
		TAG_ITEM_CHANGED = ITEM_IN_PLAYLIST_CHANGED;
	}

	public static SoundCloudPlaylistController getInstance() {
		if (instance == null) {
			new SoundCloudPlaylistController();
		}

		return instance;

	}

	@Override
	public ArrayList<String> getCategoryName() {
		ArrayList<String> categoriesName = new ArrayList<String>();
		
		//ApiWrapper wrapper = SoundCloudUserController.getInstance().getApiWrapper();
		SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
		categoriesName = soundCloudUserController.getPlaylist();
		for (Category cate : categories) {
			categoriesName.add(cate.getTitle());
		}
		return categoriesName;
	}
	

}
