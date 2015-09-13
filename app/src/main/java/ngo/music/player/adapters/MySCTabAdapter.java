package ngo.music.player.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ngo.music.player.boundary.fragment.real.MySCFavoriteFragment;
import ngo.music.player.boundary.fragment.real.MySCFollowerFragment;
import ngo.music.player.boundary.fragment.real.MySCFollowingFragment;
import ngo.music.player.boundary.fragment.real.MySCStreamFragment;
import ngo.music.player.boundary.fragment.real.SCPlaylistFragment;

public class MySCTabAdapter extends FragmentPagerAdapter {


	private final String[] TITLES = { "Streams", "Favorites", "Playlist", "Followings", "Followers"};

	public MySCTabAdapter(FragmentManager fm) {
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
			return new MySCStreamFragment();
		
		case 1:
			return new MySCFavoriteFragment();
		
		case 2:
			return new SCPlaylistFragment();
		case 3:
			return new MySCFollowingFragment();
		
		case 4:
			return new MySCFollowerFragment();
		default: return new Fragment();
		}
		

	}

}