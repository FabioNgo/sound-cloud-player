package ngo.music.soundcloudplayer.boundary.fragment.real;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.CategoryListAdapter;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.CategoryListContentFragment;
import ngo.music.soundcloudplayer.controller.UIController;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AlbumsFragment extends CategoryListContentFragment  {
	public static AlbumsFragment instance = null;
	
	
	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return ALBUM;
	}


	
}
