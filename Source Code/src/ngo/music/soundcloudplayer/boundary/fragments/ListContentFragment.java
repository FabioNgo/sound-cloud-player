package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.Adapters.CompositionListAdapter;
import ngo.music.soundcloudplayer.Adapters.CompositionViewHolder;
import ngo.music.soundcloudplayer.Adapters.ListSongAdapter;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.Adapters.PlaylistAdapter;
import ngo.music.soundcloudplayer.AsyncTask.UpdtateNewSongBackgroundTask;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.graphics.Color;
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
	protected ListView listView;

	/**
	 * Load fragment activity, often list view fragment
	 * 
	 * @param firstTime
	 *            : is the first time loading or not
	 */
	public void load() {
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
		}
		if (adapter instanceof ListSongAdapter) {
			// ((ArrayAdapter<OnlineSong>) adapter).notifyDataSetChanged();
			ArrayList<Song> songs = ((ListSongAdapter) adapter).getSongs();
			MusicPlayerService.getInstance().playNewSong(position, songs);
		}
		if (adapter instanceof PlaylistAdapter) {
			String playlist = PlaylistAdapter.getInstance().getItem(position);
			ArrayList<Song> songs = PlaylistController.getInstance()
					.getSongFromPlaylist(playlist);
			if (!songs.isEmpty()) {
				MusicPlayerService.getInstance().playNewSong(0, songs);
			}else{
				BasicFunctions.makeToastTake("No song to play", MusicPlayerMainActivity.getActivity());
			}
		}

	}/**
	 * Update UI when playlist change (Update Single exsited view in list View)
	 */
	public void update() {
		CompositionListAdapter adapter = (CompositionListAdapter) listView.getAdapter();
		adapter.update();
		for (int i = 0; i <= listView.getLastVisiblePosition()
				- listView.getFirstVisiblePosition(); i++) {
			View v = listView.getChildAt(i);
			if (v != null) {

				CompositionViewHolder holder = new CompositionViewHolder(NUM_ITEM_IN_ONE_CATEGORY, v);

				adapter.setLayoutInformation(
						holder,
						adapter.getWholeItem(i
								+ listView.getFirstVisiblePosition()), v);
			}
		}
	}

}
