package ngo.music.player.View;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

	protected ViewGroup rootView = null;

	Runnable runnable;
	
	public PlayerUI() {
		// TODO Auto-generated constructor stub
		
		/**
		 * Runnable for timer
		 */
		runnable = setRunnable();
		MusicPlayerServiceController.getInstance().addObserver(this);
	}

	/**
	 * Setup runnable for updating ui while music is playing
	 * @return
	 */
	protected abstract Runnable setRunnable();
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

	@Override
	public void updateMusicProgress() {
		runnable.run();
	}



	@Override
	public void play() {
		runnable.run();
	}


	/**
	 * Update Image
	 * 
	 * @param
	 */
	protected abstract void updateImage(Song song);


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
					// start play song
					case MUSIC_NEW_SONG:


						updateSongInfo(MusicPlayerServiceController.getInstance().getCurrentSong());
						play();
						break;
					case MUSIC_RESUME:
						resume();
					case MUSIC_PROGRESS:

						updateMusicProgress();
						break;
					case MUSIC_PAUSE:
						pause();


						break;
					case MUSIC_CUR_POINT_CHANGED:
						updateMusicProgress();
						break;
					case MUSIC_STOPPED:
						stop();
						break;

				}
			}
		}
	}

	@Override
	public void resume(){
		runnable.run();
	}
}
