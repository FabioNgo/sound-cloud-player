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
import android.widget.Toast;
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
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

public class SCMyPlaylistController extends SCPlaylistController implements
		Constants.PlaylistConstant {

	private static final int OFFSET = 5;
	static SCMyPlaylistController instance = null;
	ArrayList<SCPlaylist> playlists = new ArrayList<SCPlaylist>();

	private class loadUserPlaylistBackground extends
			AsyncTask<String, String, ArrayList<Category>> {

		ArrayList<Category> categories = new ArrayList<Category>();

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected ArrayList<Category> doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {

				ApiWrapper wrapper = SCUserController.getInstance()
						.getApiWrapper();
				User user =  SCUserController.getInstance().getUser();
				
				String currentViewUserId = String.valueOf(user.getId());
				
				String request = Constants.USER_LINK + "/" + currentViewUserId+ "/playlists";
				HttpResponse resp = wrapper.get(Request.to(request));
				String resposeStr = Http.getString(resp);
				JSONArray JArray = new JSONArray(resposeStr);
				for (int i = 0; i < JArray.length(); i++) {
					JSONObject jObject = JArray.getJSONObject(i);
					categories.add(addPlaylistInfomation(jObject));
					// params[0].add(jObject.getString(PLAYLIST_TITLE));
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return categories;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return categories;
			} catch (Exception e) {
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

	
//	/**
//	 * Add soundcloud song to soundcloud playlist
//	 * 
//	 * @param song
//	 */
//	protected boolean addSongToPlaylist(SCPlaylist scPplaylist, OnlineSong song) {
//
//		ApiWrapper wrapper = SCUserController.getInstance().getApiWrapper();
//		String request = ME_PLAYLISTS + "/" + scPplaylist.getId();
//		try {
//			HttpResponse response = wrapper.put(Request.to(request).with(
//					"playlist[tracks][][id]", song.getId()));
//			return response.getStatusLine().toString().contains("200");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//	}

	// /**
	// * Search User from Soundcloud
	// * @param query
	// * @param page
	// */
	// public ArrayList<SCPlaylist> searchSongSC(String query, int page) {
	// // TODO Auto-generated method stub
	// SCUserController soundCloudUserController =
	// SCUserController.getInstance();
	// ApiWrapper wrapper = soundCloudUserController.getApiWrapper();
	// ArrayList<SCPlaylist> playlists = new ArrayList<SCPlaylist>();
	// int offset = page*OFFSET;
	// String request = "http://api.soundcloud.com/playlists.json?q=" + query +
	// "&limit="+ String.valueOf(offset);
	//
	// //System.out.println (me);
	// // JSONObject obj = Http.getJSON(resp);
	// //JSONArray a = obj.getJSONArray("errors");
	// //System.out.println (obj);
	// try {
	// HttpResponse resp = wrapper.get(Request.to(request));
	// //System.out.println (resp.getStatusLine());
	// String me = Http.getString(resp);
	// JSONArray array = new JSONArray(me);
	// //JSONObject object = array.getJSONObject(0);
	// //System.out.println (object.toString());
	// for (int i = 0; i < array.length(); i++) {
	// JSONObject object = array.getJSONObject(i);
	// playlists.add(addPlaylistInfomation(object));
	// }
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return playlists;
	// }

	@Override
	public ArrayList<Category> getCategories() throws InterruptedException, ExecutionException {

		//ArrayList<Category> categories = new ArrayList<Category>();
		//try {
			return new loadUserPlaylistBackground().execute().get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return categories;
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return categories;
//		}
		// return categories;
	}


	@Override
	protected int setTagItemChange() {
		// TODO Auto-generated method stub
		return ITEM_IN_SC_PLAYLIST_CHANGED;
	}

	@Override
	protected int setTagDataChange() {
		// TODO Auto-generated method stub
		return SC_PLAYLIST_CHANGED;
	}





	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return SC_PLAYLIST;
	}

	@Override
	public void removeSongFromCate(Song song, String cate) {
		// TODO Auto-generated method stub

		for (Category category : categories) {
			if (category.getTitle().equals(cate)) {
				new removeSongFromSCPlaylistBackground(category, song)
						.execute();
			}
		}

		// try {
		// //storeCategories();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	@Override
	public void removeCategory(String cate) {
		// TODO Auto-generated method stub
		for (Category category : categories) {
			if (category.getTitle().equals(cate)) {
				new removeSCPlaylistBackground(category).execute();
			}
		}

	}
	
	@Override
	public void updateTitle(String oldName, String newName) throws Exception {
		// TODO Auto-generated method stub
		if ("".equals(newName)) {
			throw new Exception("Playlist name cannot be empty");
		}
		for (Category category : categories) {
			if (category.getTitle().equals(newName)) {
				throw new Exception("Playlist name exsited");
			}
		}
		for (Category category : categories) {
			if (category.getTitle().equals(oldName)) {
				new updateTitleSCPlaylistBackground(category).execute(newName);
				break;
			}
		}
		storeCategories();
		UIController.getInstance().updateUiWhenDataChanged(TAG_DATA_CHANGED);
	}

	/**
	 * Update title of a playlist
	 * @author LEBAO_000
	 *
	 */
	private class updateTitleSCPlaylistBackground extends
			AsyncTask<String, String, String> {

		SCPlaylist playlist;

		public updateTitleSCPlaylistBackground(Category category) {
			// TODO Auto-generated constructor stub
			playlist = (SCPlaylist) category;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ApiWrapper wrapper = SCUserController.getInstance().getApiWrapper();
			try {
				HttpResponse resp = wrapper.put(Request.to(
						Constants.ME_PLAYLISTS + "/"
								+ playlist.getId())
						.with("playlist[title]", params[0]));
				if (resp.getStatusLine().toString().contains("200")) {
					return "Update successfully";
				}
				return "Update unsuccessfully";

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result == null) {
				Toast.makeText(MusicPlayerMainActivity.getActivity(),
						"Cannot update playlist", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(MusicPlayerMainActivity.getActivity(), result,
						Toast.LENGTH_LONG).show();
			}
			UIController.getInstance()
					.updateUiWhenDataChanged(TAG_ITEM_CHANGED);
			super.onPostExecute(result);
		}

	}
	
	
	/**
	 * Remove a playlist in background
	 * @author LEBAO_000
	 *
	 */
	private class removeSCPlaylistBackground extends
			AsyncTask<String, String, String> {

		SCPlaylist playlist;

		public removeSCPlaylistBackground(Category category) {
			// TODO Auto-generated constructor stub
			playlist = (SCPlaylist) category;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ApiWrapper wrapper = SCUserController.getInstance().getApiWrapper();
			try {
				HttpResponse resp = wrapper.delete(Request
						.to(Constants.ME_PLAYLISTS + "/"
								+ playlist.getId()));
				if (resp.getStatusLine().toString().contains("200")) {
					return "Remove successfully";
				}
				return "Remove unsuccessfully";

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result == null) {
				Toast.makeText(MusicPlayerMainActivity.getActivity(),
						"Cannot change playlist title", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(MusicPlayerMainActivity.getActivity(), result,
						Toast.LENGTH_LONG).show();
			}
			UIController.getInstance()
					.updateUiWhenDataChanged(TAG_ITEM_CHANGED);
			super.onPostExecute(result);
		}

	}
	
	/**
	 * Remove a Song from playlist
	 * @author LEBAO_000
	 *
	 */
	private class removeSongFromSCPlaylistBackground extends
			AsyncTask<String, String, String> {

		OnlineSong song;
		SCPlaylist playlist;

		public removeSongFromSCPlaylistBackground(Category category, Song song) {
			// TODO Auto-generated constructor stub
			this.song = (OnlineSong) song;
			playlist = (SCPlaylist) category;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ApiWrapper wrapper = SCUserController.getInstance().getApiWrapper();
			try {
				HttpResponse resp = wrapper.put(Request.to(
						Constants.ME_PLAYLISTS + "/"
								+ playlist.getId())
						.with("playlist[tracks][][id]",
								Integer.parseInt(this.song.getId())));
				if (resp.getStatusLine().toString().contains("200")) {
					return "Remove successfully";
				}
				return "Remove unsuccessfully";

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result == null) {
				Toast.makeText(MusicPlayerMainActivity.getActivity(),
						"Cannot remove playlist", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(MusicPlayerMainActivity.getActivity(), result,
						Toast.LENGTH_LONG).show();
			}
			UIController.getInstance()
					.updateUiWhenDataChanged(TAG_ITEM_CHANGED);

		}

	}




}
