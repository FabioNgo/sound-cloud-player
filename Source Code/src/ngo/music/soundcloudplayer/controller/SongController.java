package ngo.music.soundcloudplayer.controller;


import java.io.IOException;

import java.util.ArrayList;

import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.Media;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.entity.Song;
import org.apache.http.HttpResponse;

import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.api.Stream;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.general.Contants;




public class SongController implements Contants{
	private ArrayList<Song> songs;
	private static SongController instance = null;
	private SongController() {
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
	/**
	 * Restricted at most 1 object is created
	 * 
	 */
	public SongController getInstance(){
		if (instance == null){
			return new SongController();
		}else{
			return instance;
		}
	}

	/**
	 * Get the stream of the song by id
	 * @param id of a song
	 * @return the link stream (.mp3) of that song
	 */
	public Stream getSongFromID(long id){
		
		
		Stream stream = null;
		SoundCloudUserController userController = SoundCloudUserController.getInstance();
		Token token = userController.getToken();
		ApiWrapper wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null, token);
		
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
	
	public Stream getSongFromUrl(String url){
		Stream stream = null;
		SoundCloudUserController userController = SoundCloudUserController.getInstance();
		Token token = userController.getToken();
		ApiWrapper wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null, token);
		
		long id = -1;
		try {
			id = wrapper.resolve(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (id == -1){
			return null;
		}
		stream = getSongFromID(id);
		
		
		
		return stream;
	}
}
