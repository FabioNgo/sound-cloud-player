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
		type = ARTIST;
	}

	@Override
	protected ArrayList<String> getCategories() {
		// TODO Auto-generated method stub
		return ArtistController.getInstance().getCategoryString();

	}

	@Override
	protected ArrayList<Song> getItemsFromCat(String cat) {
		// TODO Auto-generated method stub
		return ArtistController.getInstance().getSongFromCategory(cat);
	}

}
