package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Song;
import android.content.Context;

public class SongsInPlaylistAdapter extends SongsInCateAdapter {
	static SongsInPlaylistAdapter instance;
	public SongsInPlaylistAdapter(Context context, int resource,
			ArrayList<Song> objects, String cate) {
		
		super(context, resource, objects, cate);
		// TODO Auto-generated constructor stub
		type = PLAYLIST;
		instance = this;
	}
	@Override
	public ArrayList<Song> getSongsFromCat(String cat) {
		// TODO Auto-generated method stub
		return CompositionListAdapter.getInstance(PLAYLIST).getItemsFromCat(cat);
	}


	

}
