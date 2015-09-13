package ngo.music.player.controller;

import java.util.ArrayList;

import ngo.music.player.entity.Category;

public class AlbumController extends ReadOnlyOfflineCategoryController {
	static AlbumController instance = null;


	

	@Override
	protected int setTagItemChange() {
		// TODO Auto-generated method stub
		return ITEM_IN_ALBUM_CHANGED;
	}

	@Override
	protected int setTagDataChange() {
		// TODO Auto-generated method stub
		return ALBUM_CHANGED;
	}

	

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return ALBUM;
	}



	@Override
	protected ArrayList<Category> getCategoriesInBackGround(String ...params) {
		// TODO Auto-generated method stub
		return SongController.getInstance().getOfflineAlbums();
	}


	
}
