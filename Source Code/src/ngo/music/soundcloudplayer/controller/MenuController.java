package ngo.music.soundcloudplayer.controller;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.CategoryAddingFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.PlaylistAddingFragment;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

public class MenuController implements OnMenuItemClickListener {
	private static MenuController instance = null;
	ArrayList<Song> songs;

	public MenuController(ArrayList<Song> songs) {
		// TODO Auto-generated constructor stub
		this.songs = songs;
	}

	public static MenuController getInstance(ArrayList<Song> songs) {
		if (instance == null) {
			instance = new MenuController(songs);
		} else {
			instance.songs = songs;
		}

		return instance;
	}

	public void addToPlaylist() {
		CategoryAddingFragment playlistAddingFragment = new PlaylistAddingFragment(
				songs);
		playlistAddingFragment.show(MusicPlayerMainActivity.getActivity()
				.getSupportFragmentManager(), "New Playlist");
	}

	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getItemId()) {
		case R.id.list_addQueue:
			MusicPlayerService.getInstance().addSongToQueue(songs.get(0));
			break;
		case R.id.list_playNext:
			MusicPlayerService.getInstance().addToNext(songs.get(0));
			break;
		case R.id.list_addToPlaylist:
			addToPlaylist();
			break;
		case R.id.list_delete:
			SongController.getInstance().deleteSong(songs.get(0));
			break;
		case R.id.queue_removeFromQueue:
			MusicPlayerService.getInstance()
					.removeFromQueue(songs.get(0), true);
			break;
		case R.id.queue_playNext:
			MusicPlayerService.getInstance().addToNext(songs.get(0));
			break;
		case R.id.queue_addToPlaylist:

			addToPlaylist();
			break;
		case R.id.queue_delete:
			SongController.getInstance().deleteSong(songs.get(0));
			break;
			
		default:
			break;
		}
		return false;
	}
}
