package ngo.music.soundcloudplayer.boundary.fragment.real;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.CategoryListAdapter;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.CategoryListContentFragment;
import ngo.music.soundcloudplayer.controller.UIController;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ArtistsFragment extends CategoryListContentFragment  {
	public static ArtistsFragment instance = null;
	
	

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return ARTIST;
	}
}
