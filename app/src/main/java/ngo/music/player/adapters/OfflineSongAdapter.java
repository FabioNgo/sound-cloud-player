package ngo.music.player.adapters;

import android.content.Context;

import java.util.ArrayList;

import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.R;
import ngo.music.player.View.MusicPlayerMainActivity;

public class OfflineSongAdapter extends LiteListSongAdapter {
	
	public static OfflineSongAdapter instance = null;

	public OfflineSongAdapter(Context context, int resource) {
		super(context, resource);
	}

	@Override
	protected Song[] getSongsFromData(Object data) {
		ArrayList<Song> data1 = (ArrayList<Song>) data;
		Song[] output = new Song[data1.size()];
		data1.toArray(output);
		return output;
	}

	public static OfflineSongAdapter getInstance() {

		if (instance == null) {
			instance = createNewInstance();
		}
		return instance;
	}

	public static OfflineSongAdapter createNewInstance() {
		// TODO Auto-generated method stub
		instance = new OfflineSongAdapter(MusicPlayerMainActivity.getActivity()
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
	public Song[] getSongs() {
		// TODO Auto-generated method stub
		return (Song[]) ModelManager.getInstance(OFFLINE).getAll();
	}
	@Override
	public int getSongMenuId() {
		// TODO Auto-generated method stub
		return R.menu.song_list_menu;
	}
	
	
}
