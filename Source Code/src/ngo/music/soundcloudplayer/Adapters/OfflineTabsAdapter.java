package ngo.music.soundcloudplayer.Adapters;


import ngo.music.soundcloudplayer.boundary.FullPlayerUI;
import ngo.music.soundcloudplayer.boundary.fragments.MySoundCloudFavoriteFragment;
import ngo.music.soundcloudplayer.boundary.fragments.MySoundCloudStreamFragment;
import ngo.music.soundcloudplayer.boundary.fragments.OfflineSongsFragment;
import ngo.music.soundcloudplayer.boundary.fragments.PlaylistFragment;
import ngo.music.soundcloudplayer.boundary.fragments.SoundCloudFollowerFragment;
import ngo.music.soundcloudplayer.boundary.fragments.SoundCloudFollowingFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OfflineTabsAdapter extends FragmentPagerAdapter {


	private final String[] TITLES = { "Songs","Playlists", "Album", "Artist", "About Us" };

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
			return new PlaylistFragment();
		
		default: return new Fragment();
		}
		

	}

}