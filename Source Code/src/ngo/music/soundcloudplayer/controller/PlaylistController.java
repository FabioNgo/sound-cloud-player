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
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

public class PlaylistController extends CategoryController implements Constants.Data, Constants {

	private static PlaylistController instance = null;
	String filename = "playlists";
	
	PlaylistController() {
		// TODO Auto-generated constructor stub
		// playlists = new ArrayMap<String, ArrayList<Song>>();
		
		instance = this;
		categories = getCategories();
		TAG_DATA_CHANGED = ALBUM_CHANGED;
		TAG_ITEM_CHANGED = ITEM_IN_ALBUM_CHANGED;
	}

	public static PlaylistController getInstance() {
		if (instance == null) {
			new PlaylistController();
		}

		return instance;

	}
	@Override
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
	@Override
	public  void storeCategories() throws IOException {
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

	

}
