package ngo.music.soundcloudplayer.boundary.fragments;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.CategoryListAdapter;
import ngo.music.soundcloudplayer.adapters.PlaylistAdapter;
import ngo.music.soundcloudplayer.controller.UIController;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PlaylistFragment extends CategoryListContentFragment {
	static PlaylistFragment instance;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.list_view, container, false);

		listView = (ListView) rootView.findViewById(R.id.items_list);
		
		UIController.getInstance().addListContentFragements(this);
		return rootView;

	}



	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return PLAYLIST;
	}

	

	

}
