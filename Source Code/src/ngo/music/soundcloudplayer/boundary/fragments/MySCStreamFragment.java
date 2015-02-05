package ngo.music.soundcloudplayer.boundary.fragments;

import java.util.ArrayList;
import java.util.List;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.ListSongAdapter;
import ngo.music.soundcloudplayer.adapters.MySCStreamAdapter;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.ListViewOnItemClickHandler;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.entity.OnlineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MySCStreamFragment extends Fragment implements Constants{

	private View rootView;
	private ListView songsList;
	private ApiWrapper wrapper;

	public MySCStreamFragment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		/*
		 * Initialize View
		 */
	
		rootView = inflater.inflate(R.layout.list_view, container,false);
		songsList = (ListView) rootView.findViewById(R.id.items_list);
		SCUserController soundCloudUserController = SCUserController.getInstance();
//		Token t = soundCloudUserController.getToken();
//		wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null, t);
		wrapper = soundCloudUserController.getApiWrapper();
		new loadSongBackground().execute();
		songsList.setOnItemClickListener(new ListViewOnItemClickHandler());
		return rootView;
	}
	
	private class loadSongBackground extends AsyncTask<String , String, String>{

		ArrayList<Song> myStream;
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			SongController songController  = SongController.getInstance();
			try {
				songController.loadMyStream();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			myStream = songController.getMyStream();
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (myStream.size() == 0) {
				/*
				 * Display the notice
				 */
				TextView notification = (TextView) rootView.findViewById(R.id.notice);
				notification.setVisibility(View.VISIBLE);
				notification.setText("Do not have any song");
				
			}else{
				MySCStreamAdapter adapter = new MySCStreamAdapter(MusicPlayerMainActivity.getActivity().getApplicationContext(),R.layout.list_view, myStream ,wrapper);
				songsList.setAdapter(adapter);				
			}
			
		}
		
	}
}
