package ngo.music.player.adapters;

import android.content.Context;

public class PlaylistAdapter extends CategoryListAdapter {

	static PlaylistAdapter instance = null;

	PlaylistAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		instance = this;
	}

	

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return PLAYLIST;
	}

	@Override
	protected boolean setCanDelete() {
		// TODO Auto-generated method stub
		return true;
	}



	@Override
	protected boolean setCanEdit() {
		// TODO Auto-generated method stub
		return true;
	}

}
