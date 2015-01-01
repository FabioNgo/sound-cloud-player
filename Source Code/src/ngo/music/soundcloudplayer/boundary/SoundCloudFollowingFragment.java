package ngo.music.soundcloudplayer.boundary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.SoundCloudFollowingAdapter;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.Constants;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.BaseBundle;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SoundCloudFollowingFragment extends Fragment {
	private View rootView;
	private ListView listUsers;
	SoundCloudUserController userController = SoundCloudUserController.getInstance();
	ArrayList<User> users = new ArrayList<User>();
	
	public SoundCloudFollowingFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.user_list_view, container,false);
		
		try {
			users = new LoadFollowingUserBackground().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		final SoundCloudFollowingAdapter adapter = new SoundCloudFollowingAdapter(getActivity(), R.layout.user_list_view, users);
		
		listUsers = (ListView) rootView.findViewById(R.id.users_list);
		listUsers.setAdapter(adapter);
		if (users.size() == 0) {
			/*
			 * Display the notice
			 */
			TextView notification = new TextView(getActivity());
			notification.setText("You are following any one");
			notification.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
		
		listUsers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				reCreateActivity(adapter, position);
				
			}
			
		});
				return rootView;
	}

	/**
	 * Seting all parameter related to users to default with new user(guest)
	 * @param adapter
	 * @param position
	 */
	private void reCreateActivity(final SoundCloudFollowingAdapter adapter,
			int position) {
		SongController songController = SongController.getInstance();
		songController.isLoadStream = true;
		songController.isLoadFavoriteSong = true;
		
		User guest = adapter.getUsers().get(position);
		userController.setGuest(guest);
		Intent i = new Intent(getActivity(), MainActivity.class);
		SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
		Bundle bundle = soundCloudUserController.getBundle(soundCloudUserController.getCurrentUser());
		i.putExtra(Constants.UserContant.USER, bundle);
		MainActivity.getActivity().finish();
		startActivity(i);
	}

	private class LoadFollowingUserBackground extends AsyncTask<String, String, ArrayList<User>>{

		@Override
		protected ArrayList<User> doInBackground(String... params) {
			// TODO Auto-generated method stub
			//SoundCloudUserController userController = SoundCloudUserController.getInstance();
			ArrayList<User>  users = new ArrayList<User>();
			try {
				 users = userController.getFollowingUsers();
				
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
