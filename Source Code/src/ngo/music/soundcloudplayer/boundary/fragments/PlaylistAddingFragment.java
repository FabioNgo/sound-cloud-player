package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.SimplePlaylistAdapter;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.entity.Song;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class PlaylistAddingFragment extends DialogFragment {
	
	public static final int PLAYLIST = 0;
	public static final int SOUNDCLOUD_PLAYLIST = 1;
	private int typePlaylist;
	ArrayList<Song> songs;
	private PlaylistAddingFragment instance;
	
	/**
	 * 
	 * @param songs
	 * @param type PLAYLIST = offline playlist
	 * 				SOUNDCLOUD_PLAYLIST = soundcloud playlist
	 */
	public PlaylistAddingFragment(ArrayList<Song> songs, int type) {
		// TODO Auto-generated constructor stub
		this.songs = songs;
		typePlaylist = type;
		
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
		
		RelativeLayout newPlaylistGroup = (RelativeLayout)rootView.findViewById(R.id.new_playlist_group);
		
		newPlaylistGroup.setVisibility(View.GONE);
		
		configPlayList(rootView);
		
		configToolbar(rootView);
		return rootView;
	}
	/**
	 * Config Toolbar
	 * @param rootView
	 */
	private void configToolbar(final View rootView) {
		Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.playlist_adding_toolbar);
		toolbar.setTitle("Add to playlist");
		toolbar.inflateMenu(R.menu.add_to_playlist_menu);
//		toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//			
//			@Override
//			public boolean onMenuItemClick(MenuItem arg0) {
//				// TODO Auto-generated method stub
//				System.out.println ("TYPE PLAYLIST = " + arg0.getItemId());
//				switch (arg0.getItemId()) {
//				case R.id.new_playlist:
//					
//					final RelativeLayout newPlaylistGroup = (RelativeLayout)rootView.findViewById(R.id.new_playlist_group);
//					newPlaylistGroup.setVisibility(View.VISIBLE);
//					final EditText editText = (EditText)rootView.findViewById(R.id.new_playlist_edit_text);
//					editText.setText("");
//					final ImageView newPlaylistBtn = (ImageView)rootView.findViewById(R.id.new_playlist_submit);
//					
//					
//					newPlaylistBtn.setOnClickListener(new OnClickListener() {
//						
//						
//
//						@Override
//						public void onClick(View view) {
//							// TODO Auto-generated method stub
//							
//							try {
//								
//								switch (typePlaylist) {
//								case PLAYLIST:
//									PlaylistController.getInstance().createCategory(editText.getText().toString());
//									break;
//								case SOUNDCLOUD_PLAYLIST:
//									SoundCloudPlaylistController.getInstance().createCategory(editText.getText().toString());
//									break;
//								default:
//									break;
//								}
//								
//								rootView.findViewById(R.id.new_playlist_error_text).setVisibility(View.GONE);
//								newPlaylistGroup.setVisibility(View.GONE);
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								TextView errorText  = (TextView)rootView.findViewById(R.id.new_playlist_error_text);
//								errorText.setVisibility(View.VISIBLE);
//								errorText.setText(e.getMessage());
//							}
//							
//						}
//					});			
//					break;
//
//				default:
//					break;
//				}
//				return false;
//			}
//		});
	}
	
	
	/** Config Playlist layout
	 * @param rootView
	 */
	private void configPlayList(final View rootView) {
		final ListView listPlaylist = (ListView)rootView.findViewById(R.id.playlist_adding_list);
		listPlaylist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parentView, int position,
					long id) {
				// TODO Auto-generated method stub
				String playlistName = SimplePlaylistAdapter.getInstance().getItem(position);
				try {
					PlaylistController.getInstance().addSongsToCategory(playlistName, songs);
					
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
	}
	
	

}
