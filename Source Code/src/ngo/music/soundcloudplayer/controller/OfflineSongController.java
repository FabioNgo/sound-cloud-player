package ngo.music.soundcloudplayer.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

public class OfflineSongController implements Constants.XMLConstant {
	private String filename1 = "songs.xml";
	private String filename2 = "stackSong.xml";
	private static OfflineSongController instance = null;
	public static OfflineSongController getInstance() {
		if (instance == null) {
			instance = new OfflineSongController();
		}
		return instance;
	}

	/**
	 * Database for all songs from SDCard
	 * 
	 * @throws IOException
	 */
	public void updateDatabase() throws IOException {
		ArrayList<Song> songs = getSongsFromSDCard();
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename1);

		file.createNewFile();

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		OutputStreamWriter fileWriter = new OutputStreamWriter(fileOutputStream);
		JsonWriter jsonWriter = new JsonWriter(fileWriter);

		jsonWriter.setIndent("    ");
		jsonWriter.beginArray();
		writeSong(songs, jsonWriter);
		jsonWriter.endArray();
		jsonWriter.close();
	}

	private void writeSong(ArrayList<Song> songs, JsonWriter jsonWriter)
			throws IOException {
		// TODO Auto-generated method stub

		for (Song song : songs) {
			addSong(song, jsonWriter);

		}

	}

	private void addSong(Song song, JsonWriter writer) throws IOException {
		writer.beginObject();
		writer.name(song.getId());
		writer.beginObject();

		writer.name(XML_TAG_TITLE).value(song.getTitle());
		writer.name(XML_TAG_ARTIST).value(song.getArtist());
		writer.name(XML_TAG_ALBUM).value(song.getAlbum());
		writer.name(XML_TAG_LINK).value(song.getLink());
		writer.endObject();
		writer.endObject();
	}

	/**
	 * Write stack played song for play previous Song
	 * 
	 * @param stackSongplayed
	 * @param jsonWriter
	 * @param currentTIme
	 * @throws IOException
	 */
	public void storePlayingSong() throws IOException {
		// TODO Auto-generated method stub
		Song song = MusicPlayerService.getInstance().getCurrentSong();
		int curTime = MusicPlayerService.getInstance().getCurrentTime();
		ArrayList<Song> queue = MusicPlayerService.getInstance().getQueue();
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename2);

		file.createNewFile();

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		OutputStreamWriter fileWriter = new OutputStreamWriter(fileOutputStream);
		JsonWriter jsonWriter = new JsonWriter(fileWriter);
		writePlayedSongs(song, queue,jsonWriter, curTime);
		jsonWriter.close();
	}

	private void writePlayedSongs(Song playingSong,ArrayList<Song> queue,
			JsonWriter jsonWriter, int currentTIme) throws IOException {
		// TODO Auto-generated method stub

		jsonWriter.beginArray();
		for (Song song : queue) {
			jsonWriter.beginObject();
			jsonWriter.name(song.getId());
			if(playingSong.getId().equals(song.getId())){
				jsonWriter.value(currentTIme);
			}else{
				jsonWriter.value(0);
			}
			jsonWriter.endObject();

		}
		jsonWriter.endArray();
	}

	/**
	 * get stack of songs played
	 * 
	 * @return
	 */
	public ArrayList<Object[]> getSongsPlayed() {
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename2);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ArrayList<Object[]> songs = new ArrayList<Object[]>();
		
		try {
			FileInputStream fileReader = new FileInputStream(file);
			JsonReader reader = new JsonReader(
					new InputStreamReader(fileReader));

			String id = null;
			reader.beginArray();

			while (reader.hasNext()) {
				reader.beginObject();

				while (reader.hasNext()) {
					Object[] object = new Object[2];
					id = reader.nextName();
					object[0] = getSongbyId(id);
					object[1] = Integer.valueOf(reader.nextInt());
					songs.add(object);
				}

				reader.endObject();
			}

			reader.endArray();
			reader.close();
		} catch (Exception e) {
			Log.e("get songPlayed", e.getMessage());
			return songs;
		}
		return songs;
	}

	
	/**
	 * get all songs
	 * 
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Song> getSongs() throws IOException {
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename1);
		if (!file.exists()) {
			updateDatabase();
		}
		FileInputStream fileReader = new FileInputStream(file);
		JsonReader reader = new JsonReader(new InputStreamReader(fileReader));
		ArrayList<Song> songs = new ArrayList<Song>();
		String id = null, title = null, album = null, artist = null, link = null;
		reader.beginArray();
		while (reader.hasNext()) {
			reader.beginObject();
			id = reader.nextName();

			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals(XML_TAG_TITLE)) {
					title = reader.nextString();
				} else if (name.equals(XML_TAG_ALBUM)) {
					album = reader.nextString();
				} else if (name.equals(XML_TAG_ARTIST)) {
					artist = reader.nextString();
				} else if (name.equals(XML_TAG_LINK)) {
					link = reader.nextString();
				} else {
					reader.skipValue();
				}
			}
			songs.add(new Song(id, title, artist, album, link));
			reader.endObject();
			reader.endObject();
		}
		reader.endArray();
		reader.close();
		return songs;
	}

	/**
	 * Get song by id
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public Song getSongbyId(String input) throws IOException {
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename1);
		if (!file.exists()) {
			updateDatabase();
		}
		FileInputStream fileReader = new FileInputStream(file);
		JsonReader reader = new JsonReader(new InputStreamReader(fileReader));
		Song song = null;
		String id = null, title = null, album = null, artist = null, link = null;
		reader.beginArray();
		while (reader.hasNext()) {
			reader.beginObject();
			id = reader.nextName();
			if (id.equals(input)) {
				reader.beginObject();
				while (reader.hasNext()) {
					String name = reader.nextName();
					if (name.equals(XML_TAG_TITLE)) {
						title = reader.nextString();
					} else if (name.equals(XML_TAG_ALBUM)) {
						album = reader.nextString();
					} else if (name.equals(XML_TAG_ARTIST)) {
						artist = reader.nextString();
					} else if (name.equals(XML_TAG_LINK)) {
						link = reader.nextString();
					} else {
						reader.skipValue();
					}
				}
				song = new Song(id, title, artist, album, link);
				reader.close();
				return song;
			} else {
				reader.skipValue();
				reader.endObject();
			}

		}
		reader.endArray();
		reader.close();
		return song;
	}

	private ArrayList<Song> getSongsFromSDCard() {
		ArrayList<Song> songs = new ArrayList<Song>();
		Cursor c = MusicPlayerService
				.getInstance()
				.getContentResolver()
				.query(Media.EXTERNAL_CONTENT_URI, null,
						Media.IS_MUSIC + "!=0", null, null);
		while (c.moveToNext()) {
			String url = c.getString(c.getColumnIndex(Media.DATA));
			if (url.endsWith(".mp3")) {
				songs.add(new Song(c));
			}
		}
		c.close();
		return songs;
	}

	public Stack<String> getStackSongs() throws IOException {
		Stack<String> songs = new Stack<String>();
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename1);
		if (!file.exists()) {
			storePlayingSong();
		}
		FileInputStream fileReader = new FileInputStream(file);
		JsonReader reader = new JsonReader(new InputStreamReader(fileReader));

		String id = null;

		// list songs
		reader.beginArray();
		while (reader.hasNext()) {
			reader.beginObject();
			id = reader.nextName();
			reader.endObject();
			songs.push(id);
		}
		reader.endArray();
		reader.close();
		Collections.reverse(songs);
		return songs;
	}

	//
}
