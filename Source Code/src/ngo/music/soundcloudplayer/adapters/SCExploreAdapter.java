package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.entity.SCSong;
import ngo.music.soundcloudplayer.entity.Song;
import android.content.Context;

public class SCExploreAdapter extends ListSongAdapter {

	public static SCExploreAdapter instance = null;
	
	
	public SCExploreAdapter(Context context, int resource, ArrayList<Song> onlineSongs, ApiWrapper wrapper) {
		super(context, resource, onlineSongs, wrapper);
		
		
		// TODO Auto-generated constructor stub
	}

}
