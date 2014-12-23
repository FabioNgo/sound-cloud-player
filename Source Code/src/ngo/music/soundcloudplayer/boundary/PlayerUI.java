package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.MusicPlayerController;
import android.support.v4.app.Fragment;

import com.todddavies.components.progressbar.ProgressWheel;

public abstract class PlayerUI extends Fragment {
	protected ProgressWheel musicProgressBar;
	protected MusicPlayerController musicPlayerController;
	private int degree = 0;
	Runnable runnable;
	public PlayerUI() {
		// TODO Auto-generated constructor stub
		musicPlayerController = MusicPlayerController.getInstance();
		 runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				musicProgressBar.setProgressDegree(degree);
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
	 * update music progress Bar
	 */
	public void updateMusicProgressBar(int degree) {
		this.degree = degree;
		runnable.run();

	}

	public void pause() {
		musicProgressBar.setBackgroundResource(R.drawable.ic_media_play);
	}

	public void play() {
		musicProgressBar.setBackgroundResource(R.drawable.ic_media_pause);
	}

}
