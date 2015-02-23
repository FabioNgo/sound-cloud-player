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
import ngo.music.soundcloudplayer.entity.SCSong;
import ngo.music.soundcloudplayer.entity.SCPlaylist;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.entity.SCAccount;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

public class SCPlaylistSearchController extends SCPlaylistController implements
		Constants.Data, Constants, Constants.PlaylistConstant {

	private static final int OFFSET = 5;
	static SCPlaylistSearchController instance = null;
	ArrayList<SCPlaylist> playlists = new ArrayList<SCPlaylist>();
	String me = "";

	public static SCPlaylistSearchController getInstance() {

		if (instance == null) {
			new SCPlaylistSearchController();
		}

		return instance;

	}

	

	/**
	 * Search User from Soundcloud
	 * 
	 * @param query
	 * @param page
	 */
	public void searchPlaylistSC(String query, int page) {
		// TODO Auto-generated method stub
		SCUserController soundCloudUserController = SCUserController
				.getInstance();
		ApiWrapper wrapper = soundCloudUserController.getApiWrapper();
		// ArrayList<Category> categories = new ArrayList<Category>();
		int offset = page * OFFSET;

		String request = "http://api.soundcloud.com/playlists.json?q=" + query
				+ "&limit=" + String.valueOf(offset);
		try {
			categories = new loadCategoryBackground().execute(request)
					.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void clearSearch() {
		// TODO Auto-generated method stub
		categories.clear();
	}

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return SC_SEARCH_PLAYLIST;
	}

	@Override
	protected int setTagItemChange() {
		// TODO Auto-generated method stub
		return ITEM_IN_SC_SEARCH_PLAYLIST_CHANGED;
	}

	@Override
	protected int setTagDataChange() {
		// TODO Auto-generated method stub
		return SC_SEARCH_PLAYLIST_CHANGED;
	}

	@Override
	public void removeSongFromCate(Song song, String cate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeCategory(String cate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTitle(String oldName, String newName) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected ArrayList<Category> getCategoriesInBackGround(String... params) {
		// TODO Auto-generated method stub
		SCUserController soundCloudUserController = SCUserController
				.getInstance();
		ApiWrapper wrapper = soundCloudUserController.getApiWrapper();

		HttpResponse resp;

		try {
			resp = wrapper.get(Request.to(params[0]));
			// System.out.println (resp.getStatusLine());

			me = Http.getString(resp);
			// JSONArray array = new JSONArray(me);
			// //JSONObject object = array.getJSONObject(0);
			// //System.out.println (object.toString());
			// for (int i = 0; i < array.length(); i++) {
			// JSONObject object = array.getJSONObject(i);
			//
			//
			// categories.add(addPlaylistInfomation(object));
			// System.out.println (object.get(PLAYLIST_TITLE));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println ("LOAD PLAYLIST DONE");
		return categories;
	}

	@Override
	protected void getCategoriesPostExcecute(ArrayList<Category> categories) {
		// TODO Auto-generated method stub
		try {

			JSONArray array = new JSONArray(me);
			// JSONObject object = array.getJSONObject(0);
			// System.out.println (object.toString());
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);

				categories.add(addPlaylistInfomation(object));
				// System.out.println (object.get(PLAYLIST_TITLE));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
