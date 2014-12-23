package ngo.music.soundcloudplayer.boundary;


import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.SongAdapter;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SongsFragment extends Fragment {
	public static SongsFragment instance = null;
	private SongsFragment() {
		
	}
	View rootView = null;
	public static SongsFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new SongsFragment();
		}
		return instance;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		instance = this;
		rootView = inflater.inflate(R.layout.tab_songs_view, container,false);
		final ListView songsList = (ListView) rootView.findViewById(R.id.songs_list);
		songsList.setAdapter(SongAdapter.getInstance());
		songsList.setOnItemClickListener(new  OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				Song song = (Song) songsList.getAdapter().getItem(position);
				ArrayList<Song> songs = (((SongAdapter) songsList.getAdapter()).getSongs());
				MusicPlayerService.getInstance().playNewSong(position, songs);
			}
		});
		return rootView;
	}

}
