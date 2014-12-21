package ngo.music.soundcloudplayer.controller;

import java.util.ArrayList;

import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.Media;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.entity.Song;

public class SongController {
	private ArrayList<Song> songs;

	public SongController() {
		// TODO Auto-generated constructor stub
		getSongsFromSDCard();
	}

	public boolean playSong(Song song) {
		return true;
	}

	public boolean pauseSong(Song song) {
		return true;
	}

	public Song getSong(String songID) {
		for (Song song : songs) {
			if (song.getId().compareTo(songID) == 0) {
				return song;
			}
		}
		return null;
	}

	private void getSongsFromSDCard() {
		songs = new ArrayList<Song>();
		Cursor c = MainActivity
				.getActivity()
				.getContentResolver()
				.query(Media.EXTERNAL_CONTENT_URI, null,
						Media.IS_MUSIC + "!=0", null, null);
		while (c.moveToNext()) {
			String url = c.getString(c.getColumnIndex(Media.DATA));
			if (url.endsWith(".mp3")) {
				songs.add(new Song(c));
			}
		}
	}

	public ArrayList<String> getSongIDs() {
		ArrayList<String> songIDs = new ArrayList<String>();
		for (Song song : songs) {
			songIDs.add(song.getId());
		}
		return songIDs;
	}

	public ArrayList<Song> getSongs() {
		return songs;
	}

}
