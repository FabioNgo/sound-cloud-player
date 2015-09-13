package ngo.music.player.adapters;

import android.content.Context;

public class ArtistAdapter extends CategoryListAdapter {

	static ArtistAdapter instance = null;

	ArtistAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		instance = this;

	}

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return ARTIST;
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
