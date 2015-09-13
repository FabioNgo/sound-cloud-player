package ngo.music.player.adapters;

import android.content.Context;

public class SCPlaylistSearchAdapter extends CategoryListAdapter {

	static SCPlaylistSearchAdapter instance = null;

	SCPlaylistSearchAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		instance = this;
		//type = SC_SEARCH_PLAYLIST;
	}

//	@Override
//	protected ArrayList<String> getCategories() {
//		// TODO Auto-generated method stub
//		return SCPlaylistSearchController.getInstance().getCategoryString();
//
//	}



	@Override
	protected boolean setCanDelete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return SC_SEARCH_PLAYLIST;
	}


	@Override
	protected boolean setCanEdit() {
		// TODO Auto-generated method stub
		return false;
	}

}
