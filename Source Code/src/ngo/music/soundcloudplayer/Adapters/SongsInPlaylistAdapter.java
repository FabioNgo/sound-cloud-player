package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Song;
import android.content.Context;

public class SongsInPlaylistAdapter extends SongsInCateAdapter {

	public SongsInPlaylistAdapter(Context context, int resource,
			ArrayList<Song> objects, String cate) {
		super(context, resource, objects, cate);
		// TODO Auto-generated constructor stub
		adapterType = PLAYLIST;
	}

	

}
