package ngo.music.soundcloudplayer.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;
import android.util.JsonReader;
import android.util.JsonWriter;

public class OfflineSongController implements Constants.XMLConstant {
	private String filename = "songs.xml";
	private static OfflineSongController instance = null;

	public static OfflineSongController getInstance() {
		if (instance == null) {
			instance = new OfflineSongController();
		}
		return instance;
	}

	public void iniDatabase() throws IOException {
		ArrayList<Song> songs = getSongsFromSDCard();
		writeSong(songs);

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

	public void addSongToDatabase(Song song) throws IOException,
			XmlPullParserException {
		ArrayList<Song> songs = getSongs();
		songs.add(song);
		writeSong(songs);

	}

	//

	private void writeSong(ArrayList<Song> songs) throws IOException {
		// TODO Auto-generated method stub
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename);

		file.createNewFile();

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		OutputStreamWriter fileWriter = new OutputStreamWriter(fileOutputStream);
		JsonWriter jsonWriter = new JsonWriter(fileWriter);

		jsonWriter.setIndent("    ");
		jsonWriter.beginObject();
		jsonWriter.name("songs");
		jsonWriter.beginArray();

		for (Song song : songs) {
			addSong(song, jsonWriter);

		}

		jsonWriter.endArray();
		jsonWriter.endObject();
		jsonWriter.close();

	}

	public ArrayList<Song> getSongs() throws IOException,
			XmlPullParserException {
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename);
		if (!file.exists()) {
			iniDatabase();
		}
		FileInputStream fileReader = new FileInputStream(file);
		JsonReader reader = new JsonReader(new InputStreamReader(fileReader));
		ArrayList<Song> songs = new ArrayList<Song>();
		String id = null, title = null, album = null, artist = null, link = null;
		reader.beginObject();
		while (reader.hasNext()) {
			String name1 = reader.nextName();
			// access songs part
			if (name1.equals("songs")) {
				reader.beginArray();
				// list songs
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
			} else {
				reader.skipValue();
			}

		}
		reader.endObject();
		reader.close();
		Song song = getSongbyId(songs.get(2).getId());
		return songs;
	}

	public Song getSongbyId(String input) throws IOException {
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename);
		if (!file.exists()) {
			iniDatabase();
		}
		FileInputStream fileReader = new FileInputStream(file);
		JsonReader reader = new JsonReader(new InputStreamReader(fileReader));
		Song song = null;
		String id = null, title = null, album = null, artist = null, link = null;
		reader.beginObject();
		while (reader.hasNext()) {
			String name1 = reader.nextName();
			// access songs part
			if (name1.equals("songs")) {
				reader.beginArray();
				// list songs
				while (reader.hasNext()) {
					reader.beginObject();
					id = reader.nextName();
					if (!id.equals(input)) {
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
					} else {
						song = new Song(id, title, artist, album, link);
						reader.close();
						return song;
					}

					reader.endObject();
					reader.endObject();
				}
				reader.endArray();
			} else {
				reader.skipValue();
			}

		}
		reader.endObject();
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

	//
}
