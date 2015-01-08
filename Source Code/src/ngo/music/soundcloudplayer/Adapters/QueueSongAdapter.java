package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.todddavies.components.progressbar.ProgressWheel;
import com.volley.api.AppController;

public class QueueSongAdapter extends ArrayAdapter<Song> {
	private View v;
	private boolean isdatachanged = true;
	public QueueSongAdapter(Context context, int resource) {
		super(context, resource);
		songs = MusicPlayerService.getInstance().getQueue();
		if (songs == null) {
			songs = new ArrayList<Song>();
		}

	}

	public static QueueSongAdapter instance = null;
	private ArrayList<Song> songs;

	public static QueueSongAdapter getInstance() {

		if (instance == null) {
			instance = new QueueSongAdapter(MusicPlayerMainActivity.getActivity()
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
		NetworkImageView avatar = (NetworkImageView) v
				.findViewById(R.id.lite_player_image);
		//
		ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
		BasicFunctions.setImageViewSize(MusicPlayerMainActivity.screenWidth / 20,
				MusicPlayerMainActivity.screenWidth / 20, avatar);

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
		String songPlayingId = MusicPlayerService.getInstance()
				.getCurrentSongId();

		if (songPlayingId.equals(songs.get(position).getId())) {
			ProgressWheel progressWheel = (ProgressWheel) v
					.findViewById(R.id.lite_player_progress_bar);
			progressWheel.setVisibility(View.VISIBLE);
			if (MusicPlayerService.getInstance().isPlaying()) {
				progressWheel.setBackgroundResource(R.drawable.ic_media_pause);
			} else {
				progressWheel.setBackgroundResource(R.drawable.ic_media_play);
			}
			UIController.getInstance().addProgressBar(progressWheel);
		} else {
			ProgressWheel progressWheel = (ProgressWheel) v
					.findViewById(R.id.lite_player_progress_bar);
			progressWheel.setVisibility(View.INVISIBLE);
			UIController.getInstance().removeProgressBar(progressWheel);

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

	public void updateQueue() {
		
		songs = MusicPlayerService.getInstance().getQueue();
//		updateDataChanged(true);
		return;
	}
	public void updateDataChanged(boolean input){
		isdatachanged = input;
	}
	public boolean isDataChanged(){
		return isdatachanged;
	}
}
