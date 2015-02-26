package ngo.music.soundcloudplayer.boundary.fragment.abstracts;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.CategoryListAdapter;
import ngo.music.soundcloudplayer.adapters.SCSongAdapter;
import ngo.music.soundcloudplayer.adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.adapters.PlaylistAdapter;
import ngo.music.soundcloudplayer.adapters.QueueSongAdapter;
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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
/**
 * 
 * @author Fabio Ngo Every fragments having list view to list songs. This is
 *         used to update list when necessary
 *
 */
public abstract class ListContentFragment extends Fragment implements
		Constants.MusicService, Constants.Categories, Constants.Appplication,
		OnItemClickListener {
	public static int numFragmentsLoading = 0;
	protected View rootView;
	protected ArrayAdapter<?> adapter;
	protected int category;
	protected Toolbar toolbar;
	protected ListView listView;

	public ListContentFragment() {
		// TODO Auto-generated constructor stub
		
		category = getCategory();
	}

	protected abstract boolean hasLoadMore();
	
	protected abstract boolean hasToolbar();
	/**
	 * if no toolbar, just return
	 */
	protected abstract void setUpToolBar(Toolbar toolbar);
	protected abstract void updateToolbar(Toolbar toolbar);


	protected abstract int getCategory();

	protected abstract ArrayAdapter<?> getAdapter();

	/**
	 * Load fragment activity, often list view fragment
	 * 
	 * @param firstTime
	 *            : is the first time loading or not
	 */
	public void load() {
		adapter.notifyDataSetChanged();
		listView = (ListView) rootView.findViewById(R.id.items_list);

		listView.setAdapter((ListAdapter) adapter);
		listView.setOnItemClickListener(this);
		UIController.getInstance().addAdapter(adapter);
		updateToolbar(toolbar);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position,
			long id) {

		Adapter adapter = parent.getAdapter();

		if (adapter instanceof SCSongAdapter) {
			// ((ArrayAdapter<OnlineSong>) adapter).notifyDataSetChanged();
			ArrayList<Song> songs = ((SCSongAdapter) adapter).getSongs();
			MusicPlayerService.getInstance().playNewSong(position, category,
					songs);
			return;
		}
		if (adapter instanceof CategoryListAdapter) {
			System.out.println("IS COMPOSITIONLISTADAPTER");
			new getSongFromCategoryBackground(position).execute();

		}

	}
	public void update() {
		 adapter =(ArrayAdapter<?>) listView.getAdapter();

		adapter.notifyDataSetChanged();
		updateToolbar(toolbar);
		// for (int i = 0; i <= listView.getLastVisiblePosition()
		// - listView.getFirstVisiblePosition(); i++) {
		// View v = listView.getChildAt(i);
		// if (v != null) {
		//
		// SongInListViewHolder holder = (SongInListViewHolder) v.getTag();
		// try {
		//
		// adapter.setLayoutInformation(
		// i + listView.getFirstVisiblePosition(), holder, v);
		// } catch (IndexOutOfBoundsException e) {
		// /**
		// * When this exception occur, some item has been deleted in
		// * list.
		// */
		// break;
		//
		// }
		// }
		// }

	}
	private class getSongFromCategoryBackground extends
			AsyncTask<String, String, ArrayList<Song>> {

		int type;
		int position;
		String categoryTitle;

		public getSongFromCategoryBackground(int position) {
			// TODO Auto-generated constructor stub
			this.position = position;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			type = ((CategoryListAdapter) adapter).getAdapterType();
			categoryTitle = CategoryListAdapter.getInstance(type).getItem(
					position);
		}

		@Override
		protected ArrayList<Song> doInBackground(String... params) {
			// TODO Auto-generated method stub

			ArrayList<Song> songs;
			try {

				songs = CategoryController.getInstance(type)
						.getSongFromCategory(categoryTitle);
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

				MusicPlayerService.getInstance().playNewSong(0, category,
						result);
			} else {
				BasicFunctions.makeToastTake("No song to play",
						MusicPlayerMainActivity.getActivity());
			}
		}

	}
	

}
