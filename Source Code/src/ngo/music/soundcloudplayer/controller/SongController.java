package ngo.music.soundcloudplayer.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Endpoints;
import ngo.music.soundcloudplayer.api.Http;
import ngo.music.soundcloudplayer.api.Params;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.api.Stream;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.entity.SoundCloudAccount;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.volley.api.AppController;

import android.R.integer;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


public class SongController implements Constants, Constants.SongConstants, Constants.SoundCloudExploreConstant{

	private static final String ROOT_DIRECTORY = "/SoundCloudApp";

	private static final int NUMBER_CATEGORY = 14;
	
	private  String[] exploreLinkList; 
	private static final String TAG_NEXT_LINK_EXPLORE = "next_herf";
	private static final String TAG_TRACKS_EXPLORE = "tracks";
	private ArrayList<Song> offlineSong;
	File dir = null;
	
	private ArrayList<ArrayList<Song>> onlineSongs = new ArrayList<ArrayList<Song>>();
	
	
	
	private ArrayList<String> idList = new ArrayList<String>();
	public int offset = 0;
	
	private static SongController instance = null;

	private SongController() {
		// TODO Auto-generated constructor stub
		synchronized (this) {

			if (instance == null) {
				instance = this;
				instance.initialOnlineSongsList();
				instance.initialCategoryListLink();
				instance.getSongsFromSDCard();
				
				dir = new File(Environment.getExternalStorageDirectory() + ROOT_DIRECTORY);
				if(!(dir.exists() && dir.isDirectory())) {
					System.out.println ("CREATE FOLDER: " + dir.mkdir());
					
				}
			
				
				
			}
		}
	}

