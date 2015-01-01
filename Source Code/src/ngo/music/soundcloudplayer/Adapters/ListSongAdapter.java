package ngo.music.soundcloudplayer.Adapters;

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
import ngo.music.soundcloudplayer.boundary.LoginActivity;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.CircularImageView;
import ngo.music.soundcloudplayer.general.Constants;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
		songDetail.getLayoutParams().height = MainActivity.screenHeight/20;
		
		/*
		 * Like this song
		 */
		final TextView likeCount = (TextView) v.findViewById(R.id.like_count_id);
		likeCount.setText(song.getLikeCountString());
		
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
		
		ImageView likeIcon = (ImageView) v.findViewById(R.id.likes_count_img);
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
		playBack.setText(song.getPlaybackCountString());
		
		
		/*
		 * Download Song 
		 */
		RelativeLayout download = (RelativeLayout) v.findViewById(R.id.download_field);
		download.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SongController songController = SongController.getInstance();
				try {
					songController.downloadSong(song);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//new downloadSongFromSoundCloud(song).execute();
				
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
		subtitle.setText(song.getAuthor());
		
		/*
		 * Set gerne
		 */
		TextView gerne = (TextView) v.findViewById(R.id.song_gerne);
		if (song.getGerne() !=null){
			gerne.setText("#"+song.getGerne());
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
		avatar.setMinimumHeight(MainActivity.screenHeight/5);
		avatar.setMinimumWidth(MainActivity.screenHeight/5);
		avatar.setMaxHeight(MainActivity.screenHeight/5);
		avatar.setMaxWidth(MainActivity.screenHeight/5);
		
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
	
	private class updateFavoriteCounts extends AsyncTask<String, String, String>{

		
		private Song song;
		private TextView likeCount;
		private JSONObject me;
		public updateFavoriteCounts(Song song, TextView likeCount) {
			this.song = song;
			this.likeCount = likeCount;
			// TODO Auto-generated constructor stub
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			/*
			 * Update count
			 */
			
//			SoundCloudUserController userController = SoundCloudUserController
//					.getInstance();
//			Token token = userController.getToken();
//			ApiWrapper wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null,
//					token);

			/*
			 * API URL OF THE SONG
			 */
			String uri = TRACK_LINK + String.valueOf(song.getSoundcloudId());

			try {
				HttpResponse resp = wrapper.get(Request.to(uri));
				me = Http.getJSON(resp);
				// set information of logged user
				

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			try {
				song.setFavoriteCount(me.getInt(SongConstants.FOVORITINGS_COUNT));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println ("New like count = " + song.getLikeCountString());
			likeCount.setText(song.getLikeCountString());
			
		}
		
	}
	

}
