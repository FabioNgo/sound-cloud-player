package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.entity.Song;
import android.content.Context;

public class SoundCloudExploreAdapter extends ListSongAdapter {

	public static SoundCloudExploreAdapter instance = null;
	
	
	public SoundCloudExploreAdapter(Context context, int resource, ArrayList<Song> onlineSongs, ApiWrapper wrapper) {
		super(context, resource, onlineSongs, wrapper);
		
		
		// TODO Auto-generated constructor stub
	}

}
