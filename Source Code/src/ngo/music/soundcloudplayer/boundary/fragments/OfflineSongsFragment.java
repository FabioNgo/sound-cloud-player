package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.AsyncTask.UpdateNewSongBackgroundTask;
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

public class OfflineSongsFragment extends ListContentFragment implements OnRefreshListener{
	

	
	public OfflineSongsFragment() {
		// TODO Auto-generated constructor stub
		super();
		adapter = OfflineSongAdapter.getInstance();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.refresh_list_view, container, false);
		swipeRefreshLayoutId = R.id.songs_swipe_refresh;
		iniSwipeRefreshLayout();
		listView = (ListView) rootView.findViewById(R.id.items_list);
		UIController.getInstance().addListContentFragements(this);

		return rootView;
	}

	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		initiateRefresh();
	}

	protected void initiateRefresh() {
		// TODO Auto-generated method stub
		if (mSwipeRefreshLayout != null) {
			new UpdateNewSongBackgroundTask(mSwipeRefreshLayout).execute(listView);
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
