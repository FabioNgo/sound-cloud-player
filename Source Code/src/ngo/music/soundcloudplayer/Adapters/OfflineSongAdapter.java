package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayDeque;
import java.util.ArrayList;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OfflineSongAdapter extends ArrayAdapter<Song> {
	private OfflineSongAdapter(Context context, int resource) {
		super(context, resource);

		songs = SongController.getInstance().getOfflineSongs();

	}

	public static OfflineSongAdapter instance = null;
	private ArrayList<Song> songs;

	public static OfflineSongAdapter getInstance() {
		
		if (instance == null) {
			instance = new OfflineSongAdapter(MainActivity.getActivity().getApplicationContext(),
					R.layout.tab_songs_view);
		}
		return instance;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		if(v==null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater
					.inflate(R.layout.song_view, null);
		}
		
		setLayoutInformation(position, v);
		return v;
	}

	/**
	 * @param position
	 * @param v
	 */
	private void setLayoutInformation(int position, View v) {
		Song song = songs.get(position);
		/**
		 * Set avatar for song
		 */
		NetworkImageView avatar = (NetworkImageView) v.findViewById(R.id.song_image);

		ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
		avatar.setMinimumHeight(MainActivity.screenHeight/5);
		avatar.setMinimumWidth(MainActivity.screenHeight/5);

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
		subtitle.setText(song.getArtist()+" | "+song.getAlbum());
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
