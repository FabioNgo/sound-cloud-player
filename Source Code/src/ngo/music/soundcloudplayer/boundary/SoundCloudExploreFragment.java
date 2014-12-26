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
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class SoundCloudExploreFragment extends Fragment {
	public static SoundCloudExploreFragment instance = null;
	
	 // Flag for current page
    int current_page = 1;
    /**
     * false : not loading
     */
    boolean loadingMore = false;
    SoundCloudExploreAdapter adapter;
    ListView songsList;
    
	private SoundCloudExploreFragment() {
		
	}
	View rootView = null;

	protected ArrayList<String> myListItems;
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
		songsList = (ListView) rootView.findViewById(R.id.songs_list);
		
		
		try {
			ArrayList<Song> songs = new BackgroundLoadOnlineMusic().execute().get();
			adapter = new SoundCloudExploreAdapter(MainActivity.getActivity().getApplicationContext(),R.layout.tab_songs_view, songs);
			System.out.println ("CHANGED");
			adapter.notifyDataSetChanged(); 
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
					if(lastInScreen > totalItemCount-3 && !loadingMore){
						//loadingMore = true;
						//new loadMoreListView(songsList, adapter).execute();
						 // Setting new scroll position
						adapter.notifyDataSetChanged();
//		                
						Thread thread =  new Thread(null, loadMoreListItems);
						thread.start();
						
						songsList.setSelectionFromTop(firstVisibleItem + 1, 0);
						   /*
				          * Move to next page
				          */
				         current_page++;
						//loadingMore = false;
					}
					// TODO Auto-generated method stub
					
				}
			});
		
			
			
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		return rootView;
	}
	
	   //Runnable to load the items
	   private Runnable loadMoreListItems = new Runnable() {
		@Override
		public void run() {
			//Set flag so we cant load new items 2 at the same time
			loadingMore = true;
			//Reset the array that holds the new items
			ArrayList<Song> songs;
            SongController songController = SongController.getInstance();
            songController.loadMoreSong(current_page);
			 
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
			ArrayList<Song> songs = songController.getOnlineSongs();
			adapter = new SoundCloudExploreAdapter(MainActivity.getActivity().getApplicationContext(),R.layout.tab_songs_view, songs);
			//songsList.
			//Tell to the adapter that changes have been made, this will cause the list to refresh
			System.out.println ("CHANGED");
	         adapter.notifyDataSetChanged();
			//Done loading more.
	        loadingMore = false;
	         
	      
	     }
	   };
	/**
	 * Async Task that send a request to url
	 * Gets new list view data
	 * Appends to list view
	 * */
	private class loadMoreListView extends AsyncTask<String, String, String> {
	 
	    private ProgressDialog pDialog;
	    ListView lv ;
	    SoundCloudExploreAdapter soundCloudExploreAdapter;
	    
	    public loadMoreListView(ListView lv, SoundCloudExploreAdapter adapter) {
			// TODO Auto-generated constructor stub
	    	this.lv = lv;
	    	soundCloudExploreAdapter = adapter;
		}

		@Override
	    protected void onPreExecute() {
	        // Showing progress dialog before sending http request
	        pDialog = new ProgressDialog(MainActivity.getActivity());
	        //pDialog.setMessage("Please wait..");
	        pDialog.setIndeterminate(true);
	        pDialog.setCancelable(false);
	        pDialog.show();
	    }
	 
	    protected String doInBackground(String... unused) {
	        
	            
	                // increment current page
	                current_page += 1;
	 
	                // Next page request
	               
	 
	                // get listview current position - used to maintain scroll position
	                int currentPosition = lv.getFirstVisiblePosition();
	 
	                // Appending new data to menuItems ArrayList
	                ArrayList<Song> songs;
	                SongController songController = SongController.getInstance();
	                songController.loadMoreSong(current_page);
	   			 	songs = songController.getOnlineSongs(); 
	                soundCloudExploreAdapter = new SoundCloudExploreAdapter(MainActivity.getActivity(),R.layout.tab_songs_view, songs);
	                //Tell to the adapter that changes have been made, this will cause the list to refresh
	                soundCloudExploreAdapter.notifyDataSetChanged();
	           		//	Done loading more.
	           		loadingMore = false;
	               
	            
	        
	        return (null);
	    }       
	 
	    protected void onPostExecute(String result) {
	        // closing progress dialog
	        pDialog.dismiss();
	    }
	}
	/**
	 * load online music in backgroud
	 * @author LEBAO_000
	 *
	 */
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
		
			
			ArrayList<Song> songs;
			SongController songController = SongController.getInstance();
			 songs = songController.getOnlineSongs(); 
			

			return songs;
		}
		
		
		@Override
		protected void onPostExecute(ArrayList<Song> result) {
			// TODO Auto-generated method stub
			
			pDialog.dismiss();
		} 
	}

	
}
