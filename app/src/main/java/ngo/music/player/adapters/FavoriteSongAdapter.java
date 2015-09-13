package ngo.music.player.adapters;


import android.content.Context;

import java.util.ArrayList;

import ngo.music.player.entity.Song;
import ngo.music.player.helper.Constants;


public class FavoriteSongAdapter extends SCSongAdapter implements Constants {
	
	
	
	
	public static FavoriteSongAdapter instance = null;
	
	
	public FavoriteSongAdapter(Context context, int resource, ArrayList<Song> onlineSongs) {
		super(context, resource, onlineSongs);
		
		
		// TODO Auto-generated constructor stub
	}

	

}
