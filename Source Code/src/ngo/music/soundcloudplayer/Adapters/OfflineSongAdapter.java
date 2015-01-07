package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.controller.OfflineSongController;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.todddavies.components.progressbar.ProgressWheel;

public class OfflineSongAdapter extends ArrayAdapter<Song> {
	private View v;
	
	Context context;
	int resource;
	public OfflineSongAdapter(Context context, int resource) {
		super(context, resource);

		songs = OfflineSongController.getInstance().getSongs(false);
		this.context = context;
		this.resource = resource;
		
	}

	public static OfflineSongAdapter instance = null;
	private ArrayList<OfflineSong> songs;

	public static OfflineSongAdapter getInstance() {

		if (instance == null) {
			instance = createNewInstance();
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
		// ImageView avatar = (ImageView)
		// v.findViewById(R.id.lite_player_image);
		//
		// ImageLoader mImageLoader =
		// AppController.getInstance().getImageLoader();
		// avatar.setMinimumHeight(MainActivity.screenHeight/5);
		// avatar.setMinimumWidth(MainActivity.screenHeight/5);

		// avatar.setImageUrl(song.getArtworkUrl(), mImageLoader);

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
		if (MusicPlayerService.getInstance().getCurrentSongId()
				.compareTo(songs.get(position).getId()) == 0) {
			ProgressWheel progressWheel = (ProgressWheel) v
					.findViewById(R.id.lite_player_progress_bar);
			progressWheel.setVisibility(View.VISIBLE);
			if (MusicPlayerService.getInstance().isPlaying()) {
				progressWheel.setBackgroundResource(R.drawable.ic_media_pause);
			} else {
				progressWheel.setBackgroundResource(R.drawable.ic_media_play);
			}
			// UpdateUiFromServiceController.getInstance().addProgressBar(progressWheel);
		} else {
			ProgressWheel progressWheel = (ProgressWheel) v
					.findViewById(R.id.lite_player_progress_bar);
			progressWheel.setVisibility(View.INVISIBLE);
			// UpdateUiFromServiceController.getInstance().removeProgressBar(progressWheel);

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

	public ArrayList<OfflineSong> getSongs() {
		return songs;

	}

	public ArrayList<String> getSongIds() {
		ArrayList<String> result = new ArrayList<String>();
		for (Song song : songs) {
			result.add(song.getId());
		}
		return result;

	}
	

	public static OfflineSongAdapter createNewInstance() {
		// TODO Auto-generated method stub
		instance = new OfflineSongAdapter(MainActivity.getActivity()
				.getApplicationContext(), R.layout.list_view);
		return instance;
	}
	@Override
	public void add(Song song) {
		// TODO Auto-generated method stub
		if(!(song instanceof OfflineSong)){
			
		}else{
			songs.add((OfflineSong) song);	
		}
		
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		songs.clear();
	}
}
