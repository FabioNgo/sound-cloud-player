package ngo.music.soundcloudplayer.Adapters;


import ngo.music.soundcloudplayer.boundary.AlbumsFragment;
import ngo.music.soundcloudplayer.boundary.ArtistsFragment;
import ngo.music.soundcloudplayer.boundary.FullPlayerUI;
import ngo.music.soundcloudplayer.boundary.MyFavoriteSoundCloudFragment;
import ngo.music.soundcloudplayer.boundary.MySoundCloudStreamFragment;
import ngo.music.soundcloudplayer.boundary.OfflineSongsFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.SoundCloudExploreFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsAdapter extends FragmentPagerAdapter {


	private final String[] TITLES = { "My Streams", "My Favorites", "Offline Songs",
			"Playlists", "Genres", "Sub Genres", "Users", "About Us" };

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
		default: return new FullPlayerUI();
		}
		

	}

}