package ngo.music.player.View.fragment.abstracts;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;

import ngo.music.player.ViewHolder.CompositionViewHolder;
import ngo.music.player.adapters.CategoryListAdapter;
import ngo.music.player.View.fragment.real.AlbumsFragment;
import ngo.music.player.View.fragment.real.ArtistsFragment;
import ngo.music.player.View.fragment.real.PlaylistFragment;
import ngo.music.player.helper.Constants;

/**
 * 
 * @author Fabio Ngo Every fragments having list view to list songs. This is
 *         used to update list when necessary
 *
 */
public abstract class CategoryListContentFragment extends
		ListContentFragment implements Constants.Models {

	int type = -1;

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


			default:
				break;
		}
		return null;
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
	protected void updateToolbar(Toolbar toolbar) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @return type of category in Constants.Models
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

	@Override
	protected ArrayAdapter<?> getAdapter() {
		type = getCategory();
		return CategoryListAdapter.getInstance(type);
		
	}
}
