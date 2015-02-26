package ngo.music.soundcloudplayer.boundary.fragment.abstracts;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.CategoryListAdapter;
import ngo.music.soundcloudplayer.adapters.SCSongAdapter;
import ngo.music.soundcloudplayer.adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.adapters.PlaylistAdapter;
import ngo.music.soundcloudplayer.asynctask.UpdateNewSongBackgroundTask;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.CategoryController;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

/**
 * 
 * @author Fabio Ngo Every fragments having list view to list songs. This is
 *         used to update list when necessary
 *
 */
public abstract class RefreshListContentFragment extends ListContentFragment
		implements Constants.MusicService, Constants.Categories,
		Constants.Appplication, OnItemClickListener, OnRefreshListener {
	protected SwipeRefreshLayout mSwipeRefreshLayout;
	protected int swipeRefreshLayoutId = -1;

	@Override
	protected boolean hasLoadMore() {
		// TODO Auto-generated method stub
		return false;
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

	@Override
	final public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		numFragmentsLoading++;

		rootView = inflater.inflate(R.layout.refresh_list_view, container,
				false);
		swipeRefreshLayoutId = R.id.songs_swipe_refresh;
		iniSwipeRefreshLayout();
		adapter = getAdapter();
		load();
		numFragmentsLoading--;
		UIController.getInstance().addListContentFragments(this);
		return rootView;
	}

	protected abstract int getCategory();

	protected abstract ArrayAdapter<?> getAdapter();

	/**
	 * Load fragment activity, often list view fragment
	 * 
	 * @param firstTime
	 *            : is the first time loading or not
	 */

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		initiateRefresh();
	}

	protected void initiateRefresh() {
		// TODO Auto-generated method stub
		if (mSwipeRefreshLayout != null) {
			new UpdateNewSongBackgroundTask(mSwipeRefreshLayout)
					.execute(listView);
		}
	}

	protected void iniSwipeRefreshLayout() {
		mSwipeRefreshLayout = (SwipeRefreshLayout) rootView
				.findViewById(swipeRefreshLayoutId);
		if (mSwipeRefreshLayout != null) {
			mSwipeRefreshLayout.setOnRefreshListener(this);
			mSwipeRefreshLayout.setColorSchemeResources(R.color.primary,
					R.color.accent);
		}
	}

	protected void onRefreshComplete(ArrayList<Song> result) {
		// TODO Auto-generated method stub

	}

}
