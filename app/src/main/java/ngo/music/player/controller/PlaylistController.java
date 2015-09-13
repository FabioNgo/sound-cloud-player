package ngo.music.player.controller;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ngo.music.player.boundary.MusicPlayerMainActivity;
import ngo.music.player.entity.Category;
import ngo.music.player.entity.Song;
import ngo.music.player.service.MusicPlayerService;

public class PlaylistController extends OfflineCategoryController {

	static PlaylistController instance = null;
	String filename ="";
	

	public Category getPlaylist(String string) {
		String separator = "\1";
		String[] temp = string.split(separator);

		String title = temp[0];
		ArrayList<Song> songs = new ArrayList<Song>();
		for (int i = 1; i < temp.length; i++) {
			if (!temp[i].equals("")) {
				Song song = SongController.getInstance().getSong(temp[i]);
				if (song != null) {
					songs.add(song);
				}
			}
		}
		return new Category(title, songs);
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
	
	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return PLAYLIST;
	}
	@Override
	public Category createCategory(String name) {
		// TODO Auto-generated method stub
		return new Category(name, new ArrayList<Song>());
		
	}
	@Override
	protected ArrayList<Category> getCategoriesInBackGround(String ...params) {
		// TODO Auto-generated method stub
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
				categories.add(getPlaylist(line));
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
	protected void getCategoriesPostExcecute(ArrayList<Category> categories) {
		// TODO Auto-generated method stub
		
	}

	
	


}
