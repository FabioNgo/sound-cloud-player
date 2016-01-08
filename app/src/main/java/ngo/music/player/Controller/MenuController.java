package ngo.music.player.Controller;

import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;

import java.util.ArrayList;

import ngo.music.player.Model.Category;
import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.CategoryManager;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.ModelManager.QueueManager;
import ngo.music.player.R;
import ngo.music.player.View.MusicPlayerMainActivity;
import ngo.music.player.View.fragment.abstracts.CategoryAddingFragment;
import ngo.music.player.View.fragment.abstracts.ListItemsInCompositionListFragment;
import ngo.music.player.View.fragment.real.PlaylistAddingFragment;
import ngo.music.player.helper.Constants;

public class MenuController implements OnMenuItemClickListener, Constants.Models {
	private static MenuController instance = null;
	ArrayList<Song> songs;
	Category category;
	public MenuController(ArrayList<Song> songs) {
		// TODO Auto-generated constructor stub
		this.songs = songs;
	}

	public static MenuController getInstance(ArrayList<Song> songs,Category category) {
		if (instance == null) {
			instance = new MenuController(songs);
		} else {
			instance.songs = songs;
			instance.category = category;
		}

		return instance;
	}
	public static MenuController getInstance(ArrayList<Song> songs) {
		if (instance == null) {
			instance = new MenuController(songs);
		} else {
			instance.songs = songs;
			instance.category = null;
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
			((QueueManager)ModelManager.getInstance(QUEUE)).addSongsToCategory(songs);
			break;
		case R.id.list_playNext:
			MusicPlayerServiceController.getInstance().addToNext(songs.get(0));
			break;
		case R.id.list_addToPlaylist:
			addToPlaylist();
			break;
		case R.id.list_delete:
			ModelManager.getInstance(OFFLINE).remove(songs.get(0).getId());
			break;
		case R.id.queue_removeFromQueue:
			((QueueManager) ModelManager.getInstance(QUEUE)).removeSongFromCategory(songs.get(0));
			break;
		case R.id.queue_playNext:
			MusicPlayerServiceController.getInstance().addToNext(songs.get(0));
			break;
		case R.id.queue_addToPlaylist:
			addToPlaylist();
			break;
		case R.id.queue_delete:
			ModelManager.getInstance(OFFLINE).remove(songs.get(0).getId());
			break;
		case R.id.queue_clear:
			((QueueManager)ModelManager.getInstance(QUEUE)).clearQueue();
			break;
		case R.id.song_cat_remove:
			((CategoryManager) ModelManager.getInstance(category.getType()))
					.removeSongFromCategory(songs.get(0).getId(), category.getId());
			break;
		case R.id.song_cat_add:
			MenuController.getInstance(songs).addToPlaylist();
			break;
		case R.id.composition_list_item_shows:

			ListItemsInCompositionListFragment fragment = ListItemsInCompositionListFragment.getInstance(category.getType());
			fragment.setCategory(category);
			fragment.show(MusicPlayerMainActivity.getActivity().getSupportFragmentManager(), "Show songs in cate");
			break;
		case R.id.composition_list_item_delete:
			ModelManager.getInstance(category.getType()).remove(category.getId());
			break;
		case R.id.composition_list_item_add_to_queue:
			((QueueManager)ModelManager.getInstance(QUEUE)).addSongsToCategory(songs);

		default:
			break;
		}
		return false;
	}
}
