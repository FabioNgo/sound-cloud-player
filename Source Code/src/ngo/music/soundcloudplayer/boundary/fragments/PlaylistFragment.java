package ngo.music.soundcloudplayer.boundary.fragments;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.PlaylistAdapter;
import ngo.music.soundcloudplayer.controller.UIController;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PlaylistFragment extends ListContentFragment {

	public PlaylistFragment() {
		// TODO Auto-generated constructor stub
		super();
		adapter = PlaylistAdapter.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.refresh_list_view, container,
				false);
		

		listView = (ListView) rootView.findViewById(R.id.items_list);
		UIController.getInstance().addListContentFragements(this);

		return rootView;

	}


}
