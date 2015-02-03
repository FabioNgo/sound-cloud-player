package ngo.music.soundcloudplayer.boundary.soundcloudexploreui;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.ListSongAdapter;
import ngo.music.soundcloudplayer.adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.adapters.SCExploreAdapter;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.ListViewOnItemClickHandler;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.database.DatabaseHandler;
import ngo.music.soundcloudplayer.entity.OnlineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
    protected boolean loadingMore = false;
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

	protected ApiWrapper wrapper;
	
	
//	public static SoundCloudExploreFragment getInstance() {
//		// TODO Auto-generated method stub
//		if(instance == null) {
//			instance = new SoundCloudExploreFragment();
//		}
//		return instance;
//	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		 //inflater = getActivity().getMenuInflater();
		 //inflater.inflate(R.menu.options_menu, menu);
		 
		 //Associate searchable configuration with the SearchView
		 SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		 SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		 searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
		 
		 
		    
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		rootView = inflater.inflate(R.layout.list_view, container,false);
		songsList = (ListView) rootView.findViewById(R.id.items_list);
		SCUserController soundCloudUserController = SCUserController.getInstance();
		Token t = soundCloudUserController.getToken();
		wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null, t);
		//responseString = getArguments().getString(ME_FAVORITES) ;
				
		
		try {
			ArrayList<Song> songs;
			SongController songController = SongController.getInstance();
			System.out.println ("CATE = " + category);
			 songs = songController.getOnlineSongs(category); 
			//ArrayList<Song> songs = //new BackgroundLoadOnlineMusic().execute().get();
			//System.out.println (songs.size() + "......" + category);
			adapter = new SCExploreAdapter(MusicPlayerMainActivity.getActivity().getApplicationContext(),R.layout.list_view, songs,wrapper);
			adapter.notifyDataSetChanged();
			songsList.setAdapter(adapter);
			
			songsList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					ArrayList<Song> listSong =  adapter.getSongs();
					System.out.println ("CAT " + category);
					MusicPlayerService.getInstance().playNewExploreSong(position,category, listSong);
					// TODO Auto-generated method stub
					
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

						new loadMoreBackground().execute();
						adapter.notifyDataSetChanged();

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
	

	private class loadMoreBackground extends AsyncTask<String, String, String>{
		
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
             adapter = new SCExploreAdapter(getActivity(),R.layout.list_view, songController.getOnlineSongs(category), wrapper);

             // Setting new scroll position
             songsList.setSelectionFromTop(currentPosition + 1, 0);
             MusicPlayerService.getInstance().updateQueue(category);
             loadingMore = false;
		}
	}



	
}
