package ngo.music.soundcloudplayer.boundary;

import android.support.v4.app.Fragment;

/**
 * 
 * @author Fabio Ngo
 * Every fragments having list view to list songs. This is used to update list when necessary
 *
 */
public abstract class ListContentFragment extends Fragment {
	public abstract void load();

}
