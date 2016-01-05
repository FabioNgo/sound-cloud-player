package ngo.music.player.adapters;

import android.content.Context;

import java.util.ArrayList;
import java.util.Observable;

import ngo.music.player.Controller.MusicPlayerServiceController;
import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.ModelManager.QueueManager;
import ngo.music.player.R;
import ngo.music.player.View.MusicPlayerMainActivity;
import ngo.music.player.service.MusicPlayerService;

public class QueueSongAdapter extends LiteListSongAdapter {


	public static QueueSongAdapter instance = null;

	public QueueSongAdapter(Context context, int resource) {
		super(context, resource);
		MusicPlayerServiceController.getInstance().addObserver(this);
		ModelManager.getInstance(QUEUE).addObserver(this);
		ModelManager a = ModelManager.getInstance(QUEUE);
	}

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

		return ((QueueManager) ModelManager.getInstance(QUEUE)).getAllSong();


	}

	@Override
	public void update(Observable observable, Object data) {
		super.update(observable,data);
		if(observable instanceof MusicPlayerServiceController){
			if(data instanceof  Song){
				notifyDataSetChanged();
			}
		}
	}

	@Override
	public int getSongMenuId() {
		// TODO Auto-generated method stub
		return R.menu.song_queue_menu;
	}

	@Override
	protected ArrayList<Song> getSongsFromData(Object data) {
		return ((QueueManager)ModelManager.getInstance(QUEUE)).getAllSong();
	}
}
