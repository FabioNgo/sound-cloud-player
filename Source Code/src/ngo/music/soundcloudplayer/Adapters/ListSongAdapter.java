package ngo.music.soundcloudplayer.Adapters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;





import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.volley.api.AppController;



import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.boundary.fragments.PlaylistAddingFragment;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import ngo.music.soundcloudplayer.entity.OnlineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.CircularImageView;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.LruCache;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class ListSongAdapter extends ArrayAdapter<Song> implements Constants {
	
	private static final String ME_FAVORITE = "https://api.soundcloud.com/me/favorites/";
	protected ApiWrapper wrapper;
	 long enqueue = 0;
	 DownloadManager dm = null;
	    
	 private static final String ROOT_DIRECTORY = "/SoundCloudApp";
	    
	public static ListSongAdapter instance = null;
	protected ArrayList<Song> songs;
	
	private ShareActionProvider mShareActionProvider;
	protected ListSongAdapter(Context context, int resource, ArrayList<Song> songs2, ApiWrapper wrapper) {
		super(context, resource);
		songs = songs2;
		this.wrapper = wrapper;
		
		// TODO Auto-generated constructor stub
	}
//	public static ListSongAdapter getInstance() {
//		
//		if (instance == null) {
//			instance = new OnlineSongAdapter(MainActivity.getActivity().getApplicationContext(),
//					R.layout.tab_songs_view, songs);
//		}
//		return instance;
//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		if(v==null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.song_view, null);
		}
		
		
		setLayoutInfomation(position, v);
		return v;
	}

	/**Set layout infomation
	 * @param position positon of song in list
	 * @param v rootview
	 */
	private void setLayoutInfomation(int position, View v) {
		final Song song = songs.get(position);
		NetworkImageView avatar = configLayoutAvatar(v, song);
		configTitleSong(v, song);
		configSongDetail(v, song, avatar);
		configPopupMenu(v, song);
		
		notifyDataSetChanged();
	}

	/**
	 * Config Song detail like playback count, likes count
	 * @param v rootView
	 * @param song song want to get detail
	 * @param avatar avatar of song
	 */
	private void configSongDetail(View v, final Song song, NetworkImageView avatar) {

		/*
		 * Initial controller
		 */
		SoundCloudUserController userController = SoundCloudUserController
				.getInstance();
	
		
		/*
		 * Set song detail 
		 */
		RelativeLayout songDetail = (RelativeLayout) v.findViewById(R.id.song_info_field);
		songDetail.getLayoutParams().height = MusicPlayerMainActivity.screenHeight/20;
		
		/*
		 * Like this song
		 */
		final TextView likeCount = (TextView) v.findViewById(R.id.like_count_id);
		likeCount.setText(((OnlineSong) song).getLikeCountString());
		
		RelativeLayout likeCountLayout =  (RelativeLayout) v.findViewById(R.id.likes_count_field);
		likeCountLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (SoundCloudUserController.getInstance().likeSong(song)){
					notifyDataSetChanged();
					Toast.makeText(getContext(), "You liked this song", Toast.LENGTH_LONG).show();
					//new updateFavoriteCounts(song, likeCount).execute();
				}else{
					Toast.makeText(getContext(), "You need to log in to like this song", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		//ImageView likeIcon = (ImageView) v.findViewById(R.id.likes_count_img);
		/*
		 * if not login and not like 
		 */
//		if (userController.isLogin()){
//			//HttpResponse resp = wrapper.get(Request.to(ME_FAVORITE));
//			//String responseString = 
//			if (userController.isLiked(song.getSoundcloudId())){
//				likeIcon.setImageResource(R.drawable.like_button);
//			}else{
//				likeIcon.setImageResource(R.drawable.love);
//			}
//		}else{
//			likeIcon.setImageResource(R.drawable.like_button);
//		}
		
		
		
		TextView playBack = (TextView) v.findViewById(R.id.play_count_id);
		playBack.setText(((OnlineSong) song).getPlaybackCountString());
		
		
		/*
		 * Download Song 
		 */
		RelativeLayout download = (RelativeLayout) v.findViewById(R.id.download_field);
		download.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				new downloadSongBackground().execute(song);
			}
		});
		
		
		/*
		 * Share song 
		 */
		RelativeLayout share = (RelativeLayout) v.findViewById(R.id.share_field);
		share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				
				System.out.println ("SHARE");
				//MenuItem item = menu.findItem(R.id.menu_item_share);
				//mShareActionProvider = (ShareActionProvider) item.getActionProvider();
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				//if (song instanceof OnlineSong){
					String shareBody =  ((OnlineSong)song).getPermalinkUrl();
					String shareSubject =  ((OnlineSong)song).getTitle();
					System.out.println (shareBody);
					sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,shareSubject );
					sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareBody );
					MusicPlayerMainActivity.getActivity().startActivity(Intent.createChooser(sharingIntent, "Share via "));
				//}
				
				
				return;
			}
		});
		
		
	}

	/**
	 * config Title of Song like Title, Author, Gerne
	 * @param v RootView
	 * @param song the song want to get info
	 */
	private void configTitleSong(View v, Song song) {
		/*
		 * Set title
		 */
		TextView title = (TextView) v.findViewById(R.id.song_title);
		title.setText(song.getTitle());
		
		/*
		 * Set sub title
		 */
		TextView subtitle = (TextView) v.findViewById(R.id.song_subtitle);
		subtitle.setText(song.getArtist());
		
		/*
		 * Set gerne
		 */
		TextView gerne = (TextView) v.findViewById(R.id.song_gerne);
		if (song.getGenre() !=null){
			gerne.setText("#"+song.getGenre());
		}
	}
	
	/**
	 * Config popup Menu
	 * @param v
	 * @param song
	 */
	private void configPopupMenu(View v, final Song song){
		final ImageView menu = (ImageView) v.findViewById(R.id.song_menu);
		menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PopupMenu popup = new PopupMenu(MusicPlayerMainActivity.getActivity(), menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                    .inflate(R.menu.song_list_menu, popup.getMenu());
                

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    

					@Override
					public boolean onMenuItemClick(MenuItem arg0) {
						// TODO Auto-generated method stub
						switch (arg0.getItemId()) {
						case R.id.list_addQueue:
							MusicPlayerService.getInstance().addSongToQueue(song);
							break;
						case R.id.list_playNext:
							MusicPlayerService.getInstance().addToNext(song);
							break;
						case R.id.list_addToPlaylist:
							ArrayList<Song> songs = new ArrayList<Song>();
							songs.add(song);
							PlaylistAddingFragment playlistAddingFragment = new PlaylistAddingFragment(songs);
							playlistAddingFragment.show(MusicPlayerMainActivity.getActivity().getSupportFragmentManager(), "New Playlist");
							break;
						case R.id.list_delete:
							SongController.getInstance().deleteSong(song);
							break;
						default:
							break;
						}
						
						return false;
					}
                });

                popup.show(); //showing popup menu
			}
		});	
	}

	/**
	 * config avatar of Song
	 * @param v rootView
	 * @param song song want to get ava
	 * @return ImageView
	 */
	private NetworkImageView configLayoutAvatar(View v, Song song) {
		/**
		 * Set avatar for song
		 */
		NetworkImageView avatar = (NetworkImageView) v.findViewById(R.id.song_image);
		ImageLoader mImageLoader = AppController.getInstance().getImageLoader(); 
		avatar.setDefaultImageResId(R.drawable.ic_launcher);
		avatar.setMinimumHeight(MusicPlayerMainActivity.screenHeight/5);
		avatar.setMinimumWidth(MusicPlayerMainActivity.screenHeight/5);
		avatar.setMaxHeight(MusicPlayerMainActivity.screenHeight/5);
		avatar.setMaxWidth(MusicPlayerMainActivity.screenHeight/5);
		
		if (song.getArtworkUrl() != null){
			avatar.setImageUrl(song.getArtworkUrl(), mImageLoader);
		}
		notifyDataSetChanged();
		return avatar;
	}
	
	@Override
	public Song getItem(int position) {
		return songs.get(position);
	}
	@Override
	public int getCount() {
		return songs.size();
	}
	public ArrayList<Song> getSongs() {
		return songs;
	}
	
	/**
	 * Download song running in background
	 * @author LEBAO_000
	 *
	 */
	private class downloadSongBackground extends AsyncTask<Song, String, String>{

		String result= "";
		@Override
		
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			Toast.makeText(MusicPlayerMainActivity.getActivity(), "Start download...", Toast.LENGTH_LONG).show();
		}
		@Override
		protected String doInBackground(Song... params) {
			
			
			BroadcastReceiver receiver = new BroadcastReceiver() {
		        @Override
		        public void onReceive(Context context, Intent intent) {
		            String action = intent.getAction();
		            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
		                long downloadId = intent.getLongExtra(
		                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
		                Query query = new Query();
		                
		                query.setFilterById(enqueue);
		                Cursor c = dm.query(query);
		                if (c.moveToFirst()) {
		                    int columnIndex = c
		                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
		                    if (DownloadManager.STATUS_SUCCESSFUL == c
		                            .getInt(columnIndex)) {

		                    }
		                }
		            }
		        }

				
		    };
		    
		    dm = (DownloadManager)MusicPlayerMainActivity.getActivity(). getSystemService(MusicPlayerMainActivity.DOWNLOAD_SERVICE);
		    android.app.DownloadManager.Request request;

			try {
				request = new android.app.DownloadManager.Request(
				        Uri.parse(params[0].getLink()));
				request.setTitle(params[0].getTitle());
				
				//OutputStream output = new FileOutputStream(outputName);

				//android.app.DownloadManager.Request downloadManager =  new DownloadManager.Request(uri);
				request.setDestinationInExternalPublicDir(ROOT_DIRECTORY, params[0].getTitle() + ".mp3");
				request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				request.setMimeType("audio/mpeg");
				 enqueue = dm.enqueue(request);
				 result = "Download successfully";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			


		    
			
		

			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
		   
			
		    Intent i = new Intent();
		    i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
		    Toast.makeText(MusicPlayerMainActivity.getActivity(), result, Toast.LENGTH_LONG).show();
		   // MusicPlayerMainActivity.getActivity().startActivity(i);
			
		}
		
	}
	
	private void setShareIntent(Intent shareIntent){
		if (mShareActionProvider !=null){
			mShareActionProvider.setShareIntent(shareIntent);
		}
	}


}
