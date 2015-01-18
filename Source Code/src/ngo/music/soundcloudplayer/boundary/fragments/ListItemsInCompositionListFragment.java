package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.Adapters.SimplePlaylistAdapter;
import ngo.music.soundcloudplayer.Adapters.SongsInCateAdapter;
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

public class ListItemsInCompositionListFragment extends DialogFragment implements Constants.Categories {
	ArrayList<Song> songs;
	String cat = "";
	int type = -1;
	private ListItemsInCompositionListFragment instance;
	public ListItemsInCompositionListFragment(ArrayList<Song> songs,String cat) {
		// TODO Auto-generated constructor stub
		this.songs = songs;
		instance = this;
		this.cat = cat;
	}
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
		final View rootView = inflater.inflate(R.layout.list_items_in_conposition_list_layout, container,false);
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
		listPlaylist.setAdapter(new SongsInCateAdapter(MusicPlayerMainActivity.getActivity(),R.layout.song_in_cate,songs,cat));
		
		return rootView;
	}
	
}
