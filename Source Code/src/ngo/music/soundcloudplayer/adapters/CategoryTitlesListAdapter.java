package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.CategoryController;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public abstract class CategoryTitlesListAdapter extends ArrayAdapter<String> implements Constants.Categories {
	
	private int type;
	public static CategoryTitlesListAdapter getInstance(int type){
		switch (type) {
		case PLAYLIST:
			if(PlaylistTitlesListAdapter.instance == null){
				createInstance(type);
			}
			return PlaylistTitlesListAdapter.instance;
		case SC_PLAYLIST:
			if(SCPlaylistTitlesListAdapter.instance == null){
				createInstance(type);
			}
			return SCPlaylistTitlesListAdapter.instance;
		default:
			return null;
		}
	}
	public static void createInstance(int type){
		switch (type) {
		case PLAYLIST:
			PlaylistTitlesListAdapter.instance = new PlaylistTitlesListAdapter();
			break;
		case SC_PLAYLIST:
			SCPlaylistTitlesListAdapter.instance = new SCPlaylistTitlesListAdapter();
			break;
		default:
			break;
		}
	}
	protected abstract int setType();
	private ArrayList<String> titles = new ArrayList<String>();

	protected CategoryTitlesListAdapter() {
		super(MusicPlayerMainActivity.getActivity(), R.layout.single_playilist_list_adding);
		// TODO Auto-generated constructor stub
		type = setType();
		titles = CategoryController.getInstance(type).getCategoryName();
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
		tv.setText(titles.get(position));
	}

	public void updateCategory() {
		// TODO Auto-generated method stub
		titles = CategoryController.getInstance(type).getCategoryName();
		this.notifyDataSetChanged();
	}
	@Override
	public String getItem(int position) {
		return titles.get(position);
	}

	@Override
	public int getCount() {
		return titles.size();
	}
}