package ngo.music.player.boundary.fragment.abstracts;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import ngo.music.player.Controller.UIController;
import ngo.music.player.R;
import ngo.music.player.helper.Constants;

/**
 * 
 * @author Fabio Ngo Every fragments having list view to list songs. This is
 *         used to update list when necessary
 *
 */
public abstract class NoRefreshListContentFragment extends ListContentFragment
		implements Constants.MusicService, Constants.Models,
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
		//add this fragment to UIController to update UI when data changed
		UIController.getInstance().addListContentFragments(this);
		return rootView;
	}

	/**
	 * setUp loadmore method
	 */
	protected abstract void setUpLoadMore();
	
}
