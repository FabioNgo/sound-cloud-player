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

import ngo.music.player.Model.Song;
import ngo.music.player.R;
import ngo.music.player.adapters.SongsInCateAdapter;
import ngo.music.player.View.fragment.real.ListItemInAlbumFragment;
import ngo.music.player.View.fragment.real.ListItemInArtistFragment;
import ngo.music.player.View.fragment.real.ListItemInPlaylistFragment;
import ngo.music.player.helper.Constants;
import ngo.music.player.service.MusicPlayerService;

/**
 * List Item Fragment when click show Songs on each category item
 * @author Fabio Ngo
 *
 */
public abstract class ListItemsInCompositionListFragment extends DialogFragment implements Constants.Models {
	Song[] songs;
	String cat = "";
	int category = -1;
	View rootView;
	SongsInCateAdapter adapter;

	protected ListItemsInCompositionListFragment(Song[] songs, String cat) {
		// TODO Auto-generated constructor stub
		this.songs = songs;
		this.cat = cat;
		category = getCategory();
	}

	public static ListItemsInCompositionListFragment createInstance(Song[] songs,
																	String catTitle, int type) {
		// TODO Auto-generated method stub
		switch (type) {
			case PLAYLIST:
				return new ListItemInPlaylistFragment(songs, catTitle);

			case ALBUM:
				return new ListItemInAlbumFragment(songs, catTitle);
			case ARTIST:
				return new ListItemInArtistFragment(songs, catTitle);

			default:
				return null;
		}
	}

	protected abstract int getCategory();

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
		toolbar.setTitle(cat);
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
		adapter = SongsInCateAdapter.createInstance(category,R.layout.song_in_cate,cat);
		listPlaylist.setAdapter(adapter);

		return rootView;
	}

	public void update(){
		adapter.update();
		Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.list_items_composition_list_toolbar);
		toolbar.setSubtitle(adapter.getCount()+" song(s)");
	}
	
}
