package ngo.music.soundcloudplayer.Adapters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.volley.api.AppController;













import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Http;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.UserLoginActivity;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import ngo.music.soundcloudplayer.entity.OnlineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.CircularImageView;
import ngo.music.soundcloudplayer.general.Constants;
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
import android.view.LayoutInflater;
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
	
	protected ListSongAdapter(Context context, int resource, ArrayList<Song> onlineSongs, ApiWrapper wrapper) {
		super(context, resource);
		songs = onlineSongs;
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
		Song song = songs.get(position);
		NetworkImageView avatar = configLayoutAvatar(v, song);
		configTitleSong(v, song);
		configSongDetail(v, song, avatar);
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
				// TODO Auto-generated method stub
//				SongController songController = SongController.getInstance();
//				try {
//					songController.downloadSong(song);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
					
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

		                       // ImageView view = (ImageView) findViewById(R.id.imageView1);
//		                        String uriString = c
//		                                .getString(c
//		                                        .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
		                        //view.setImageURI(Uri.parse(uriString));
		                    }
		                }
		            }
		        }

				
		    };
		    
		    dm = (DownloadManager)MusicPlayerMainActivity.getActivity(). getSystemService(MusicPlayerMainActivity.DOWNLOAD_SERVICE);
		    android.app.DownloadManager.Request request;
			request = new android.app.DownloadManager.Request(
			        Uri.parse(params[0].getLink()));
			request.setTitle(params[0].getTitle() + ".mp3");
			File dir = new File(Environment.getExternalStorageDirectory()
					+ ROOT_DIRECTORY);
			if (!(dir.exists() && dir.isDirectory())) {
				System.out.println("CREATE FOLDER: " + dir.mkdir());

			}
			String outputName = dir + "/" + params[0].getTitle() + ".mp3";
			//OutputStream output = new FileOutputStream(outputName);

			//android.app.DownloadManager.Request downloadManager =  new DownloadManager.Request(uri);
			request.setDestinationInExternalPublicDir(ROOT_DIRECTORY, params[0].getTitle() + ".mp3");
			request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			request.setMimeType("audio/mpeg");
			 enqueue = dm.enqueue(request);
			 result = "Download successfully";
		    
		    
			
		

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


}
