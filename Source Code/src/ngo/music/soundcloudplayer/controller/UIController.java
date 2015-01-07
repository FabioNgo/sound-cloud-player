package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.concurrent.TimeUnit;

import com.todddavies.components.progressbar.ProgressWheel;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.Adapters.QueueSongAdapter;
import ngo.music.soundcloudplayer.boundary.FullPlayerUI;
import ngo.music.soundcloudplayer.boundary.ListContentFragment;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.boundary.OfflineSongsFragment;
import ngo.music.soundcloudplayer.boundary.PlayerUI;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.general.States;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.os.CountDownTimer;
import android.text.format.Time;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class UIController implements Constants.MusicService,
		Constants.Appplication {
	private static UIController instance;
	private MusicPlayerService musicPlayerService;
	private CountDownTimer timer;
	private ArrayList<PlayerUI> uiFragments;
	private ArrayList<ProgressWheel> musicProgressBars;
	private ArrayList<ArrayAdapter<Song>> adapters;
	private Song playingSong;
	private ArrayList<ListContentFragment> listContentFragments;
	private boolean loaded = false;

	private UIController() {
		// TODO Auto-generated constructor stub

		instance = this;
		reset();

	}

	public static UIController getInstance() {
		if (instance == null) {
			new UIController();
		}
		return instance;
	}

	public void addUiFragment(PlayerUI fragment) {

		uiFragments.add(fragment);
		addProgressBar(fragment.getProgressBar());

	}

	public void addListContentFragements(ListContentFragment input) {
		for (int i = 0; i < listContentFragments.size(); i++) {
			if (listContentFragments.get(i).equals(input)) {
				listContentFragments.remove(i);
				i--;
			}
		}
		// if (!listContentFragments.contains(input)) {
		listContentFragments.add(input);
		if (loaded) {
			Log.i("UIController.addListCOntentFragments", input.toString());
			input.load(true);
		}
		// }

		// listContentFragments.add(input);

		/**
		 * Try to load Fragment
		 */

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
				// try {
				// OfflineSongController.getInstance().storePlayingSong();
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
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
		// Log.i("updateUI",String.valueOf(TAG));
		switch (TAG) {
		case MUSIC_PLAYING:
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
			for (ArrayAdapter<Song> arrayAdapter : adapters) {

				if (arrayAdapter instanceof QueueSongAdapter) {
					((QueueSongAdapter) arrayAdapter).updateQueue();
				}

				arrayAdapter.notifyDataSetChanged();
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
		case APP_RUNNING:
			Log.i("Update UI", "APP START");
			for (PlayerUI playerUI : uiFragments) {

				playerUI.updateSongInfo(MusicPlayerService.getInstance()
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
										.getCurrentTime()
								/ MusicPlayerService.getInstance()
										.getDuration());
						progressbar.setProgressDegree(degree);
						progressbar
								.setBackgroundResource(R.drawable.ic_media_play);
					}
				}
				/**
				 * Setting music progress bar on listviews
				 */

			}
			// for (ArrayAdapter<Song> arrayAdapter : adapters) {
			// arrayAdapter.notifyDataSetChanged();
			// }

			Log.i("listFragments", listContentFragments.toString());
			for (ListContentFragment listContentFragment : listContentFragments) {
				listContentFragment.load(true);
			}
			loaded = true;
			States.appState = APP_RUNNING;
			break;
		case MUSIC_NEW_SONG:
			for (PlayerUI playerUI : uiFragments) {

				Song currentSong = MusicPlayerService.getInstance()
						.getCurrentSong();
				playerUI.updateTitle(currentSong);
				playerUI.updateSubtitle(currentSong);
				playerUI.updateSongInfo(currentSong);

			}

			for (ArrayAdapter<Song> arrayAdapter : adapters) {

				if (arrayAdapter instanceof QueueSongAdapter) {
					((QueueSongAdapter) arrayAdapter).updateQueue();
				}

				// arrayAdapter.notifyDataSetChanged();
			}
			for (ListContentFragment listContentFragment : listContentFragments) {
				listContentFragment.load(false);
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

	public void reset() {
		// TODO Auto-generated method stub
		uiFragments = new ArrayList<PlayerUI>();
		musicProgressBars = new ArrayList<ProgressWheel>();
		adapters = new ArrayList<ArrayAdapter<Song>>();
		listContentFragments = new ArrayList<ListContentFragment>();
	}

}
