package ngo.music.player.View.fragment.real;

import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;

import java.util.Observable;

import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.View.fragment.abstracts.ListContentFragment;
import ngo.music.player.adapters.OfflineSongAdapter;

public class OfflineSongsFragment extends ListContentFragment {

	public ArrayAdapter<?> getAdapter() {
		// TODO Auto-generated method stub
		return OfflineSongAdapter.getInstance();
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
	public void update(Observable observable, Object data) {

	}
}
