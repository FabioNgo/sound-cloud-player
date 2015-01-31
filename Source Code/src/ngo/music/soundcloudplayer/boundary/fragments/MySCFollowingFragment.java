package ngo.music.soundcloudplayer.boundary.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.SCExploreAdapter;
import ngo.music.soundcloudplayer.adapters.SCFollowAdapter;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.controller.UserController;
import ngo.music.soundcloudplayer.entity.OnlineSong;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.general.States;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.BaseBundle;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MySCFollowingFragment extends Fragment {
	private View rootView;
	private ListView listUsers;
	SCUserController userController = SCUserController
			.getInstance();
	ArrayList<User> users = new ArrayList<User>();
	int offset = 0;
	protected boolean loadingMore = false;
	int currentPosition;
	SCFollowAdapter adapter;

	public MySCFollowingFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.user_list_view, container, false);
		listUsers = (ListView) rootView.findViewById(R.id.users_list);
		
		if (SCUserController.getInstance().isLogin()) {
			try {
				users = new LoadFollowingUserBackground().execute().get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (users.size() == 0) {
			/*
			 * Display the notice
			 */
			TextView notification = new TextView(getActivity());
			notification.setText("You are following no one");
			notification.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {

			adapter = new SCFollowAdapter(getActivity(),
					R.layout.user_list_view, users);

			adapter.notifyDataSetChanged();

			listUsers.setAdapter(adapter);

			listUsers.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					reCreateActivity(adapter, position);

				}

			});

			listUsers.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// what is the bottom iten that is visible
					int lastInScreen = firstVisibleItem + visibleItemCount;
					// adapter.notifyDataSetChanged();
					// is the bottom item visible & not loading more already ?
					// Load more !
					if (lastInScreen >= totalItemCount - 1 && !loadingMore) {
						loadingMore = true;
						currentPosition = listUsers.getFirstVisiblePosition();
						// new loadMoreListView(songsList, adapter).execute();
						// Setting new scroll position
						new LoadFollowingUserBackground().execute();

						// adapter.notifyDataSetChanged();

						// loadingMore = false;
					}
					// TODO Auto-generated method stub

				}
			});
		}

		return rootView;
	}

	/**
	 * Seting all parameter related to users to default with new user(guest)
	 * 
	 * @param adapter
	 * @param position
	 */
	private void reCreateActivity(
			final SCFollowAdapter adapter, int position) {
		SongController songController = SongController.getInstance();
		songController.isLoadStream = true;
		songController.isLoadFavoriteSong = true;

		User guest = adapter.getUsers().get(position);
		userController.setGuest(guest);
		userController.clearUserData();
		Intent i = new Intent(getActivity(), MusicPlayerMainActivity.class);

		Bundle bundle = userController.getBundle(userController
				.getCurrentUser());
		i.putExtra(Constants.UserContant.USER, bundle);
		MusicPlayerMainActivity.getActivity().finish();
		startActivity(i);
	}

	/**
	 * Load info of following in background
	 * 
	 * @author LEBAO_000
	 *
	 */
	private class LoadFollowingUserBackground extends
			AsyncTask<String, String, ArrayList<User>> {
		ArrayList<User> users = new ArrayList<User>();

		@Override
		protected ArrayList<User> doInBackground(String... params) {
			// TODO Auto-generated method stub
			// SoundCloudUserController userController =
			// SoundCloudUserController.getInstance();
			// ArrayList<User> users = new ArrayList<User>();
			try {
				users = userController.getFollowingUsers(offset);
				// System.out.println ("SIZE = " +users.size());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return users;

		}

		@Override
		protected void onPostExecute(ArrayList<User> result) {
			// TODO Auto-generated method stub

			// Appending new data to menuItems ArrayList
			adapter = new SCFollowAdapter(
					MusicPlayerService.getInstance(), R.layout.user_list_view,
					users);

			// Setting new scroll position
			listUsers.setSelectionFromTop(currentPosition + 1, 0);
			offset = offset + 50;
			loadingMore = false;
		}

	}

}
