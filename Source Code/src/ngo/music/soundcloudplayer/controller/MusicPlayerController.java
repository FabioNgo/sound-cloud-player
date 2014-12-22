package ngo.music.soundcloudplayer.controller;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.todddavies.components.progressbar.ProgressWheel;

public class MusicPlayerController implements OnClickListener{
	private static MusicPlayerController instance;
	private ArrayList<ProgressWheel> musicProgressBars;
	private MusicPlayerService musicPlayerService;
	private CountDownTimer timer;
	private MusicPlayerController() {
		// TODO Auto-generated constructor stub
		if (instance == null) {
			instance = this;
			musicProgressBars = new ArrayList<ProgressWheel>();
			musicPlayerService = MusicPlayerService.getInstance();
		}
	}

	public static MusicPlayerController getInstance() {
		if (instance == null) {
			new MusicPlayerController();
		}
		return instance;
	}

	public void addMusicProgressBar(ProgressWheel musicProgressBar) {
		musicProgressBar.setOnClickListener(this);
		musicProgressBars.add(musicProgressBar);
		
	}

	private void pause() {
		// TODO Auto-generated method stub
		for (ProgressWheel musicProgressBar : musicProgressBars) {
			musicProgressBar.setBackgroundResource(R.drawable.ic_media_play);
		}

		musicPlayerService.pause();
		timer.cancel();

		// double percentage = mediaPlayer.getCurrentPosition()
		// / mediaPlayer.getDuration();
		// musicProgressBar.setProgressPercentage(percentage);
	}

	private void play() {
		// TODO Auto-generated method stub

		musicPlayerService.playCurrentSong();
		for (final ProgressWheel musicProgressBar : musicProgressBars) {
			musicProgressBar
					.setBackgroundResource(android.R.drawable.ic_media_pause);
			timer = new CountDownTimer(musicPlayerService.getDuration()
					- musicPlayerService.getCurrentTime(), 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					// TODO Auto-generated method stub

					musicProgressBar
							.setProgressPercentage((double) musicPlayerService
									.getCurrentTime()
									/ musicPlayerService.getDuration());

					Log.i("Time", musicPlayerService.getCurrentTime() + " of "
							+ musicPlayerService.getDuration());
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					musicProgressBar.setProgressPercentage(1.0);
				}
			};
			timer.start();
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v instanceof ProgressWheel) {
			if (!musicPlayerService.isPlaying()) {
				play();
			} else {
				pause();
			}
		}
	}
	public String getPlayingSongID() {
		return musicPlayerService.getSongID();
	}
}
