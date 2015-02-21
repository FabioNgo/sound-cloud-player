package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.ListSongAdapter;
import ngo.music.soundcloudplayer.adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.adapters.SCExploreAdapter;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.boundary.SCActivity;
import ngo.music.soundcloudplayer.controller.ListViewOnItemClickHandler;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.database.DatabaseHandler;
import ngo.music.soundcloudplayer.entity.OnlineSong;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public abstract class SoundCloudExploreFragment extends Fragment implements
		Constants, Constants.SoundCloudExploreConstant {
	// public static SoundCloudExploreFragment instance = null;

	// Flag for current page
	protected int current_page;
	/**
	 * false : not loading
	 */
	protected boolean loadingMore = false;
	protected ListSongAdapter adapter;
	protected ListView songsList;

	/*
	 * Category in Explore
	 */
	protected int category;

	// private SoundCloudExploreFragment() {
	//
	// }
	protected View rootView = null;

	protected ApiWrapper wrapper;

	public SoundCloudExploreFragment() {
		category = setCategory();
		current_page = setCurrentPage();
	}

	// public static SoundCloudExploreFragment getInstance() {
	// // TODO Auto-generated method stub
	// if(instance == null) {
	// instance = new SoundCloudExploreFragment();
	// }
	// return instance;
	// }
	protected abstract int setCategory();

	protected abstract int setCurrentPage();

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.list_view, container, false);
		songsList = (ListView) rootView.findViewById(R.id.items_list);
		SCUserController soundCloudUserController = SCUserController
				.getInstance();
		Token t = soundCloudUserController.getToken();
		wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null, t);
		// responseString = getArguments().getString(ME_FAVORITES) ;

		try {
			ArrayList<Song> songs;
			SongController songController = SongController.getInstance();

			songs = songController.getOnlineSongs(category);
			// ArrayList<Song> songs = //new
			// BackgroundLoadOnlineMusic().execute().get();
			// System.out.println (songs.size() + "......" + category);
			adapter = new SCExploreAdapter(MusicPlayerMainActivity
					.getActivity().getApplicationContext(), R.layout.list_view,
					songs, wrapper);
			adapter.notifyDataSetChanged();
			songsList.setAdapter(adapter);

			songsList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					ArrayList<Song> listSong = adapter.getSongs();

					MusicPlayerService.getInstance().playNewExploreSong(
							position, category, listSong);
					// TODO Auto-generated method stub

				}
			});

			songsList.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
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

		} catch (IllegalStateException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		/*
		 * Move to next page
		 */

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
		adapter.notifyDataSetChanged();
	}

	private class loadMoreBackground extends AsyncTask<Integer, String, String> {

		SongController songController = SongController.getInstance();
		private int mCategory;

		@Override
		protected String doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			mCategory = params[0].intValue();
			songController = SongController.getInstance();
			songController.loadMoreSong(current_page, category);
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
					

					MusicPlayerMainActivity.getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int currentPosition = songsList.getFirstVisiblePosition();
							adapter.update(mCategory);	
							songsList.setSelectionFromTop(currentPosition + 1, 0);
							MusicPlayerService.getInstance().updateQueue(mCategory);
							loadingMore = false;
						}
					});
					
				}
			});
//			adapter = new SCExploreAdapter(SCActivity.getActivity(),
//					R.layout.list_view,
//					songController.getOnlineSongs(mCategory), wrapper);

			// Setting new scroll position
			
			
		}
	}

}
