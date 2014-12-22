package ngo.music.soundcloudplayer.interfaces;

import android.support.v4.app.Fragment;

public interface UiInterface {
	/**
	 * update Title of the song
	 */
	void updateTitle(String title);
	/**
	 * update sub Title of the song
	 */
	void updateSubTitle(String subTitle);
	/**
	 * update music progress Bar
	 */
	void updateMusicProgressBar(double percentage);
	
	
	
}
