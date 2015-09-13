package ngo.music.player.controller;

import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import ngo.music.player.boundary.MusicPlayerMainActivity;
import ngo.music.player.entity.Category;
import ngo.music.player.entity.SCPlaylist;
import ngo.music.player.entity.Song;
import ngo.music.player.helper.Constants;

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


			return null;

	}

	@Override
	public Category createCategory(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Add array of song to playlist
	 * 
	 * @author LEBAO_000
	 *
	 */
	class addSongToSCPlaylistBackground extends
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
			return null;

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

			super.onPostExecute(result);
		}

	}

	

}
