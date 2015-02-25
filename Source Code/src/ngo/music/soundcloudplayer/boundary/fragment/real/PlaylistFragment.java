package ngo.music.soundcloudplayer.boundary.fragment.real;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.CategoryListAdapter;
import ngo.music.soundcloudplayer.adapters.PlaylistAdapter;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.CategoryListContentFragment;
import ngo.music.soundcloudplayer.controller.UIController;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PlaylistFragment extends CategoryListContentFragment {
	public static PlaylistFragment instance;

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return PLAYLIST;
	}

}
