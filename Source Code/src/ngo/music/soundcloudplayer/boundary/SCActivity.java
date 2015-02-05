package ngo.music.soundcloudplayer.boundary;

import java.io.IOException;

import org.json.JSONException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.R.id;
import ngo.music.soundcloudplayer.R.menu;
import ngo.music.soundcloudplayer.controller.SCPlaylistSearchController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.general.Constants;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class SCActivity extends MusicPlayerMainActivity {

	public SCActivity() {
		// TODO Auto-generated constructor stub
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main_menu, menu);
		menu.setGroupVisible(R.id.search_group, true);
		
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				//switchTab(Constants.SoundCloudExploreConstant.SEARCH);
				
				SongController.getInstance().clearSearch();
				SCUserController.getInstance().clearSearch();
				SCPlaylistSearchController.getInstance().clearSearch();
				MusicPlayerMainActivity.query = query;
				//new searchBackground().execute(query);
				SCActivity.type = SCActivity.SOUNDCLOUD_SEARCH;
				
				SCPlaylistSearchController.getInstance().searchPlaylistSC(query, 0);
				
				//new searchBackground().execute(query);
//				SongController.getInstance().searchSongSC(query, 0);
//				SCUserController.getInstance().searchUserSC(query, 0);
				Intent i = new Intent(getActivity(), SCActivity.class);
				SCUserController soundCloudUserController = SCUserController.getInstance();
				
				Bundle bundle;
				try {
					bundle = soundCloudUserController.getBundle(soundCloudUserController.getCurrentUser());
					i.putExtra(USER, bundle);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//i.putExtra(ME_FAVORITES,stringResponse);
				SCActivity.getActivity().finish();
				startActivity(i);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		
		return true;
	}
	
	private class searchBackground extends AsyncTask<String, String, String>{
		ProgressDialog pDialog = new ProgressDialog(getActivity());
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			pDialog.setMessage("Searching......");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			
			//pDialog.show();
			
			SongController.getInstance().clearSearch();
			
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
//			SCActivity.type = SCActivity.SOUNDCLOUD_SEARCH;
//			MusicPlayerMainActivity.query = params[0];
			//SCPlaylistSearchController.getInstance().searchPlaylistSC(query, 0);
			//SongController.getInstance().searchSongSC(query, 0);
			//SCUserController.getInstance().searchUserSC(query, 0);
			//SCPlaylistSearchController.getInstance().searchPlaylistSC(params[0], 1);
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			new searchBackground2().execute(query);
//			Intent i = new Intent(getActivity(), SCActivity.class);
//			SCUserController soundCloudUserController = SCUserController.getInstance();
//			
//			Bundle bundle = soundCloudUserController.getBundle(soundCloudUserController.getCurrentUser());
//			i.putExtra(USER, bundle);
//			//i.putExtra(ME_FAVORITES,stringResponse);
//			SCActivity.getActivity().finish();
//			startActivity(i);
			
		
		}
	}
	
	private class searchBackground2 extends AsyncTask<String, String, String>{
		ProgressDialog pDialog = new ProgressDialog(getActivity());
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			pDialog.setMessage("Searching......");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			
			//pDialog.show();
			
			SongController.getInstance().clearSearch();
			SCUserController.getInstance().clearSearch();
			
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			SCActivity.type = SCActivity.SOUNDCLOUD_SEARCH;
			MusicPlayerMainActivity.query = params[0];
			//SongController.getInstance().searchSongSC(query, 0);
//			try {
//				SCUserController.getInstance().getSCUserSearch(query, 0);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			//SCPlaylistSearchController.getInstance().searchPlaylistSC(params[0], 1);
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			Intent i = new Intent(getActivity(), SCActivity.class);
			SCUserController soundCloudUserController = SCUserController.getInstance();
			
			Bundle bundle;
			try {
				bundle = soundCloudUserController.getBundle(soundCloudUserController.getCurrentUser());
				i.putExtra(USER, bundle);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//i.putExtra(ME_FAVORITES,stringResponse);
			SCActivity.getActivity().finish();
			startActivity(i);
			
		
		}
	}

}
