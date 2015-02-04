package ngo.music.soundcloudplayer.adapters;

public class SCPlaylistTitlesListAdapter extends CategoryTitlesListAdapter {
	static SCPlaylistTitlesListAdapter instance;
	@Override
	protected int setType() {
		// TODO Auto-generated method stub
		return SC_PLAYLIST;
	}

}
