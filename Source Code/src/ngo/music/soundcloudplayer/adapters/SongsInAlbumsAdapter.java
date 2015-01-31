package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.entity.Song;
import android.content.Context;

public class SongsInAlbumsAdapter extends SongsInCateAdapter {
	static SongsInAlbumsAdapter instance;
	public SongsInAlbumsAdapter(Context context, int resource,
			String cate) {
		
		super(context, resource, cate);
		// TODO Auto-generated constructor stub
		type = ALBUM;
		instance = this;
	}
	@Override
	public ArrayList<Song> getSongsFromCat(String cat) {
		// TODO Auto-generated method stub
		return CompositionListAdapter.getInstance(ALBUM).getItemsFromCat(cat);
	}


	

}
