package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.Toast;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Http;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.entity.SCSong;
import ngo.music.soundcloudplayer.entity.SCPlaylist;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;

public abstract class SCPlaylistController extends CategoryController implements
		Constants.PlaylistConstant {

	@Override
	public void storeCategories() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSongsToCategory(String categoryName, ArrayList<Song> songs)
			throws Exception {
		// TODO Auto-generated method stub
		for (Category category : categories) {
			if (category.getTitle().equals(categoryName)) {
				new addSongToSCPlaylistBackground(category, songs).execute();
			}
		}

	}

	

	/**
	 * Add Infomation of a playlist
	 * 
	 * @param jObject
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("finally")
	protected SCPlaylist addPlaylistInfomation(JSONObject jObject)
			throws JSONException {
		String title = jObject.getString(PLAYLIST_TITLE);
		ArrayList<Song> songs = new ArrayList<Song>();
		try {
			ApiWrapper wrapper = SCUserController.getInstance().getApiWrapper();

			HttpResponse resp = wrapper.get(Request.to(Constants.ME_PLAYLISTS
					+ "/" + jObject.getString(PLAYLIST_ID)));
			if (resp.getStatusLine().toString().contains("200")) {
				String me = Http.getString(resp);
				JSONObject playlistObject = new JSONObject(me);

				JSONArray songsJson = playlistObject.getJSONArray("tracks");
				for (int i = 0; i < songsJson.length(); i++) {
					JSONObject object = songsJson.getJSONObject(i);

					songs.add(SongController.getInstance().addSongInformationSimple(
							object));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			SCPlaylist playlist = new SCPlaylist(title, songs);

			// System.out.println(jObject.getString(PLAYLIST_TITLE));
			playlist.setTitle(jObject.getString(PLAYLIST_TITLE));
			playlist.setId(jObject.getString(PLAYLIST_ID));
			playlist.setArtworkUrl(jObject.getString(PLAYLIST_ARTWORK_URL));
			playlist.setUserId(jObject.getInt(PLAYLIST_CREATOR_ID));
			playlist.setDuration(jObject.getLong(PLAYLIST_DURATION));
			playlist.setDescription(jObject.getString(PLAYLIST_DESCRIPTION));
			playlist.setPermalinkUrl(jObject.getString(PLAYLIST_PERMALINK_URL));

			return playlist;
		}
	}

	@Override
	public Category createCategory(String name) {
		// TODO Auto-generated method stub
		ApiWrapper wrapper = SCUserController.getInstance().getApiWrapper();
		try {
			HttpResponse resp = wrapper.post((Request
					.to(Constants.ME_PLAYLISTS)).with("playlist[title]", name));
			if (resp.getStatusLine().toString().contains("201")) {
				String me = Http.getString(resp);
				JSONObject jObject = new JSONObject(me);
				return addPlaylistInfomation(jObject);
			}
			return null;

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Add array of song to playlist
	 * 
	 * @author LEBAO_000
	 *
	 */
	private class addSongToSCPlaylistBackground extends
			AsyncTask<String, String, String> {

		SCPlaylist playlist;
		ArrayList<Song> songs;

		public addSongToSCPlaylistBackground(Category category,
				ArrayList<Song> songs) {
			// TODO Auto-generated constructor stub
			playlist = (SCPlaylist) category;
			this.songs = songs;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ApiWrapper wrapper = SCUserController.getInstance().getApiWrapper();
			try {
				HttpResponse resp = null;
				for (Song onlineSong : songs){	
						resp = wrapper.put(Request
							.to(Constants.ME_PLAYLISTS + "/" + playlist.getId())
							.with("playlist[tracks][][id]", (Integer.parseInt(((SCSong)onlineSong).getId()))));
						
				}

				if (resp.getStatusLine().toString().contains("200")) {
					return "Add successfully";
				}
				return "Add unsuccessfully";

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block

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
						"Cannot add this song to your playlist",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(MusicPlayerMainActivity.getActivity(), result,
						Toast.LENGTH_LONG).show();
			}
			UIController.getInstance()
					.updateUiWhenDataChanged(TAG_ITEM_CHANGED);
			super.onPostExecute(result);
		}

	}

	

}
