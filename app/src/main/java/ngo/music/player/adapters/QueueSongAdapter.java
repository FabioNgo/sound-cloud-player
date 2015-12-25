package ngo.music.player.adapters;

import android.content.Context;

import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.ModelManager.QueueManager;
import ngo.music.player.R;
import ngo.music.player.View.MusicPlayerMainActivity;

public class QueueSongAdapter extends LiteListSongAdapter {


	public static QueueSongAdapter instance = null;

	public QueueSongAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		ModelManager a = ModelManager.getInstance(QUEUE);
	}

	public static QueueSongAdapter getInstance() {

		if (instance == null) {
			instance = new QueueSongAdapter(MusicPlayerMainActivity
					.getActivity().getApplicationContext(),
					R.layout.song_in_list);
		}
		return instance;
	}

	@Override
	public Song[] getSongs() {

		return ((QueueManager) ModelManager.getInstance(QUEUE)).getAllSong();


	}

	@Override
	public int getSongMenuId() {
		// TODO Auto-generated method stub
		return R.menu.song_queue_menu;
	}

	@Override
	protected Song[] getSongsFromData(Object data) {
		return ((QueueManager)ModelManager.getInstance(QUEUE)).getAllSong();
	}
}
