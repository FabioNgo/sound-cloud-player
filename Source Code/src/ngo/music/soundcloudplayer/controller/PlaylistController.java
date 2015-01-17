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

public class PlaylistController implements Constants.Data, Constants {

	private static PlaylistController instance = null;
	private static final String filename = "playlists";
	LinkedHashMap<String, ArrayList<Song>> playlists;

	private PlaylistController() {
		// TODO Auto-generated constructor stub
		// playlists = new ArrayMap<String, ArrayList<Song>>();
		playlists = getPlaylists();
	}

	public static PlaylistController getInstance() {
		if (instance == null) {
			instance = new PlaylistController();
		}

		return instance;

	}

	/**
	 * get stack of songs played
	 * 
	 * @return
	 */
	public LinkedHashMap<String, ArrayList<Song>> getPlaylists() {
		LinkedHashMap<String, ArrayList<Song>> playlists = new LinkedHashMap<String, ArrayList<Song>>();
		File file = new File(MusicPlayerMainActivity.getActivity()

		.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return playlists;
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
				reader.beginObject();
				while (reader.hasNext()) {

					String id = reader.nextName();
					String value = reader.nextString();
					songs.add(SongController.getInstance().getSong(id));

				}
				reader.endObject();
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

	private void writePlaylists(
			LinkedHashMap<String, ArrayList<Song>> playlists2,
			JsonWriter jsonWriter) throws IOException {
		// TODO Auto-generated method stub
		jsonWriter.beginArray();
		for (String playlist : playlists2.keySet()) {
			ArrayList<Song> songs = playlists2.get(playlist);
			jsonWriter.beginObject();
			jsonWriter.name(playlist);
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

	public void createPlaylist(String name) throws Exception {
		if (playlists.containsKey(name)) {
			throw new Exception("A playlist with the same name is existed");
		}
		if (name.equals("")) {
			throw new Exception("A playlist cannot be created without a name");
		}
		playlists.put(name, new ArrayList<Song>());
		UIController.getInstance().updateUiWhenDataChanged(PLAYLIST_CHANGED);
	}

	public void addSongsToPlaylist(String playlistName, ArrayList<Song> songs)
			throws Exception {
		if (!playlists.containsKey(playlistName)) {
			throw new Exception("No playlist with the same name is existed");
		}
		for (Song song : songs) {
			ArrayList<Song> exsitedSongs = playlists.get(playlistName);
			if (!exsitedSongs.contains(song)) {
				exsitedSongs.add(song);
			}
		}
		BasicFunctions.makeToastTake("Songs were added to playlist \""
				+ playlistName + "\"", MusicPlayerMainActivity.getActivity());
		storePlaylist();
		UIController.getInstance().updateUiWhenDataChanged(PLAYLIST_CHANGED);
	}

	public ArrayList<String> getPlaylistsName() {
		ArrayList<String> playlistNames = new ArrayList<String>();
		for (String string : playlists.keySet()) {
			playlistNames.add(string);
		}
		return playlistNames;
	}

	public ArrayList<Song> getSongFromPlaylist(String playlist) {
		return playlists.get(playlist);
	}

	/**
	 * get playlist Name and songs Titles
	 * 
	 * @return
	 */
	public ArrayList<String> getPlaylistsString() {
		// TODO Auto-generated method stub
		ArrayList<String> playlistsString = new ArrayList<String>();
		for (String string : playlists.keySet()) {
			String temp = string;
			ArrayList<Song> songs = getSongFromPlaylist(string);
			for (Song song : songs) {
				temp += "\1" + song.getTitle();
			}

			playlistsString.add(temp);
		}
		return playlistsString;

	}

}
