package ngo.music.player.adapters;

import android.content.Context;

public class SongsInSCSearchPlaylistAdapter extends SongsInCateAdapter {
	static SongsInSCSearchPlaylistAdapter instance;
	public SongsInSCSearchPlaylistAdapter(Context context, int resource,
			String cate) {
		
		super(context, resource, cate);
		// TODO Auto-generated constructor stub
		
		instance = this;
	}
	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return SC_SEARCH_PLAYLIST;
	}
	@Override
	protected boolean setCanRemoveItem() {
		// TODO Auto-generated method stub
		return false;
	}


	

}
