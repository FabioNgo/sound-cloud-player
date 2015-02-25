package ngo.music.soundcloudplayer.boundary.fragment.real;

import java.util.ArrayList;
import java.util.Random;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.ViewHolder.CompositionViewHolder;
import ngo.music.soundcloudplayer.ViewHolder.SongInListViewHolder;
import ngo.music.soundcloudplayer.adapters.CategoryListAdapter;
import ngo.music.soundcloudplayer.adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.adapters.QueueSongAdapter;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.ListContentFragment;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.NoRefreshListContentFragment;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class QueueFragment extends NoRefreshListContentFragment {
	public static QueueFragment instance = null;

	public static QueueFragment getInstance() {
		// TODO Auto-generated method stub
		if (instance == null) {
			instance = new QueueFragment();
		}
		return instance;
	}

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
