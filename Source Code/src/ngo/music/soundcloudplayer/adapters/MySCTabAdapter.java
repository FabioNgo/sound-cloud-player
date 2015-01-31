package ngo.music.soundcloudplayer.adapters;


import ngo.music.soundcloudplayer.boundary.FullPlayerUI;
import ngo.music.soundcloudplayer.boundary.fragments.MySCFavoriteFragment;
import ngo.music.soundcloudplayer.boundary.fragments.MySCStreamFragment;
import ngo.music.soundcloudplayer.boundary.fragments.OfflineSongsFragment;
import ngo.music.soundcloudplayer.boundary.fragments.PlaylistFragment;
import ngo.music.soundcloudplayer.boundary.fragments.MySCFollowerFragment;
import ngo.music.soundcloudplayer.boundary.fragments.MySCFollowingFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
			return new MySCPlaylistFragment();
		case 3:
			return new MySCFollowingFragment();
		
		case 4:
			return new MySCFollowerFragment();
		default: return new Fragment();
		}
		

	}

}