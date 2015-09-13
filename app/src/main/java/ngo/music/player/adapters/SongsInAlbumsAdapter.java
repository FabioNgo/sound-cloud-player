package ngo.music.player.adapters;

import android.content.Context;

public class SongsInAlbumsAdapter extends SongsInCateAdapter {
	static SongsInAlbumsAdapter instance;
	public SongsInAlbumsAdapter(Context context, int resource,
			String cate) {
		
		super(context, resource, cate);
		// TODO Auto-generated constructor stub
		instance = this;
	}
	
	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return ALBUM;
	}

	@Override
	protected boolean setCanRemoveItem() {
		// TODO Auto-generated method stub
		return false;
	}


	

}
