package ngo.music.soundcloudplayer.controller;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.Adapters.PlaylistAdapter;
import ngo.music.soundcloudplayer.Adapters.QueueSongAdapter;
import ngo.music.soundcloudplayer.Adapters.SimplePlaylistAdapter;
import ngo.music.soundcloudplayer.boundary.PlayerUI;
import ngo.music.soundcloudplayer.boundary.QueueSongUI;
import ngo.music.soundcloudplayer.boundary.fragments.ListContentFragment;
import ngo.music.soundcloudplayer.boundary.fragments.PlaylistFragment;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.general.States;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.todddavies.components.progressbar.ProgressWheel;

public class UIController implements Constants.MusicService, Constants.Data,
		Constants.Appplication {
	private static UIController instance;
	private CountDownTimer timer;
	private ArrayList<PlayerUI> uiFragments;
	private ArrayList<ProgressWheel> musicProgressBars;
	private ArrayList<ArrayAdapter<?>> adapters;
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

	/**
	 * reset all list
	 */
	public void reset() {
		// TODO Auto-generated method stub
		uiFragments = new ArrayList<PlayerUI>();
		musicProgressBars = new ArrayList<ProgressWheel>();
		adapters = new ArrayList<ArrayAdapter<?>>();
		listContentFragments = new ArrayList<ListContentFragment>();
	}

	/**
	 * add an UI Fragment to the líst
	 * 
	 * @param fragment
	 */
	public void addUiFragment(PlayerUI fragment) {

		uiFragments.add(fragment);
		addProgressBar(fragment.getProgressBar());

	}

	/**
	 * if input is on the list, remove old to add new one. Then, try to load new
	 * one
	 * 
	 * @param input
	 *            : a listContentFragment to add
	 */
	public void addListContentFragements(ListContentFragment input) {
		/**
		 * Remove old one
		 */
		for (int i = 0; i < listContentFragments.size(); i++) {
			if (listContentFragments.get(i).getClass().equals(input.getClass())) {
				listContentFragments.remove(i);
				i--;
			}
		}
		/**
		 * add new one
		 */
		listContentFragments.add(input);
		/**
		 * try to load new fragment
		 */
		if (loaded) {

			input.load();
		}

	}

	/**
	 * add progressbar to the list to update progression from service
	 * 
	 * @param progressbar
	 */
	public void addProgressBar(ProgressWheel progressbar) {
		if (!musicProgressBars.contains(progressbar)) {
			musicProgressBars.add(progressbar);
		}
	}

	/**
	 * Remove progressbar from the list
	 * 
	 * @param progressbar
	 */
	public void removeProgressBar(ProgressWheel progressbar) {
		// TODO Auto-generated method stub
		if (musicProgressBars.contains(progressbar)) {
			musicProgressBars.remove(progressbar);
		}
	}

	/**
	 * Add an adapter into the list
	 * 
	 * @param adapter
	 */
	public void addAdapter(ArrayAdapter<?> adapter) {
		for (int i = 0; i < adapters.size(); i++) {
			if (adapters.get(i).getClass().equals(adapter.getClass())) {
				adapters.remove(i);
				i--;
			}
		}

		adapters.add(adapter);

	}

	/**
	 * Remove adapter from the list
	 * 
	 * @param adapter
	 */
	public void removeAdapter(ArrayAdapter<?> adapter) {
		// TODO Auto-generated method stub
		if (adapters.contains(adapter)) {
			adapters.remove(adapter);
		}
	}

	/**
	 * Start timer, to update info from service after 1 second
	 */
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

			}
		};
		timer.start();
	}

	/**
	 * Stop updating info from service
	 */
	protected void stopTimer() {
		// TODO Auto-generated method stub
		if (timer != null) {
			timer.cancel();
		}
	}

	/**
	 * Update UI related to music player
	 * 
	 * @param TAG
	 *            MUSIC_PLAYING: When music is being played, (update all info of
	 *            the playing song and start timer to update info). MUSIC_PAUSE:
	 *            When music is paused ( update play button). MUSIC_NEW_SONG:
	 *            When a new song played (update all info of a new song)
	 *            MUSIC_CUR_POINT_CHANGED: When the cursor is changed, use for
	 *            rewinding and fast forwarding APP_RUNNING: When Main activity
	 *            is running
	 * 
	 */
	public void updateUiWhilePlayingMusic(final int TAG) {
		// TODO Auto-generated method stub
		Song curSong = MusicPlayerService.getInstance().getCurrentSong();
		switch (TAG) {
		case MUSIC_PLAYING:
			for (ProgressWheel progressbar : musicProgressBars) {

				progressbar
						.setBackgroundResource(R.drawable.ic_media_pause_progress);
			}
			startTimer();
			for (PlayerUI playerUI : uiFragments) {

				playerUI.updateSongInfo(curSong);

				playerUI.play();

			}
			for (ArrayAdapter<?> arrayAdapter : adapters) {

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

					progressbar
							.setBackgroundResource(R.drawable.ic_media_play_progress);
				}

			}
			break;

		case MUSIC_NEW_SONG:
			for (PlayerUI playerUI : uiFragments) {

				playerUI.updateSongInfo(curSong);

			}

			for (ListAdapter arrayAdapter : adapters) {

				if (arrayAdapter instanceof QueueSongAdapter) {
					((QueueSongAdapter) arrayAdapter).updateQueue();
				}

			}

			break;
		case MUSIC_CUR_POINT_CHANGED:

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

			break;
		case MUSIC_STOPPED:
			if (timer != null) {
				timer.cancel();
			}
			for (PlayerUI playerUI : uiFragments) {

				playerUI.stop();
			}
			for (ProgressWheel progressbar : musicProgressBars) {

				progressbar
						.setBackgroundResource(R.drawable.ic_media_play_progress);
			}

			break;

		default:
			for (PlayerUI playerUI : uiFragments) {
				playerUI.update();
			}
		}

	}

	/**
	 * Update UI when application start or stop
	 * 
	 * @param TAG
	 *            APP_RUNNING: WHen app is running
	 * 
	 */
	public void updateUiAppChanged(final int TAG) {

		// TODO Auto-generated method stub
		Song curSong = MusicPlayerService.getInstance().getCurrentSong();
		switch (TAG) {
		case APP_RUNNING:
			for (PlayerUI playerUI : uiFragments) {

				playerUI.updateSongInfo(curSong);

				if (MusicPlayerService.getInstance().isPlaying()) {
					playerUI.play();
					startTimer();
					for (ProgressWheel progressbar : musicProgressBars) {

						progressbar
								.setBackgroundResource(R.drawable.ic_media_pause_progress);
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
								.setBackgroundResource(R.drawable.ic_media_play_progress);
					}
				}

			}

			for (ListContentFragment listContentFragment : listContentFragments) {
				listContentFragment.load();
			}
			loaded = true;
			States.appState = APP_RUNNING;
			break;
		default:

		}

	}

	/**
	 * Update UI related application
	 * 
	 * @param TAG
	 *            OFFLINE_SONG_CHANGED: When offline songs database changed.
	 *            QUEUE_CHANGED: When queue changed
	 * 
	 */
	public void updateUiWhenDataChanged(final int TAG) {
		// TODO Auto-generated method stub
		switch (TAG) {
		case OFFLINE_SONG_CHANGED:
			OfflineSongAdapter.getInstance().updateSongs();
			OfflineSongAdapter.getInstance().notifyDataSetChanged();
			break;
		case QUEUE_CHANGED:
			QueueSongAdapter.getInstance().notifyDataSetChanged();
			QueueSongUI.getInstance().update();
			break;
		case PLAYLIST_CHANGED:
			SimplePlaylistAdapter.getInstance().updatePlaylist();
			PlaylistFragment.getInstance().update();
		default:

		}

	}

	/**
	 * Get current time of the song
	 * 
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public String getCurrentTime() {
		int mTime = MusicPlayerService.getInstance().getCurrentTime();

		return String.format(
				"%02d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(mTime),
				TimeUnit.MILLISECONDS.toSeconds(mTime)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(mTime)));
	}

	/**
	 * Get Duration of the song
	 * 
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public String getDuration() {
		long mTime = MusicPlayerService.getInstance().getDuration();
		return String.format(
				"%02d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(mTime),
				TimeUnit.MILLISECONDS.toSeconds(mTime)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(mTime)));
	}

}
