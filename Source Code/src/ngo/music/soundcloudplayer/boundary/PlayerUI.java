package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.UpdateUiFromServiceController;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.support.v4.app.Fragment;
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

	public PlayerUI() {
		// TODO Auto-generated constructor stub

		runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				musicProgressBar.setProgressDegree(degree);
				currentTimeText.setText(UpdateUiFromServiceController.getInstance()
						.getCurrentTime());
				durationText.setText(UpdateUiFromServiceController.getInstance()
						.getDuration());
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

}
