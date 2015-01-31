package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.asynctask.UpdateNewSongBackgroundTask;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.Song;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ArtistsFragment extends ListContentFragment implements OnRefreshListener{
	public static ArtistsFragment instance = null;

	ListView songsList;
	public ArtistsFragment() {
		// TODO Auto-generated constructor stub
		super();
		instance = this;
	}
	public static ArtistsFragment getInstance() {
		// TODO Auto-generated method stub
		if (instance == null) {
			instance = new ArtistsFragment();
		}
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.refresh_list_view, container, false);
		swipeRefreshLayoutId = R.id.songs_swipe_refresh;
		iniSwipeRefreshLayout();
		songsList = (ListView) rootView.findViewById(R.id.items_list);
		UIController.getInstance().addListContentFragements(instance);

		return rootView;
	}

	@Override
	public void load() {

		// TODO Auto-generated method stub
		if (adapter == null) {
			adapter = OfflineSongAdapter.getInstance();
		}

		UIController.getInstance().addAdapter(adapter);
		songsList.setAdapter((ListAdapter) adapter);

		songsList.setOnItemClickListener(this);
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		initiateRefresh();
	}

	protected void initiateRefresh() {
		// TODO Auto-generated method stub
		if (mSwipeRefreshLayout != null) {
			new UpdateNewSongBackgroundTask(mSwipeRefreshLayout).execute(songsList);
		}
	}

	protected void iniSwipeRefreshLayout() {
		mSwipeRefreshLayout = (SwipeRefreshLayout) rootView
				.findViewById(swipeRefreshLayoutId);
		if (mSwipeRefreshLayout != null) {
			mSwipeRefreshLayout.setOnRefreshListener(this);
			mSwipeRefreshLayout.setColorSchemeResources(R.color.primary,R.color.accent);
		}
	}

	protected void onRefreshComplete(ArrayList<Song> result) {
		// TODO Auto-generated method stub

	}

	public ListAdapter getAdapter() {
		// TODO Auto-generated method stub
		return adapter;
	}
}
