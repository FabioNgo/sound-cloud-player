package ngo.music.player.boundary.fragment.real;

import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.CategoryManager;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.R;
import ngo.music.player.adapters.QueueSongAdapter;
import ngo.music.player.boundary.fragment.abstracts.NoRefreshListContentFragment;
import ngo.music.player.service.MusicPlayerService;

public class QueueFragment extends NoRefreshListContentFragment {
	

	

	@Override
	protected boolean hasLoadMore() {
		// TODO Auto-generated method stub
		return false;
	}


	

	@Override
	protected ArrayAdapter<?> getAdapter() {
		// TODO Auto-generated method stub
		return QueueSongAdapter.getInstance();
	}

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return QUEUE;
	}

	@Override
	protected boolean hasToolbar() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void setUpToolBar(Toolbar toolbar) {
		// TODO Auto-generated method stub
		toolbar.setLogo(R.drawable.logo);
		toolbar.setTitle("Queue");
		toolbar.inflateMenu(R.menu.queue_menu);
		toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
					case R.id.queue_shuffle_all:
						JSONObject[] array = ((CategoryManager) ModelManager.getInstance(QUEUE)).getSongsFromCategory("queue");
						Song[] songs = new Song[array.length];
						for (int i = 0; i < songs.length; i++) {
							try {
								songs[i] = (Song) ModelManager.getInstance(OFFLINE).get(array[i].getString("id"));
							} catch (JSONException e) {
								continue;
							}
						}
						Random random = new Random(System.currentTimeMillis());
						int position = Math.abs(random.nextInt())
								% MusicPlayerService.getInstance().getQueueSize();
						MusicPlayerService.getInstance().playNewSong(position, songs);
						if (!MusicPlayerService.getInstance().isShuffle()) {
							MusicPlayerService.getInstance().setShuffle();
						}
						break;

					case R.id.queue_clear:
						MusicPlayerService.getInstance().clearQueue();
						break;

					case R.id.queue_settings:
						break;
					default:
						break;
				}
				return false;
			}
		});
	}

	@Override
	protected void updateToolbar(Toolbar toolbar) {
		// TODO Auto-generated method stub
		toolbar.setSubtitle(String.valueOf(MusicPlayerService.getInstance()
				.getQueueSize()) + " songs");
	}

	@Override
	protected void setUpLoadMore() {
		// TODO Auto-generated method stub
		
	}


}
