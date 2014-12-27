package ngo.music.soundcloudplayer.Adapters;

import ngo.music.soundcloudplayer.boundary.FullPlayerUI;
import ngo.music.soundcloudplayer.boundary.OfflineSongsFragment;
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
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.SoundCloudExploreFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.TrendingAudioFragment;
import ngo.music.soundcloudplayer.boundary.soundcloudexploreui.TrendingMusicFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SoundCloudExploreTabAdater extends FragmentPagerAdapter {
	private final String[] TITLES = {
			"Trending Music", "Trending Audio", "Alternative Rock","Ambient", "Classical", "Country", "Dance", "Deep House",
			"Disco", "Drum & Bass", "Dubstep","Electro", "Electronic", "Folk"};

	public SoundCloudExploreTabAdater(FragmentManager fm) {
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
			return TrendingMusicFragment.getInstance();
		case 1:
			return TrendingAudioFragment.getInstance();
		case 2:
			return AlternativeRockFragment.getInstance();
		case 3:
			return AmbientFragment.getInstance();
		case 4:
			return ClassicalFragment.getInstance();
		case 5:
			return CountryFragment.getInstance();
		case 6:
			return DanceFragment.getInstance();
		case 7:
			return DeepHouseFragment.getInstance();
		case 8:
			return DiscoFragment.getInstance();
		case 9:
			return DrumBassFragment.getInstance();
		case 10:
			return DubstepFragment.getInstance();
		case 11:
			return ElectroFragment.getInstance();
		case 12:
			return ElectronicFragment.getInstance();
		case 13:
			return FolkFragment.getInstance();
		default: 
			return TrendingMusicFragment.getInstance();
		}
		

	}

}
