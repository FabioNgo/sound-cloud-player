package ngo.music.player.adapters;

import android.content.Context;

public class SCPlaylistAdapter extends CategoryListAdapter {

	static SCPlaylistAdapter instance = null;

	SCPlaylistAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		instance = this;
		
	}


	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return SC_PLAYLIST;
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
