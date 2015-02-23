package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;

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
