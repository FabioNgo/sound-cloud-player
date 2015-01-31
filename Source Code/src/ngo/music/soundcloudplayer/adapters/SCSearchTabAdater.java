package ngo.music.soundcloudplayer.adapters;

import ngo.music.soundcloudplayer.boundary.FullPlayerUI;
import ngo.music.soundcloudplayer.boundary.fragments.OfflineSongsFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.AlternativeRockFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.AmbientFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.ClassicalFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.CountryFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.DanceFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.DeepHouseFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.DiscoFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.DrumBassFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.DubstepFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.ElectroFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.ElectronicFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.FolkFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.SCSongSearchFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.SCUserSearchFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.SoundCloudExploreFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.TrendingAudioFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.TrendingMusicFragment;
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
			return new SCPlaylistFragment();
		default: 
			return new SCSongSearchFragment();
		}
		

	}

}
