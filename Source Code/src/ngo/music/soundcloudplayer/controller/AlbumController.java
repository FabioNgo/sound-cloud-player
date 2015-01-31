package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.general.Constants;

public class AlbumController extends CategoryController implements
		Constants.Data, Constants {
	private static AlbumController instance = null;

	public AlbumController() {
		// TODO Auto-generated constructor stub
		// playlists = new ArrayMap<String, ArrayList<Song>>();

		instance = this;
		categories = getCategories();
		TAG_DATA_CHANGED = ALBUM_CHANGED;
		TAG_ITEM_CHANGED = ITEM_IN_ALBUM_CHANGED;
	}

	public static AlbumController getInstance() {
		if (instance == null) {
			new AlbumController();
		}

		return instance;

	}

	@Override
	public ArrayList<Category> getCategories() {
		// TODO Auto-generated method stub
		ArrayList<Category> cate = SongController.getInstance().getOfflineAlbums();
		return cate;
	}

	@Override
	public void storeCategories() throws IOException {
		// TODO Auto-generated method stub
		
	}
}
