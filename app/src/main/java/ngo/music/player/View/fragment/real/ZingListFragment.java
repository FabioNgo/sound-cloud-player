package ngo.music.player.View.fragment.real;

import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;

import java.util.Observable;

import ngo.music.player.View.fragment.abstracts.CategoryListContentFragment;
import ngo.music.player.View.fragment.abstracts.ListContentFragment;
import ngo.music.player.adapters.ZingSongAdapter;

public class ZingListFragment extends ListContentFragment {
	public static ZingListFragment instance;



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
		return ZING;
	}



	@Override
	public void update(Observable observable, Object data) {

	}
}
