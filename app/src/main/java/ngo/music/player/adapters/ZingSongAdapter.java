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

	public ZingSongAdapter(Context context, int resource) {
		super(context, resource);
		MusicPlayerServiceController.getInstance().addObserver(this);
		ModelManager.getInstance(ZING).addObserver(this);
	}

	@Override
	protected ArrayList<Song> getSongsFromData(Object data) {
		return (ArrayList<Song>) data;
	}

	public static ZingSongAdapter getInstance() {

		if (instance == null) {
			instance = createNewInstance();
		}
		return instance;
	}

	public static ZingSongAdapter createNewInstance() {
		// TODO Auto-generated method stub
		instance = new ZingSongAdapter(MusicPlayerMainActivity.getActivity()
				.getApplicationContext(), R.layout.list_view);
		return instance;
	}

	@Override
	public void add(Song song) {
		// TODO Auto-generated method stub
//		if (!(song instanceof OfflineSong)) {
//
//		} else {
//			songs.add(song);
//		}

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
	public ArrayList<Song> getSongs() {
		// TODO Auto-generated method stub
		ArrayList<Model> temp = ModelManager.getInstance(ZING).getAll();
		//System.out.println ("LIST MODEL SIZE = " + temp.size());
		ArrayList<Song> output = new ArrayList<>();
		for (Model model:temp) {
			output.add((Song) model);
		}
		return output;
	}
	@Override
	public int getSongMenuId() {
		// TODO Auto-generated method stub
		return R.menu.song_list_menu;
	}
	
	
}
