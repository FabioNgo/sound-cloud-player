package ngo.music.soundcloudplayer.boundary.fragments;

import ngo.music.soundcloudplayer.ViewHolder.CompositionViewHolder;
import ngo.music.soundcloudplayer.adapters.CompositionListAdapter;
import ngo.music.soundcloudplayer.general.Constants;
import android.view.View;

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

				CompositionViewHolder holder = (CompositionViewHolder) v.getTag();
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
			
		case ALBUM:
			return new AlbumsFragment();
		case ARTIST:
			return new ArtistsFragment();
		case SC_PLAYLIST:
			return new SCPlaylistFragment();
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
		case ALBUM:
			if(AlbumsFragment.instance == null){
				createInstance(type);
			}
			return AlbumsFragment.instance;
		case ARTIST:
			if(ArtistsFragment.instance == null){
				createInstance(type);
			}
			return ArtistsFragment.instance;

		case SC_PLAYLIST:
			if(SCPlaylistFragment.instance == null){
				createInstance(type);
			}
			return SCPlaylistFragment.instance;
		default:
			break;
		}
		return null;
	}
	
}
