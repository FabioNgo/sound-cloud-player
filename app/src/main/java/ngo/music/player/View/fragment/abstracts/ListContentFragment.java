package ngo.music.player.View.fragment.abstracts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Observable;
import java.util.Observer;

import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.R;
import ngo.music.player.adapters.LiteListSongAdapter;
import ngo.music.player.helper.Constants;
import ngo.music.player.service.MusicPlayerService;

/**
 * 
 * @author Fabio Ngo Every fragments having list view of songs. This is
 *         used to update list when necessary
 *
 */
public abstract class ListContentFragment extends Fragment implements
		Constants.MusicService, Constants.Models, Constants.Appplication,
		OnItemClickListener, Comparable<ListContentFragment>,Observer {
	protected View rootView;
	protected ArrayAdapter<?> adapter;
	protected int category;
	protected Toolbar toolbar;
	protected ListView listView;

	public ListContentFragment() {
		// TODO Auto-generated constructor stub

		category = getCategory();
		Observable observable = ModelManager.getInstance(category);
		if(observable !=null){
			observable.addObserver(this);
		}
	}
	@Override
	final public View onCreateView(LayoutInflater inflater,
								   ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.list_view, container, false);
		listView = (ListView) rootView.findViewById(R.id.items_list);
		toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
		adapter = getAdapter();

		if(hasToolbar()){

			setUpToolBar(toolbar);
		}else{
			toolbar.setVisibility(View.GONE);
		}

		load();
		return rootView;
	}
	/**
	 * 
	 * @return true if list song fragment has toolbar
	 * else @return false
	 */
	protected abstract boolean hasToolbar();

	/**
	 * Initialize toolbar
	 * if no toolbar, just return
	 */
	protected abstract void setUpToolBar(Toolbar toolbar);
	/**
	 * Update Toolbar when data changed 
	 * @param toolbar
	 */
	protected abstract void updateToolbar(Toolbar toolbar);
	/**
	 * get category type in Contants.Models (e.g: OFFLINE_SONG, AMBIENT...)
	 * @return
	 */
	protected abstract int getCategory();
	/**
	 * getAdapter of the list (E.g: OfflineSongAdapter, SCSongAdapter....)
	 * @return
	 */
	protected abstract ArrayAdapter<?> getAdapter();

	/**
	 * Load fragment activity, often list view fragment
	 *
	 *
	 */
	public void load() {
		listView = (ListView) rootView.findViewById(R.id.items_list);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		updateToolbar(toolbar);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position,
			long id) {

		Adapter adapter = parent.getAdapter();

		if (adapter instanceof LiteListSongAdapter) {
			// ((ArrayAdapter<OnlineSong>) adapter).notifyDataSetChanged();
			Song[] songs = ((LiteListSongAdapter) adapter).getSongs();
			MusicPlayerService.getInstance().playNewSong(position,
					songs);
			return;
		}


	}


	
	/**
	 * compare Song, use in sorting and filtering
	 */
	@Override
	public int compareTo(ListContentFragment another) {
		// TODO Auto-generated method stub
		return this.getClass().toString().compareTo(another.getClass().toString());
	}
}
