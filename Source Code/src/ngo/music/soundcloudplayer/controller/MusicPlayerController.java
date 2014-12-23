package ngo.music.soundcloudplayer.controller;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.boundary.PlayerUI;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.os.CountDownTimer;
import android.util.Log;

public class MusicPlayerController implements Constants.MusicService {
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

		MusicPlayerService.getInstance().pause();

		stopTimer();
		// TODO Auto-generated method stub
		for (PlayerUI playerUI : uiFragments) {
			playerUI.pause();
		}

	}

	public void play() {
		// TODO Auto-generated method stub

		MusicPlayerService.getInstance().playCurrentSong();

	}

	public void startTimer() {
		timer = new CountDownTimer(MusicPlayerService.getInstance()
				.getDuration()
				- MusicPlayerService.getInstance().getCurrentTime(), 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				int degree = (int) Math.round(360
						* (double) MusicPlayerService.getInstance()
								.getCurrentTime()
						/ MusicPlayerService.getInstance().getDuration());
				for (PlayerUI fragment : uiFragments) {

					fragment.updateMusicProgressBar(degree);
				}
				// Log.i("TIME", String.valueOf(degree));
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

	

	// public void updateNewSong() {
	// // TODO Auto-generated method stub
	// startTimer();
	// }

	public void startPause() {
		// TODO Auto-generated method stub
		boolean a = MusicPlayerService.getInstance().isPlaying();
		if (!MusicPlayerService.getInstance().isPlaying()) {
			play();
		} else {
			pause();
		}

	}

	public void updateUI(final int TAG) {
		// TODO Auto-generated method stub

		MainActivity.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (TAG == MUSIC_START) {
					startTimer();
					for (PlayerUI playerUI : uiFragments) {
						playerUI.updateTitle(MusicPlayerService.getInstance()
								.getCurrentSong().getTitle());
						playerUI.updateSubTitle(MusicPlayerService
								.getInstance().getCurrentSong().getArtist());

						playerUI.play();

					}
				}
				if (TAG == MUSIC_PAUSE) {
					stopTimer();
					for (PlayerUI playerUI : uiFragments) {

						playerUI.pause();

					}
				}
			}
		});

	}

	public void play(Song song) {
		// TODO Auto-generated method stub

		MusicPlayerService.getInstance().playNewSong(song);

	}
}
