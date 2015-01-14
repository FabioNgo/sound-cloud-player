package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.Adapters.ListSongAdapter;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.AsyncTask.UpdtateNewSongBackgroundTask;
import ngo.music.soundcloudplayer.entity.Song;
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

/**
 * 
 * @author Fabio Ngo Every fragments having list view to list songs. This is
 *         used to update list when necessary
 *
 */
public abstract class ListContentFragment extends Fragment implements
		Constants.MusicService, Constants.Appplication, OnItemClickListener {

	protected SwipeRefreshLayout mSwipeRefreshLayout;
	protected int swipeRefreshLayoutId = -1;
	protected View rootView;
	protected ArrayAdapter<Song> adapter;

	/**
	 * Load fragment activity, often list view fragment
	 * 
	 * @param firstTime
	 *            : is the first time loading or not
	 */
	public abstract void load();

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position,
			long id) {
		Adapter adapter = parent.getAdapter();
		if (adapter instanceof OfflineSongAdapter) {
			ArrayList<Song> songs = ((OfflineSongAdapter) adapter)
					.getSongs();
			MusicPlayerService.getInstance()
					.playNewSong(position, songs);
		}
		if (adapter instanceof ListSongAdapter) {
			// ((ArrayAdapter<OnlineSong>) adapter).notifyDataSetChanged();
			ArrayList<Song> songs = ((ListSongAdapter) adapter)
					.getSongs();
			MusicPlayerService.getInstance().playNewSong(position, songs);
		}

	}

	

}
