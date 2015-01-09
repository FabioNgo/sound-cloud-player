package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
					.getApplicationContext(), R.layout.song_in_queue);
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
			v = inflater.inflate(R.layout.song_in_queue, null);

		}

		setLayoutInformation(position, v);
		return v;
	}

	/**
	 * @param position
	 * @param v
	 */
	private void setLayoutInformation(int position, View v) {
		
		final Song song = songs.get(position);
		/**
		 * Set avatar for song
		 */
		NetworkImageView avatar = (NetworkImageView) v
				.findViewById(R.id.song_queue_image);
		//
		ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
		BasicFunctions.setImageViewSize(MusicPlayerMainActivity.screenWidth / 20,
				MusicPlayerMainActivity.screenWidth / 20, avatar);

		avatar.setImageUrl(song.getArtworkUrl(), mImageLoader);
		/**
		 * Button
		 */
		final ImageView menu = (ImageView) v.findViewById(R.id.song_queue_menu);
		menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PopupMenu popup = new PopupMenu(MusicPlayerMainActivity.getActivity(), menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                    .inflate(R.menu.song_queue_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    

					@Override
					public boolean onMenuItemClick(MenuItem arg0) {
						// TODO Auto-generated method stub
						switch (arg0.getItemId()) {
						case R.id.queue_removeFromQueue:
							MusicPlayerService.getInstance().removeFromQueue(song);
							break;
						case R.id.queue_playNext:
							MusicPlayerService.getInstance().addToNext(song);
							break;
						case R.id.queue_addToPlaylist:
							break;
						case R.id.queue_delete:
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
		/*
		 * Set title
		 */
		TextView title = (TextView) v.findViewById(R.id.song_queue_title);
		title.setText(song.getTitle());

		/*
		 * Set sub title
		 */
		TextView subtitle = (TextView) v
				.findViewById(R.id.song_queue_subtitle);
		subtitle.setText(song.getArtist() + " | " + song.getAlbum());
		/**
		 * Set progress bar
		 */
		String songPlayingId = MusicPlayerService.getInstance()
				.getCurrentSongId();

		if (songPlayingId.equals(songs.get(position).getId())) {
			ProgressWheel progressWheel = (ProgressWheel) v
					.findViewById(R.id.song_queue_progress_bar);
			progressWheel.setVisibility(View.VISIBLE);
			if (MusicPlayerService.getInstance().isPlaying()) {
				progressWheel.setBackgroundResource(R.drawable.ic_media_pause);
			} else {
				progressWheel.setBackgroundResource(R.drawable.ic_media_play);
			}
			UIController.getInstance().addProgressBar(progressWheel);
		} else {
			ProgressWheel progressWheel = (ProgressWheel) v
					.findViewById(R.id.song_queue_progress_bar);
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
