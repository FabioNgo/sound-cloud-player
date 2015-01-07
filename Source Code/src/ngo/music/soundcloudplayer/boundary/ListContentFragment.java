package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.general.Constants;
import android.support.v4.app.Fragment;

/**
 * 
 * @author Fabio Ngo
 * Every fragments having list view to list songs. This is used to update list when necessary
 *
 */
public abstract class ListContentFragment extends Fragment implements Constants.MusicService, Constants.Appplication {
	public abstract void load(boolean firstTime);

}
