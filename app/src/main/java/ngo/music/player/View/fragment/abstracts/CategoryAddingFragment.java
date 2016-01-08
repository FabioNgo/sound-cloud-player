package ngo.music.player.View.fragment.abstracts;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ngo.music.player.Model.Category;
import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.CategoryManager;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.R;
import ngo.music.player.View.MusicPlayerMainActivity;
import ngo.music.player.adapters.CategoryTitlesListAdapter;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;

public abstract class CategoryAddingFragment extends DialogFragment implements Constants.Models,Observer {

	ArrayList<Song> songs;
	CategoryAddingFragment instance = null;
	private int type;
	public CategoryAddingFragment(){

	}
	/**
	 * 
	 * @param songs
	 */
	public CategoryAddingFragment(ArrayList<Song> songs) {
		// TODO Auto-generated constructor stub
		this.songs = songs;
		this.type = setType();
		instance = this;
		ModelManager.getInstance(type).addObserver(this);
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
		final View rootView = inflater.inflate(R.layout.playlist_adding_layout, container,false);
		
		RelativeLayout newPlaylistGroup = (RelativeLayout)rootView.findViewById(R.id.new_playlist_group);
		
		newPlaylistGroup.setVisibility(View.GONE);
		
		configCategory(rootView);
		
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
		toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				System.out.println ("TYPE PLAYLIST = " + arg0.getItemId());
				switch (arg0.getItemId()) {
				case R.id.new_playlist:
					
					final RelativeLayout newPlaylistGroup = (RelativeLayout)rootView.findViewById(R.id.new_playlist_group);
					newPlaylistGroup.setVisibility(View.VISIBLE);
					final EditText editText = (EditText)rootView.findViewById(R.id.new_playlist_edit_text);
					editText.setText("");
					final ImageView newPlaylistBtn = (ImageView)rootView.findViewById(R.id.new_playlist_submit);
					
					
					newPlaylistBtn.setOnClickListener(new OnClickListener() {
						
						

						@Override
						public void onClick(View view) {
							// TODO Auto-generated method stub
							
							try {

								((CategoryManager) ModelManager.getInstance(type)).newCategory(editText.getText().toString());
								rootView.findViewById(R.id.new_playlist_error_text).setVisibility(View.GONE);
								newPlaylistGroup.setVisibility(View.GONE);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								TextView errorText  = (TextView)rootView.findViewById(R.id.new_playlist_error_text);
								errorText.setVisibility(View.VISIBLE);
								errorText.setText(e.getMessage());
							}
							
						}
					});			
					break;

				default:
					break;
				}
				return false;
			}
		});
	}
	
	
	/** Config Playlist layout
	 * @param rootView
	 */
	private void configCategory(final View rootView) {
		final ListView listCategory = (ListView)rootView.findViewById(R.id.playlist_adding_list);
		listCategory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parentView, int position,
					long id) {
				// TODO Auto-generated method stub
				Category category = CategoryTitlesListAdapter.getInstance(type).getItem(position);

				((CategoryManager)ModelManager.getInstance(type)).addSongToCategory(songs.get(0).getId(), category.getId());
				Helper.makeToastText("Done", MusicPlayerMainActivity.getActivity());
				instance.dismiss();

			}
		});


		listCategory.setAdapter(CategoryTitlesListAdapter.getInstance(type));
	}

	@Override
	public void update(Observable observable, Object data) {

	}
}
