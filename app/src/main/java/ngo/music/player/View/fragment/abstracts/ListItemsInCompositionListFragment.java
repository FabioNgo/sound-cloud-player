package ngo.music.player.View.fragment.abstracts;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ngo.music.player.Model.Category;
import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.CategoryManager;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.R;
import ngo.music.player.View.fragment.real.ListItemsInAlbumFragment;
import ngo.music.player.View.fragment.real.ListItemsInArtistFragment;
import ngo.music.player.View.fragment.real.ListItemsInPlaylistFragment;
import ngo.music.player.adapters.SongsInCategoryAdapter;
import ngo.music.player.helper.Constants;
import ngo.music.player.service.MusicPlayerService;

/**
 * List Item Fragment when click show Songs on each category item
 * @author Fabio Ngo
 *
 */
public abstract class ListItemsInCompositionListFragment extends DialogFragment implements Constants.Models,Observer {
	Category category;
	View rootView;
	Song[] songs;
	SongsInCategoryAdapter adapter;
	int type;
	static ArrayList<ListItemsInCompositionListFragment> children;
	public ListItemsInCompositionListFragment(){
		type = getType();
		ModelManager.getInstance(type).addObserver(this);
	}
	public void setCategory(Category category){
		this.category = category;
		songs = ((CategoryManager) ModelManager.getInstance(getType())).getSongsFromCategory(category.getId());
	}
	/**
	 * Get ListItemsInCompositionListFragment instance depending on the type, to implement singleton pattern
	 *
	 * @param type - type of Controller
	 * @return null if no matched type is passed in
	 * @see {@link Constants
	 */
	public static ListItemsInCompositionListFragment getInstance(int type) {

		if (children == null) {
			children = new ArrayList<>(Constants.Models.SIZE);
			for(int i=0;i<Constants.Models.SIZE;i++){
				children.add(createNewInstance(i));
			}
		}

		return children.get(type);
	}
	/**
	 * Create new Controller instance to implement singleton pattern
	 *
	 * @param type - type of Controller
	 * @return null if the type does not match declared types in {@link Constants}
	 */
	private static ListItemsInCompositionListFragment createNewInstance(int type) {
		// TODO Auto-generated method stub
		switch (type) {
			case Constants.Models.PLAYLIST:
				return new ListItemsInPlaylistFragment();
			case Constants.Models.ALBUM:
				return new ListItemsInAlbumFragment();
			case Constants.Models.ARTIST:
				return new ListItemsInArtistFragment();
			default:
				return null;
		}

	}
	protected abstract int getType();

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	  Dialog dialog = super.onCreateDialog(savedInstanceState);

	  // request a window without the title
	  dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
	  return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView  = inflater.inflate(R.layout.list_items_in_conposition_list_layout, container,false);
		Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.list_items_composition_list_toolbar);
		toolbar.setTitle(category.getAttribute("title"));
		toolbar.setSubtitle(songs.length + " song(s)");

		final ListView listPlaylist = (ListView)rootView.findViewById(R.id.list_items_composition_list);
		listPlaylist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parentView, int position,
					long id) {
				// TODO Auto-generated method stub
				MusicPlayerService.getInstance().playNewSong(position, songs);
			}
		});
		adapter = SongsInCategoryAdapter.createInstance(getType(), R.layout.song_in_cate, category);
		listPlaylist.setAdapter(adapter);

		return rootView;
	}



	@Override
	public void update(Observable observable, Object data) {
		if(observable instanceof CategoryManager){
			Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.list_items_composition_list_toolbar);
			songs = ((CategoryManager) observable).getSongsFromCategory(category.getId());
			toolbar.setSubtitle(songs.length+" song(s)");
		}
	}
}
