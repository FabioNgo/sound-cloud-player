package ngo.music.soundcloudplayer.boundary;

import com.todddavies.components.progressbar.ProgressWheel;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FullPlayerUI extends Fragment {
	Toolbar toolbar;
	boolean isPlaying = false;
	ProgressWheel musicProgressBar;
	public FullPlayerUI() {
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

		View rootView = inflater.inflate(R.layout.fullplayer, container, false);
		toolbar = (Toolbar)rootView.findViewById(R.id.full_player_toolbar);
		
		toolbar.setTitle("Title");
		toolbar.setSubtitle("subtitle");
		toolbar.inflateMenu(R.menu.global);
		ImageView full_player_song_image = (ImageView)rootView.findViewById(R.id.full_player_song_image);
		BasicFunctions.ResizeImageView(MainActivity.screenWidth, full_player_song_image);
		musicProgressBar = (ProgressWheel) rootView.findViewById(R.id.full_player_progress_bar);
		musicProgressBar.setProgressPercentage(20);
		if(isPlaying) {
			play();
		}else {
			pause();
		}
		musicProgressBar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isPlaying) {
					pause();
				}else {
					play();
				}
			}
		});
		return rootView;
	}

	private void pause() {
		// TODO Auto-generated method stub
		isPlaying = false;
		musicProgressBar.setBackgroundResource(R.drawable.ic_action_github);
		
	}

	private void play() {
		// TODO Auto-generated method stub
		isPlaying = true;
		musicProgressBar.setBackgroundResource(R.drawable.ic_launcher);
	}
	
	

}
