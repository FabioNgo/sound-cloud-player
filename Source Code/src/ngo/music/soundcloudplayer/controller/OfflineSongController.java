package ngo.music.soundcloudplayer.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract.Instances;
import android.provider.MediaStore.Audio.Media;
import android.util.Xml;

public class OfflineSongController implements Constants.XMLConstant {
	private String namespace = "";
	private String filename = "songs.xml";
	private static OfflineSongController instance = null;

	public static OfflineSongController getInstance() {
		if (instance == null) {
			instance = new OfflineSongController();
		}
		return instance;
	}


	public void iniDatabase() throws IOException {
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename);
		if (!file.exists()) {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			getSongsFromSDCard();
			ArrayList<Song> songs = getSongsFromSDCard();
			XmlSerializer xmlSerializer = Xml.newSerializer();
			xmlSerializer.setOutput(writer);
			xmlSerializer.startDocument("UTF-8", true);
			xmlSerializer.startTag(namespace, "songs");
			for (Song song : songs) {
				addSong(song, xmlSerializer);
			}
			xmlSerializer.endTag(namespace, "songs");
			xmlSerializer.endDocument();
			writer.write(xmlSerializer.toString());
		}
		

	}

	private void addSong(Song song, XmlSerializer xmlSerializer) throws IllegalArgumentException, IllegalStateException, IOException {
		xmlSerializer.startTag(namespace, XML_TAG_SONG);
		addTag(xmlSerializer, XML_TAG_ID, song.getId());
		addTag(xmlSerializer, XML_TAG_TITLE, song.getTitle());
		addTag(xmlSerializer, XML_TAG_ALBUM, song.getAlbum());
		addTag(xmlSerializer, XML_TAG_ARTIST, song.getArtist());
		addTag(xmlSerializer, XML_TAG_LINK, song.getLink());
		xmlSerializer.endTag(namespace, XML_TAG_SONG);

	}
	private void addTag(XmlSerializer xmlSerializer, String tag, String text) throws IllegalArgumentException, IllegalStateException, IOException{
		
		xmlSerializer.startTag(namespace, tag);
		xmlSerializer.text(text);
		xmlSerializer.endTag(namespace, tag);
	}
	public ArrayList<Song> getSongs() throws IOException, XmlPullParserException {
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename);
		if (!file.exists()) {
			iniDatabase();
		}

		InputStream in = new FileInputStream(file);
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readSongs(parser);
		} finally {
			in.close();
		}

	}

	// ArrayList<String> data = new ArrayList<String>();
	// data.add(String.valueOf(currentSongPosition));
	// // for (Song song : songsPlaying) {
	// // data.add(song.getId());
	// // }
	// String rawData = "";
	// for (String string2 : data) {
	// rawData += string2 + ",";
	// }
	// BufferedWriter bufferedWriter;
	// try {
	// bufferedWriter = new BufferedWriter(new FileWriter(new File(
	// getApplicationContext().getExternalFilesDir(
	// ACCESSIBILITY_SERVICE), filename)));
	//
	// bufferedWriter.write(rawData);
	// bufferedWriter.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }

	private ArrayList<Song> readSongs(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ArrayList<Song> songs = new ArrayList<Song>();

		parser.require(XmlPullParser.START_TAG, namespace, "songs");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("song")) {
				songs.add(readSong(parser));
			} else {
				skip(parser);
			}
		}
		return songs;
	}

	// Parses the contents of an entry. If it encounters a title, summary, or
	// link tag, hands them off
	// to their respective "read" methods for processing. Otherwise, skips the
	// tag.
	private Song readSong(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		parser.require(XmlPullParser.START_TAG, namespace, "song");
		String title = null;
		String artist = null;
		String id = null;
		String link = null;
		String album = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(XML_TAG_ID)) {
				id = readTag(parser,name);
			} else if (name.equals(XML_TAG_TITLE)) {
				title = readTag(parser,name);
			} else if (name.equals(XML_TAG_ARTIST)) {
				artist = readTag(parser,name);
			} else if (name.equals(XML_TAG_ALBUM)) {
				album = readTag(parser,name);
			} else if (name.equals(XML_TAG_LINK)) {
				link = readTag(parser,name);
			} else {
				skip(parser);
			}
		}

		return new Song(id, title, artist, album, link);
	}

	// For the tags id
	private String readTag(XmlPullParser parser,String TAG) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, namespace, TAG);
		String result = readText(parser);
		parser.require(XmlPullParser.END_TAG, namespace, TAG);
		return result;
	}

	

	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	/**
	 * Skip Tags You Don't Care About
	 * 
	 * @param parser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
	private ArrayList<Song> getSongsFromSDCard() {
		ArrayList<Song> songs = new ArrayList<Song>();
		Cursor c = MusicPlayerService.getInstance().getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null,
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
	
}
