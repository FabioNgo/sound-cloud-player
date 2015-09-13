package ngo.music.player.adapters;

import android.content.Context;

import java.util.ArrayList;

import ngo.music.player.boundary.MusicPlayerMainActivity;
import ngo.music.player.controller.SongController;
import ngo.music.player.entity.OfflineSong;
import ngo.music.player.entity.Song;
import ngo.music.player.R;

public class OfflineSongAdapter extends LiteListSongAdapter {
	
	public static OfflineSongAdapter instance = null;

	public static OfflineSongAdapter getInstance() {

		if (instance == null) {
			instance = createNewInstance();
		}
		return instance;
	}
	public OfflineSongAdapter(Context context, int resource) {
		super(context, resource);


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
		if (!(song instanceof OfflineSong)) {

		} else {
			songs.add(song);
		}

	}

	

	
	@Override
	public ArrayList<Song> getSongs() {
		// TODO Auto-generated method stub
		return SongController.getInstance().getOfflineSongs(true);
	}
	@Override
	public int getSongMenuId() {
		// TODO Auto-generated method stub
		return R.menu.song_list_menu;
	}
	
	
}
