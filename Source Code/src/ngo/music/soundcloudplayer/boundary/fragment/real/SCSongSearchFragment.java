package ngo.music.soundcloudplayer.boundary.fragment.real;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.SCSongAdapter;
import ngo.music.soundcloudplayer.adapters.SCSearchSongAdapter;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.SoundCloudExploreFragment;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;

public class SCSongSearchFragment extends SoundCloudExploreFragment {

	public SCSongSearchFragment() {
		// TODO Auto-generated constructor stub
		super();
		query = MusicPlayerMainActivity.query;
	}

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return SEARCH;
	}

	
}
