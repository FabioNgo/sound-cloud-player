package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.CompositionListAdapter;
import ngo.music.soundcloudplayer.adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.adapters.CategoryTitlesListAdapter;
import ngo.music.soundcloudplayer.adapters.SongsInCateAdapter;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
/**
 * List Item Fragment when click show Songs on each category item
 * @author Fabio Ngo
 *
 */
public abstract class ListItemsInCompositionListFragment extends DialogFragment implements Constants.Categories {
	ArrayList<Song> songs;
	String cat = "";
	int type = -1;
	View rootView;
	SongsInCateAdapter adapter;
	ListItemsInCompositionListFragment(ArrayList<Song> songs,String cat) {
		// TODO Auto-generated constructor stub
		this.songs = songs;
		this.cat = cat;
		type = setType();
	}
	protected abstract int setType();
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
		toolbar.setSubtitle(songs.size()+" song(s)");
		
		
		
		final ListView listPlaylist = (ListView)rootView.findViewById(R.id.list_items_composition_list);
		listPlaylist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parentView, int position,
					long id) {
				// TODO Auto-generated method stub
				MusicPlayerService.getInstance().playNewSong(position, songs);
			}
		});
		adapter = SongsInCateAdapter.createInstance(type,R.layout.song_in_cate,cat);
		listPlaylist.setAdapter(adapter);
		
		return rootView;
	}
	public static ListItemsInCompositionListFragment createInstance(ArrayList<Song> songs,
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
	public void update(){
		adapter.update();
		Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.list_items_composition_list_toolbar);
		toolbar.setSubtitle(adapter.getCount()+" song(s)");
	}
	
}
