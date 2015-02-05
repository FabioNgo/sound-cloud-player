package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.AlbumController;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.controller.SCMyPlaylistController;
import ngo.music.soundcloudplayer.controller.SCPlaylistSearchController;
import ngo.music.soundcloudplayer.entity.Song;
import android.content.Context;

public class SCPlaylistSearchAdapter extends CompositionListAdapter {

	static SCPlaylistSearchAdapter instance = null;

	SCPlaylistSearchAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		instance = this;
		//type = SC_SEARCH_PLAYLIST;
	}

	@Override
	protected ArrayList<String> getCategories() {
		// TODO Auto-generated method stub
		return SCPlaylistSearchController.getInstance().getCategoryString();

	}



	@Override
	protected boolean setCanDelete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return SC_SEARCH_PLAYLIST;
	}

	@Override
	protected ArrayList<Song> getSongsFromCat(String cat) {
		// TODO Auto-generated method stub
		return SCPlaylistSearchController.getInstance().getSongFromCategory(cat);
	}

	@Override
	protected boolean setCanEdit() {
		// TODO Auto-generated method stub
		return false;
	}

}
