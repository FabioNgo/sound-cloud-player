package ngo.music.soundcloudplayer.Adapters;

import ngo.music.soundcloudplayer.Adapters.Fragments.SuperAwesomeCardFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {

	private final String[] TITLES = { "Categories", "Home", "Top Paid", "Top Free", "Top Grossing", "Top New Paid",
			"Top New Free", "Trending" };

	public MyPagerAdapter(FragmentManager fm) {
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
		return SuperAwesomeCardFragment.newInstance(position);
	}

}