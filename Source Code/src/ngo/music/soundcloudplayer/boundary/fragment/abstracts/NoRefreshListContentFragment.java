package ngo.music.soundcloudplayer.boundary.fragment.abstracts;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.SCSongAdapter;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.boundary.SCActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 
 * @author Fabio Ngo Every fragments having list view to list songs. This is
 *         used to update list when necessary
 *
 */
public abstract class NoRefreshListContentFragment extends ListContentFragment
		implements Constants.MusicService, Constants.Categories,
		Constants.Appplication, OnItemClickListener {
	protected boolean loadingMore = false;
	protected int current_page;


	@Override
	final public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		numFragmentsLoading++;
		rootView = inflater.inflate(R.layout.list_view, container, false);
		listView = (ListView) rootView.findViewById(R.id.items_list);
		toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
		adapter = getAdapter();
		load();
		if (hasLoadMore()) {
			setUpLoadMore();
		}
		if(hasToolbar()){
			
			setUpToolBar(toolbar);
		}else{
			toolbar.setVisibility(View.GONE);
		}
		// rootView = childrenOnCreateView(inflater, container,
		// savedInstanceState);
		
		load();
		numFragmentsLoading--;
		UIController.getInstance().addListContentFragments(this);
		return rootView;
	}


	protected abstract void setUpLoadMore();
	
}
