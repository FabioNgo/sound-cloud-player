package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.AlbumController;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.entity.Song;
import android.content.Context;

public class AlbumAdapter extends CategoryListAdapter {

	static AlbumAdapter instance = null;

	AlbumAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		instance = this;
		
	}

	

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return ALBUM;
	}

	@Override
	protected boolean setCanDelete() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	protected boolean setCanEdit() {
		// TODO Auto-generated method stub
		return false;
	}

}
