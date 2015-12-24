package ngo.music.player.boundary.fragment.real;

import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;

import ngo.music.player.adapters.OfflineSongAdapter;
import ngo.music.player.boundary.fragment.abstracts.NoRefreshListContentFragment;

public class OfflineSongsFragment extends NoRefreshListContentFragment {

	public ArrayAdapter<?> getAdapter() {
		// TODO Auto-generated method stub
		return OfflineSongAdapter.getInstance();
	}

	@Override
	protected boolean hasLoadMore() {
		return false;
	}

	@Override
	protected boolean hasToolbar() {
		return false;
	}

	@Override
	protected void setUpToolBar(Toolbar toolbar) {

	}

	@Override
	protected void updateToolbar(Toolbar toolbar) {

	}


	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return OFFLINE;
	}


    @Override
    protected void setUpLoadMore() {

    }


}
