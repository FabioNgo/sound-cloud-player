package ngo.music.player.controller;

import java.util.ArrayList;

import ngo.music.player.entity.Category;

public class ArtistController extends ReadOnlyOfflineCategoryController{
	static ArtistController instance = null;



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


	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return ARTIST;
	}


	@Override
	protected ArrayList<Category> getCategoriesInBackGround(String... params) {
		// TODO Auto-generated method stub
		return SongController.getInstance().getOfflineArtists();
	}


	
}
