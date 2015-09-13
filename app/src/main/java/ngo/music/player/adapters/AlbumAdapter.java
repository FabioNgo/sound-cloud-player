package ngo.music.player.adapters;

import android.content.Context;

public class AlbumAdapter extends CategoryListAdapter {

	static AlbumAdapter instance = null;

	AlbumAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		instance = this;
		
	}

	

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return ALBUM;
	}

	@Override
	protected boolean setCanDelete() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	protected boolean setCanEdit() {
		// TODO Auto-generated method stub
		return false;
	}

}
