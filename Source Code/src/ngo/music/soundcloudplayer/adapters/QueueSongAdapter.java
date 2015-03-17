package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.MenuController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Context;
import android.view.MenuItem;

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
