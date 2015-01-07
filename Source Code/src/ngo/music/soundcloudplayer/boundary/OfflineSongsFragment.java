package ngo.music.soundcloudplayer.boundary;

import java.util.ArrayList;

import com.todddavies.components.progressbar.ProgressWheel;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.controller.OfflineSongController;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.States;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class OfflineSongsFragment extends ListContentFragment implements OnItemClickListener {
	public static OfflineSongsFragment instance = null;
	OfflineSongAdapter adapter;
	ListView songsList;
	View rootView = null;

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
		songsList = (ListView) rootView.findViewById(R.id.songs_list);
		UIController.getInstance().addListContentFragements(instance);
		
		return rootView;
	}

	@Override
	public void load(boolean isfirstLoad) {

		// TODO Auto-generated method stub
		Log.i("Load", this.toString());
		if(adapter == null){
			adapter = OfflineSongAdapter.getInstance();
		}
		if (isfirstLoad) {
			UIController.getInstance().addAdapter(adapter);
			songsList.setAdapter(adapter);
			
		}
		songsList.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View arg1,
			int position, long id) {
		// TODO Auto-generated method stub

		// OfflineSongAdapter.getInstance().notifyDataSetChanged();

		// Song songSelected = (Song)
		// songsList.getAdapter().getItem(position);

		ArrayList<OfflineSong> songs = ((OfflineSongAdapter) parent.getAdapter()).getSongs();

		MusicPlayerService.getInstance().playNewOfflineSong(position,
				songs);
	}
}
