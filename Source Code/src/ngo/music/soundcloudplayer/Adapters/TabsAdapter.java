package ngo.music.soundcloudplayer.Adapters;


import ngo.music.soundcloudplayer.boundary.FullPlayerUI;
import ngo.music.soundcloudplayer.boundary.fragments.MyFavoriteSoundCloudFragment;
import ngo.music.soundcloudplayer.boundary.fragments.MySoundCloudStreamFragment;
import ngo.music.soundcloudplayer.boundary.fragments.OfflineSongsFragment;
import ngo.music.soundcloudplayer.boundary.fragments.PlaylistFragment;
import ngo.music.soundcloudplayer.boundary.fragments.SoundCloudFollowerFragment;
import ngo.music.soundcloudplayer.boundary.fragments.SoundCloudFollowingFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsAdapter extends FragmentPagerAdapter {


	private final String[] TITLES = { "My Streams", "My Favorites", "Offline Songs",
			"Playlists", "Album", "Artist", "My Followings", "My Followers", "About Us" };

	public TabsAdapter(FragmentManager fm) {
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
			return new MySoundCloudStreamFragment();
		
		case 1:
			return new MyFavoriteSoundCloudFragment();
		
		case 2:
			return new OfflineSongsFragment();
		case 3:
			return new PlaylistFragment();
		case 6:
			return new SoundCloudFollowingFragment();
		
		case 7:
			return new SoundCloudFollowerFragment();
		default: return new Fragment();
		}
		

	}

}