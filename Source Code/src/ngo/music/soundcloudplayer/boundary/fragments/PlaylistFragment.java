package ngo.music.soundcloudplayer.boundary.fragments;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.CompositionListAdapter;
import ngo.music.soundcloudplayer.Adapters.PlaylistAdapter;
import ngo.music.soundcloudplayer.controller.UIController;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PlaylistFragment extends CompositionListContentFragment {
	static PlaylistFragment instance;

	public PlaylistFragment() {
		// TODO Auto-generated constructor stub
		super();
		adapter = CompositionListAdapter.getInstance(PLAYLIST);
		instance = this;
		UIController.getInstance().addListContentFragements(this);
		type = PLAYLIST;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.list_view, container, false);

		listView = (ListView) rootView.findViewById(R.id.items_list);
		

		return rootView;

	}

	

	

}
