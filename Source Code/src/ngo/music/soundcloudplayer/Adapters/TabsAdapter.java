package ngo.music.soundcloudplayer.Adapters;

import ngo.music.soundcloudplayer.Adapters.Fragments.SingleSwipePageFragment;
import ngo.music.soundcloudplayer.boundary.AlbumsFragment;
import ngo.music.soundcloudplayer.boundary.ArtistsFragment;
import ngo.music.soundcloudplayer.boundary.FullPlayerUI;
import ngo.music.soundcloudplayer.boundary.OnlineSongsFragment;
import ngo.music.soundcloudplayer.boundary.OfflineSongsFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsAdapter extends FragmentPagerAdapter {


	private final String[] TITLES = { "Online songs", "Albums", "My songs",
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
		System.out.println ("GET ITEM " + position); 
		switch (position) {
		case 0:
			return OnlineSongsFragment.getInstance();
		
		case 2:
			return OfflineSongsFragment.getInstance();
		default: return new FullPlayerUI();
		}
		

	}

}