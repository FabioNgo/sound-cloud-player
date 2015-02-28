package ngo.music.soundcloudplayer.boundary.fragment.real;

import java.util.ArrayList;
import java.util.Random;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.QueueSongAdapter;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.NoRefreshListContentFragment;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class QueueFragment extends NoRefreshListContentFragment {
	

	

	@Override
	protected boolean hasLoadMore() {
		// TODO Auto-generated method stub
		return false;
	}

//	@Override
//	public void load() {
//		// TODO Auto-generated method stub
//		super.load();
//		if (toolbar == null)
//			return;
//		toolbar.setTitle("Playing Queue");
//		toolbar.setSubtitle(String.valueOf(MusicPlayerService.getInstance()
//				.getQueueSize()) + " songs");
//
//	}

	

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
					ArrayList<Song> songs = MusicPlayerService.getInstance()
							.getQueue();
					Random random = new Random(System.currentTimeMillis());
					int position = Math.abs(random.nextInt())
							% MusicPlayerService.getInstance().getQueueSize();
					MusicPlayerService.getInstance().playNewSong(position,
							category, songs);
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
