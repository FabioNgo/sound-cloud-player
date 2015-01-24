package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Endpoints;
import ngo.music.soundcloudplayer.api.Http;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.entity.OnlineSong;
import ngo.music.soundcloudplayer.entity.Playlist;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;

public class SoundCloudPlaylistController extends CategoryController implements Constants.Data, Constants, Constants.PlaylistConstant {

	private static SoundCloudPlaylistController instance = null;
	ArrayList<Playlist> playlists =  new ArrayList<Playlist>();
	
	
	SoundCloudPlaylistController() {
		// TODO Auto-generated constructor stub
		// playlists = new ArrayMap<String, ArrayList<Song>>();
		System.out.println ("SOUND CLOUD PLAYLIST CREATED");
		instance = this;
		filename = "playlists";
		categories = getCategories();
		TAG_DATA_CHANGED = PLAYLIST_CHANGED;
		TAG_ITEM_CHANGED = ITEM_IN_PLAYLIST_CHANGED;
	}

	public static SoundCloudPlaylistController getInstance() {
		if (instance == null) {
			new SoundCloudPlaylistController();
		}

		return instance;

	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<String> getCategoryName() {
		
		ArrayList<String> categoriesName = new ArrayList<String>();
		try {
			
			playlists = new loadPlaylistBackground().execute(categoriesName).get();
			//ApiWrapper wrapper = SoundCloudUserController.getInstance().getApiWrapper();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return categoriesName;
	}
	
	

	
	
	private class loadPlaylistBackground extends AsyncTask<ArrayList<String>, String, ArrayList<Playlist>>{
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected ArrayList<Playlist> doInBackground(ArrayList<String>... params) {
			// TODO Auto-generated method stub
			
			
			ApiWrapper  wrapper = SoundCloudUserController.getInstance().getApiWrapper();
			String currentViewUserId = String.valueOf(SoundCloudUserController.getInstance().getUser().getId());
			
			
			try {
				HttpResponse resp;
				String request = Constants.USER_LINK + "/"+ currentViewUserId +"/playlists";
				resp = wrapper.get(Request.to(request));
				String resposeStr = Http.getString(resp);
				JSONArray JArray =  new JSONArray(resposeStr);
				for (int i = 0; i< JArray.length(); i++){
					JSONObject jObject = JArray.getJSONObject(i);
					playlists.add(addPlaylistInfomation(jObject));
					params[0].add(jObject.getString(PLAYLIST_TITLE));
				}
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return playlists;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return playlists;
		}
		
		@Override
		protected void onPostExecute(ArrayList<Playlist> result) {
			// TODO Auto-generated method stub
			
			
		}
		
		
	}
	
	private Playlist addPlaylistInfomation(JSONObject jObject) throws JSONException{
		Playlist playlist = new Playlist();
		playlist.setId(jObject.getString(PLAYLIST_ID));
		playlist.setTitle(jObject.getString(PLAYLIST_TITLE));
		playlist.setUserId(jObject.getInt(PLAYLIST_CREATOR_ID));
		playlist.setDuration(jObject.getLong(PLAYLIST_DURATION));
		playlist.setDescription(jObject.getString(PLAYLIST_DESCRIPTION));
		playlist.setPermalinkUrl(jObject.getString(PLAYLIST_PERMALINK_URL));
		return playlist;
	}
	
	/**
	 * Add soundcloud song to soundcloud playlist
	 * @param song
	 */
	public boolean addSongToPlaylist(Playlist scPplaylist, OnlineSong song){
	
		ApiWrapper wrapper = SoundCloudUserController.getInstance().getApiWrapper();
		String request = ME_PLAYLISTS + "/" + scPplaylist.getId(); 
		try {
			HttpResponse response = wrapper.put(Request.to(request)
									.with("playlist[tracks][][id]", song.getId()));
			return response.getStatusLine().toString().contains("200");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public void addSongsToCategory(String categoryName, ArrayList<Song> songs)throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
