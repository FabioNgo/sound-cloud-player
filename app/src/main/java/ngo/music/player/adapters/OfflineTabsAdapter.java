package ngo.music.player.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ngo.music.player.boundary.fragment.abstracts.CategoryListContentFragment;
import ngo.music.player.boundary.fragment.real.OfflineSongsFragment;
import ngo.music.player.helper.Constants;

public class OfflineTabsAdapter extends FragmentPagerAdapter implements
		Constants.Models {

	private final String[] TITLES = { "Songs", "Playlists", "Albums",
			"Artists", "About Us" };

	public OfflineTabsAdapter(FragmentManager fm) {
		super(fm);

	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLES[position];
	}

	@Override
	public int getCount() {
		return TITLES.length;
	}

	@Override
	public Fragment getItem(int position) {

		switch (position) {
		case 0:
			return new OfflineSongsFragment();
		case 1:

			return CategoryListContentFragment.createInstance(PLAYLIST);
		case 2:
			return CategoryListContentFragment.createInstance(ALBUM);
		case 3:
			return CategoryListContentFragment.createInstance(ARTIST);
		default:
			return new Fragment();
		}

	}

}