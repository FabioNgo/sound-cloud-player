package ngo.music.player.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ngo.music.player.boundary.fragment.real.SCPlaylistSearchFragment;
import ngo.music.player.boundary.fragment.real.SCSongSearchFragment;
import ngo.music.player.boundary.fragment.real.SCUserSearchFragment;

public class SCSearchTabAdater extends FragmentPagerAdapter {
	private final String[] TITLES = {"SONG" , "USER" , "PLAY LIST"};

	public SCSearchTabAdater(FragmentManager fm) {
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
			return new SCSongSearchFragment();
		case 1:
			return new SCUserSearchFragment();
		case 2:
			return new SCPlaylistSearchFragment();
		default: 
			return new SCSongSearchFragment();
		}
		

	}

}
