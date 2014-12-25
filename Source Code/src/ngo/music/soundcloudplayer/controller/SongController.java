package ngo.music.soundcloudplayer.controller;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

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

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;





public class SongController implements Constants, Constants.SongConstants{

	private ArrayList<Song> offlineSong;
	private ArrayList<Song> onlineSong;
	public int offset = 0;
	private static SongController instance = null;
	
	
	private SongController() {
		// TODO Auto-generated constructor stub
		if(instance==null) {
			instance = this;
			instance.getSongsFromSDCard();
		}
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
		for (Song song : offlineSong) {
			if (song.getId().compareTo(songID) == 0) {
				return song;
			}
		}
		return null;
	}

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

	public ArrayList<Song> getSongs() {
		return offlineSong;
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
	
	/**
	 * upload Song from memory.
	 * @param song
	 * @param songFile
	 * @param artWorkFile
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws JSONException
	 */
	public void uploadSong(Song song, File songFile, File artWorkFile) throws ClassNotFoundException, IOException, JSONException{
		
		final ApiWrapper wrapper = ApiWrapper.fromFile(songFile);
        System.out.println("Uploading " + songFile);
        try {
            HttpResponse resp = wrapper.post(Request.to(Endpoints.TRACKS)
                    .add(Params.Track.TITLE,     song.getTitle())
                    .add(Params.Track.TAG_LIST, song.getTagList())
                    .add(Params.Track.DESCRIPTION, song.getDescription())
                    .add(Params.Track.DOWNLOADABLE, song.isDownloadable())
                    .add(Params.Track.SHARING, song.getSharing())
                    .add(Params.Track.PERMALINK, song.getPermalink())
                    .add(Params.Track.LABEL_NAME, song.getLabelName())
                    .add(Params.Track.RELEASE, song.getRelease())
                    
                    .withFile(Params.Track.ASSET_DATA, songFile)
                    // you can add more parameters here, e.g.
                     .withFile(Params.Track.ARTWORK_DATA, artWorkFile) /* to add artwork */

                    // set a progress listener (optional)
                    .setProgressListener(new Request.TransferProgressListener() {
                        @Override public void transferred(long amount) {
                            System.err.print(".");
                        }
                    }));

            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                System.out.println("\n201 Created "+resp.getFirstHeader("Location").getValue());

                // dump the representation of the new track
                System.out.println("\n" + Http.getJSON(resp).toString(4));
            } else {
                System.err.println("Invalid status received: " + resp.getStatusLine());
            }
        } finally {
            // serialise wrapper state again (token might have been refreshed)
            wrapper.toFile(songFile);
        }
		
	}
	
	/**
	 * download song
	 * @param url stream link of the song
	 * @param fileName file name of the song want to be named
	 * @throws IOException 
	 */
	public void downloadSong(String streamLink,String filename) throws IOException{
		
		new DownloadFileFromURL(streamLink, filename).execute();
		
	
	}
	
