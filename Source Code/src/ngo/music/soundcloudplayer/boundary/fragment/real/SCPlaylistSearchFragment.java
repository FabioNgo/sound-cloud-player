package ngo.music.soundcloudplayer.boundary.fragment.real;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.CategoryListAdapter;
import ngo.music.soundcloudplayer.adapters.SCExploreAdapter;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.CategoryListContentFragment;
import ngo.music.soundcloudplayer.controller.SCPlaylistSearchController;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

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
