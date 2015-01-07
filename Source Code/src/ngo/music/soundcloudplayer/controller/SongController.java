package ngo.music.soundcloudplayer.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Endpoints;
import ngo.music.soundcloudplayer.api.Http;
import ngo.music.soundcloudplayer.api.Params;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.api.Stream;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.OnlineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.entity.SoundCloudAccount;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.cmc.music.common.ID3WriteException;
import org.cmc.music.metadata.IMusicMetadata;
import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;
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
import android.media.RemoteController.MetadataEditor;
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
	private ArrayList<OfflineSong> offlineSong;
	private ArrayList<Song> favoriteSong = new ArrayList<Song>();
	private ArrayList<Song> streamList= new ArrayList<Song>();
	File dir = null;
	
	private boolean isInitialSongCategory = true;
	public boolean isLoadFavoriteSong = true;
	public boolean isLoadStream = true;
	
	private ArrayList<ArrayList<Song>> onlineSongs = new ArrayList<ArrayList<Song>>();
	private int[] categoryCurrentPage = new int[NUMBER_CATEGORY];
	
	
	
	private ArrayList<Integer> idList = new ArrayList<Integer>();
	private ArrayList<Integer> favoriteIdList = new ArrayList<Integer>();
	private ArrayList<Integer> myStreamIdList = new ArrayList<Integer>();
	public int offset = 0;

	
	
	private static SongController instance = null;

	private SongController() {
		// TODO Auto-generated constructor stub
		synchronized (this) {

			if (instance == null) {
				instance = this;
//				instance.loadFavoriteSong();
//				instance.loadMyStream();
				instance.initialOnlineSongsList();
				instance.initialCategoryListLink();
				
				
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



	public ArrayList<String> getSongIDs() {
		ArrayList<String> songIDs = new ArrayList<String>();
		for (Song song : offlineSong) {
			songIDs.add(song.getId());
		}
		return songIDs;
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
		SoundCloudUserController userController = SoundCloudUserController.getInstance();
		ApiWrapper wrapper = userController.getApiWrapper();
		/*
		 * API URL OF THE SONG
		 */
		String uri = TRACK_LINK + id;

		try {
			HttpResponse resp = wrapper.get(Request.to(uri));
			JSONObject me = Http.getJSON(resp);
			// set information of logged user
			currentSong = addSongInformation(me);

		} catch (IOException e) {
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
		
		ApiWrapper wrapper = userController.getApiWrapper();

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

//	/**
//	 * upload Song from memory.
//	 * 
//	 * @param song
//	 * @param songFile
//	 * @param artWorkFile
//	 * @throws ClassNotFoundException
//	 * @throws IOException
//	 * @throws JSONException
//	 */
//	public void uploadSong(Song song, File songFile, File artWorkFile)
//			throws ClassNotFoundException, IOException, JSONException {
//
//		final ApiWrapper wrapper = ApiWrapper.fromFile(songFile);
////		SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
////		ApiWrapper wrapper = soundCloudUserController.getApiWrapper();
//		
//		try {
//			HttpResponse resp = wrapper.post(Request.to(Endpoints.TRACKS)
//					.add(Params.Track.TITLE, "test title")
//				//	.add(Params.Track.GENRE, "test genre")
//				//	.add(Params.Track.DESCRIPTION, "test desc")
//				//	.add(Params.Track.SHARING, "public")
//					//.add(Params.Track.PERMALINK, song.getPermalink())
//					//.add(Params.Track.RELEASE, song.getRelease())
//					.withFile(Params.Track.ASSET_DATA, songFile)
//					// you can add more parameters here, e.g.
//					//.withFile(Params.Track.ARTWORK_DATA, artWorkFile)
//					/* to add artwork */
//
//					// set a progress listener (optional)
//					.setProgressListener(
//							new Request.TransferProgressListener() {
//								@Override
//								public void transferred(long amount) {
//									System.err.print(".");
//								}
//							}
//					)
//			);
//
//
//			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
//				System.out.println("\n201 Created "
//						+ resp.getFirstHeader("Location").getValue());
//
//				// dump the representation of the new track
//				System.out.println("\n" + Http.getJSON(resp).toString(4));
//			} else {
//				System.err.println("Invalid status received: "
//						+ resp.getStatusLine());
//			}
//		} finally {
//			// serialise wrapper state again (token might have been refreshed)
//			wrapper.toFile(songFile);
//		}
//
//	}


	private ArrayList<Song> getSongsFromSoundCloud(int currentPage, int category){
		
		if (currentPage <= categoryCurrentPage[category] || !(BasicFunctions.isConnectingToInternet(MainActivity.getActivity()))){
			return onlineSongs.get(category);
		}
		String urlLink = exploreLinkList[category];
		String offset = "offset=" + String.valueOf((currentPage-1)*10);
		
		urlLink = urlLink.replace("offset=0", offset);
		
		//update current page of this category
		categoryCurrentPage[category] = currentPage;
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
			//System.out.println ("SIZE = " + listSong.length());
			ArrayList<Song> song = onlineSongs.get(category);
			for (int i = 0 ; i< listSong.length(); i++){
				
				JSONObject jsonObject = listSong.getJSONObject(i);
				int position = searchId(idList, jsonObject.getInt(ID));
				if (position < 0){
					song.add((Song) addSongInformation(jsonObject));
					idList.add(- (position + 1), jsonObject.getInt(ID));
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
			throws  IOException {
		Song song = new OnlineSong();
		
		try {
			((OnlineSong) song).setTagList(me.getString(TAG_LIST));
		
		song.setTitle(me.getString(TITLE));
		//song.setTrackType(me.getString(TRACK_TYPE));
		//song.setUri(me.getString(URI));
		//song.setUserId(me.getInt(USER_ID));
		//song.setVideoUrl(me.getString(VIDEO_URL));
		//song.setWaveformUrl(me.getString(WAVEFORM_URL));
		song.setArtworkUrl(me.getString(ARTWORK_URL));
		((OnlineSong) song).setStreamUrl(me.getString(STREAM_URL));
		//song.setCommentable(me.getBoolean(COMMENTABLE));
		//song.setCommentCount(me.getInt(COMMENT_COUNT));
		//song.setContentSize(me.getLong(CONTENT_SIZE));
		//song.setCreatedAt(me.getString(CREATED_AT));
		//song.setDescription(me.getString(DESCRIPTION));
		//song.setDownloadable(me.getBoolean(DOWNLOADABLE));
		//song.setDownloadCount(me.getInt(DOWNLOAD_COUNT));
		//song.setDownloadUrl(me.getString(DOWNLOAD_URL));
		//song.setDuration(me.getLong(DURATION));
		
		//song.setFormat(me.getString(FORMAT));
		song.setGenre(me.getString(GENRE));
		//song.setKeySignature(me.getString(KEY_SIGNATURE));
		//song.setLabelID(me.getInt(LABEL_ID));
		//song.setLabelName(me.getString(LABEL_NAME));
		//song.setLicense(me.getString(LICENSE));
		//song.setPermalink(me.getString(PERMALINK));
		//song.setPermalinkUrl(me.getString(PERMALINK_URL));
		
		
		
		
		//song.setRelease(me.getString(RELEASE));
		//song.setReleaseDay(me.getInt(RELEASE_DAY));
		//song.setReleaseMonth(me.getInt(RELEASE_MONTH));
		//song.setReleaseYear(me.getInt(RELEASE_YEAR));
		//song.setSharing(me.getString(SHARING));
		song.setId(me.getString(ID));

		//song.setStreamable(me.getBoolean(STREAMABLE));
		

		//song.setStreamable(me.getBoolean(STREAMABLE));

		SoundCloudAccount soundCloudAccount = getUserInfoOfSong(me);
		((OnlineSong) song).setUser(soundCloudAccount);
		((OnlineSong) song).setLikesCount(me.getInt(LIKES_COUNT));
		((OnlineSong) song).setPlaybackCount(me.getInt(PLAYBACK_COUNT));
		//Stream stream = wrapper.resolveStreamUrl(me.getString(STREAM_URL), true);
		//song.setStreamUrl(stream.streamUrl);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return song;
			//e.printStackTrace();
		}
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
	private int searchId(ArrayList<Integer> songidList, int songID){
		
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
		if (isInitialSongCategory){
		//System.out.println ("TEST INITIAL");
			for (int i = 0 ; i< NUMBER_CATEGORY;i++){
			//System.out.println ("CATEGORY  = " +i);
				categoryCurrentPage[i] = 0;
				getSongsFromSoundCloud(1,i);
			}
			isInitialSongCategory = false;
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
	/**
	 * @return the favoriteSong
	 */
	public ArrayList<Song> getFavoriteSong() {
		return favoriteSong;
	}

	
	/**
	 * 
	 * @author LEBAO_000
	 *
	 */
	public ArrayList<Song> getMyStream(){
		return streamList;
	}
	private class downloadSongFromSoundCloud extends AsyncTask<String,String,String>{

		private Song song;
		NotificationCompat.Builder mBuilder;
		
		NotificationManager mNotifyManager;
		int incr;
		private String result;
		
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
			Toast.makeText(MainActivity.getActivity(), "Start download", Toast.LENGTH_LONG).show();
			   //Displays the progress bar for the first time.
        	//mNotifyManager.notify(1, mBuilder.build());
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String streamUrl;
			
			if (!BasicFunctions.isConnectingToInternet(MainActivity.getActivity())){
				return "No internet connection";
			}
				
			try {
				streamUrl = song.getLink();
			
		        int count;
		        
		        URL url = new URL(streamUrl);
		        URLConnection conection = url.openConnection();
		        conection.connect();
		        // getting file length
		       // int lenghtOfFile = conection.getContentLength();
		        
		        // input stream to read file - with 8k buffer
		        InputStream input = new BufferedInputStream(url.openStream());
	
		        // Output stream to write file
		        String outputName = dir + "/" + song.getTitle()  + ".mp3";
	            OutputStream output = new FileOutputStream(outputName);
	            
	            byte data[] = new byte[2048];
	 
	           // long total = 0;
	            
	            while ((count = input.read(data)) != -1) {
	                mBuilder.setProgress(0, 0, true);
	                mNotifyManager.notify(1, mBuilder.build());
	                output.write(data, 0, count);
	            }
	            
	           // updateMetaData(outputName,song);
	            result = "Download sucessfully";
	            // flushing output
	            output.flush();
	 
	            // closing streams
		        output.close();
		        input.close();
		        
		        
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = "You are not allowed to download this file";
			} 
			return result;
		}
		
		private void updateMetaData(String path, Song song) {
			//System.out.println (path);
			File src = new File(path);
		 	MusicMetadataSet src_set = null;
		 	
            try {
                src_set = new MyID3().read(src);
      
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } // read metadata

            //Perhap no metada
            if (src_set == null) {
                Log.i("NULL", "NULL");
            }
            else{
            	File dst = new File(path);
	            MusicMetadata meta = new MusicMetadata(song.getTitle());
	           // meta.setAlbum("Chirag");
	            meta.setArtist(song.getArtist());
	            meta.setProducerArtist(song.getArtist());
	            try {
	            	new MyID3().update(src, src_set, meta);
	                //new MyID3().write(src, dst, src_set, meta);
	                
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (ID3WriteException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }  // write updated metadata
			// TODO Auto-generated method stub
            }
			
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
	
	/**
	 * Load list favorite of user
	 */
	public void loadFavoriteSong() {
		if (isLoadFavoriteSong && BasicFunctions.isConnectingToInternet(MainActivity.getActivity())){
			//System.out.println ("LOAD FAVORITE");
			favoriteSong = new ArrayList<Song>();
			favoriteIdList = new ArrayList<Integer>();
			//new loadFavoriteSongBackground().execute();
			SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
			
			ApiWrapper wrapper = soundCloudUserController.getApiWrapper();
			User user = soundCloudUserController.getUser();
			/*
			 * NO user login or selected
			 */
			if (user == null) return;
			HttpResponse resp;
			try {
				String request = USER_LINK + "/" +String.valueOf(user.getId()) + "/favorites";
				resp = wrapper.get(Request.to(request));
				String responseString = Http.getString(resp);
				JSONArray array =  new JSONArray(responseString);
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject  = array.getJSONObject(i);
					
					int position =searchId(favoriteIdList, jsonObject.getInt(ID)); 
					if (position < 0){
						try{
							favoriteSong.add(addSongInformation2(jsonObject));
							favoriteIdList.add(- (position + 1), jsonObject.getInt(ID));
						}catch(JSONException e){
							e.printStackTrace();
						}
					}
					
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		// TODO Auto-generated method stub
		isLoadFavoriteSong = false;
	}
	
	/**
	 * Load list tracks uploaded by current user
	 */
	public void loadMyStream() {
		
		//System.out.println (isLoadStream);
		if (isLoadStream && BasicFunctions.isConnectingToInternet(MainActivity.getActivity())){
			streamList = new ArrayList<Song>();
			myStreamIdList = new ArrayList<Integer>();
			//new loadMyStreamBackground().execute();
			SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
			
			ApiWrapper wrapper = soundCloudUserController.getApiWrapper();
			HttpResponse resp;
			try {
				User user = soundCloudUserController.getUser();
				/*
				 * NO user login or selected
				 */
				if (user == null) return;
				
				//	resp = wrapper.get(Request.to(ME_MY_STREAM));
				
					String request = USER_LINK + "/" + String.valueOf(user.getId()) + "/tracks";
					resp = wrapper.get(Request.to(request));
				
				String responseString = Http.getString(resp);
				//System.out.println (responseString);
				JSONArray array =  new JSONArray(responseString);
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject  = array.getJSONObject(i);
					int position =searchId(myStreamIdList, jsonObject.getInt(ID)); 
					if (position < 0){
						try{
							streamList.add(addSongInformation2(jsonObject));
							myStreamIdList.add(- (position + 1), jsonObject.getInt(ID));
						}catch(JSONException e){
							e.printStackTrace();
						}
					}
					
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		// TODO Auto-generated method stub
		isLoadStream = false;
	}
	
//	}
	
	/**
	 * add information into song entity class
	 * 
	 * @param me
	 * @param wrapper
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	private Song addSongInformation2(JSONObject me)
			throws JSONException, IOException {
		Song song = new OnlineSong();
		
		
		//song.setCommentable(me.getBoolean(COMMENTABLE));
		//song.setCommentCount(me.getInt(COMMENT_COUNT));
		//song.setContentSize(me.getLong(CONTENT_SIZE));
		//song.setCreatedAt(me.getString(CREATED_AT));
		//song.setDescription(me.getString(DESCRIPTION));
		//song.setDownloadable(me.getBoolean(DOWNLOADABLE));
		//song.setDownloadCount(me.getInt(DOWNLOAD_COUNT));
		//song.setDownloadUrl(me.getString(DOWNLOAD_URL));
		//song.setDuration(me.getLong(DURATION));
		//song.setFavoriteCount(me.getInt(FOVORITINGS_COUNT));
		//song.setLikesCount(me.getInt(LIKES_COUNT));
		//song.setFormat(me.getString(FORMAT));
		 song.setGenre(me.getString(GENRE));
		//song.setKeySignature(me.getString(KEY_SIGNATURE));
		//song.setLabelID(me.getInt(LABEL_ID));
		//song.setLabelName(me.getString(LABEL_NAME));
		//song.setLicense(me.getString(LICENSE));
		//song.setPermalink(me.getString(PERMALINK));
		//song.setPermalinkUrl(me.getString(PERMALINK_URL));
		
		
		((OnlineSong) song).setPlaybackCount(me.getInt(PLAYBACK_COUNT));
		
		//song.setRelease(me.getString(RELEASE));
		//song.setReleaseDay(me.getInt(RELEASE_DAY));
		//song.setReleaseMonth(me.getInt(RELEASE_MONTH));
		//song.setReleaseYear(me.getInt(RELEASE_YEAR));
		//song.setSharing(me.getString(SHARING));
		song.setId(me.getString(ID));

		//song.setStreamable(me.getBoolean(STREAMABLE));
		

		//song.setStreamable(me.getBoolean(STREAMABLE));
		song.setTagList(me.getString(TAG_LIST));
		song.setTitle(me.getString(TITLE));
		//song.setTrackType(me.getString(TRACK_TYPE));
		//song.setUri(me.getString(URI));
		//song.setUserId(me.getInt(USER_ID));
		//song.setVideoUrl(me.getString(VIDEO_URL));
		((OnlineSong) song).setWaveformUrl(me.getString(WAVEFORM_URL));
		song.setArtworkUrl(me.getString(ARTWORK_URL));
		((OnlineSong) song).setStreamUrl(me.getString(STREAM_URL));
		//SoundCloudAccount soundCloudAccount = getUserInfoOfSong(me);
		//song.setUser(soundCloudAccount);
		SoundCloudAccount soundCloudAccount = new SoundCloudAccount();
		JSONObject jsonObjectUser = me.getJSONObject(USER);
		soundCloudAccount.setId(jsonObjectUser.getInt(ID));
		//soundCloudAccount.setFullName(jsonObjectUser.getString(Constants.UserContant.FULLNAME));
		soundCloudAccount.setUsername(jsonObjectUser.getString(Constants.UserContant.USERNAME));
		((OnlineSong) song).setUser(soundCloudAccount);
		//Stream stream = wrapper.resolveStreamUrl(me.getString(STREAM_URL), true);
		//song.setStreamUrl(stream.streamUrl);
		
		return song;
	}
	

	public void clear (){
		favoriteIdList.clear();
		streamList.clear();
		
		isLoadFavoriteSong = true;
		isLoadStream = true;
		favoriteIdList.clear();
		myStreamIdList.clear();
	}
	
	
}
