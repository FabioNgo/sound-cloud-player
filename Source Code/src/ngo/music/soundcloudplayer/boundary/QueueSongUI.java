package ngo.music.soundcloudplayer.boundary;

import java.util.ArrayList;
import java.util.Random;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.ViewHolder.CompositionViewHolder;
import ngo.music.soundcloudplayer.ViewHolder.SongInQueueViewHolder;
import ngo.music.soundcloudplayer.adapters.CategoryListAdapter;
import ngo.music.soundcloudplayer.adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.adapters.QueueSongAdapter;
import ngo.music.soundcloudplayer.boundary.fragments.ListContentFragment;
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
import android.widget.ListView;

public class QueueSongUI extends ListContentFragment {
	public static QueueSongUI instance = null;
	Toolbar toolbar;
	View rootView;

	public QueueSongUI() {
		// TODO Auto-generated constructor stub
		instance = this;

	}

	public static QueueSongUI getInstance() {
		// TODO Auto-generated method stub
		if (instance == null) {
			instance = new QueueSongUI();
		}
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.play_queue, container, false);
		toolbar = (Toolbar) rootView.findViewById(R.id.queue_toolbar);
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
							songs);
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
		listView = (ListView) rootView.findViewById(R.id.queue_list_view);
		UIController.getInstance().addListContentFragements(instance);
		return rootView;
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		adapter = QueueSongAdapter.getInstance();
		if (toolbar == null)
			return;
		toolbar.setTitle("Playing Queue");
		toolbar.setSubtitle(String.valueOf(MusicPlayerService.getInstance()
				.getQueueSize()) + " songs");

		UIController.getInstance().addAdapter(adapter);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub

				MusicPlayerService.getInstance().playSongInQueue(position);

			}

		});

	}

	public void update() {
		QueueSongAdapter adapter = (QueueSongAdapter) listView.getAdapter();
		toolbar.setSubtitle(String.valueOf(MusicPlayerService.getInstance()
				.getQueueSize()) + " songs");
		adapter.notifyDataSetChanged();
		for (int i = 0; i <= listView.getLastVisiblePosition()
				- listView.getFirstVisiblePosition(); i++) {
			View v = listView.getChildAt(i);
			if (v != null) {

				SongInQueueViewHolder holder = (SongInQueueViewHolder) v
						.getTag();
				try {
					
					adapter.setLayoutInformation(i + listView.getFirstVisiblePosition(), holder, v);
				} catch (IndexOutOfBoundsException e) {
					/**
					 * When this exception occur, some item has been deleted in
					 * list.
					 */
					break;

				}
			}
		}
		
	}
	
	

}
