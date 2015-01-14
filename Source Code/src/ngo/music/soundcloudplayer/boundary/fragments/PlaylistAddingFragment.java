package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlaylistAddingFragment extends DialogFragment {

	public PlaylistAddingFragment() {
		// TODO Auto-generated constructor stub
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
		final View v = inflater.inflate(R.layout.playlist_adding_layout, null);
		Toolbar toolbar = (Toolbar)v.findViewById(R.id.playlist_adding_toolbar);
		toolbar.setTitle("Add to playlist");
		toolbar.inflateMenu(R.menu.add_to_playlist_menu);
		RelativeLayout newPlaylistGroup = (RelativeLayout)v.findViewById(R.id.new_playlist_group);
		newPlaylistGroup.setVisibility(View.GONE);
		toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				switch (arg0.getItemId()) {
				case R.id.new_playlist:
					BasicFunctions.makeToastTake("Asdasd", getActivity());
					RelativeLayout newPlaylistGroup = (RelativeLayout)v.findViewById(R.id.new_playlist_group);
					newPlaylistGroup.setVisibility(View.VISIBLE);
					EditText editText = (EditText)v.findViewById(R.id.new_playlist_edit_text);
					editText.requestFocus();
					getDialog().getWindow().setSoftInputMode(
			                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
					break;

				default:
					break;
				}
				return false;
			}
		});
		ListView listPlaylist = (ListView)v.findViewById(R.id.playlist_adding_list);
		listPlaylist.setAdapter(new PlaylistAdapter(getActivity(), R.layout.single_playilist_list_adding));
		return v;
	}
	private class PlaylistAdapter extends ArrayAdapter<String>{
		ArrayList<String> playlist = new ArrayList<String>();
		public PlaylistAdapter(Context context, int resource) {
			super(context, resource);
			// TODO Auto-generated constructor stub
			playlist = PlaylistController.getInstance().getPlaylistsName();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = convertView;
			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.song_in_list, null);

			}

			setLayoutInformation(position, v);
			return v;
		}
		private void setLayoutInformation(int position, View v) {
			// TODO Auto-generated method stub
			TextView tv = (TextView)v.findViewById(R.id.single_playlist_title);
			tv.setText(playlist.get(position));
		}
	}
}