	public ArrayList<Song> getAllSongsOnline(){
		
		ArrayList<Song> onlineSongs = new ArrayList<Song>();
		SoundCloudUserController userController = SoundCloudUserController.getInstance();
		Token token = userController.getToken();
		ApiWrapper wrapper =  new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null, token);
		//String me = "";
		try {
			/*
			 * get file data from website
			 */
			
			HttpResponse resp = wrapper.get(Request.to("/tracks")
												.with(Params.Track.STREAMABLE, true)
												.with(Params.Track.TYPE, "original"));
			String me  = Http.getString(resp);
			
			/*
			 * Get Json Object from data
			 */
			JSONArray jsonArray = new JSONArray(me);
			
			for (int i = 0 ; i< jsonArray.length(); i++){
				

				try{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					//System.out.println (jsonObject.getString(TITLE));
					onlineSongs.add(addSongInformation(jsonObject, wrapper));
					
				}catch(JSONException e){
					e.printStackTrace();
					
				}
			}
			
			return onlineSongs;
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
		
		
		return onlineSongs;
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
		
		
		song.setCommentable(me.getBoolean(COMMENTABLE));
		song.setCommentCount(me.getInt(COMMENT_COUNT));
		song.setContentSize(me.getLong(CONTENT_SIZE));
		song.setCreatedAt(me.getString(CREATED_AT));
		song.setDescription(me.getString(DESCRIPTION));
		song.setDownloadable(me.getBoolean(DOWNLOADABLE));
		song.setDownloadCount(me.getInt(DOWNLOAD_COUNT));
		//song.setDownloadUrl(me.getString(DOWNLOAD_URL));
		song.setDuration(me.getLong(DURATION));
		song.setFavoriteCount(me.getInt(FOVORITINGS_COUNT));
		song.setFormat(me.getString(FORMAT));
		song.setGerne(me.getString(GENRE));
		song.setKeySignature(me.getString(KEY_SIGNATURE));
		//song.setLabelID(me.getInt(LABEL_ID));
		//song.setLabelName(me.getString(LABEL_NAME));
		song.setLicense(me.getString(LICENSE));
		song.setPermalink(me.getString(PERMALINK));
		song.setPermalinkUrl(me.getString(PERMALINK_URL));
		song.setPlaybackCount(me.getInt(PLAYBACK_COUNT));
		//song.setRelease(me.getString(RELEASE));
		//song.setReleaseDay(me.getInt(RELEASE_DAY));
		//song.setReleaseMonth(me.getInt(RELEASE_MONTH));
		//song.setReleaseYear(me.getInt(RELEASE_YEAR));
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
		soundCloudAccount.setUsername(jsonObjectUser.getString(Constants.UserContant.USERNAME));
		
		return soundCloudAccount;
	}
	
	/**
	 * Background Async Task to download file
	 * */
	class DownloadFileFromURL extends AsyncTask<String, String, String> {
	 
		String filename;
		String streamUrl;
		public DownloadFileFromURL(String streamUrl, String filename) {
			// TODO Auto-generated constructor stub
			this.streamUrl = streamUrl;
			this.filename = filename;
		}
	    /**
	     * Before starting background thread
	     * Show Progress Bar Dialog
	     * */
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	     // TODO Auto-generated method stub
     		
	        //showDialog(progress_bar_type);
	    }
	    
	    
	 
	    /**
	     * Downloading file in background thread
	     * */
	    @Override
	    protected String doInBackground(String... f_url) {
	   
	        int count;
	        try {
	            URL url = new URL(f_url[0]);
	            URLConnection conection = url.openConnection();
	            conection.connect();
	            // getting file length
	            int lenghtOfFile = conection.getContentLength();
	 
	            // input stream to read file - with 8k buffer
	            InputStream input = new BufferedInputStream(url.openStream(), 8192);
	 
	            // Output stream to write file
	            String outputName = "/sdcard/SoundCloudApp/"+ filename;
	            OutputStream output = new FileOutputStream(outputName);
	 
	            byte data[] = new byte[1024];
	 
	            long total = 0;
	 
	            while ((count = input.read(data)) != -1) {
	                total += count;
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
	 
	        } catch (Exception e) {
	            Log.e("Error: ", e.getMessage());
	        }
	 
	        return null;
	    }
	 
	    /**
	     * Updating progress bar
	     * */
	    protected void onProgressUpdate(String... progress) {
	        // setting progress percentage
	        //pDialog.setProgress(Integer.parseInt(progress[0]));
	   }
	 
	    /**
	     * After completing background task
	     * Dismiss the progress dialog
	     * **/
	    @Override
	    protected void onPostExecute(String file_url) {
	        // dismiss the dialog after the file was downloaded
	        //dismissDialog(progress_bar_type);
	 
	        // Displaying downloaded image into image view
	        // Reading image path from sdcard
	        //String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
	        // setting downloaded into image view
	       // my_image.setImageDrawable(Drawable.createFromPath(imagePath));
	    }
	 
	}
}
