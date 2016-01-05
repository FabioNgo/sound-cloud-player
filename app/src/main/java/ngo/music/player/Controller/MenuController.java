package ngo.music.player.Controller;

import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;

import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.ModelManager.QueueManager;
import ngo.music.player.R;
import ngo.music.player.View.MusicPlayerMainActivity;
import ngo.music.player.View.fragment.abstracts.CategoryAddingFragment;
import ngo.music.player.View.fragment.real.PlaylistAddingFragment;
import ngo.music.player.helper.Constants;

public class MenuController implements OnMenuItemClickListener, Constants.Models {
	private static MenuController instance = null;
	Song[] songs;

	public MenuController(Song[] songs) {
		// TODO Auto-generated constructor stub
		this.songs = songs;
	}

	public static MenuController getInstance(Song[] songs) {
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
			((QueueManager)ModelManager.getInstance(QUEUE)).addSongToCategory(songs[0]);
			break;
		case R.id.list_playNext:
			MusicPlayerServiceController.getInstance().addToNext(songs[0]);
			break;
		case R.id.list_addToPlaylist:
			addToPlaylist();
			break;
		case R.id.list_delete:
			ModelManager.getInstance(OFFLINE).remove(songs[0].getId());
			break;
		case R.id.queue_removeFromQueue:
			((QueueManager) ModelManager.getInstance(QUEUE)).removeSongFromCategory(songs[0]);
			break;
		case R.id.queue_playNext:
			MusicPlayerServiceController.getInstance().addToNext(songs[0]);
			break;
		case R.id.queue_addToPlaylist:
			addToPlaylist();
			break;
		case R.id.queue_delete:
			ModelManager.getInstance(OFFLINE).remove(songs[0].getId());
			break;
		case R.id.queue_clear:
			((QueueManager)ModelManager.getInstance(QUEUE)).clearQueue();
			break;
		default:
			break;
		}
		return false;
	}
}
