package ngo.music.soundcloudplayer.boundary.fragment.real;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.SCFollowAdapter;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.general.States;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MySCFollowerFragment extends Fragment {
	private View rootView;
	private ListView listUsers;
	SCUserController userController = SCUserController
			.getInstance();
	ArrayList<User> users = new ArrayList<User>();
	protected boolean loadingMore = false;
	protected int offset = 0;
	SCFollowAdapter adapter;
	public int currentPosition;

	public MySCFollowerFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.user_list_view, container, false);
		// ArrayList<User> users = new ArrayList<User>();
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
		adapter = new SCFollowAdapter(getActivity(),
				R.layout.user_list_view, users);
		// adapter.notifyDataSetChanged();
		listUsers = (ListView) rootView.findViewById(R.id.users_list);
		listUsers.setAdapter(adapter);
		listUsers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				reCreateActivity(adapter, position);

			}
		});

		listUsers.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// what is the bottom iten that is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;
				// adapter.notifyDataSetChanged();
				// is the bottom item visible & not loading more already ? Load
				// more !
				if (lastInScreen >= totalItemCount - 1 && !loadingMore) {
					loadingMore = true;
					// new loadMoreListView(songsList, adapter).execute();
					// Setting new scroll position
					new LoadFollowerUserBackground().execute();

					// loadingMore = false;
				}
				// TODO Auto-generated method stub

			}
		});

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
		MusicPlayerMainActivity.type = MusicPlayerMainActivity.MY_SOUNDCLOUD;

		Bundle bundle;
		try {
			bundle = userController.getBundle(userController.getCurrentUser());
			i.putExtra(Constants.UserContant.USER, bundle);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MusicPlayerMainActivity.getActivity().finish();
		startActivity(i);
	}

	private class LoadFollowerUserBackground extends
			AsyncTask<String, String, ArrayList<User>> {

		ArrayList<User> users = new ArrayList<User>();

		@Override
		protected ArrayList<User> doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (SCUserController.getInstance().isLogin()) {

				try {
					users = userController.getFollowerUsers(offset);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
