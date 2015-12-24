package ngo.music.player.Controller;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.todddavies.components.progressbar.ProgressWheel;

import java.util.ArrayList;

import ngo.music.player.Model.Song;
import ngo.music.player.R;
import ngo.music.player.adapters.CategoryListAdapter;
import ngo.music.player.adapters.CategoryTitlesListAdapter;
import ngo.music.player.adapters.LiteListSongAdapter;
import ngo.music.player.adapters.OfflineSongAdapter;
import ngo.music.player.adapters.QueueSongAdapter;
import ngo.music.player.adapters.SongsInCateAdapter;
import ngo.music.player.boundary.FullPlayerUI;
import ngo.music.player.boundary.LitePlayerUI;
import ngo.music.player.boundary.PlayerUI;
import ngo.music.player.boundary.fragment.abstracts.CategoryListContentFragment;
import ngo.music.player.boundary.fragment.abstracts.ListContentFragment;
import ngo.music.player.boundary.fragment.real.QueueFragment;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;
import ngo.music.player.helper.States;
import ngo.music.player.service.MusicPlayerService;

public class UIController implements Constants.MusicService, Constants.Data,
		Constants.Appplication, Constants.Models {

	private static UIController instance;
	protected boolean canAnnounceNextSong = true;
	private CountDownTimer timer;
	private ArrayList<PlayerUI> playerUiFragments;
	private ProgressWheel[] musicProgressBars;
	private ArrayList<ArrayAdapter<?>> adapters;
	private ArrayList<ListContentFragment> listContentFragments;

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
		playerUiFragments = new ArrayList<PlayerUI>();
		musicProgressBars = new ProgressWheel[2];

		adapters = new ArrayList<ArrayAdapter<?>>();
		listContentFragments = new ArrayList<ListContentFragment>();
	}

	/**
	 * add an UI Fragment to the l�st
	 * 
	 * @param fragment
	 */
	public void addPlayerUiFragment(PlayerUI fragment) {
		boolean isExisted = false;
		for (int i = 0; i < playerUiFragments.size(); i++) {
			if (playerUiFragments.get(i).compareTo(fragment) == 0) {
				playerUiFragments.set(i, fragment);
				isExisted = true;
			}

		}
		if (!isExisted) {
			playerUiFragments.add(fragment);
		}
		if (fragment instanceof FullPlayerUI) {
			addProgressBar(fragment.getProgressBar(), "FullUi");
			return;
		}
		if (fragment instanceof LitePlayerUI) {
			addProgressBar(fragment.getProgressBar(), "LiteUi");
		}

	}

	/**
	 * if input is on the list, remove old to add new one. Then, try to load new
	 * one
	 * 
	 * @param input
	 *            : a listContentFragment to add
	 */
	public void addListContentFragments(ListContentFragment input) {
		if (input == null) {
			Log.e("Add ContentFragments", input.getClass().toString());
			return;
		}
		boolean isExsited = false;
		for (int i = 0; i < listContentFragments.size(); i++) {
			if (listContentFragments.get(i).compareTo(input) == 0) {
				listContentFragments.set(i, input);
				isExsited = true;
			}
			
		}
		if(!isExsited){
			listContentFragments.add(input);
		}

	}

	/**
	 * add progressbar to the list to update progression from service
	 * 
	 * @param progressbar
	 */
	public void addProgressBar(ProgressWheel progressbar, String Tag) {
		if ("FullUi".equals(Tag)) {
			musicProgressBars[0] = progressbar;
			return;
		}
		if ("LiteUi".equals(Tag)) {
			musicProgressBars[1] = progressbar;
			return;
		}
	}

	/**
	 * Remove progressbar from the list
	 * 
	 * @param progressbar
	 */
	// public void removeProgressBar(ProgressWheel progressbar) {
	// // TODO Auto-generated method stub
	// if (musicProgressBars.contains(progressbar)) {
	// musicProgressBars.remove(progressbar);
	// }
	// }

	/**
	 * Add an adapter into the list
	 * 
	 * @param adapter
	 */
	public void addAdapter(ArrayAdapter<?> adapter) {
		boolean isExsited = false;
		if (adapter == null) {
			Log.e("Add Adapter", adapter.getClass().toString());
			return;
		} else {
			Log.i("Add Adapter", adapter.getClass().toString());
		}
		for (int i = 0; i < adapters.size(); i++) {
			if (adapters.get(i).getClass().equals(adapter.getClass())) {
				adapters.set(i, adapter);
				isExsited = true;
				break;
			}
		}
		if(!isExsited){
			adapters.add(adapter);
		}

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
				long currentTime = MusicPlayerService.getInstance()
						.getCurrentTime();
				long duration = MusicPlayerService.getInstance().getDuration();
				double ratio = (currentTime * 100.0) / duration;
				if ((currentTime * 100) / duration == 50 && canAnnounceNextSong) {
					String format = String.format("Next song: %s",
							MusicPlayerService.getInstance().getNextSong()
									.getAttribute("title"));
					Helper.makeToastTake(format,
							MusicPlayerService.getInstance());
					canAnnounceNextSong = false;
				}
				for (ProgressWheel progressbar : musicProgressBars) {

					progressbar.setProgressDegree((int) (ratio * 3.6));
				}
				for (PlayerUI ui : playerUiFragments) {
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
		String format = "";
		if (curSong != null) {
			switch (TAG) {

			case MUSIC_PLAYING:
				startTimer();
				if (States.appState == APP_RUNNING) {

					try {

						format = String
								.format("Song playing: %s \nNext song: %s",
										curSong.getAttribute("title"), MusicPlayerService
												.getInstance().getNextSong()
												.getAttribute("title"));
						Helper.makeToastTake(format,
								MusicPlayerService.getInstance());
					} catch (Exception e) {

					}
					for (ProgressWheel progressbar : musicProgressBars) {

						progressbar
								.setBackgroundResource(R.drawable.ic_media_pause_progress);
					}

					for (PlayerUI playerUI : playerUiFragments) {

						playerUI.updateSongInfo(curSong);

						playerUI.play();

					}
					for (ArrayAdapter<?> arrayAdapter : adapters) {

						if (arrayAdapter instanceof QueueSongAdapter) {
							((QueueSongAdapter) arrayAdapter).updateSongs();
						}

						arrayAdapter.notifyDataSetChanged();
					}
				}
				break;
			case MUSIC_PAUSE:
				if (States.appState == APP_RUNNING) {
					stopTimer();
					for (PlayerUI playerUI : playerUiFragments) {

						playerUI.pause();
						for (ProgressWheel progressbar : musicProgressBars) {

							progressbar
									.setBackgroundResource(R.drawable.ic_media_play_progress);
						}

					}
				}
				break;

			case MUSIC_NEW_SONG:
				canAnnounceNextSong = true;
				// format = String.format("Song playing: %s",
				// curSong.getTitle(),
				// curSong.getArtist());
				// Helper.makeToastTake(format,
				// MusicPlayerService.getInstance());
				// System.out.println ("UI CONTROLLER = " + uiFragments);
				if (States.appState == APP_RUNNING) {
					for (PlayerUI playerUI : playerUiFragments) {

						playerUI.updateSongInfo(curSong);

					}

					for (ListAdapter arrayAdapter : adapters) {

						if (arrayAdapter instanceof LiteListSongAdapter) {
							((LiteListSongAdapter) arrayAdapter).updateSongs();
						}

					}
				}
				break;
			case MUSIC_CUR_POINT_CHANGED:

				int degree = (int) Math.round(360
						* (double) MusicPlayerService.getInstance()
								.getCurrentTime()
						/ MusicPlayerService.getInstance().getDuration());
				for (PlayerUI fragment : playerUiFragments) {

					fragment.updateMusicProgress();
				}
				for (ProgressWheel progressWheel : musicProgressBars) {
					progressWheel.setProgressDegree(degree);
				}

				break;
			case MUSIC_STOPPED:
				if (States.appState == APP_RUNNING) {
					if (timer != null) {
						timer.cancel();
					}
					for (PlayerUI playerUI : playerUiFragments) {

						playerUI.stop();
					}
					for (ProgressWheel progressbar : musicProgressBars) {

						progressbar
								.setBackgroundResource(R.drawable.ic_media_play_progress);
					}
				}
				break;

			default:
				for (PlayerUI playerUI : playerUiFragments) {
					playerUI.update();
				}
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
			if (!MusicPlayerService.isLoaded) {
				return;
			}
			// if (States.appState != APP_RUNNING) {
			if (MusicPlayerService.getInstance().isPlaying()) {

				startTimer();
				for (ProgressWheel progressbar : musicProgressBars) {

					progressbar
							.setBackgroundResource(R.drawable.ic_media_pause_progress);
				}
			} else {
				for (ProgressWheel progressbar : musicProgressBars) {
					if (progressbar != null) {
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
			for (PlayerUI playerUI : playerUiFragments) {

				playerUI.updateSongInfo(curSong);
				playerUI.play();

			}

			for (ListContentFragment listContentFragment : listContentFragments) {
				listContentFragment.load();
			}
			// loaded = true;
			States.appState = APP_RUNNING;
			// }
			break;
		case APP_STOPPED:
			// Helper.makeToastTake("Stopped",
			// MusicPlayerMainActivity.getActivity());
			// stopTimer();
			// States.appState = APP_STOPPED;
		default:

		}

	}

	public ListContentFragment getListContentFragment(String classString) {
		for (ListContentFragment listContentFragment : listContentFragments) {
			if (listContentFragment.getClass().toString().equals(classString)) {
				return listContentFragment;
			}
		}
		return null;
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
			CategoryListAdapter.getInstance(ALBUM).update();
			CategoryListAdapter.getInstance(ARTIST).update();
			break;
		case QUEUE_CHANGED:
			for (ListAdapter arrayAdapter : adapters) {

				if (arrayAdapter instanceof QueueSongAdapter) {
					((QueueSongAdapter) arrayAdapter).updateSongs();
				}

			}
			getListContentFragment(QueueFragment.class.toString()).update();
			break;
		case PLAYLIST_CHANGED:
			CategoryTitlesListAdapter.getInstance(PLAYLIST).updateCategory();
			CategoryListContentFragment.getInstance(PLAYLIST).update();
			break;
		case ITEM_IN_PLAYLIST_CHANGED:
			if (SongsInCateAdapter.getInstance(PLAYLIST) != null) {
				SongsInCateAdapter.getInstance(PLAYLIST).update();
			}
			if (CategoryListContentFragment.getInstance(PLAYLIST) != null) {
				CategoryListContentFragment.getInstance(PLAYLIST).update();
			}

			break;
		case ALBUM_CHANGED:

			CategoryListContentFragment.getInstance(ALBUM).update();
			break;
		case ITEM_IN_ALBUM_CHANGED:
			if (SongsInCateAdapter.getInstance(ALBUM) != null) {
				SongsInCateAdapter.getInstance(ALBUM).update();
			}
			if (CategoryListContentFragment.getInstance(ALBUM) != null) {
				CategoryListContentFragment.getInstance(ALBUM).update();
			}

			break;

		default:
			break;
		}

	}

	
	
}
