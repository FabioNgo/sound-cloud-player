package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.SimplePlaylistAdapter;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
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

public class PlaylistAddingFragment extends DialogFragment {
	ArrayList<Song> songs;
	private PlaylistAddingFragment instance;
	public PlaylistAddingFragment(ArrayList<Song> songs) {
		// TODO Auto-generated constructor stub
		this.songs = songs;
		instance = this;
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
		final View rootView = inflater.inflate(R.layout.playlist_adding_layout, container,false);
		Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.playlist_adding_toolbar);
		toolbar.setTitle("Add to playlist");
		toolbar.inflateMenu(R.menu.add_to_playlist_menu);
		RelativeLayout newPlaylistGroup = (RelativeLayout)rootView.findViewById(R.id.new_playlist_group);
		
		newPlaylistGroup.setVisibility(View.GONE);
		final ListView listPlaylist = (ListView)rootView.findViewById(R.id.playlist_adding_list);
		listPlaylist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parentView, int position,
					long id) {
				// TODO Auto-generated method stub
				String playlistName = SimplePlaylistAdapter.getInstance().getItem(position);
				try {
					PlaylistController.getInstance().addSongsToPlaylist(playlistName, songs);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("add song to playlist",e.toString());
				}
				finally{
					instance.dismiss();
				}
			}
		});
		listPlaylist.setAdapter(SimplePlaylistAdapter.getInstance());
		toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				switch (arg0.getItemId()) {
				case R.id.new_playlist:
					final RelativeLayout newPlaylistGroup = (RelativeLayout)rootView.findViewById(R.id.new_playlist_group);
					newPlaylistGroup.setVisibility(View.VISIBLE);
					final EditText editText = (EditText)rootView.findViewById(R.id.new_playlist_edit_text);
					final ImageView newPlaylistBtn = (ImageView)rootView.findViewById(R.id.new_playlist_submit);
					
					
					newPlaylistBtn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View view) {
							// TODO Auto-generated method stub
							
							try {
								PlaylistController.getInstance().createPlaylist(editText.getText().toString());
								rootView.findViewById(R.id.new_playlist_error_text).setVisibility(View.GONE);
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								TextView errorText  = (TextView)rootView.findViewById(R.id.new_playlist_error_text);
								errorText.setVisibility(View.VISIBLE);
								errorText.setText(e.getMessage());
							}
							newPlaylistGroup.setVisibility(View.GONE);
						}
					});			
					break;

				default:
					break;
				}
				return false;
			}
		});
		return rootView;
	}

}
