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

import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

import org.xmlpull.v1.XmlPullParserException;

import com.facebook.LoginActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

public class OfflineSongController implements Constants.XMLConstant {
	private String filename1 = "songs.xml";
	private String filename2 = "stackSong.xml";
	private ArrayList<OfflineSong> offlineSongs = null;
	private static OfflineSongController instance = null;

	public static OfflineSongController getInstance() {
		if (instance == null) {
			instance = new OfflineSongController();
		}
		return instance;
	}
	public OfflineSongController() {
		// TODO Auto-generated constructor stub
		offlineSongs = getSongsFromSDCard();
	}

	/**
	 * Database for all songs from SDCard
	 * 
	 * @throws IOException
	 */
	// public void updateDatabase() throws IOException {
	// ArrayList<OfflineSong> songs = getSongsFromSDCard();
	// File file = new File(MusicPlayerService.getInstance()
	// .getApplicationContext()
	// .getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename1);
	//
	// file.createNewFile();
	//
	// FileOutputStream fileOutputStream = new FileOutputStream(file);
	// OutputStreamWriter fileWriter = new OutputStreamWriter(fileOutputStream);
	// JsonWriter jsonWriter = new JsonWriter(fileWriter);
	//
	// jsonWriter.setIndent("    ");
	// jsonWriter.beginArray();
	// writeSong(songs, jsonWriter);
	// jsonWriter.endArray();
	// jsonWriter.close();
	// }

	// private void writeSong(ArrayList<OfflineSong> songs, JsonWriter
	// jsonWriter)
	// throws IOException {
	// // TODO Auto-generated method stub
	//
	// for (OfflineSong song : songs) {
	// addSong(song, jsonWriter);
	//
	// }
	//
	// }

	// private void addSong(OfflineSong song, JsonWriter writer) throws
	// IOException {
	// writer.beginObject();
	// writer.name(song.getId());
	// writer.beginObject();
	//
	// writer.name(XML_TAG_TITLE).value(song.getTitle());
	// writer.name(XML_TAG_ARTIST).value(song.getArtist());
	// writer.name(XML_TAG_ALBUM).value(song.getAlbum());
	// writer.name(XML_TAG_LINK).value(song.getLink());
	// writer.endObject();
	// writer.endObject();
	// }

	/**
	 * Write stack played song for play previous OfflineSong
	 * 
	 * @param stackSongplayed
	 * @param jsonWriter
	 * @param currentTIme
	 * @throws IOException
	 */
	public void storePlayingSong() throws IOException {
		// TODO Auto-generated method stub
		OfflineSong song = (OfflineSong) MusicPlayerService.getInstance()
				.getCurrentSong();
		int curTime = MusicPlayerService.getInstance().getCurrentTime();
		ArrayList<Song> queue = MusicPlayerService.getInstance().getQueue();
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename2);

		file.createNewFile();

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		OutputStreamWriter fileWriter = new OutputStreamWriter(fileOutputStream);
		JsonWriter jsonWriter = new JsonWriter(fileWriter);
		writePlayedSongs(song, queue, jsonWriter, curTime);
		jsonWriter.close();
	}

	private void writePlayedSongs(Song playingSong, ArrayList<Song> queue,
			JsonWriter jsonWriter, int currentTIme) throws IOException {
		// TODO Auto-generated method stub

		jsonWriter.beginArray();
		for (Song song : queue) {
			jsonWriter.beginObject();
			jsonWriter.name(song.getId());
			if (playingSong.getId().equals(song.getId())) {
				jsonWriter.value(currentTIme);
			} else {
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
			Log.e("get songPlayed", e.toString());
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
	public ArrayList<OfflineSong> getSongs() {
		
		return offlineSongs;
	}

	/**
	 * Get song by id
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public OfflineSong getSongbyId(String input) throws IOException {
		for (OfflineSong offlineSong : offlineSongs) {
			if (offlineSong.getId().equals("input"))
				;
			return offlineSong;
		}
		return null;
	}

	private ArrayList<OfflineSong> getSongsFromSDCard() {
		ArrayList<OfflineSong> songs = new ArrayList<OfflineSong>();
		Cursor c = MusicPlayerService.getInstance()
				.getContentResolver()
				.query(Media.EXTERNAL_CONTENT_URI, null,
						Media.IS_MUSIC + "!=0", null, null);
		while (c.moveToNext()) {
			String url = c.getString(c.getColumnIndex(Media.DATA));
			if (url.endsWith(".mp3")) {
				songs.add(new OfflineSong(c));
			}
		}
		c.close();
		return songs;
	}

	
}
