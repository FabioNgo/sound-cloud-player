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
import org.json.JSONException;
import org.json.JSONObject;

import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Http;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.api.Stream;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.general.Contants;




public class SongController implements Contants, Contants.SongContants{
	private ArrayList<Song> songs;
	private static SongController instance = null;
	private Song currentSong;
	
	private SongController() {
		// TODO Auto-generated constructor stub
		getSongsFromSDCard();
	}
	
	/**
	 * Restricted at most 1 object is created
	 * 
	 */
	public static SongController getInstance(){
		if (instance == null){
			return new SongController();
		}else{
			return instance;
		}
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
	 * Get the stream of the song by id
	 * @param id of a song
	 * @return the link stream (.mp3) of that song
	 */
	public Song getSongFromID(long id){
		
		Song currentSong = null;
		Stream stream = null;
		SoundCloudUserController userController = SoundCloudUserController.getInstance();
		Token token = userController.getToken();
		ApiWrapper wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null, token);
		
		/*
		 * API URL OF THE SONG
		 */
		String uri = "http://api.soundcloud.com/tracks/" + id;
		
		
		try {
			HttpResponse resp = wrapper.get(Request.to(uri));
			 JSONObject me = Http.getJSON(resp);
		     //set information of logged user
		     currentSong  = addSongInformation(me, wrapper);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return currentSong;
	}
	
	/**
	 * Get Stream from URL
	 * @param url : url of the song
	 * @return Stream
	 */
	public Song getSongFromUrl(String url){
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
		return getSongFromID(id);
		
	}
	
	public void uploadSong(){
		
	}
	/**
	 * add information into song entity class
	 * @param me
	 * @param wrapper
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	private Song addSongInformation(JSONObject me, ApiWrapper wrapper) throws JSONException, IOException{
		Song song = new Song();
		
		song.setBpm(me.getInt(BPM));
		song.setCommentable(me.getBoolean(COMMENTABLE));
		song.setCommentCount(me.getInt(COMMENT_COUNT));
		song.setContentSize(me.getLong(CONTENT_SIZE));
		song.setCreatedAt(me.getString(CREATED_AT));
		song.setDescription(me.getString(DESCRIPTION));
		song.setDownloadable(me.getBoolean(DOWNLOADABLE));
		song.setDownloadCount(me.getInt(DOWNLOAD_COUNT));
		song.setDownloadUrl(me.getString(DOWNLOAD_URL));
		song.setDuration(me.getLong(DURATION));
		song.setFavoriteCount(me.getInt(FOVORITINGS_COUNT));
		song.setFormat(me.getString(FORMAT));
		song.setGerne(me.getString(GENRE));
		song.setKeySignature(me.getString(KEY_SIGNATURE));
		song.setLabelID(me.getInt(LABEL_ID));
		song.setLabelName(me.getString(LABEL_NAME));
		song.setLicense(me.getString(LICENSE));
		song.setPermalink(me.getString(PERMALINK));
		song.setPermalinkUrl(me.getString(PERMALINK_URL));
		song.setPlaybackCount(me.getInt(PLAYBACK_COUNT));
		song.setRelease(me.getString(RELEASE));
		song.setReleaseDay(me.getInt(RELEASE_DAY));
		song.setReleaseMonth(me.getInt(RELEASE_MONTH));
		song.setReleaseYear(me.getInt(RELEASE_YEAR));
		song.setSharing(me.getString(SHARING));
		song.setSoundcloudId(me.getInt(ID));
		song.setStreamable(me.getBoolean(STREAMABLE));
		
		song.setTagList(me.getString(TAG_LIST));
		song.setTitle(me.getString(TITLE));
		song.setTrackType(me.getString(TRACK_TYPE));
		song.setUri(me.getString(URI));
		song.setUserId(me.getInt(USER_ID));
		song.setVideoUrl(me.getString(VIDEO_URL));
		song.setWaveformUrl(me.getString(WAVEFORM_URL));
		
		Stream stream = wrapper.resolveStreamUrl(me.getString(STREAM_URL), true);
		song.setStreamUrl(stream.streamUrl);
		
		return song;
	}
}
