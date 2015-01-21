package ngo.music.soundcloudplayer.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import android.content.Context;
import android.drm.DrmStore.ConstraintsColumns;
import android.support.v4.util.ArrayMap;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Stream;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

public abstract class CategoryController implements Constants.Data, Constants,
		Constants.Categories {

	protected String filename = "";
	ArrayList<Category> categories;
	protected int TAG_DATA_CHANGED = -1;
	protected int TAG_ITEM_CHANGED = -1;

	public ArrayList<Category> getCategories() {
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

	public void storeCategories() throws IOException {
		// TODO Auto-generated method stub

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

	public void createCategory(String name) throws Exception {
		for (Category category : categories) {
			if (category.getTitle().equals(name)) {
				throw new Exception("A playlist with the same name is existed");
			}
		}

		if (name.equals("")) {
			throw new Exception("A playlist cannot be created without a name");
		}
		categories.add(new Category(name,new ArrayList<Song>()));
		UIController.getInstance().updateUiWhenDataChanged(TAG_DATA_CHANGED);
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

	public ArrayList<String> getCategoryName() {
		ArrayList<String> categoriesName = new ArrayList<String>();
		for (Category cate : categories) {
			categoriesName.add(cate.getTitle());
		}
		return categoriesName;
	}

	public ArrayList<Song> getSongFromCategory(String categoryName) {
		for (Category category : categories) {
			if(category.getTitle().equals(categoryName)){
				return category.getSongs();
			}
		}
		return null;
	}

	/**
	 * get category Name and songs Titles
	 * 
	 * @return
	 */
	public ArrayList<String> getCategoryString() {
		// TODO Auto-generated method stub
		ArrayList<String> categoriesString = new ArrayList<String>();
		for (Category cate : categories) {
			

			categoriesString.add(cate.toString());
		}
		return categoriesString;

	}

	public void removeSongFromCate(Song song, String cate) {
		// TODO Auto-generated method stub
		for (Category category : categories) {
			if(category.getTitle().equals(cate)){
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

	public void removeCategory(String cate) {
		for (int i = 0; i < categories.size(); i++) {
			if(categories.get(i).getTitle().equals(cate)){
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

	/**
	 * 
	 * @param type
	 *            from Constant.Categproes
	 * @return
	 */
	public static CategoryController createInstance(int type) {
		switch (type) {
		case PLAYLIST:
			return new PlaylistController();

		default:
			return null;
		}
	}

	public static CategoryController getInstance(int type) {
		// TODO Auto-generated method stub
		switch (type) {
		case PLAYLIST:
			return PlaylistController.getInstance();

		default:
			return null;
		}
	}

}
