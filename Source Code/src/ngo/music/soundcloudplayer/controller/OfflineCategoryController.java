package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;

public abstract class OfflineCategoryController extends CategoryController {

	

	public void addSongsToCategory(String categoryName, ArrayList<Song> songs)
			throws Exception {

		for (Category cate : categories) {
			if (cate.getTitle().equals(categoryName)) {
				cate.addSongs(songs);
				BasicFunctions.makeToastTake("Songs were added successfully",
						MusicPlayerMainActivity.getActivity());
				storeCategories();
				UIController.getInstance().updateUiWhenDataChanged(
						TAG_ITEM_CHANGED);
				return;
			}
		}
		throw new Exception("Playlist does not exsist");

	}

	@Override
	public void removeSongFromCate(Song song, String cate) {
		// TODO Auto-generated method stub
		for (Category category : categories) {
			if (category.getTitle().equals(cate)) {
				category.removeSong(song);
			}
		}
		UIController.getInstance().updateUiWhenDataChanged(TAG_ITEM_CHANGED);
		try {
			storeCategories();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void removeCategory(String cate) {
		// TODO Auto-generated method stub
		for (int i = 0; i < categories.size(); i++) {
			if (categories.get(i).getTitle().equals(cate)) {
				categories.remove(i);
				break;
			}
		}
		UIController.getInstance().updateUiWhenDataChanged(TAG_DATA_CHANGED);
		try {
			storeCategories();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void updateTitle(String oldName, String newName) throws Exception {
		// TODO Auto-generated method stub
		if ("".equals(newName)) {
			throw new Exception("Playlist name cannot be empty");
		}
		for (Category category : categories) {
			if (category.getTitle().equals(newName)) {
				throw new Exception("Playlist name exsited");
			}
		}
		for (Category category : categories) {
			if (category.getTitle().equals(oldName)) {
				category.setTitle(newName);
				break;
			}
		}
		storeCategories();
		UIController.getInstance().updateUiWhenDataChanged(TAG_DATA_CHANGED);
	}

	

}
