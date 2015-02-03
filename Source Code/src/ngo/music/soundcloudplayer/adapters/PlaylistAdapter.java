package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.entity.Song;
import android.content.Context;

public class PlaylistAdapter extends CompositionListAdapter {

	static PlaylistAdapter instance = null;

	PlaylistAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		instance = this;
		type = PLAYLIST;
		canDelete = true;
	}

	@Override
	protected ArrayList<String> getCategories() {
		// TODO Auto-generated method stub
		return PlaylistController.getInstance().getCategoryString();

	}

	@Override
	protected ArrayList<Song> getItemsFromCat(String cat) {
		// TODO Auto-generated method stub
		return PlaylistController.getInstance().getSongFromCategory(cat);
	}

}
