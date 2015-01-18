package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.entity.Song;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SimplePlaylistAdapter extends ArrayAdapter<String> {
	public static SimplePlaylistAdapter instance = null;
	public static SimplePlaylistAdapter getInstance(){
		if(instance == null){
			instance = new SimplePlaylistAdapter();
		}
		return instance;
	}

	private ArrayList<String> playlists = new ArrayList<String>();

	private SimplePlaylistAdapter() {
		super(MusicPlayerMainActivity.getActivity(), R.layout.single_playilist_list_adding);
		// TODO Auto-generated constructor stub
		playlists = PlaylistController.getInstance().getCategoryName();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.single_playilist_list_adding, null);

		}

		setLayoutInformation(position, v);
		return v;
	}

	private void setLayoutInformation(int position, View v) {
		// TODO Auto-generated method stub
		TextView tv = (TextView) v.findViewById(R.id.single_playlist_title);
		tv.setText(playlists.get(position));
	}

	public void updatePlaylist() {
		// TODO Auto-generated method stub
		playlists = PlaylistController.getInstance().getCategoryName();
		this.notifyDataSetChanged();
	}
	@Override
	public String getItem(int position) {
		return playlists.get(position);
	}

	@Override
	public int getCount() {
		return playlists.size();
	}
}