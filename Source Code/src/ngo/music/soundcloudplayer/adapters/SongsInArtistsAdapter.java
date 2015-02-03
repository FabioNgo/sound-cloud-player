package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Song;
import android.content.Context;

public class SongsInArtistsAdapter extends SongsInCateAdapter {
	static SongsInArtistsAdapter instance;
	public SongsInArtistsAdapter(Context context, int resource,
			String cate) {
		
		super(context, resource, cate);
		// TODO Auto-generated constructor stub
		type = ARTIST;
		instance = this;
	}
	@Override
	public ArrayList<Song> getSongsFromCat(String cat) {
		// TODO Auto-generated method stub
		return CompositionListAdapter.getInstance(ARTIST).getItemsFromCat(cat);
	}


	

}
