package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.todddavies.components.progressbar.ProgressWheel;

public abstract class PlayerUI extends Fragment {
	protected ProgressWheel musicProgressBar;

	TextView currentTimeText;
	TextView durationText;
	Runnable runnable;
	protected int musicProgressBar_id = -1;
	protected boolean hasTextTime = false;
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
				if (hasTextTime) {
					currentTimeText.setText(UIController
							.getInstance().getCurrentTime());
					durationText.setText(UIController
							.getInstance().getDuration());
				}
			}
		};
	}
	
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
	 * @param song
	 */
	public void updateSongInfo(Song song){
		updateTitle(song);
		updateSubtitle(song);
		updateImage(song);
		updateOtherInfo(song);
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
		updateTitle(MusicPlayerService.getInstance().getCurrentSong());
		updateSubtitle(MusicPlayerService.getInstance().getCurrentSong());
		runnable.run();
	}
	protected void iniMusicProgressBar(){
		musicProgressBar = (ProgressWheel) rootView
				.findViewById(musicProgressBar_id);
		musicProgressBar.setBackgroundResource(R.drawable.ic_media_play);
		musicProgressBar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerService.getInstance().startPause();
			}
		});
	}
	public ProgressWheel getProgressBar(){
		return musicProgressBar;
	}
	/**
	 * Implement other update
	 */
	public abstract void update();
	/**
	 * Update Image
	 * @param currentSong
	 */
	protected abstract void updateImage(Song song);
}
