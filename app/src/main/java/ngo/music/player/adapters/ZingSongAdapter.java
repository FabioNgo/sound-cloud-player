package ngo.music.player.adapters;

import android.content.Context;

import java.util.ArrayList;
import java.util.Observable;

import ngo.music.player.Controller.MusicPlayerServiceController;
import ngo.music.player.Model.Model;
import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.R;
import ngo.music.player.View.MusicPlayerMainActivity;

public class ZingSongAdapter extends LiteListSongAdapter {

	public static ZingSongAdapter instance = null;


	@Override
	protected ArrayList<Song> getSongsFromData(Object data) {
		return (ArrayList<Song>) data;
	}

	public static ZingSongAdapter getInstance() {

		if (instance == null) {
			instance = new ZingSongAdapter();
		}
		return instance;
	}

	@Override
	protected int setType() {
		return ZING;
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
		return R.menu.song_list_menu;
	}
	
	
}
