package ngo.music.player.adapters;

import android.content.Context;

import java.util.ArrayList;

import ngo.music.player.boundary.MusicPlayerMainActivity;
import ngo.music.player.entity.Song;
import ngo.music.player.service.MusicPlayerService;
import ngo.music.player.R;

public class QueueSongAdapter extends LiteListSongAdapter {

	

	public QueueSongAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}

	public static QueueSongAdapter instance = null;

	public static QueueSongAdapter getInstance() {

		if (instance == null) {
			instance = new QueueSongAdapter(MusicPlayerMainActivity
					.getActivity().getApplicationContext(),
					R.layout.song_in_list);
		}
		return instance;
	}

	@Override
	public ArrayList<Song> getSongs() {
		return MusicPlayerService.getInstance().getQueue();
	}

	@Override
	public int getSongMenuId() {
		// TODO Auto-generated method stub
		return R.menu.song_queue_menu;
	}



}
