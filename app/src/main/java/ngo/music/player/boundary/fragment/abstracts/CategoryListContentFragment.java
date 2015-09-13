package ngo.music.player.boundary.fragment.abstracts;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;

import ngo.music.player.ViewHolder.CompositionViewHolder;
import ngo.music.player.adapters.CategoryListAdapter;
import ngo.music.player.boundary.fragment.real.AlbumsFragment;
import ngo.music.player.boundary.fragment.real.ArtistsFragment;
import ngo.music.player.boundary.fragment.real.PlaylistFragment;
import ngo.music.player.boundary.fragment.real.SCPlaylistFragment;
import ngo.music.player.boundary.fragment.real.SCPlaylistSearchFragment;
import ngo.music.player.helper.Constants;

/**
 * 
 * @author Fabio Ngo Every fragments having list view to list songs. This is
 *         used to update list when necessary
 *
 */
public abstract class CategoryListContentFragment extends
		NoRefreshListContentFragment implements Constants.Categories {

	int type = -1;

	@Override
	protected boolean hasLoadMore() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean hasToolbar() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void setUpToolBar(Toolbar toolbar) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setUpLoadMore() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateToolbar(Toolbar toolbar) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @return type of category in Constants.Categories
	 */
	protected abstract int getCategory();

	/**
	 * Update UI when playlist change (Update Single exsited view in list View)
	 * 
	 */
	public void update() {

		CategoryListAdapter adapter = (CategoryListAdapter) listView
				.getAdapter();
		// System.out.println (listView.get);
		/**
		 * ADAPTER BI TRA VE NULL
		 */
		if (adapter == null)
			return;
		adapter.update();

		for (int i = 0; i <= listView.getLastVisiblePosition()
				- listView.getFirstVisiblePosition(); i++) {
			View v = listView.getChildAt(i);
			if (v != null) {

				CompositionViewHolder holder = (CompositionViewHolder) v
						.getTag();
				try {
					adapter.setLayoutInformation(
							holder,
							adapter.getWholeItem(i
									+ listView.getFirstVisiblePosition()), v);
				} catch (IndexOutOfBoundsException e) {
					/**
					 * When this exception occur, some item has been deleted in
					 * list.
					 */
					break;

				}
			}
		}
		adapter.update();
	}

	public static CategoryListContentFragment createInstance(int type) {
		switch (type) {
		case PLAYLIST:
			PlaylistFragment.instance = new PlaylistFragment();
			return PlaylistFragment.instance;
		case ALBUM:
			AlbumsFragment.instance = new AlbumsFragment();
			return AlbumsFragment.instance;
		case ARTIST:
			ArtistsFragment.instance = new ArtistsFragment();
			return ArtistsFragment.instance;
		case SC_PLAYLIST:
			SCPlaylistFragment.instance = new SCPlaylistFragment();
			return SCPlaylistFragment.instance;
		case SC_SEARCH_PLAYLIST:
			SCPlaylistSearchFragment.instance = new SCPlaylistSearchFragment();
			return SCPlaylistSearchFragment.instance;
		default:
			break;
		}
		return null;
	}

	public static CategoryListContentFragment getInstance(int type) {
		switch (type) {
		case PLAYLIST:
			if (PlaylistFragment.instance == null) {
				createInstance(type);
			}
			return PlaylistFragment.instance;
		case ALBUM:
			if (AlbumsFragment.instance == null) {
				createInstance(type);
			}
			return AlbumsFragment.instance;
		case ARTIST:
			if (ArtistsFragment.instance == null) {
				createInstance(type);
			}
			return ArtistsFragment.instance;

		case SC_PLAYLIST:
			if (SCPlaylistFragment.instance == null) {
				createInstance(type);
			}
			return SCPlaylistFragment.instance;
		case SC_SEARCH_PLAYLIST:
			if (SCPlaylistSearchFragment.instance == null) {
				createInstance(type);
			}
			return SCPlaylistSearchFragment.instance;
		default:
			break;
		}
		return null;
	}

	@Override
	protected ArrayAdapter<?> getAdapter() {
		type = getCategory();
		return CategoryListAdapter.getInstance(type);
		
	}
}
