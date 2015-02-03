package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.general.Constants;

public class ArtistController extends CategoryController implements
		Constants.Data, Constants {
	private static ArtistController instance = null;

	public ArtistController() {
		// TODO Auto-generated constructor stub
		// playlists = new ArrayMap<String, ArrayList<Song>>();

		instance = this;
		categories = getCategories();
		TAG_DATA_CHANGED = ARTIST_CHANGED;
		TAG_ITEM_CHANGED = ITEM_IN_ARTIST_CHANGED;
	}

	public static ArtistController getInstance() {
		if (instance == null) {
			new ArtistController();
		}

		return instance;

	}

	@Override
	public ArrayList<Category> getCategories() {
		// TODO Auto-generated method stub
		ArrayList<Category> cate = SongController.getInstance().getOfflineArtists();
		return cate;
	}

	@Override
	public void storeCategories() throws IOException {
		// TODO Auto-generated method stub
		
	}
}
