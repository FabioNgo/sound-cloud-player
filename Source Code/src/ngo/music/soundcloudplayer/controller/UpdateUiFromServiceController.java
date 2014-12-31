package ngo.music.soundcloudplayer.controller;

import java.util.ArrayList;
import java.util.Currency;
import java.util.concurrent.TimeUnit;

import com.todddavies.components.progressbar.ProgressWheel;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.boundary.FullPlayerUI;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.boundary.PlayerUI;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.os.CountDownTimer;
import android.text.format.Time;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class UpdateUiFromServiceController implements Constants.MusicService {
	private static UpdateUiFromServiceController instance;
	private MusicPlayerService musicPlayerService;
	private CountDownTimer timer;
	private ArrayList<PlayerUI> uiFragments;
	private ArrayList<ProgressWheel> musicProgressBars;
	private ArrayList<ArrayAdapter<Song>> adapters;
	private Song playingSong;

	private UpdateUiFromServiceController() {
		// TODO Auto-generated constructor stub

		instance = this;
		uiFragments = new ArrayList<PlayerUI>();
		musicProgressBars = new ArrayList<ProgressWheel>();
		adapters = new ArrayList<ArrayAdapter<Song>>();

	}

	public static UpdateUiFromServiceController getInstance() {
		if (instance == null) {
			new UpdateUiFromServiceController();
		}
		return instance;
	}

	public void addUiFragment(PlayerUI fragment) {

		uiFragments.add(fragment);
		addProgressBar(fragment.getProgressBar());

	}

	public void addProgressBar(ProgressWheel progressbar) {
		if (!musicProgressBars.contains(progressbar)) {
			musicProgressBars.add(progressbar);
		}
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
				for (ProgressWheel progressbar : musicProgressBars) {

					progressbar.setProgressDegree(degree);
				}
				for (PlayerUI ui : uiFragments) {
					ui.updateMusicProgress();
				}

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				Log.i("", "Done");

			}
		};
		timer.start();
	}

	protected void stopTimer() {
		// TODO Auto-generated method stub
		if (timer != null) {
			timer.cancel();
		}
	}

	// public void updateNewSong() {
	// // TODO Auto-generated method stub
	// startTimer();
	// }

	public void updateUI(final int TAG) {
		// TODO Auto-generated method stub
		switch (TAG) {
		case MUSIC_START:
			for (ProgressWheel progressbar : musicProgressBars) {

				progressbar.setBackgroundResource(R.drawable.ic_media_pause);
			}
			startTimer();
			for (PlayerUI playerUI : uiFragments) {

				playerUI.updateTitle(MusicPlayerService.getInstance()
						.getCurrentSong());
				playerUI.updateSubtitle(MusicPlayerService.getInstance()
						.getCurrentSong());

				playerUI.play();

			}
			break;
		case MUSIC_PAUSE:
			stopTimer();
			for (PlayerUI playerUI : uiFragments) {

				playerUI.pause();
				for (ProgressWheel progressbar : musicProgressBars) {

					progressbar.setBackgroundResource(R.drawable.ic_media_play);
				}

			}
			break;
		case APP_START:
			for (PlayerUI playerUI : uiFragments) {

				playerUI.updateTitle(MusicPlayerService.getInstance()
						.getCurrentSong());
				playerUI.updateSubtitle(MusicPlayerService.getInstance()
						.getCurrentSong());

				if (MusicPlayerService.getInstance().isPlaying()) {
					playerUI.play();
					startTimer();
					for (ProgressWheel progressbar : musicProgressBars) {

						progressbar
								.setBackgroundResource(R.drawable.ic_media_pause);
					}
				} else {
					for (ProgressWheel progressbar : musicProgressBars) {
						int degree = (int) Math.round(360
								* (double) MusicPlayerService.getInstance()
										.getTimeLastStop()
								/ MusicPlayerService.getInstance()
										.getDuration());
						progressbar.setProgressDegree(degree);
						progressbar
								.setBackgroundResource(R.drawable.ic_media_play);
					}
				}

			}
			break;
		case MUSIC_NEW_SONG:
			for (PlayerUI playerUI : uiFragments) {

				playerUI.updateTitle(MusicPlayerService.getInstance()
						.getCurrentSong());
				playerUI.updateSubtitle(MusicPlayerService.getInstance()
						.getCurrentSong());

			}
			for (ArrayAdapter<Song> arrayAdapter : adapters) {
				arrayAdapter.notifyDataSetChanged();
			}

			break;
		case MUSIC_CUR_POINT_CHANGED:
			for (PlayerUI playerUI : uiFragments) {

				int degree = (int) Math.round(360
						* (double) MusicPlayerService.getInstance()
								.getCurrentTime()
						/ MusicPlayerService.getInstance().getDuration());
				for (PlayerUI fragment : uiFragments) {

					fragment.updateMusicProgress();
				}
				for (ProgressWheel progressWheel : musicProgressBars) {
					progressWheel.setProgressDegree(degree);
				}

			}

			break;
		default:
			for (PlayerUI playerUI : uiFragments) {
				playerUI.update();
			}
		}
	}

	// TODO Auto-generated method stub

	// public void play(Song song) {
	// // TODO Auto-generated method stub
	//
	// MusicPlayerService.getInstance().playNewSong(song);
	//
	// }

	public String getCurrentTime() {
		int mTime = MusicPlayerService.getInstance().getCurrentTime();

		return String.format(
				"%02d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(mTime),
				TimeUnit.MILLISECONDS.toSeconds(mTime)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(mTime)));
	}

	public String getDuration() {
		long mTime = MusicPlayerService.getInstance().getDuration();
		return String.format(
				"%02d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(mTime),
				TimeUnit.MILLISECONDS.toSeconds(mTime)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(mTime)));
	}

	public void removeProgressBar(ProgressWheel progressbar) {
		// TODO Auto-generated method stub
		if (musicProgressBars.contains(progressbar)) {
			musicProgressBars.remove(progressbar);
		}
	}

	public void removeAdapter(ArrayAdapter<Song> adapter) {
		// TODO Auto-generated method stub
		if (adapters.contains(adapter)) {
			adapters.remove(adapter);
		}
	}

	public void addAdapter(ArrayAdapter<Song> adapter) {
		if (!adapters.contains(adapter)) {
			adapters.add(adapter);
		}
	}

}
