package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.UpdateUiFromServiceController;
import ngo.music.soundcloudplayer.entity.Song;
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
	public abstract void updateTitle(Song song);

	/**
	 * update sub Title of the song
	 */
	public abstract void updateSubtitle(Song song);

	/**
	 * update music progress Bar and Displayed Time
	 */
	public void updateMusicProgress() {
		if (MusicPlayerService.getInstance().isPlaying()) {
			play();
		} 
		runnable.run();
	}

	public void pause() {

	}

	public void play() {
		updateTitle(MusicPlayerService.getInstance().getCurrentSong());
		updateSubtitle(MusicPlayerService.getInstance().getCurrentSong());
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
	public ProgressWheel getProgressBar(){
		return musicProgressBar;
	}
}
