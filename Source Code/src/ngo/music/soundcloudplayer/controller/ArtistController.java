package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;

public class ArtistController extends OfflinePlayListController{
	static ArtistController instance = null;

	

	

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

	@Override
	public void addSongsToCategory(String categoryName, ArrayList<Song> songs)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return ARTIST;
	}

	@Override
	public void createCategory(String name) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
