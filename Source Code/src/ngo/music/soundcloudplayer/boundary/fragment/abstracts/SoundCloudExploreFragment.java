package ngo.music.soundcloudplayer.boundary.fragment.abstracts;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.SCSongAdapter;
import ngo.music.soundcloudplayer.adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.adapters.SCExploreAdapter;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.boundary.SCActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.database.DatabaseCreate;
import ngo.music.soundcloudplayer.entity.SCSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore.ConstraintsColumns;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public abstract class SoundCloudExploreFragment extends
		NoRefreshListContentFragment implements Constants,
		Constants.SoundCloudExploreConstant {
	// public static SoundCloudExploreFragment instance = null;
	protected String query;

	@Override
	protected boolean hasLoadMore() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected boolean hasToolbar() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void setUpToolBar(Toolbar toolbar) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateToolbar(Toolbar toolbar) {
		// TODO Auto-generated method stub

	}

	// private SoundCloudExploreFragment() {
	//
	// }

	protected ApiWrapper wrapper;

	public SoundCloudExploreFragment() {
		super();
		// current_page = setCurrentPage();
		SCUserController soundCloudUserController = SCUserController
				.getInstance();
		Token t = soundCloudUserController.getToken();
		wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null, t);
		current_page = 0;
	}

	// public static SoundCloudExploreFragment getInstance() {
	// // TODO Auto-generated method stub
	// if(instance == null) {
	// instance = new SoundCloudExploreFragment();
	// }
	// return instance;
	// }
	protected abstract int getCategory();

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		// inflater = getActivity().getMenuInflater();
		// inflater.inflate(R.menu.options_menu, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getActivity()
				.getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getActivity().getComponentName()));

	}

	@Override
	protected void setUpLoadMore() {
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			//
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				// what is the bottom iten that is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;
				// adapter.notifyDataSetChanged();
				// is the bottom item visible & not loading more already ?
				// Load more !
				if (lastInScreen >= totalItemCount - 1 && !loadingMore) {
					loadingMore = true;

					new loadMoreBackground().execute(Integer.valueOf(category));
					adapter.notifyDataSetChanged();

				}

				// TODO Auto-generated method stub

			}
		});
	}

	class loadMoreBackground extends AsyncTask<Integer, String, String> {

		SongController songController = SongController.getInstance();
		private int mCategory;

		@Override
		protected String doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			mCategory = params[0].intValue();
			songController = SongController.getInstance();
			songController.loadMoreSong(current_page, category, query);
			current_page++;
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			// Appending new data to menuItems ArrayList
			SCActivity.getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					MusicPlayerMainActivity.getActivity().runOnUiThread(
							new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									int currentPosition = listView
											.getFirstVisiblePosition();
									((SCSongAdapter) adapter)
											.update(mCategory);
									adapter.notifyDataSetChanged();
									listView.setSelectionFromTop(
											currentPosition + 1, 0);

									MusicPlayerService.getInstance()
											.updateQueue(mCategory);
									loadingMore = false;
								}
							});

				}
			});

		}
	}

	@Override
	protected ArrayAdapter<?> getAdapter() {
		// TODO Auto-generated method stub
		ArrayList<Song> songs;
		SongController songController = SongController.getInstance();

		songs = songController.getOnlineSongs(category);
		// ArrayList<Song> songs = //new
		// BackgroundLoadOnlineMusic().execute().get();
		// System.out.println (songs.size() + "......" + category);
		return new SCExploreAdapter(MusicPlayerMainActivity.getActivity()
				.getApplicationContext(), R.layout.list_view, songs, wrapper);
	}
}
