package ngo.music.soundcloudplayer.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Endpoints;
import ngo.music.soundcloudplayer.api.Http;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.entity.OnlineSong;
import ngo.music.soundcloudplayer.entity.SCPlaylist;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.entity.SCAccount;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

public class SCPlaylistSearchController extends SCPlaylistController implements Constants.Data, Constants, Constants.PlaylistConstant {

	private static final int OFFSET = 5;
	private static SCPlaylistSearchController instance = null;
	ArrayList<SCPlaylist> playlists =  new ArrayList<SCPlaylist>();
	
	
	SCPlaylistSearchController() {
		// TODO Auto-generated constructor stub
		// playlists = new ArrayMap<String, ArrayList<Song>>();
		System.out.println ("SOUND CLOUD PLAYLIST CREATED");
		instance = this;
		categories = getCategories();
		
		TAG_DATA_CHANGED = PLAYLIST_CHANGED;
		TAG_ITEM_CHANGED = ITEM_IN_PLAYLIST_CHANGED;
	}

	public static SCPlaylistSearchController getInstance() {
		if (instance == null) {
			new SCPlaylistSearchController();
		}

		return instance;

	}


	private class loadSearchPlaylistBackground extends AsyncTask<String, String, ArrayList<Category>>{
		SCUserController soundCloudUserController = SCUserController.getInstance();
		ApiWrapper wrapper = soundCloudUserController.getApiWrapper();
		
		
		
		@Override
		protected ArrayList<Category> doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try {
				HttpResponse resp = wrapper.get(Request.to(params[0]));
				//System.out.println (resp.getStatusLine());
				String me =  Http.getString(resp);
				JSONArray array =  new JSONArray(me);
				//JSONObject object = array.getJSONObject(0);
				//System.out.println (object.toString());
				for (int i = 0; i < array.length(); i++) {
					JSONObject object  = array.getJSONObject(i);
			//		System.out.println (object.get(PLAYLIST_TITLE));
			
					categories.add(addPlaylistInfomation(object));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return categories;
		}
		
		@Override
		protected void onPostExecute(ArrayList<Category> result) {
			// TODO Auto-generated method stub
			
			
		}
	}
		
	
	/**
	 * Search User from Soundcloud
	 * @param query
	 * @param page
	 */
	public void searchPlaylistSC(String query, int page) {
		// TODO Auto-generated method stub
		SCUserController soundCloudUserController = SCUserController.getInstance();
		ApiWrapper wrapper = soundCloudUserController.getApiWrapper();
		//ArrayList<Category> categories = new ArrayList<Category>(); 
		int offset = page*OFFSET;
		
		String request =  "http://api.soundcloud.com/playlists.json?q=" + query + "&limit="+ String.valueOf(offset);
		try {
			categories = new loadSearchPlaylistBackground().execute(request).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println (me);
	//	JSONObject obj = Http.getJSON(resp);
		//JSONArray a = obj.getJSONArray("errors");
		//System.out.println (obj);
//		try {
//			HttpResponse resp = wrapper.get(Request.to(request));
//			//System.out.println (resp.getStatusLine());
//			String me =  Http.getString(resp);
//			JSONArray array =  new JSONArray(me);
//			//JSONObject object = array.getJSONObject(0);
//			//System.out.println (object.toString());
//			for (int i = 0; i < array.length(); i++) {
//				JSONObject object  = array.getJSONObject(i);
//				
//				categories.add(addPlaylistInfomation(object));
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//return categories;
	}

	
	@Override
	public ArrayList<Category> getCategories() {
		if (categories == null){
			return new ArrayList<Category>();
		}
		return categories;
	}
	@Override
	public  void storeCategories() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	protected SCPlaylist addPlaylistInfomation(JSONObject jObject) throws JSONException{
		SCPlaylist playlist = new SCPlaylist("");
		playlist.setTitle(jObject.getString(PLAYLIST_TITLE));
		playlist.setId(jObject.getString(PLAYLIST_ID));
		
		playlist.setUserId(jObject.getInt(PLAYLIST_CREATOR_ID));
		playlist.setDuration(jObject.getLong(PLAYLIST_DURATION));
		playlist.setDescription(jObject.getString(PLAYLIST_DESCRIPTION));
		playlist.setPermalinkUrl(jObject.getString(PLAYLIST_PERMALINK_URL));
		return playlist;
	}

	public void clearSearch() {
		// TODO Auto-generated method stub
		categories.clear();
	}
	
	
	

}
