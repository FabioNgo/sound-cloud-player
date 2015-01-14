package ngo.music.soundcloudplayer.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Stream;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

public class PlaylistController implements Constants {

	private static PlaylistController instance = null;
	private static final String filename = "playlists";
	ArrayMap<String, ArrayList<Song>> playlists;

	private PlaylistController() {
		// TODO Auto-generated constructor stub
		playlists = new ArrayMap<String, ArrayList<Song>>();
	}

	public static PlaylistController getInstance() {
		if (instance == null) {
			return new PlaylistController();
		} else {
			return instance;
		}
	}

	/**
	 * get stack of songs played
	 * 
	 * @return
	 */
	public ArrayMap<String, ArrayList<Song>> getPlaylists() {
		ArrayMap<String, ArrayList<Song>> playlists = new ArrayMap<String, ArrayList<Song>>();
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		try {
			FileInputStream fileReader = new FileInputStream(file);
			JsonReader reader = new JsonReader(
					new InputStreamReader(fileReader));

			
			reader.beginArray();

			while (reader.hasNext()) {
				reader.beginObject();
				String playlist = reader.nextName();
				ArrayList<Song> songs = new ArrayList<Song>();
				reader.beginArray();
				while (reader.hasNext()) {
					String id = reader.nextString();
					songs.add(SongController.getInstance().getSong(id));

				}
				reader.endArray();
				playlists.put(playlist, songs);
				reader.endObject();
			}

			reader.endArray();
			reader.close();
		} catch (Exception e) {
			Log.e("get playlist", e.toString());
			return playlists;
		}
		return playlists;
	}

	/**
	 * Write stack played song for play previous OfflineSong
	 * 
	 * @param stackSongplayed
	 * @param jsonWriter
	 * @param currentTIme
	 * @throws IOException
	 */
	public void storePlaylist() throws IOException {
		// TODO Auto-generated method stub

		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename);
		file.createNewFile();
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		OutputStreamWriter fileWriter = new OutputStreamWriter(fileOutputStream);
		JsonWriter jsonWriter = new JsonWriter(fileWriter);
		writePlaylists(playlists, jsonWriter);
		jsonWriter.close();
	}

	private void writePlaylists(ArrayMap<String, ArrayList<Song>> playlists,
			JsonWriter jsonWriter) throws IOException {
		// TODO Auto-generated method stub
		jsonWriter.beginArray();
		for (String playlist : playlists.keySet()) {
			ArrayList<Song> songs = playlists.get(playlist);
			jsonWriter.beginObject();
			jsonWriter.name(playlist);
			jsonWriter.beginArray();
			for (Song song : songs) {
				jsonWriter.beginObject();
				jsonWriter.name(song.getId());

				jsonWriter.endObject();

			}
			jsonWriter.endArray();
			jsonWriter.endObject();
		}
		jsonWriter.endArray();
	}

	public Stream getPlaylistFromId(long id) {
		Stream stream = null;
		SoundCloudUserController userController = SoundCloudUserController
				.getInstance();
		Token token = userController.getToken();
		ApiWrapper wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null,
				token);

		/*
		 * API URL OF THE SONG
		 */
		String uri = "http://api.soundcloud.com/tracks/" + id + "/stream";
		try {
			stream = wrapper.resolveStreamUrl(uri, true);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return stream;
	}
	public void createPlaylist(String name) throws Exception{
		if(playlists.containsKey(name)){
			throw new Exception("A playlist with the same name is existed");
		}
		playlists.put(name, new ArrayList<Song>());
	}
	public void addSongToPlaylist(String playlistName, Song song) throws Exception{
		if(!playlists.containsKey(playlistName)){
			throw new Exception("No playlist with the same name is existed");
		}
		playlists.get(playlistName).add(song);
	}
	public ArrayList<String> getPlaylistsName(){
		ArrayList<String> playlistNames = new ArrayList<String>();
		for (String string : playlists.keySet()) {
			playlistNames.add(string);
		}
		Collections.sort(playlistNames);
		return playlistNames;
	}
	public ArrayList<Song> getSongFromPlaylist(String playlist){
		return playlists.get(playlist);
	}

}
