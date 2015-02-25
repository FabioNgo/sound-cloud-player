package ngo.music.soundcloudplayer.adapters;

import ngo.music.soundcloudplayer.boundary.FullPlayerUI;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.SoundCloudExploreFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.AlternativeRockFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.AmbientFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.ClassicalFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.CountryFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.DanceFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.DeepHouseFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.DiscoFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.DrumBassFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.DubstepFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.ElectroFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.ElectronicFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.FolkFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.OfflineSongsFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.SCPlaylistFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.SCPlaylistSearchFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.SCSongSearchFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.SCUserSearchFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.TrendingAudioFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.TrendingMusicFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
