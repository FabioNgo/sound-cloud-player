package ngo.music.player.boundary.fragment.real;

import android.os.AsyncTask;

import ngo.music.player.boundary.MusicPlayerMainActivity;
import ngo.music.player.boundary.fragment.abstracts.CategoryListContentFragment;
import ngo.music.player.controller.SCPlaylistSearchController;
import ngo.music.player.controller.SongController;

public class SCPlaylistSearchFragment extends CategoryListContentFragment {
	public static SCPlaylistSearchFragment instance = null;

	String query;
	int current_page;
	protected boolean loadingMore = false;

	public SCPlaylistSearchFragment() {
		super();
		query = MusicPlayerMainActivity.query;
		current_page = 1;

		// adapter = CompositionListAdapter.getInstance(SC_SEARCH_PLAYLIST);
		// instance = this;

	}

	protected class loadMoreBackground extends
			AsyncTask<String, String, String> {

		SongController songController = SongController.getInstance();

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			SCPlaylistSearchController.getInstance().searchPlaylistSC(query,
					current_page);
			current_page++;
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			int currentPosition = listView.getFirstVisiblePosition();

			// Appending new data to menuItems ArrayList

			// Setting new scroll position
			listView.setSelectionFromTop(currentPosition + 1, 0);

			loadingMore = false;
		}
	}

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return SC_SEARCH_PLAYLIST;
	}
}
