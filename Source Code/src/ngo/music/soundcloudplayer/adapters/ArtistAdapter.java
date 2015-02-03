package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.AlbumController;
import ngo.music.soundcloudplayer.controller.ArtistController;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.entity.Song;
import android.content.Context;

public class ArtistAdapter extends CompositionListAdapter {

	static ArtistAdapter instance = null;

	ArtistAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		instance = this;
		
		
	}

	@Override
	protected ArrayList<String> getCategories() {
		// TODO Auto-generated method stub
		return ArtistController.getInstance().getCategoryString();

	}

	@Override
	protected ArrayList<Song> getSongsFromCat(String cat) {
		// TODO Auto-generated method stub
		return ArtistController.getInstance().getSongFromCategory(cat);
	}

	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return ARTIST;
	}

	@Override
	protected boolean setCanDelete() {
		// TODO Auto-generated method stub
		return false;
	}

}
