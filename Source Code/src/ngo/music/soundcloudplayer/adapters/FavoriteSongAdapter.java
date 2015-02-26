package ngo.music.soundcloudplayer.adapters;


import java.util.ArrayList;

import ngo.music.soundcloudplayer.api.ApiWrapper;

import ngo.music.soundcloudplayer.entity.Song;

import ngo.music.soundcloudplayer.general.Constants;

import android.content.Context;


public class FavoriteSongAdapter extends SCSongAdapter implements Constants {
	
	
	
	
	public static FavoriteSongAdapter instance = null;
	
	
	public FavoriteSongAdapter(Context context, int resource, ArrayList<Song> onlineSongs, ApiWrapper wrapper) {
		super(context, resource, onlineSongs, wrapper);
		
		
		// TODO Auto-generated constructor stub
	}

	

}
