package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.general.Constants;

public class ArtistController extends CategoryController{
	private static ArtistController instance = null;

	

	public static ArtistController getInstance() {
		if (instance == null) {
			instance = new ArtistController();
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

	@Override
	protected int setTagItemChange() {
		// TODO Auto-generated method stub
		return ITEM_IN_ARTIST_CHANGED;
	}

	@Override
	protected int setTagDataChange() {
		// TODO Auto-generated method stub
		return ARTIST_CHANGED;
	}
}
