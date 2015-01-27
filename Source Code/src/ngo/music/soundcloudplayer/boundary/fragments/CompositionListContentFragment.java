package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.Adapters.CompositionListAdapter;
import ngo.music.soundcloudplayer.Adapters.CompositionViewHolder;
import ngo.music.soundcloudplayer.Adapters.ListSongAdapter;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.Adapters.PlaylistAdapter;
import ngo.music.soundcloudplayer.AsyncTask.UpdateNewSongBackgroundTask;
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
public abstract class CompositionListContentFragment extends
		ListContentFragment implements Constants.Categories {
	int type = -1;
	/**
	 * Update UI when playlist change (Update Single exsited view in list View)
	 * 
	 */
	public void update() {
		CompositionListAdapter adapter = (CompositionListAdapter) listView
				.getAdapter();
		adapter.update();
		for (int i = 0; i <= listView.getLastVisiblePosition()
				- listView.getFirstVisiblePosition(); i++) {
			View v = listView.getChildAt(i);
			if (v != null) {

				CompositionViewHolder holder = new CompositionViewHolder(
						NUM_ITEM_IN_ONE_CATEGORY, v);
				try {
					adapter.setLayoutInformation(
							holder,
							adapter.getWholeItem(i
									+ listView.getFirstVisiblePosition()), v);
				} catch (IndexOutOfBoundsException e) {
					/**
					 * When this exception occur, some item has been deleted in list.
					 */
					break;
					
				}
			}
		}
		adapter.update();
	}
	public static CompositionListContentFragment createInstance(int type){
		switch (type) {
		case PLAYLIST:
			return new PlaylistFragment();
			

		default:
			break;
		}
		return null;
	}
	public static CompositionListContentFragment getInstance(int type){
		switch (type) {
		case PLAYLIST:
			if(PlaylistFragment.instance == null){
				createInstance(type);
			}
			return PlaylistFragment.instance;
			

		default:
			break;
		}
		return null;
	}
	
}
