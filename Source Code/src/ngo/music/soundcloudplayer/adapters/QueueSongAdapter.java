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
		songs = getSongs();

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
	public boolean onMenuItemClick(MenuItem arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getItemId()) {
		case R.id.queue_removeFromQueue:
			MusicPlayerService.getInstance().removeFromQueue(song, true);
			break;
		case R.id.queue_playNext:
			MusicPlayerService.getInstance().addToNext(song);
			break;
		case R.id.queue_addToPlaylist:
			ArrayList<Song> songs = new ArrayList<Song>();
			songs.add(song);
			MenuController.getInstance().addToPlaylist(songs);
			break;
		case R.id.queue_delete:
			SongController.getInstance().deleteSong(song);
			break;
		default:
			break;
		}

		return false;
	}

}
