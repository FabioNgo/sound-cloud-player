package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.AlbumController;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.controller.SCPlaylistController;
import ngo.music.soundcloudplayer.entity.Song;
import android.content.Context;

public class SCPlaylistAdapter extends CompositionListAdapter {

	static SCPlaylistAdapter instance = null;

	SCPlaylistAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		instance = this;
		type = SC_PLAYLIST;
	}

	@Override
	protected ArrayList<String> getCategories() {
		// TODO Auto-generated method stub
		return SCPlaylistController.getInstance().getCategoryString();

	}

	@Override
	protected ArrayList<Song> getItemsFromCat(String cat) {
		// TODO Auto-generated method stub
		return SCPlaylistController.getInstance().getSongFromCategory(cat);
	}

}
