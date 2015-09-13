package ngo.music.player.boundary;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.todddavies.components.progressbar.ProgressWheel;

import ngo.music.player.entity.Song;
import ngo.music.player.helper.BasicFunctions;
import ngo.music.player.service.MusicPlayerService;
import ngo.music.player.R;

public abstract class PlayerUI extends Fragment implements Comparable<PlayerUI>{
	protected ProgressWheel musicProgressBar;
	public static int numberPlayerLoading= 0;
	TextView currentTimeText;
	TextView durationText;
	Runnable runnable;
	protected View rootView = null;
	
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
					currentTimeText.setText(BasicFunctions.toFormatedTime(MusicPlayerService.getInstance()
							.getCurrentTime()));
					durationText.setText(BasicFunctions.toFormatedTime(MusicPlayerService.getInstance()
							.getDuration()));
				}
			}
		};
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

	/**
	 * update info of song: Artist, Image, Description....
	 * 
	 * @param song
	 */
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
	public void updateMusicProgress() {
		if (MusicPlayerService.getInstance().isPlaying()) {
			play();
		}
		runnable.run();
	}

	public void pause() {

	}

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

	/**
	 * Implement other update
	 */
	public abstract void update();

	/**
	 * Update Image
	 * 
	 * @param
	 */
	protected abstract void updateImage(Song song);

	public void stop() {
		// TODO Auto-generated method stub
		
		if (hasTextTime()) {
			currentTimeText.setText(BasicFunctions.toFormatedTime(0));
			durationText.setText(BasicFunctions.toFormatedTime(0));
		}

	}
	@Override
	public int compareTo(PlayerUI another) {
		// TODO Auto-generated method stub
		return this.getClass().toString().compareTo(another.getClass().toString());
	}
}
