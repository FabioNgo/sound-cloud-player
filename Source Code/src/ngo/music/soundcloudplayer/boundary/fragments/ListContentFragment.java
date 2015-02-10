package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.adapters.CompositionListAdapter;
import ngo.music.soundcloudplayer.adapters.ListSongAdapter;
import ngo.music.soundcloudplayer.adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.adapters.PlaylistAdapter;
import ngo.music.soundcloudplayer.asynctask.UpdateNewSongBackgroundTask;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.CategoryController;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 
 * @author Fabio Ngo Every fragments having list view to list songs. This is
 *         used to update list when necessary
 *
 */
public abstract class ListContentFragment extends Fragment implements
		Constants.MusicService,Constants.Categories, Constants.Appplication, OnItemClickListener {

	protected SwipeRefreshLayout mSwipeRefreshLayout;
	protected int swipeRefreshLayoutId = -1;
	protected View rootView;
	protected ArrayAdapter<?> adapter;
	protected ListView listView = new ListView(MusicPlayerMainActivity.getActivity());

	/**
	 * Load fragment activity, often list view fragment
	 * 
	 * @param firstTime
	 *            : is the first time loading or not
	 */
	public void load() {
		adapter.notifyDataSetChanged();
		UIController.getInstance().addAdapter(adapter);
		listView.setAdapter((ListAdapter) adapter);
		
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position,
			long id) {
		
		
		
		Adapter adapter = parent.getAdapter();
		
		if (adapter instanceof OfflineSongAdapter) {
			ArrayList<Song> songs = ((OfflineSongAdapter) adapter).getSongs();
			MusicPlayerService.getInstance().playNewSong(position, songs);
			return;
		}
		if (adapter instanceof ListSongAdapter) {
			// ((ArrayAdapter<OnlineSong>) adapter).notifyDataSetChanged();
			ArrayList<Song> songs = ((ListSongAdapter) adapter).getSongs();
			MusicPlayerService.getInstance().playNewSong(position, songs);
			return;
		}
		if (adapter instanceof CompositionListAdapter) {
			System.out.println ("IS COMPOSITIONLISTADAPTER");
			new getSongFromCategoryBackground(position).execute();
			
		}

	}
	
	private class getSongFromCategoryBackground extends AsyncTask<String, String, ArrayList<Song>>{

		int type;
		int position;
		String category;
		
		public getSongFromCategoryBackground(int position) {
			// TODO Auto-generated constructor stub
			this.position = position;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			type = ((CompositionListAdapter)adapter).getAdapterType();
			category = CompositionListAdapter.getInstance(type).getItem(position);
		}
		@Override
		protected ArrayList<Song> doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			ArrayList<Song> songs;
			try {
				
				songs = CategoryController.getInstance(type).getSongFromCategory(category);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				return new ArrayList<Song>();
			}
			
			return songs;
		}
		
		@Override
		protected void onPostExecute(ArrayList<Song> result) {
			// TODO Auto-generated method stub
			if (!result.isEmpty() && result != null) {
				
				MusicPlayerService.getInstance().playNewSong(0, result);
			}else{
				BasicFunctions.makeToastTake("No song to play", MusicPlayerMainActivity.getActivity());
			}
		}
		
	}
	

}
