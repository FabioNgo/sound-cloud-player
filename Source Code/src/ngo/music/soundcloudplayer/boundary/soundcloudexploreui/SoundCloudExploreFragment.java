package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.Adapters.ListSongAdapter;
import ngo.music.soundcloudplayer.Adapters.SoundCloudExploreAdapter;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.ListViewOnItemClickHandler;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import ngo.music.soundcloudplayer.database.DatabaseHandler;
import ngo.music.soundcloudplayer.entity.OnlineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class SoundCloudExploreFragment extends Fragment  implements Constants{
	//public static SoundCloudExploreFragment instance = null;
	
	 // Flag for current page
    protected int current_page;
    /**
     * false : not loading
     */
    boolean loadingMore = false;
    protected ListSongAdapter adapter;
    protected ListView songsList;
    
    /*
     * Category in Explore
     */
    protected int category;
    
//	private SoundCloudExploreFragment() {
//		
//	}
	protected View rootView = null;

	ApiWrapper wrapper;
	
	
//	public static SoundCloudExploreFragment getInstance() {
//		// TODO Auto-generated method stub
//		if(instance == null) {
//			instance = new SoundCloudExploreFragment();
//		}
//		return instance;
//	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		rootView = inflater.inflate(R.layout.list_view, container,false);
		songsList = (ListView) rootView.findViewById(R.id.songs_list);
		SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
		Token t = soundCloudUserController.getToken();
		wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null, t);
		//responseString = getArguments().getString(ME_FAVORITES) ;
				
		
		try {
			ArrayList<Song> songs;
			SongController songController = SongController.getInstance();
			 songs = songController.getOnlineSongs(category); 
			//ArrayList<Song> songs = //new BackgroundLoadOnlineMusic().execute().get();
			//System.out.println (songs.size() + "......" + category);
			adapter = new SoundCloudExploreAdapter(MusicPlayerMainActivity.getActivity().getApplicationContext(),R.layout.list_view, songs,wrapper);
			adapter.notifyDataSetChanged();
			songsList.setAdapter(adapter);
			
			songsList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					ArrayList<Song> listSong =  adapter.getSongs();
					MusicPlayerService.getInstance().playNewExploreSong(position,category, listSong);
					// TODO Auto-generated method stub
					
				}
			});
			
			
			 
			// Creating a button - Load More
			Button btnLoadMore = new Button(getActivity());
			btnLoadMore.setText("Load More");
			 
			// Adding button to listview at footer
			//songsList.addFooterView(btnLoadMore);
			btnLoadMore.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new loadMoreButtonBackground().execute();
				}
			});
			songsList.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					
				}
//				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					
					//what is the bottom iten that is visible
					int lastInScreen = firstVisibleItem + visibleItemCount;
					//adapter.notifyDataSetChanged();
					//is the bottom item visible & not loading more already ? Load more !
					if(lastInScreen >= totalItemCount-1 && !loadingMore){
						loadingMore = true;

						new loadMoreButtonBackground().execute();

					}
					// TODO Auto-generated method stub
					
				}
			});
		
			
			
			
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		  /*
         * Move to next page
         */
        
		return rootView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
	
		super.onResume();
		adapter.notifyDataSetChanged();
	}
	

	private class loadMoreButtonBackground extends AsyncTask<String, String, String>{
		
		SongController songController = SongController.getInstance();
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
				songController = SongController.getInstance();
	           songController.loadMoreSong(current_page,category);
	           current_page++;
	           return null;
		}
		
		
		@Override
		protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
			 int currentPosition = songsList.getFirstVisiblePosition();
			 
             // Appending new data to menuItems ArrayList
             adapter = new SoundCloudExploreAdapter(getActivity(),R.layout.list_view, songController.getOnlineSongs(category), wrapper);

             // Setting new scroll position
             songsList.setSelectionFromTop(currentPosition + 1, 0);
             MusicPlayerService.getInstance().updateQueue(category);
             loadingMore = false;
		}
	}



	
}
