package ngo.music.player.controller;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ngo.music.player.entity.Category;
import ngo.music.player.entity.SCPlaylist;
import ngo.music.player.entity.Song;
import ngo.music.player.helper.Constants;

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
		return null;
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
