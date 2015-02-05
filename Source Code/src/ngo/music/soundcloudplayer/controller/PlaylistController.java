package ngo.music.soundcloudplayer.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

public class PlaylistController extends CategoryController {

	static PlaylistController instance = null;
	String filename ="";
	

	
	@Override
	public ArrayList<Category> getCategories() {

		filename = "playlists";
		ArrayList<Category> categories = new ArrayList<Category>();
		File file = new File(MusicPlayerMainActivity.getActivity()

		.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return categories;
		}

		try {
			FileReader reader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				categories.add(new Category(line));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categories;
	}
	@Override
	public  void storeCategories() throws IOException {
		// TODO Auto-generated method stub
		filename = "playlists";
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename);
		file.createNewFile();
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter writer = new BufferedWriter(fileWriter);
		for (Category category : categories) {
			writer.write(category.toStoredString());
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}
	@Override
	protected int setTagItemChange() {
		// TODO Auto-generated method stub
		return ITEM_IN_PLAYLIST_CHANGED;
	}
	@Override
	protected int setTagDataChange() {
		// TODO Auto-generated method stub
		return PLAYLIST_CHANGED;
	}
	public void addSongsToCategory(String categoryName, ArrayList<Song> songs)
			throws Exception {
		
		for (Category cate : categories) {
			if(cate.getTitle().equals(categoryName)){
				cate.addSongs(songs);
				BasicFunctions.makeToastTake("Songs were added successfully",
						MusicPlayerMainActivity.getActivity());
				storeCategories();
				UIController.getInstance().updateUiWhenDataChanged(TAG_ITEM_CHANGED);
				return;
			}
		}
		throw new Exception("Playlist does not exsist");
		
	}
	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return PLAYLIST;
	}
	@Override
	public void createCategory(String name) throws Exception {
		// TODO Auto-generated method stub
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
