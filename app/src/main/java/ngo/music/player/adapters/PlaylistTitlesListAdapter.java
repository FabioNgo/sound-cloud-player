package ngo.music.player.adapters;

public class PlaylistTitlesListAdapter extends CategoryTitlesListAdapter {
	static PlaylistTitlesListAdapter instance;
	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return PLAYLIST;
	}

}
