package ngo.music.player.boundary.fragment.abstracts;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ngo.music.player.asynctask.UpdateNewSongBackgroundTask;
import ngo.music.player.controller.UIController;
import ngo.music.player.entity.Song;
import ngo.music.player.helper.Constants;
import ngo.music.player.R;

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
