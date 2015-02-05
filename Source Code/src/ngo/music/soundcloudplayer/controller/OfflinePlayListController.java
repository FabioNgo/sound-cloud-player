package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.entity.Song;

public abstract class OfflinePlayListController extends CategoryController {

	
	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int setTagItemChange() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int setTagDataChange() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<Category> getCategories() throws InterruptedException,
			ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeCategories() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSongsToCategory(String categoryName, ArrayList<Song> songs)
			throws Exception {
		// TODO Auto-generated method stub

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
		if("".equals(newName)){
			throw new Exception("Playlist name cannot be empty");
		}
		for (Category category : categories) {
			if(category.getTitle().equals(newName)){
				throw new Exception("Playlist name exsited");
			}
		}
		for (Category category : categories) {
			if(category.getTitle().equals(oldName)){
				category.setTitle(newName);
				break;
			}
		}
		storeCategories();
		UIController.getInstance().updateUiWhenDataChanged(TAG_DATA_CHANGED);
	}
	
	@Override
	public void createCategory(String name) throws Exception {
		for (Category category : categories) {
			if (category.getTitle().equals(name)) {
				throw new Exception("A playlist with the same name is existed");
			}
		}
	
		if (name.equals("")) {
			throw new Exception("A playlist cannot be created without a name");
		}
		categories.add(new Category(name, new ArrayList<Song>()));
		UIController.getInstance().updateUiWhenDataChanged(TAG_DATA_CHANGED);
	}



}
