package ngo.music.soundcloudplayer.Adapters;

import java.io.IOException;
import java.util.ArrayList;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.volley.api.AppController;











import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.LoginActivity;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.CircularImageView;
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

public class SoundCloudExploreAdapter extends ArrayAdapter<Song> {
	
	public SoundCloudExploreAdapter(Context context, int resource, ArrayList<Song> onlineSongs) {
		super(context, resource);

		songs = onlineSongs;

	}

	public static SoundCloudExploreAdapter instance = null;
	private ArrayList<Song> songs;

//	public static OnlineSongAdapter getInstance() {
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
		 * Set song detail 
		 */
		RelativeLayout songDetail = (RelativeLayout) v.findViewById(R.id.song_info_field);
		songDetail.getLayoutParams().height = MainActivity.screenHeight/20;
		
		TextView likeCount = (TextView) v.findViewById(R.id.like_count_id);
		likeCount.setText(song.getLikeCountString());
		
		
		TextView playBack = (TextView) v.findViewById(R.id.play_count_id);
		playBack.setText(song.getPlaybackCountString());
		
		ImageView download = (ImageView) v.findViewById(R.id.download_count_img);
		
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
		gerne.setText(song.getGerne());
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
		avatar.setImageUrl(song.getArtworkUrl(), mImageLoader);
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
	
	
	

}
