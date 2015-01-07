package ngo.music.soundcloudplayer.boundary;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.Adapters.QueueSongAdapter;
import ngo.music.soundcloudplayer.controller.ListViewOnItemClickHandler;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class QueueSongUI extends ListContentFragment {
	public static QueueSongUI instance = null;
	Toolbar toolbar;
	View rootView;
	ListView queueView;

	private QueueSongUI() {
		// TODO Auto-generated constructor stub
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
		toolbar.inflateMenu(R.menu.global);
		queueView = (ListView) rootView.findViewById(R.id.queue_list_view);
		UIController.getInstance().addListContentFragements(instance);
		return rootView;
	}

	/**
	 * Load view items on Fragment
	 */
	@Override
	public void load(boolean isFirstLoad) {
		// TODO Auto-generated method stub

		toolbar.setTitle("Title");
		toolbar.setSubtitle("subtitle");

		final QueueSongAdapter adapter = QueueSongAdapter.getInstance();
		if (isFirstLoad) {
			UIController.getInstance().addAdapter(adapter);
			queueView.setAdapter(adapter);

		}
		queueView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub

				OfflineSongAdapter.getInstance().notifyDataSetChanged();

				ArrayList<Song> songs = adapter.getSongs();

				MusicPlayerService.getInstance().playSongInQueue(position);

			}

		});

	}
}
