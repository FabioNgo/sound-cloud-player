package ngo.music.soundcloudplayer.boundary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.SoundCloudFollowingFollowerAdapter;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.Constants;
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

public class SoundCloudFollowerFragment extends Fragment {
	private View rootView;
	private ListView listUsers;
	SoundCloudUserController userController = SoundCloudUserController.getInstance();
	ArrayList<User> users = new ArrayList<User>();
	
	public SoundCloudFollowerFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.user_list_view, container,false);
		//ArrayList<User> users = new ArrayList<User>();
		try {
			users = new LoadFollowerUserBackground().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//	
		final SoundCloudFollowingFollowerAdapter adapter = new SoundCloudFollowingFollowerAdapter(getActivity(), R.layout.user_list_view, users);
		
		listUsers = (ListView) rootView.findViewById(R.id.users_list);
		listUsers.setAdapter(adapter);
		listUsers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				
				User guest = adapter.getUsers().get(position);
				
				userController.setGuest(guest);
				//MainActivity.isExplore = false;	//Main Screen
				
				
				Intent i = new Intent(getActivity(), MainActivity.class);
				
				SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
				Bundle bundle = soundCloudUserController.getBundle(soundCloudUserController.getCurrentUser());
				i.putExtra(Constants.UserContant.USER, bundle);
				
				startActivity(i);
				MainActivity.getActivity().finish(); 
				
			}
		});
		return rootView;
	}

	private class LoadFollowerUserBackground extends AsyncTask<String, String, ArrayList<User>>{

		@Override
		protected ArrayList<User> doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			ArrayList<User> users = new ArrayList<User>();
			try {
				users = userController.getFollowerUsers();
		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return users;
			
		}
		
		
	}
}
