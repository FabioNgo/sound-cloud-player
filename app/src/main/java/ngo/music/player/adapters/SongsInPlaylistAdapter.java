package ngo.music.player.adapters;

import android.content.Context;

import ngo.music.player.Model.Category;

public class SongsInPlaylistAdapter extends SongsInCateAdapter {
	static SongsInPlaylistAdapter instance;
	public SongsInPlaylistAdapter(Context context, int resource,
			Category cate) {
		
		super(context, resource, cate);
		// TODO Auto-generated constructor stub
		
		instance = this;
	}
	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return PLAYLIST;
	}
	@Override
	protected boolean setCanRemoveItem() {
		// TODO Auto-generated method stub
		return true;
	}


	

}
