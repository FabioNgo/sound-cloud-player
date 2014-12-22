package ngo.music.soundcloudplayer.controller;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.PlayerUI;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.todddavies.components.progressbar.ProgressWheel;

public class MusicPlayerController {
	private static MusicPlayerController instance;
	private MusicPlayerService musicPlayerService;
	private CountDownTimer timer;
	private ArrayList<PlayerUI> uiFragments;

	private MusicPlayerController() {
		// TODO Auto-generated constructor stub

		instance = this;
		uiFragments = new ArrayList<PlayerUI>();
		musicPlayerService = MusicPlayerService.getInstance();

	}

	public static MusicPlayerController getInstance() {
		if (instance == null) {
			new MusicPlayerController();
		}
		return instance;
	}

	public void addUiFragment(PlayerUI fragment) {

		uiFragments.add(fragment);

	}

	public void pause() {
		synchronized (musicPlayerService) {
			musicPlayerService.pause();
		}

		stopTimer();
		// TODO Auto-generated method stub
		for (PlayerUI playerUI : uiFragments) {
			playerUI.pause();
		}

	}

	public void play() {
		// TODO Auto-generated method stub
		synchronized (musicPlayerService) {
			musicPlayerService.playCurrentSong();
		}

		startTimer();
		// TODO Auto-generated method stub
		for (PlayerUI playerUI : uiFragments) {
			playerUI.play();
		}

	}

	public void startTimer() {
		timer = new CountDownTimer(musicPlayerService.getDuration()
				- musicPlayerService.getCurrentTime(), 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				int degree = (int) Math.round(360*
						(double) musicPlayerService.getCurrentTime()
								/ musicPlayerService.getDuration());
				for (PlayerUI fragment : uiFragments) {
					
					fragment.updateMusicProgressBar(degree);
				}
				Log.i("TIME", String.valueOf(degree));
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}
		};
		timer.start();
	}

	protected void stopTimer() {
		// TODO Auto-generated method stub
		timer.cancel();
	}

	public String getPlayingSongID() {
		return musicPlayerService.getSongID();
	}

	public Song getCurrentSong() {
		return musicPlayerService.getCurrentSong();
	}

	public void updateNewSong() {
		// TODO Auto-generated method stub
		startTimer();
	}

	public void startPause() {
		// TODO Auto-generated method stub
		if (!musicPlayerService.isPlaying()) {
			play();
		} else {
			pause();
		}

	}
}
