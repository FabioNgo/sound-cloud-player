package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.UpdateUiFromServiceController;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.todddavies.components.progressbar.ProgressWheel;

public abstract class PlayerUI extends Fragment {
	protected ProgressWheel musicProgressBar;

	private int degree = 0;
	String title;
	String subtitle;
	TextView currentTimeText;
	TextView durationText;
	Runnable runnable;
	protected int musicProgressBar_id = -1;
	protected boolean hasTextTime = false;
	protected View rootView = null;

	public PlayerUI() {
		// TODO Auto-generated constructor stub

		runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				musicProgressBar.setProgressDegree(degree);
				if (hasTextTime) {
					currentTimeText.setText(UpdateUiFromServiceController
							.getInstance().getCurrentTime());
					durationText.setText(UpdateUiFromServiceController
							.getInstance().getDuration());
				}
			}
		};
	}
	
	/**
	 * update Title of the song
	 */
	public abstract void updateTitle(String title);

	/**
	 * update sub Title of the song
	 */
	public abstract void updateSubTitle(String subTitle);

	/**
	 * update music progress Bar and Displayed Time
	 */
	public void updateMusicProgressBar(int degree) {
		this.degree = degree;
		if (MusicPlayerService.getInstance().isPlaying()) {
			play();
		} else {
			pause();
		}
		runnable.run();

	}

	public void pause() {
		musicProgressBar.setBackgroundResource(R.drawable.ic_media_play);

	}

	public void play() {
		musicProgressBar.setBackgroundResource(R.drawable.ic_media_pause);
		updateTitle(title);
		updateSubTitle(subtitle);
	}
	protected void iniMusicProgressBar(){
		musicProgressBar = (ProgressWheel) rootView
				.findViewById(musicProgressBar_id);
		musicProgressBar.setBackgroundResource(R.drawable.ic_media_play);
		musicProgressBar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerService.getInstance().startPause();
			}
		});
	}

}
