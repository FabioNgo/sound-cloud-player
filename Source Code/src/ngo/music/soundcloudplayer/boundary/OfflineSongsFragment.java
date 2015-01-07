package ngo.music.soundcloudplayer.boundary;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.Song;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class OfflineSongsFragment extends ListContentFragment {
	public static OfflineSongsFragment instance = null;

	ListView songsList;

	public static OfflineSongsFragment getInstance() {
		// TODO Auto-generated method stub
		if (instance == null) {
			instance = new OfflineSongsFragment();
		}
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.list_view, container, false);
		swipeRefreshLayoutId = R.id.songs_swipe_refresh;
		iniSwipeRefreshLayout();
		songsList = (ListView) rootView.findViewById(R.id.songs_list);
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

}
