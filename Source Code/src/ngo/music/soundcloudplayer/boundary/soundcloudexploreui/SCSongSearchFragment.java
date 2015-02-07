package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.SCSearchSongAdapter;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

public class SCSongSearchFragment extends SoundCloudExploreFragment {

	private String query;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		current_page = 0;
		query = MusicPlayerMainActivity.query;
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}

	protected class loadMoreBackground extends
			AsyncTask<Integer, String, String> {

		SongController songController = SongController.getInstance();
		private int mCategory;

		@Override
		protected String doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			songController = SongController.getInstance();

			mCategory = params[0];
			songController.searchSongSC(query, current_page);
			current_page++;
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			MusicPlayerMainActivity.getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					int currentPosition = songsList.getFirstVisiblePosition();
					
					// Appending new data to menuItems ArrayList
					
					adapter.update(mCategory);

					// Setting new scroll position
					songsList.setSelectionFromTop(currentPosition + 1, 0);
					MusicPlayerService.getInstance().updateQueue(mCategory);
					loadingMore = false;
				}
			});
			
		}
	}

	@Override
	protected int setCategory() {
		// TODO Auto-generated method stub
		return SEARCH;
	}

	@Override
	protected int setCurrentPage() {
		// TODO Auto-generated method stub
		return 0;
	}

}
