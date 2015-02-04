package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;

public class AlbumController extends CategoryController {
	static AlbumController instance = null;


	

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
	public void addSongsToCategory(String categoryName, ArrayList<Song> songs)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return ALBUM;
	}
}
