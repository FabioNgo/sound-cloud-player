package ngo.music.soundcloudplayer.Adapters;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.todddavies.components.progressbar.ProgressWheel;
import com.volley.api.AppController;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.boundary.OfflineSongsFragment;
import ngo.music.soundcloudplayer.controller.OfflineSongController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.UpdateUiFromServiceController;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.CircularImageView;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
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

public class QueueSongAdapter extends ArrayAdapter<Song> {
	private View v;
	
	public QueueSongAdapter(Context context, int resource) {
		super(context, resource);


		//songs = SongController.getInstance().getOfflineSongs();

		songs = MusicPlayerService.getInstance().getQueue();
		if(songs == null){
			songs = new ArrayList<Song>();
		}


	}

	public static QueueSongAdapter instance = null;
	private ArrayList<Song> songs;

	public static QueueSongAdapter getInstance() {

		if (instance == null) {
			instance = new QueueSongAdapter(MainActivity.getActivity()
					.getApplicationContext(), R.layout.list_view);
		}
		return instance;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.liteplayer, null);
			
		}

		setLayoutInformation(position, v);
		return v;
	}

	/**
	 * @param position
	 * @param v
	 */
	private void setLayoutInformation(int position, View v) {
		Song song = null;
		song = songs.get(position);
		/**
		 * Set avatar for song
		 */
		NetworkImageView avatar = (NetworkImageView) v.findViewById(R.id.lite_player_image);
//
		ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
		BasicFunctions.setImageViewSize(MainActivity.screenWidth/20, MainActivity.screenWidth/20, avatar);

		avatar.setImageUrl(song.getArtworkUrl(), mImageLoader);

		/*
		 * Set title
		 */
		TextView title = (TextView) v.findViewById(R.id.lite_player_title);
		title.setText(song.getTitle());

		/*
		 * Set sub title
		 */
		TextView subtitle = (TextView) v
				.findViewById(R.id.lite_player_subtitle);
		subtitle.setText(song.getArtist() + " | " + song.getAlbum());
		/**
		 * Set progress bar
		 */
		if (MusicPlayerService.getInstance().getCurrentSongId().compareTo(songs.get(position).getId())==0) {
			ProgressWheel progressWheel = (ProgressWheel) v
					.findViewById(R.id.lite_player_progress_bar);
			progressWheel.setVisibility(View.VISIBLE);
			if(MusicPlayerService.getInstance().isPlaying()){
				progressWheel.setBackgroundResource(R.drawable.ic_media_pause);
			}else{
				progressWheel.setBackgroundResource(R.drawable.ic_media_play);
			}
			UpdateUiFromServiceController.getInstance().addProgressBar(progressWheel);
		}else{
			ProgressWheel progressWheel = (ProgressWheel) v
					.findViewById(R.id.lite_player_progress_bar);
			progressWheel.setVisibility(View.INVISIBLE);
			UpdateUiFromServiceController.getInstance().removeProgressBar(progressWheel);
			
		}
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
	public ArrayList<String> getSongIds() {
		ArrayList<String> result = new ArrayList<String>();
		for (Song song : songs) {
			result.add(song.getId());
		}
		return result;
		
	}
	public void updateQueue(){
		this.songs.clear();
		this.songs.addAll(MusicPlayerService.getInstance().getQueue());
		return;
	}
	
}
