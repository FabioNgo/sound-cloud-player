package ngo.music.player.adapters;

import android.content.Context;

public class SongsInArtistsAdapter extends SongsInCateAdapter {
	static SongsInArtistsAdapter instance;
	public SongsInArtistsAdapter(Context context, int resource,
			String cate) {
		
		super(context, resource, cate);
		// TODO Auto-generated constructor stub
		instance = this;
	}
	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return ARTIST;
	}
	@Override
	protected boolean setCanRemoveItem() {
		// TODO Auto-generated method stub
		return false;
	}


	

}