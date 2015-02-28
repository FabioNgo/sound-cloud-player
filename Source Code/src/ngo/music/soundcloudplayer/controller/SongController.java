package ngo.music.soundcloudplayer.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.adapters.QueueSongAdapter;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Endpoints;
import ngo.music.soundcloudplayer.api.Http;
import ngo.music.soundcloudplayer.api.Params;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.api.Stream;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.database.ArtistSCDatabaseTable;
import ngo.music.soundcloudplayer.database.SCSongDatabaseTable;
import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.entity.OfflineSong;

import ngo.music.soundcloudplayer.entity.SCSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.entity.SCAccount;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.cmc.music.common.ID3WriteException;
import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.volley.api.AppController;

import android.R.integer;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.drm.DrmStore.ConstraintsColumns;
import android.media.RemoteController.MetadataEditor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.app.NotificationCompat;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

public class SongController implements Constants, Constants.SongConstants,
		Constants.SoundCloudExploreConstant, Constants.Categories,
		Constants.MusicService, Constants.Data {

	private static final int OFFSET = 10;

	private static final String ROOT_DIRECTORY = "/SoundCloudApp";

	private String[] exploreLinkList;
	private static final String TAG_NEXT_LINK_EXPLORE = "next_herf";
	private static final String TAG_TRACKS_EXPLORE = "tracks";

	private ArrayList<Song> favoriteSong = new ArrayList<Song>();
	private ArrayList<Song> streamList = new ArrayList<Song>();
	File dir = null;

	private boolean isInitialSongCategory = true;
	public boolean isLoadFavoriteSong = true;
	public boolean isLoadStream = true;

	private ArrayList<ArrayList<Song>> onlineSongs = new ArrayList<ArrayList<Song>>();
	private int[] categoryCurrentPage;

	private ArrayList<Integer> idList = new ArrayList<Integer>();
	private ArrayList<Integer> favoriteIdList = new ArrayList<Integer>();
	private ArrayList<Integer> myStreamIdList = new ArrayList<Integer>();
	public int offset = 0;

	/**
	 * Offline Song part
	 */
	private String filename = "stackSong.xml";
	private ArrayList<Song> offlineSongs = null;

	private int[] categoriesList;
	private static SongController instance = null;

	/*
	 * #####################################################
	 * #################### General Part ####################
	 * ######################################################
	 */
	private SongController() {
		// TODO Auto-generated constructor stub
		synchronized (this) {

			if (instance == null) {
				instance = this;
				// instance.loadFavoriteSong();
				// instance.loadMyStream();

				instance.initialCategoryListLink();

				instance.initialOnlineSongsList();
				categoryCurrentPage = new int[categoriesList.length];
				dir = new File(Environment.getExternalStorageDirectory()
						+ ROOT_DIRECTORY);
				if (!(dir.exists() && dir.isDirectory())) {
					// System.out.println("CREATE FOLDER: " + dir.mkdir());

				}
				offlineSongs = getSongsFromSDCard();
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
		for (Song song : offlineSongs) {
			if (song.getId().equals(songID)) {
				return song;
			}
		}
		for (ArrayList<Song> cat : onlineSongs) {
			for (Song song : cat) {
				if (song.getId().equals(songID)) {
					return song;
				}
			}

		}
		return null;
	}

	/**
	 * Get songs which load from the internet
	 * 
	 * @return
	 */
	public ArrayList<Song> getOnlineSongs(int category) {
		return onlineSongs.get(indexCategory(category));
	}
	private int indexCategory(int category){
		for (int i = 0; i < categoriesList.length; i++) {
			if (categoriesList[i] == category) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * Get the stream of the song by id
	 * 
	 * @param id
	 *            of a song
	 * @return song
	 * @throws IOException
	 * @throws JSONException
	 */
	public Song getSongFromID(long id) throws IOException, JSONException {

		SCSong onlineSong = null;
		/*
		 * If song already in local database
		 */
		SCSongDatabaseTable songDb = SCSongDatabaseTable
				.getInstance(MusicPlayerMainActivity.getActivity());
		// onlineSong = songDb.getSong(String.valueOf(id));
		// if (onlineSong != null){
		// return onlineSong;
		// }
		// Song currentSong = null;
		SCUserController userController = SCUserController.getInstance();
		ApiWrapper wrapper = userController.getApiWrapper();
		/*
		 * API URL OF THE SONG
		 */
		String uri = TRACK_LINK + id;

		HttpResponse resp = wrapper.get(Request.to(uri));
		JSONObject me = Http.getJSON(resp);
		// set information of logged user
		onlineSong = addSongInformationSimple(me);
		// songDb.addSong(onlineSong);

		return onlineSong;
	}

	/**
	 * Get Stream from URL
	 * 
	 * @param url
	 *            : url of the song
	 * @return Stream
	 * @throws IOException
	 * @throws JSONException
	 */
	public Song getSongFromUrl(String url) throws IOException, JSONException {
		// Stream stream = null;
		SCUserController userController = SCUserController.getInstance();

		ApiWrapper wrapper = userController.getApiWrapper();

		long id = -1;
		// try {
		id = wrapper.resolve(url);
		// } catch (IOException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		if (id == -1) {
			return null;
		}
		return getSongFromID(id);

	}

	// /**
	// * upload Song from memory.
	// *
	// * @param song
	// * @param songFile
	// * @param artWorkFile
	// * @throws ClassNotFoundException
	// * @throws IOException
	// * @throws JSONException
	// */
	// public void uploadSong(Song song, File songFile, File artWorkFile)
	// throws ClassNotFoundException, IOException, JSONException {
	//
	// final ApiWrapper wrapper = ApiWrapper.fromFile(songFile);
	// // SoundCloudUserController soundCloudUserController =
	// SoundCloudUserController.getInstance();
	// // ApiWrapper wrapper = soundCloudUserController.getApiWrapper();
	//
	// try {
	// HttpResponse resp = wrapper.post(Request.to(Endpoints.TRACKS)
	// .add(Params.Track.TITLE, "test title")
	// // .add(Params.Track.GENRE, "test genre")
	// // .add(Params.Track.DESCRIPTION, "test desc")
	// // .add(Params.Track.SHARING, "public")
	// //.add(Params.Track.PERMALINK, song.getPermalink())
	// //.add(Params.Track.RELEASE, song.getRelease())
	// .withFile(Params.Track.ASSET_DATA, songFile)
	// // you can add more parameters here, e.g.
	// //.withFile(Params.Track.ARTWORK_DATA, artWorkFile)
	// /* to add artwork */
	//
	// // set a progress listener (optional)
	// .setProgressListener(
	// new Request.TransferProgressListener() {
	// @Override
	// public void transferred(long amount) {
	// System.err.print(".");
	// }
	// }
	// )
	// );
	//
	//
	// if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
	// System.out.println("\n201 Created "
	// + resp.getFirstHeader("Location").getValue());
	//
	// // dump the representation of the new track
	// System.out.println("\n" + Http.getJSON(resp).toString(4));
	// } else {
	// System.err.println("Invalid status received: "
	// + resp.getStatusLine());
	// }
	// } finally {
	// // serialise wrapper state again (token might have been refreshed)
	// wrapper.toFile(songFile);
	// }
	//
	// }

	private ArrayList<Song> getSongsFromSoundCloud(int currentPage, int category) {

		// System.out.println ("CP  = " + currentPage + "   " + category);
		if (currentPage == 0) {
			return getOnlineSongs(category);
		}
		/*
		 * category = Search
		 */
		if (category == SEARCH) {
			return searchSongSC(MusicPlayerMainActivity.query, currentPage);
		}

		if (currentPage <= categoryCurrentPage[indexCategory(category)]
				|| !(BasicFunctions
						.isConnectingToInternet(MusicPlayerMainActivity
								.getActivity()))) {
			// System.out.println (currentPage);
			return getOnlineSongs(category);
		}

		String urlLink = exploreLinkList[indexCategory(category)];
		String offset = "offset=" + String.valueOf((currentPage - 1) * OFFSET);

		urlLink = urlLink.replace("offset=0", offset);

		// update current page of this category
		categoryCurrentPage[indexCategory(category)] = currentPage;
		try {

			/*
			 * Get Json Object from data
			 */

			URL oracle = new URL(urlLink);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					oracle.openStream()));

			String inputLine = in.readLine();
			in.close();

			JSONObject track = new JSONObject(inputLine);
			JSONArray listSong = track.getJSONArray(TAG_TRACKS_EXPLORE);
			// System.out.println ("SIZE = " + listSong.length());
			ArrayList<Song> song = getOnlineSongs(category);
			for (int i = 0; i < listSong.length(); i++) {
				JSONObject jsonObject = listSong.getJSONObject(i);
				int position = searchId(idList, jsonObject.getInt(ID));
				if (position < 0) {
					song.add((Song) addSongInformationSimple(jsonObject));
					idList.add(-(position + 1), jsonObject.getInt(ID));
				}

			}
		} catch (Exception e) {

		}

		return getOnlineSongs(category);
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

	/**
	 * @param me
	 * @throws JSONException
	 */
	private SCAccount getUserInfoOfSong(JSONObject me) throws JSONException {
		SCAccount soundCloudAccount = new SCAccount();
		JSONObject jsonObjectUser = me.getJSONObject(USER);
		soundCloudAccount.setId(String.valueOf(jsonObjectUser.getInt(ID)));

		soundCloudAccount.setUsername(jsonObjectUser
				.getString(Constants.UserContant.USERNAME));

		return soundCloudAccount;
	}

	/**
	 * add more song to the list
	 * 
	 * @param current_page
	 */
	public void loadMoreSong(int current_page, int category, String addition) {
		// System.out.println ("LOAD MORE SONG WITH CURRENT PAGE : " +
		// current_page);
		if (category == SEARCH) {
			searchSongSC(addition, current_page);
		} else {
			getSongsFromSoundCloud(current_page, category);
		}
		// TODO Auto-generated method stub

	}

	/**
	 * search the Id of song in a list
	 * 
	 */
	private int searchId(ArrayList<Integer> songidList, int songID) {

		return Collections.binarySearch(songidList, songID);

	}

	/**
	 * Initialize onlineSong list
	 */
	private void initialOnlineSongsList() {
		for (int i = 0; i < categoriesList.length; i++) {
			onlineSongs.add(new ArrayList<Song>());
		}
	}

	/**
	 * Initialize link of category
	 */
	public void initialCategoryListLink() {
		exploreLinkList = new String[] { TRENDING_MUSIC_LINK,
				TRENDING_AUDIO_LINK, ALTERNATIVE_ROCK_LINK, AMBIENT_LINK,
				CLASSICAL_LINK, COUNTRY_LINK, DANCE_EDM_LINK, DEEP_HOUSE_LINK,
				DISCO_LINK, DRUM_BASS_LINK, DUBSTEP_LINK, DANCE_HALL_LINK,
				ELECTRONIC_LINK, FOLK_LINK };
		categoriesList = new int[] { TRENDING_MUSIC, TRENDING_AUDIO,
				ALTERNATIVE_ROCK, AMBIENT, CLASSICAL, COUNTRY, DANCE_EDM,
				DEEP_HOUSE, DISCO, DRUM_BASS, DUBSTEP, DANCE_HALL, ELECTRONIC,
				FOLK };
	}

	/**
	 * Load 1st page of each category
	 */
	public void initialSongCategory() {
		if (isInitialSongCategory) {
			// System.out.println ("TEST INITIAL");
			for (int i = 0; i < categoriesList.length; i++) {
				// System.out.println ("CATEGORY  = " +i);
				categoryCurrentPage[i] = 0;
				getSongsFromSoundCloud(0, categoriesList[i]);
			}
			isInitialSongCategory = false;
		}
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
	public ArrayList<Song> getMyStream() {
		return streamList;
	}

	/**
	 * Load list favorite of user
	 * 
	 * @throws Exception
	 */
	public void loadFavoriteSong() throws Exception {
		if (isLoadFavoriteSong
				&& BasicFunctions
						.isConnectingToInternet(MusicPlayerMainActivity
								.getActivity())) {
			// System.out.println ("LOAD FAVORITE");
			favoriteSong = new ArrayList<Song>();
			favoriteIdList = new ArrayList<Integer>();
			// new loadFavoriteSongBackground().execute();
			SCUserController soundCloudUserController = SCUserController
					.getInstance();

			ApiWrapper wrapper = soundCloudUserController.getApiWrapper();
			User user = soundCloudUserController.getUser();
			/*
			 * NO user login or selected
			 */
			if (user == null)
				return;
			HttpResponse resp;
			try {
				String request = USER_LINK + "/" + String.valueOf(user.getId())
						+ "/favorites";
				resp = wrapper.get(Request.to(request));
				String responseString = Http.getString(resp);
				JSONArray array = new JSONArray(responseString);
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject = array.getJSONObject(i);
					// System.out.println (jsonObject);
					int position = searchId(favoriteIdList,
							jsonObject.getInt(ID));
					if (position < 0) {
						try {
							favoriteSong
									.add(addSongInformationSimple(jsonObject));
							favoriteIdList.add(-(position + 1),
									jsonObject.getInt(ID));
						} catch (JSONException e) {
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
	 * 
	 * @throws Exception
	 */
	public void loadMyStream() throws Exception {

		// System.out.println (isLoadStream);
		if (isLoadStream
				&& BasicFunctions
						.isConnectingToInternet(MusicPlayerMainActivity
								.getActivity())) {
			streamList = new ArrayList<Song>();
			myStreamIdList = new ArrayList<Integer>();
			// new loadMyStreamBackground().execute();
			SCUserController soundCloudUserController = SCUserController
					.getInstance();

			ApiWrapper wrapper = soundCloudUserController.getApiWrapper();
			HttpResponse resp;
			try {
				User user = soundCloudUserController.getUser();
				/*
				 * NO user login or selected
				 */
				if (user == null)
					return;

				// resp = wrapper.get(Request.to(ME_MY_STREAM));

				String request = USER_LINK + "/" + String.valueOf(user.getId())
						+ "/tracks";
				resp = wrapper.get(Request.to(request));

				String responseString = Http.getString(resp);
				// System.out.println (responseString);
				JSONArray array = new JSONArray(responseString);
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject = array.getJSONObject(i);
					int position = searchId(myStreamIdList,
							jsonObject.getInt(ID));
					if (position < 0) {
						try {
							streamList
									.add(addSongInformationSimple(jsonObject));
							myStreamIdList.add(-(position + 1),
									jsonObject.getInt(ID));
						} catch (JSONException e) {
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

	// }

	/**
	 * add information into song entity class
	 * 
	 * @param me
	 * @param wrapper
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public SCSong addSongInformationSimple(JSONObject me) throws JSONException,
			IOException {
		SCSong song = null;

		String id = me.getString(ID);
		song = getSCSongFromDatabase(id);

		if (song != null) {
			return song;
		}

		JSONObject jsonObjectUser = me.getJSONObject(USER);
		SCAccount soundCloudAccount = SCUserController.getInstance()
				.getUserbyId(jsonObjectUser.getString(ID));
		// soundCloudAccount.setFullName(jsonObjectUser.getString(Constants.UserContant.FULLNAME));

		song = new SCSong(me.getString(ID), me.getString(TITLE),
				soundCloudAccount.getFullName(), "soundcloud.com",
				me.getString(STREAM_URL), me.getLong(DURATION));

		// song.setCommentable(me.getBoolean(COMMENTABLE));
		// song.setCommentCount(me.getInt(COMMENT_COUNT));
		// song.setContentSize(me.getLong(CONTENT_SIZE));
		// song.setCreatedAt(me.getString(CREATED_AT));
		// song.setDescription(me.getString(DESCRIPTION));
		// song.setDownloadable(me.getBoolean(DOWNLOADABLE));
		// song.setDownloadCount(me.getInt(DOWNLOAD_COUNT));
		// song.setDownloadUrl(me.getString(DOWNLOAD_URL));
		// song.setDuration(me.getLong(DURATION));
		// song.setFavoriteCount(me.getInt(FOVORITINGS_COUNT));
		// song.setLikesCount(me.getInt(LIKES_COUNT));
		// song.setFormat(me.getString(FORMAT));
		// song.setGenre(me.getString(GENRE));
		// song.setKeySignature(me.getString(KEY_SIGNATURE));
		// song.setLabelID(me.getInt(LABEL_ID));
		// song.setLabelName(me.getString(LABEL_NAME));
		// song.setLicense(me.getString(LICENSE));
		// song.setPermalink(me.getString(PERMALINK));
		// ((OnlineSong) song).setPermalinkUrl(me.getString(PERMALINK_URL));

		// ((OnlineSong) song).setPlaybackCount(me.getInt(PLAYBACK_COUNT));

		// song.setRelease(me.getString(RELEASE));
		// song.setReleaseDay(me.getInt(RELEASE_DAY));
		// song.setReleaseMonth(me.getInt(RELEASE_MONTH));
		// song.setReleaseYear(me.getInt(RELEASE_YEAR));
		// song.setSharing(me.getString(SHARING));

		// song.setStreamable(me.getBoolean(STREAMABLE));

		// song.setStreamable(me.getBoolean(STREAMABLE));
		// song.setTagList(me.getString(TAG_LIST));
		// song.setTrackType(me.getString(TRACK_TYPE));
		// song.setUri(me.getString(URI));
		// song.setUserId(me.getInt(USER_ID));
		// song.setVideoUrl(me.getString(VIDEO_URL));

		((SCSong) song).setUser(soundCloudAccount);
		// ((OnlineSong) song).setWaveformUrl(me.getString(WAVEFORM_URL));
		song.setArtworkUrl(me.getString(ARTWORK_URL));
		// SoundCloudAccount soundCloudAccount = getUserInfoOfSong(me);
		// song.setUser(soundCloudAccount);
		// addOnlineSongToDatabase(song);
		// Stream stream = wrapper.resolveStreamUrl(me.getString(STREAM_URL),
		// true);
		// song.setStreamUrl(stream.streamUrl);

		return song;
	}

	public void clear() {
		favoriteIdList.clear();
		streamList.clear();

		isLoadFavoriteSong = true;
		isLoadStream = true;
		favoriteIdList.clear();
		myStreamIdList.clear();
	}

	/**
	 * SearchSong from Soundcloud
	 * 
	 * @param query
	 * @param page
	 */
	public ArrayList<Song> searchSongSC(String query, int page) {
		// TODO Auto-generated method stub

		SCUserController soundCloudUserController = SCUserController
				.getInstance();
		ApiWrapper wrapper = soundCloudUserController.getApiWrapper();
		ArrayList<Song> searchSongs = onlineSongs.get(SEARCH);
		int offset = (page) * 10;
		String request = "http://api.soundcloud.com/tracks.json?q=" + query
				+ "&limit=10&offset=" + String.valueOf(offset);

		// System.out.println(request);
		// System.out.println (me);
		// JSONObject obj = Http.getJSON(resp);
		// JSONArray a = obj.getJSONArray("errors");
		// System.out.println (obj);
		try {
			HttpResponse resp = wrapper.get(Request.to(request));
			// System.out.println (resp.getStatusLine());
			String me = Http.getString(resp);
			JSONArray array = new JSONArray(me);
			// JSONObject object = array.getJSONObject(0);
			// System.out.println (object.toString());
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				searchSongs.add((Song) addSongInformationSimple(object));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return searchSongs;
	}

	/**
	 * Refresh search list
	 */
	public void clearSearch() {
		onlineSongs.get(SEARCH).clear();

	}

	/**
	 * Get online song
	 * 
	 * @param id
	 * @return
	 */
	private SCSong getSCSongFromDatabase(String id) {
		SCSong onlineSong;
		SCSongDatabaseTable songDb = SCSongDatabaseTable
				.getInstance(MusicPlayerMainActivity.getActivity());

		// System.out.println ("USER ID " + id);
		onlineSong = songDb.getSong(id);
		if (onlineSong == null) {
			return null;
		} else {

			SCAccount scAccount = SCUserController.getInstance()
					.getSCArtistFromDatabase(onlineSong.getUserId());
			onlineSong.setUser(scAccount);
			onlineSong.setArtist(scAccount.getFullName());
			return onlineSong;
		}
	}

	/**
	 * Add a online song to Database
	 * 
	 * @param song
	 * @throws IOException
	 */
	private void addOnlineSongToDatabase(SCSong song) throws IOException {
		SCSongDatabaseTable songDb = SCSongDatabaseTable
				.getInstance(MusicPlayerMainActivity.getActivity());
		songDb.addSong(song);
	}

	/****************************************************
	 * ****************Offline Song Part*****************
	 ****************************************************/
	/**
	 * get all songs
	 * 
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Song> getOfflineSongs(boolean checkUpdate) {
		if (checkUpdate) {
			offlineSongs = getSongsFromSDCard();
		}

		return offlineSongs;
	}

	/**
	 * get stack of songs played
	 * 
	 * @return
	 */
	public ArrayList<Object[]> getSongsPlayed() {
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename);
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
					object[0] = getSong(id);
					object[1] = Integer.valueOf(reader.nextInt());
					if (object[0] != null) {
						songs.add(object);
					}
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
		long curTime = MusicPlayerService.getInstance().getCurrentTime();
		ArrayList<Song> queue = MusicPlayerService.getInstance().getQueue();
		File file = new File(MusicPlayerService.getInstance()
				.getApplicationContext()
				.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE), filename);

		file.createNewFile();

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		OutputStreamWriter fileWriter = new OutputStreamWriter(fileOutputStream);
		JsonWriter jsonWriter = new JsonWriter(fileWriter);
		writePlayedSongs(song, queue, jsonWriter, curTime);
		jsonWriter.close();
	}

	private void writePlayedSongs(Song playingSong, ArrayList<Song> queue,
			JsonWriter jsonWriter, long currentTIme) throws IOException {
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

	private ArrayList<Song> getSongsFromSDCard() {
		ArrayList<Song> songs = new ArrayList<Song>();
		Cursor c = MusicPlayerMainActivity
				.getActivity()
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

	public void deleteSong(Song song) {
		MusicPlayerService.getInstance().removeFromQueue(song, true);
		if (song instanceof OfflineSong) {
			Uri deleteUri = ContentUris.withAppendedId(
					Media.EXTERNAL_CONTENT_URI, Integer.valueOf(song.getId()));
			MusicPlayerService.getInstance().getContentResolver()
					.delete(deleteUri, null, null);
			File file;
			try {
				file = new File(song.getLink());
				if (file.isFile()) {
					if (file.exists()) {
						file.delete();

					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		UIController.getInstance()
				.updateUiWhenDataChanged(OFFLINE_SONG_CHANGED);

	}

	public ArrayList<Category> getOfflineAlbums() {
		// TODO Auto-generated method stub
		boolean isAlbumExsited = false;
		ArrayList<Category> cate = new ArrayList<Category>();
		for (Song song : offlineSongs) {
			String album = song.getAlbum();
			for (Category category : cate) {
				if (category.getTitle().equals(album)) {
					try {
						category.addSong(song);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					isAlbumExsited = true;
					break;
				} else {
					isAlbumExsited = false;
				}
			}
			if (!isAlbumExsited) {
				cate.add(new Category(album, song));
				isAlbumExsited = false;
			}

		}
		return cate;
	}

	/**
	 * Get the artists of offline song
	 * 
	 * @return
	 */
	public ArrayList<Category> getOfflineArtists() {
		// TODO Auto-generated method stub
		boolean isArtistExsited = false;
		ArrayList<Category> cate = new ArrayList<Category>();
		for (Song song : offlineSongs) {
			String artist = song.getArtist();
			for (Category category : cate) {
				if (category.getTitle().equals(artist)) {
					try {
						category.addSong(song);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Autso-generated catch block
						e.printStackTrace();
					}
					isArtistExsited = true;
					break;
				} else {
					isArtistExsited = false;
				}
			}
			if (!isArtistExsited) {
				cate.add(new Category(artist, song));
				isArtistExsited = false;
			}

		}
		return cate;
	}

}
