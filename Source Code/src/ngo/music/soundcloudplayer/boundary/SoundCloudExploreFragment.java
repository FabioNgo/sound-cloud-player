package ngo.music.soundcloudplayer.boundary;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.Adapters.SoundCloudExploreAdapter;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import ngo.music.soundcloudplayer.database.DatabaseHandler;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class SoundCloudExploreFragment extends Fragment {
	public static SoundCloudExploreFragment instance = null;
	private SoundCloudExploreFragment() {
		
	}
	View rootView = null;
	public static SoundCloudExploreFragment getInstance() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new SoundCloudExploreFragment();
		}
		return instance;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		rootView = inflater.inflate(R.layout.tab_songs_view, container,false);
		final ListView songsList = (ListView) rootView.findViewById(R.id.songs_list);
		
		
		try {
			ArrayList<Song> songs = new BackgroundLoadOnlineMusic().execute().get();
			songsList.setAdapter(new SoundCloudExploreAdapter(MainActivity.getActivity().getApplicationContext(),
						R.layout.tab_songs_view, songs));
			
			songsList.setOnItemClickListener(new  OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View arg1, int position,
						long id) {
					// TODO Auto-generated method stub
//					Song song = (Song) songsList.getAdapter().getItem(position);
//					
//					MusicPlayerService.getInstance().playNewSong(position, songs);
				}
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return rootView;
	}
	private class BackgroundLoadOnlineMusic extends AsyncTask<String, String, ArrayList<Song>>{

		private ProgressDialog pDialog;
	
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			
			pDialog.show();
		}
		@Override
		protected ArrayList<Song> doInBackground(String... arg) {
			// TODO Auto-generated method stub
		
			final ArrayList<Song> songs;
			SongController songController = SongController.getInstance();
			 songs = songController.getAllSongsOnline(); 
			

			return songs;
		}
		
		
		@Override
		protected void onPostExecute(ArrayList<Song> result) {
			// TODO Auto-generated method stub
			
			pDialog.dismiss();
		} 
	}

	
}
