package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.Adapters.ListSongAdapter;
import ngo.music.soundcloudplayer.Adapters.SoundCloudExploreAdapter;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import ngo.music.soundcloudplayer.database.DatabaseHandler;
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
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
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
			System.out.println (songs.size() + "......" + category);
			adapter = new SoundCloudExploreAdapter(MainActivity.getActivity().getApplicationContext(),R.layout.list_view, songs,wrapper);
			//adapter.setNotifyOnChange(true);
			
			//System.out.println ("CHANGED");
			//adapter.notifyDataSetChanged(); 
			songsList.setAdapter(adapter);
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
			songsList.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					
					//what is the bottom iten that is visible
					int lastInScreen = firstVisibleItem + visibleItemCount;
					
					//is the bottom item visible & not loading more already ? Load more !
					if(lastInScreen >= totalItemCount-3 && !loadingMore){
						//loadingMore = true;
						//new loadMoreListView(songsList, adapter).execute();
						 // Setting new scroll position
						adapter.notifyDataSetChanged();
//		                
						Thread thread =  new Thread(null, loadMoreListItems);
						thread.start();
						
						songsList.setSelectionFromTop(firstVisibleItem + 1, 0);
						 
						//loadingMore = false;
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
		System.out.println ("RESUMED");
		super.onResume();
		adapter.notifyDataSetChanged();
	}
	
	
	   //Runnable to load the items
	   private Runnable loadMoreListItems = new Runnable() {
		@Override
		public void run() {
			//Set flag so we cant load new items 2 at the same time
			loadingMore = true;
			//Reset the array that holds the new items
            SongController songController = SongController.getInstance();
            songController.loadMoreSong(current_page,category);
            current_page++;
			//Done! now continue on the UI thread
	       	MainActivity.getActivity().runOnUiThread(new Thread(returnRes));
					
					
		}

		
	};
	
	
	//Since we cant update our UI from a thread this Runnable takes care of that!
	private Runnable returnRes = new Runnable() {
		@Override
		public void run() {
			//Loop thru the new items and add them to the adapter
			SongController songController = SongController.getInstance();
			ArrayList<Song> songs = songController.getOnlineSongs(category);
			//adapter.setNotifyOnChange(true);
			adapter = new SoundCloudExploreAdapter(MainActivity.getActivity().getApplicationContext(),R.layout.list_view, songs,wrapper);
			//songsList.
			//Tell to the adapter that changes have been made, this will cause the list to refresh
			//System.out.println ("CHANGED");
	         adapter.notifyDataSetChanged();
			//Done loading more.
	        loadingMore = false;
	         
	      
	     }
	   };



	
}
