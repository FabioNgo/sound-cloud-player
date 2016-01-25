package ngo.music.player.adapters;

import java.util.ArrayList;
import java.util.Observable;

import ngo.music.player.Controller.MusicPlayerServiceController;
import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.ModelManager.QueueManager;
import ngo.music.player.R;

public class QueueSongAdapter extends LiteListSongAdapter {


	public static QueueSongAdapter instance = null;



	@Override
	protected int setType() {
		return QUEUE;
	}

	public static QueueSongAdapter getInstance() {

		if (instance == null) {
			instance = new QueueSongAdapter();
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
