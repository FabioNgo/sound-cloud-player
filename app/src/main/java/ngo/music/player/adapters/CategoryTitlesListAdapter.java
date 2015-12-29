package ngo.music.player.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ngo.music.player.Model.Category;
import ngo.music.player.Model.Model;
import ngo.music.player.ModelManager.CategoryManager;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.R;
import ngo.music.player.View.MusicPlayerMainActivity;
import ngo.music.player.helper.Constants;

public abstract class CategoryTitlesListAdapter extends ArrayAdapter<Category> implements Constants.Models,Observer {
	
	private int type;
	private Category[] categories;

	protected CategoryTitlesListAdapter() {
		super(MusicPlayerMainActivity.getActivity(), R.layout.single_playlist_list_adding);
		// TODO Auto-generated constructor stub
		type = setType();
		categories = (Category[])  ModelManager.getInstance(type).getAll();
		ModelManager.getInstance(type).addObserver(this);
	}

	public static CategoryTitlesListAdapter getInstance(int type){
		switch (type) {
			case PLAYLIST:
				if(PlaylistTitlesListAdapter.instance == null){
					createInstance(type);
				}
				return PlaylistTitlesListAdapter.instance;

				default:
				return null;
		}
	}

	public static void createInstance(int type){
		switch (type) {
		case PLAYLIST:
			PlaylistTitlesListAdapter.instance = new PlaylistTitlesListAdapter();
			break;

		default:
			break;
		}
	}

	protected abstract int setType();

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.single_playlist_list_adding, null);

		}

		setLayoutInformation(position, v);
		return v;
	}

	private void setLayoutInformation(int position, View v) {
		// TODO Auto-generated method stub
		TextView tv = (TextView) v.findViewById(R.id.single_playlist_title);
		tv.setText(categories[position].getAttribute("title"));
	}

	@Override
	public void update(Observable observable, Object data) {
		if(observable instanceof ModelManager) {
			ArrayList<Model> temp = (ArrayList<Model>) data;
			categories = temp.toArray(categories);
//			categories = (Category[]) data;
			notifyDataSetChanged();
		}

	}

	@Override
	public Category getItem(int position) {
		return categories[position];
	}

	@Override
	public int getCount() {
		return categories.length;
	}
}