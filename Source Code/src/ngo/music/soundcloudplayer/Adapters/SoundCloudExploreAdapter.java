package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.volley.api.AppController;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.CircularImageView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

	/**
	 * @param position
	 * @param v
	 */
	private void setLayoutInfomation(int position, View v) {
		Song song = songs.get(position);
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
		
		
		/*
		 * Set title
		 */
		TextView title = (TextView) v.findViewById(R.id.song_title);
		title.setText(song.getTitle());
		
		/*
		 * Set sub title
		 */
		TextView subtitle = (TextView) v.findViewById(R.id.song_subtitle);
		subtitle.setText(song.getAuthor()+" | "+ song.getPlaybackCountString());
		
		//notifyDataSetChanged();
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
