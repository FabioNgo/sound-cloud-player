package ngo.music.player.boundary.fragment.real;

import android.widget.ArrayAdapter;

import ngo.music.player.adapters.OfflineSongAdapter;
import ngo.music.player.boundary.fragment.abstracts.RefreshListContentFragment;

public class OfflineSongsFragment extends RefreshListContentFragment {

	public ArrayAdapter<?> getAdapter() {
		// TODO Auto-generated method stub
		return OfflineSongAdapter.getInstance();
	}

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return OFFLINE;
	}

	

}
