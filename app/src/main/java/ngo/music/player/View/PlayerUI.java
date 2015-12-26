package ngo.music.player.View;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.todddavies.components.progressbar.ProgressWheel;

import java.util.Observable;
import java.util.Observer;

import ngo.music.player.Controller.MusicPlayerServiceController;
import ngo.music.player.Model.Song;
import ngo.music.player.R;
import ngo.music.player.adapters.LiteListSongAdapter;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;
import ngo.music.player.helper.States;
import ngo.music.player.service.MusicPlayerService;

public abstract class PlayerUI extends Fragment implements Comparable<PlayerUI>, PlayUIInterface,Observer, Constants.MusicService,Constants.Appplication{
	public static int numberPlayerLoading= 0;
	protected ProgressWheel musicProgressBar;
	protected View rootView = null;
	TextView currentTimeText;
	TextView durationText;
	Runnable runnable;
	
	public PlayerUI() {
		// TODO Auto-generated constructor stub
		
		/**
		 * Runnable for timer
		 */
		runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (hasTextTime()) {
					currentTimeText.setText(Helper.toFormatedTime(MusicPlayerService.getInstance()
							.getCurrentTime()));
					durationText.setText(Helper.toFormatedTime(MusicPlayerServiceController.getInstance()
							.getDuration()));
				}
			}
		};
		MusicPlayerServiceController.getInstance().addObserver(this);
	}
	
	/**
	 * Player UI has text time or not
	 * @return
	 */
	protected abstract boolean hasTextTime();
	/**
	 * update Title of the song
	 */
	protected abstract void updateTitle(Song song);

	/**
	 * update sub Title of the song
	 */
	protected abstract void updateSubtitle(Song song);


	@Override
	public void updateSongInfo(Song song) {

		if (song != null) {
//			System.out.println("TITLE SONG : " + song.getTitle());
//			System.out.println("BEGIN UPDATE SONG");
			updateOtherInfo(song);
//			System.out.println("AFTER OTHER INFO");
			updateTitle(song);
//			System.out.println("AFTER TITLE");
			updateSubtitle(song);
//			System.out.println("AFTER SUB TITLE");

			updateImage(song);
//			System.out.println("END UPDATE SONG");
		}

	}

	protected abstract void updateOtherInfo(Song song);

	/**
	 * update music progress Bar and Displayed Time
	 */
	@Override
	public void updateMusicProgress() {
		if (MusicPlayerService.getInstance().isPlaying()) {
			play();
		}
		runnable.run();
	}

	@Override
	public void pause() {

	}

	@Override
	public void play() {
		runnable.run();
	}

	protected void iniMusicProgressBar() {
		musicProgressBar = (ProgressWheel) rootView
				.findViewById(R.id.player_progress_bar);

		musicProgressBar
				.setBackgroundResource(R.drawable.ic_media_play_progress);
		musicProgressBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerService.getInstance().startPause();
			}
		});
	}

	public ProgressWheel getProgressBar() {
		return musicProgressBar;
	}

	@Override
	public abstract void update();

	/**
	 * Update Image
	 * 
	 * @param
	 */
	protected abstract void updateImage(Song song);

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
		if (hasTextTime()) {
			currentTimeText.setText(Helper.toFormatedTime(0));
			durationText.setText(Helper.toFormatedTime(0));
		}

	}
	@Override
	public int compareTo(PlayerUI another) {
		// TODO Auto-generated method stub
		return this.getClass().toString().compareTo(another.getClass().toString());
	}

	@Override
	public void update(Observable observable, Object data) {

		if(observable instanceof MusicPlayerServiceController){
			if(data instanceof Song){
				Song song = (Song) data;

				updateSongInfo(song);
			}else {
				int TAG = (int) data;

				switch (TAG) {
					case MUSIC_PLAYING:

						musicProgressBar.setBackgroundResource(R.drawable.ic_media_pause_progress);
						updateSongInfo(MusicPlayerServiceController.getInstance().getCurrentSong());
						play();

						break;
					case MUSIC_PROGRESS:
						double ratio = (MusicPlayerService.getInstance().getCurrentTime() * 100.0) / MusicPlayerServiceController.getInstance().getDuration();
						musicProgressBar.setProgressDegree((int) (ratio * 3.6));
						updateMusicProgress();
						break;
					case MUSIC_PAUSE:
						pause();
						musicProgressBar.setBackgroundResource(R.drawable.ic_media_play_progress);

						break;
					case MUSIC_CUR_POINT_CHANGED:

						int degree = (int) Math.round(360
								* (double) MusicPlayerService.getInstance().getCurrentTime()
								/ MusicPlayerServiceController.getInstance().getDuration());
						updateMusicProgress();
						musicProgressBar.setProgressDegree(degree);
						break;
					case MUSIC_STOPPED:

						stop();
						musicProgressBar.setBackgroundResource(R.drawable.ic_media_play_progress);

						break;
				}
			}
		}
	}
}