	/**
	 * Restricted at most 1 object is created
	 * 
	 */
	public static SongController getInstance() {

		if (instance == null) {
			return new SongController();
		} else {
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
		for (Song song : offlineSong) {
			if (song.getId().compareTo(songID) == 0) {
				return song;
			}
		}
		return null;
	}

	/**
	 * Load song from sd card
	 */
	private void getSongsFromSDCard() {
		offlineSong = new ArrayList<Song>();
		Cursor c = MainActivity
				.getActivity()
				.getContentResolver()
				.query(Media.EXTERNAL_CONTENT_URI, null,
						Media.IS_MUSIC + "!=0", null, null);
		while (c.moveToNext()) {
			String url = c.getString(c.getColumnIndex(Media.DATA));
			if (url.endsWith(".mp3")) {
				offlineSong.add(new Song(c));
			}
		}
	}

	public ArrayList<String> getSongIDs() {
		ArrayList<String> songIDs = new ArrayList<String>();
		for (Song song : offlineSong) {
			songIDs.add(song.getId());
		}
		return songIDs;
	}


	/**
	 * Get Song which store in the storage
	 * @return
	 */
		

	public ArrayList<Song> getSongs() {
		return MusicPlayerService.getInstance().getSongs();

	}
	
	/**
	 * Get songs which load from the internet
	 * @return
	 */
	public ArrayList<Song> getOnlineSongs(int category) {
		
		return onlineSongs.get(category);
	}
	

	/**
	 * Get the stream of the song by id
	 * 
	 * @param id
	 *            of a song
	 * @return the link stream (.mp3) of that song
	 */
	public Song getSongFromID(long id) {

		Song currentSong = null;
		Stream stream = null;
		SoundCloudUserController userController = SoundCloudUserController
				.getInstance();
		Token token = userController.getToken();
		ApiWrapper wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null,
				token);

		/*
		 * API URL OF THE SONG
		 */
		String uri = "http://api.soundcloud.com/tracks/" + id;

		try {
			HttpResponse resp = wrapper.get(Request.to(uri));
			JSONObject me = Http.getJSON(resp);
			// set information of logged user
			currentSong = addSongInformation(me);

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
	 * 
	 * @param url
	 *            : url of the song
	 * @return Stream
	 */
	public Song getSongFromUrl(String url) {
		Stream stream = null;
		SoundCloudUserController userController = SoundCloudUserController
				.getInstance();
		Token token = userController.getToken();
		ApiWrapper wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null,
				token);

		long id = -1;
		try {
			id = wrapper.resolve(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (id == -1) {
			return null;
		}
		return getSongFromID(id);

	}

	/**
	 * upload Song from memory.
	 * 
	 * @param song
	 * @param songFile
	 * @param artWorkFile
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws JSONException
	 */
	public void uploadSong(Song song, File songFile, File artWorkFile)
			throws ClassNotFoundException, IOException, JSONException {

		final ApiWrapper wrapper = ApiWrapper.fromFile(songFile);
		System.out.println("Uploading " + songFile);
		try {
			HttpResponse resp = wrapper.post(Request.to(Endpoints.TRACKS)
					.add(Params.Track.TITLE, song.getTitle())
					.add(Params.Track.TAG_LIST, song.getTagList())
					.add(Params.Track.DESCRIPTION, song.getDescription())
					.add(Params.Track.DOWNLOADABLE, song.isDownloadable())
					.add(Params.Track.SHARING, song.getSharing())
					.add(Params.Track.PERMALINK, song.getPermalink())
					.add(Params.Track.LABEL_NAME, song.getLabelName())
					.add(Params.Track.RELEASE, song.getRelease())

					.withFile(Params.Track.ASSET_DATA, songFile)
					// you can add more parameters here, e.g.
					.withFile(Params.Track.ARTWORK_DATA, artWorkFile)
					/* to add artwork */

					// set a progress listener (optional)
					.setProgressListener(
							new Request.TransferProgressListener() {
								@Override
								public void transferred(long amount) {
									System.err.print(".");
								}
							}));

			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
				System.out.println("\n201 Created "
						+ resp.getFirstHeader("Location").getValue());

				// dump the representation of the new track
				System.out.println("\n" + Http.getJSON(resp).toString(4));
			} else {
				System.err.println("Invalid status received: "
						+ resp.getStatusLine());
			}
		} finally {
			// serialise wrapper state again (token might have been refreshed)
			wrapper.toFile(songFile);
		}

	}


	private ArrayList<Song> getSongsFromSoundCloud(int currentPage, int category){
		
		String urlLink = exploreLinkList[category];
		String offset = "offset=" + String.valueOf((currentPage-1)*10);
		
		urlLink = urlLink.replace("offset=0", offset);
		
		try {
			
			/*
			 * Get Json Object from data
			 */
			
			URL oracle = new URL(urlLink);
	        BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
	        
	        String inputLine = in.readLine();
	        in.close();
			
			JSONObject track = new JSONObject(inputLine);
			JSONArray listSong = track.getJSONArray(TAG_TRACKS_EXPLORE);
			ArrayList<Song> song = onlineSongs.get(category);
			for (int i = 0 ; i< listSong.length(); i++){
				JSONObject jsonObject = listSong.getJSONObject(i);

				int position =searchId(idList, jsonObject.getString(ID)); 
				if (position < 0){
					try{
						song.add(addSongInformation(jsonObject));
						idList.add(- (position + 1), jsonObject.getString(ID));
					}catch(JSONException e){
						e.printStackTrace();
					}
				}
			}
		}catch (Exception e){
			
		}
			
		
		return onlineSongs.get(category);
	}
	/**
	 * add information into song entity class
	 * 
	 * @param me
	 * @param wrapper
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	private Song addSongInformation(JSONObject me)
			throws JSONException, IOException {
		Song song = new Song();
		
		
		//song.setCommentable(me.getBoolean(COMMENTABLE));
		//song.setCommentCount(me.getInt(COMMENT_COUNT));
		//song.setContentSize(me.getLong(CONTENT_SIZE));
		//song.setCreatedAt(me.getString(CREATED_AT));
		song.setDescription(me.getString(DESCRIPTION));
		//song.setDownloadable(me.getBoolean(DOWNLOADABLE));
		//song.setDownloadCount(me.getInt(DOWNLOAD_COUNT));
		//song.setDownloadUrl(me.getString(DOWNLOAD_URL));
		song.setDuration(me.getLong(DURATION));
		song.setLikesCount(me.getInt(LIKES_COUNT));
		//song.setFormat(me.getString(FORMAT));
		song.setGerne(me.getString(GENRE));
		//song.setKeySignature(me.getString(KEY_SIGNATURE));
		//song.setLabelID(me.getInt(LABEL_ID));
		//song.setLabelName(me.getString(LABEL_NAME));
		//song.setLicense(me.getString(LICENSE));
		//song.setPermalink(me.getString(PERMALINK));
		//song.setPermalinkUrl(me.getString(PERMALINK_URL));
		
		
		song.setPlaybackCount(me.getInt(PLAYBACK_COUNT));
		
		//song.setRelease(me.getString(RELEASE));
		//song.setReleaseDay(me.getInt(RELEASE_DAY));
		//song.setReleaseMonth(me.getInt(RELEASE_MONTH));
		//song.setReleaseYear(me.getInt(RELEASE_YEAR));
		//song.setSharing(me.getString(SHARING));
		song.setSoundcloudId(me.getInt(ID));

		//song.setStreamable(me.getBoolean(STREAMABLE));
		

		//song.setStreamable(me.getBoolean(STREAMABLE));
		song.setTagList(me.getString(TAG_LIST));
		song.setTitle(me.getString(TITLE));
		//song.setTrackType(me.getString(TRACK_TYPE));
		//song.setUri(me.getString(URI));
		//song.setUserId(me.getInt(USER_ID));
		//song.setVideoUrl(me.getString(VIDEO_URL));
		song.setWaveformUrl(me.getString(WAVEFORM_URL));
		song.setArtworkUrl(me.getString(ARTWORK_URL));
		song.setStreamUrl(me.getString(STREAM_URL));
		SoundCloudAccount soundCloudAccount = getUserInfoOfSong(me);
		song.setUser(soundCloudAccount);
		
		//Stream stream = wrapper.resolveStreamUrl(me.getString(STREAM_URL), true);
		//song.setStreamUrl(stream.streamUrl);
		
		return song;
	}

	/**
	 * @param me
	 * @throws JSONException
	 */
	private SoundCloudAccount getUserInfoOfSong(JSONObject me) throws JSONException {
		SoundCloudAccount soundCloudAccount = new SoundCloudAccount();
		JSONObject jsonObjectUser = me.getJSONObject(USER);
		soundCloudAccount.setId(jsonObjectUser.getInt(ID));
		soundCloudAccount.setFullName(jsonObjectUser.getString(Constants.UserContant.FULLNAME));
		soundCloudAccount.setUsername(jsonObjectUser.getString(Constants.UserContant.USERNAME));
		
		return soundCloudAccount;
	}
	

	/**
	 * add more song to the list
	 * @param current_page
	 */
	public void loadMoreSong(int current_page, int category) {
		//System.out.println ("LOAD MORE SONG WITH CURRENT PAGE : " + current_page);
		getSongsFromSoundCloud(current_page, category);
		// TODO Auto-generated method stub
		
	}
	/**
	 * search the Id of song in a list
	 * 
	 */
	private int searchId(ArrayList<String> songidList, String songID){
		
		return Collections.binarySearch(songidList,songID);
	

	}
	
	/**
	 * Initialize onlineSong list
	 */
	private void initialOnlineSongsList(){
		for (int i = 0; i < NUMBER_CATEGORY;i++){
			onlineSongs.add(new ArrayList<Song>());
		}
	}
	
	/**
	 * Initialize link of category
	 */
	public void initialCategoryListLink(){
		exploreLinkList = new String[] {TRENDING_MUSIC_LINK,
						   TRENDING_AUDIO_LINK,
						   ALTERNATIVE_ROCK_LINK,
						   AMBIENT_LINK,
						   CLASSICAL_LINK,
						   COUNTRY_LINK,
						   DANCE_LINK,
						   DEEP_HOUSE_LINK,
						   DISCO_LINK,
						   DRUM_BASS_LINK,
						   DUBSTEP_LINK,
						   ELECTRO_LINK,
						   ELECTRONIC_LINK,
						   FOLK_LINK};
		
	}
	
	/**
	 * Load 1st page of each category
	 */
	public void initialSongCategory(){
		//System.out.println ("TEST INITIAL");
		for (int i = 0 ; i< NUMBER_CATEGORY;i++){
			//System.out.println ("CATEGORY  = " +i);
			getSongsFromSoundCloud(1,i);
		}
	}

	/**
	 * 
	 * @param id id of the song
	 * @throws IOException 
	 */
	public void downloadSong(Song song) throws IOException {
		// TODO Auto-generated method stub

		new downloadSongFromSoundCloud(song).execute();

	}
	private class downloadSongFromSoundCloud extends AsyncTask<String,String,String>{

		private Song song;
		NotificationCompat.Builder mBuilder;
		String result;
		NotificationManager mNotifyManager;
		int incr;
		
		public downloadSongFromSoundCloud(Song song) {
			// TODO Auto-generated constructor stub
			this.song = song;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mNotifyManager = (NotificationManager) MainActivity.getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
			mBuilder = new NotificationCompat.Builder(MainActivity.getActivity());
			mBuilder.setContentTitle("Music Download")
			    .setContentText("Download in progress")
			    .setSmallIcon(R.drawable.download);
			mBuilder.setAutoCancel(true);
			   //Displays the progress bar for the first time.
        	//mNotifyManager.notify(1, mBuilder.build());
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String streamUrl;
			
			SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
			ApiWrapper wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null, soundCloudUserController.getToken());
			
				
			try {
				streamUrl = wrapper.resolveStreamUrl(song.getStreamUrl(), true).streamUrl;
			
		        int count;
		        
		        URL url = new URL(streamUrl);
		        URLConnection conection = url.openConnection();
		        conection.connect();
		        // getting file length
		        int lenghtOfFile = conection.getContentLength();
		        
		        // input stream to read file - with 8k buffer
		        InputStream input = new BufferedInputStream(url.openStream());
	
		        // Output stream to write file
		        String outputName = dir + "/" + song.getTitle() +".mp3";
	            OutputStream output = new FileOutputStream(outputName);
	 
	            byte data[] = new byte[1024];
	 
	            long total = 0;
	            
	            while ((count = input.read(data)) != -1) {
	             //   total += count;
	                	//System.out.println ("INCRE " + incr);
	                	mBuilder.setProgress(0, 0, true);
	                	mNotifyManager.notify(1, mBuilder.build());
	                // publishing the progress....
	            // After this onProgressUpdate will be called
	            //publishProgress(""+(int)((total*100)/lenghtOfFile));
	 
	                // writing data to file
	                output.write(data, 0, count);
	            }
	 
	            // flushing output
	            output.flush();
	 
	            // closing streams
		        output.close();
		        input.close();
		        result = "Download sucessfully";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				result = "You are not allowed to download this file";
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// When the loop is finished, updates the notification
            mBuilder.setContentText("Download complete")
            // Removes the progress bar
                    .setProgress(0,0,false);
            mNotifyManager.notify(1, mBuilder.build());
            Toast.makeText(MainActivity.getActivity(), result, Toast.LENGTH_LONG).show();
            
			// TODO Auto-generated method stub
			
		}
		
	}
}
