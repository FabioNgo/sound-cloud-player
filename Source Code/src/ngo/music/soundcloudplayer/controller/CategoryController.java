package ngo.music.soundcloudplayer.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

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
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

public abstract class CategoryController implements Constants.Data, Constants,
Constants.Categories{

	
	protected String filename = "";
	LinkedHashMap<String, ArrayList<Song>> categories;
	protected int TAG_DATA_CHANGED = -1;
	

	
	public LinkedHashMap<String, ArrayList<Song>> getCategories() {
		LinkedHashMap<String, ArrayList<Song>> categories = new LinkedHashMap<String, ArrayList<Song>>();
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
			FileInputStream fileReader = new FileInputStream(file);
			JsonReader reader = new JsonReader(
					new InputStreamReader(fileReader));

			reader.beginArray();

			while (reader.hasNext()) {
				reader.beginObject();
				String category = reader.nextName();
				ArrayList<Song> songs = new ArrayList<Song>();
				reader.beginObject();
				while (reader.hasNext()) {

					String id = reader.nextName();
					String value = reader.nextString();
					songs.add(SongController.getInstance().getSong(id));

				}
				reader.endObject();
				categories.put(category, songs);
				reader.endObject();
			}

			reader.endArray();
			reader.close();
		} catch (Exception e) {
			Log.e("get category", e.toString());
			return categories;
		}
		return categories;
	}

	public void storeCategories() throws IOException {
		// TODO Auto-generated method stub

		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename);
		file.createNewFile();
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		OutputStreamWriter fileWriter = new OutputStreamWriter(fileOutputStream);
		JsonWriter jsonWriter = new JsonWriter(fileWriter);
		writeCategories(categories, jsonWriter);
		jsonWriter.close();
	}

	private void writeCategories(
			LinkedHashMap<String, ArrayList<Song>> categories,
			JsonWriter jsonWriter) throws IOException {
		// TODO Auto-generated method stub
		jsonWriter.beginArray();
		for (String category : categories.keySet()) {
			ArrayList<Song> songs = categories.get(category);
			jsonWriter.beginObject();
			jsonWriter.name(category);
			jsonWriter.beginObject();

			for (Song song : songs) {

				jsonWriter.name(song.getId());
				jsonWriter.value("");

			}
			jsonWriter.endObject();
			jsonWriter.endObject();
		}
		jsonWriter.endArray();
	}

	
	public void createCategory(String name) throws Exception {
		if (categories.containsKey(name)) {
			throw new Exception("A playlist with the same name is existed");
		}
		if (name.equals("")) {
			throw new Exception("A playlist cannot be created without a name");
		}
		categories.put(name, new ArrayList<Song>());
		UIController.getInstance().updateUiWhenDataChanged(TAG_DATA_CHANGED);
	}

	public void addSongsToCategory(String categoryName, ArrayList<Song> songs)
			throws Exception {
		if (!categories.containsKey(categoryName)) {
			throw new Exception("No playlist with the same name is existed");
		}
		for (Song song : songs) {
			ArrayList<Song> exsitedSongs = categories.get(categoryName);
			if (!exsitedSongs.contains(song)) {
				exsitedSongs.add(song);
			}
		}
		BasicFunctions.makeToastTake("Songs were added successfully", MusicPlayerMainActivity.getActivity());
		storeCategories();
		UIController.getInstance().updateUiWhenDataChanged(TAG_DATA_CHANGED);
	}

	public ArrayList<String> getCategoryName() {
		ArrayList<String> playlistNames = new ArrayList<String>();
		for (String string : categories.keySet()) {
			playlistNames.add(string);
		}
		return playlistNames;
	}

	public ArrayList<Song> getSongFromCategory(String category) {
		return categories.get(category);
	}

	/**
	 * get category Name and songs Titles
	 * 
	 * @return
	 */
	public ArrayList<String> getCategoryString() {
		// TODO Auto-generated method stub
		ArrayList<String> playlistsString = new ArrayList<String>();
		for (String string : categories.keySet()) {
			String temp = string;
			ArrayList<Song> songs = getSongFromCategory(string);
			for (Song song : songs) {
				temp += "\1" + song.getTitle();
			}

			playlistsString.add(temp);
		}
		return playlistsString;

	}

	public void removeSongFromCate(Song song, String cate) {
		// TODO Auto-generated method stub
		ArrayList<Song> songs = getSongFromCategory(cate);
		for (int i = 0; i < songs.size(); i++) {
			if(songs.get(i).getId().equals(song.getId())){
				songs.remove(i);
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
	public void removeCategory(String cate){
		categories.remove(cate);
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
	 * @param type from Constant.Categproes
	 * @return
	 */
	public static CategoryController createInstance(int type){
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
