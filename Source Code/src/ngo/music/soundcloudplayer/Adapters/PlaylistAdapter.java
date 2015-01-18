package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.entity.Song;
import android.content.Context;

public class PlaylistAdapter extends CompositionListAdapter {

	public static PlaylistAdapter instance = null;

	public static PlaylistAdapter getInstance() {
		if (instance == null) {
			instance = createNewInstance();
		}
		return instance;

	}

	private PlaylistAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		instance = this;

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

	public static PlaylistAdapter createNewInstance() {
		// TODO Auto-generated method stub
		instance = new PlaylistAdapter(MusicPlayerMainActivity.getActivity()
				.getApplicationContext(), R.layout.list_view);
		return instance;
	}
	
	
	

}
