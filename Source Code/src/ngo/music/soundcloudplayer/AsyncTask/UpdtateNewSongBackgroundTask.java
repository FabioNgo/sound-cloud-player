package ngo.music.soundcloudplayer.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import ngo.music.soundcloudplayer.Adapters.MyStreamAdapter;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.boundary.fragments.ListContentFragment;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.OnlineSong;
import ngo.music.soundcloudplayer.entity.Song;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class UpdtateNewSongBackgroundTask extends
		AsyncTask<ListView, Void, ArrayList<Song>> {

	static final int TASK_DURATION = 3 * 1000; // 3 seconds
	private Adapter adapter;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	public UpdtateNewSongBackgroundTask(SwipeRefreshLayout mSwipeRefreshLayout) {
		// TODO Auto-generated constructor stub
		this.mSwipeRefreshLayout = mSwipeRefreshLayout;
	}
	@Override
	protected ArrayList<Song> doInBackground(ListView... params) {
		// Sleep for a small amount of time to simulate a background-task
		try {
			Thread.sleep(TASK_DURATION);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ArrayList<Song> result = null;
		// Return a new song list
		adapter = params[0].getAdapter();
		if (adapter instanceof OfflineSongAdapter) {
			result = SongController.getInstance()
					.getOfflineSongs(true);

			
		}
//		if (adapter instanceof MyStreamAdapter) {
//			result = SongController.getInstance()
//					.getMyStream();
//
//			
//		}
		return result;
	}

	@Override
	protected void onPostExecute(ArrayList<Song> result) {
		super.onPostExecute(result);

		// Tell the Fragment that the refresh has completed
		onRefreshComplete(result);
	}

	
	private void onRefreshComplete(ArrayList<Song> result) {
		// TODO Auto-generated method stub
		if (adapter instanceof OfflineSongAdapter) {
			ArrayList<Song> songs = (ArrayList<Song>) result;
			((OfflineSongAdapter) adapter).clear();
			for (Song song : songs) {
				((OfflineSongAdapter) adapter).add(song);
			}
			((OfflineSongAdapter) adapter).notifyDataSetChanged();
			mSwipeRefreshLayout.setRefreshing(false);
		}
	}

}