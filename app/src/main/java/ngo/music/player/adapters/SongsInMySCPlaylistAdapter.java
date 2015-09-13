package ngo.music.player.adapters;

import android.content.Context;

public class SongsInMySCPlaylistAdapter extends SongsInCateAdapter {
	static SongsInMySCPlaylistAdapter instance;
	public SongsInMySCPlaylistAdapter(Context context, int resource,
			String cate) {
		
		super(context, resource, cate);
		// TODO Auto-generated constructor stub
		
		instance = this;
	}
	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return SC_PLAYLIST;
	}
	@Override
	protected boolean setCanRemoveItem() {
		// TODO Auto-generated method stub
		return true;
	}


	

}
