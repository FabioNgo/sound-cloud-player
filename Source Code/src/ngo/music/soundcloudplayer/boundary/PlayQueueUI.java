package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PlayQueueUI extends Fragment {
	Toolbar toolbar;

	public PlayQueueUI() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.play_queue, container, false);
		toolbar = (Toolbar)rootView.findViewById(R.id.play_queue_toolbar);
		
		toolbar.setTitle("Title");
		toolbar.setSubtitle("subtitle");
		toolbar.inflateMenu(R.menu.global);
		ImageView full_player_song_image = (ImageView)rootView.findViewById(R.id.play_queue_image);
		BasicFunctions.ResizeImageView(MainActivity.screenWidth, full_player_song_image);
		
		return rootView;
	}
	
	

}
