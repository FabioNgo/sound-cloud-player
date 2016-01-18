package ngo.music.player.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.todddavies.components.progressbar.ProgressWheel;
import com.volley.api.AppController;

import ngo.music.player.Controller.MusicPlayerServiceController;
import ngo.music.player.Model.Song;
import ngo.music.player.R;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;
import ngo.music.player.service.MusicPlayerService;

public class LitePlayerUI extends PlayerUI implements Constants.MusicService {

	NetworkImageView image;
	ImageView playPauseBtn;
	protected ProgressWheel musicProgressBar;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.liteplayer, container, false);
		iniMusicProgressBar();

		image = (NetworkImageView) rootView
				.findViewById(R.id.lite_player_image);
		Helper.setImageViewSize(container.getLayoutParams().height,
				container.getLayoutParams().height, image);
		playPauseBtn = (ImageView)rootView.findViewById(R.id.player_play_pause_btn);
		playPauseBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MusicPlayerService.getInstance().startPause();
			}
		});

		updateSongInfo(MusicPlayerServiceController.getInstance().getCurrentSong());
		return rootView;
	}
	protected void iniMusicProgressBar() {
		musicProgressBar = (ProgressWheel) rootView
				.findViewById(R.id.player_progress_bar);

	}
	@Override
	protected Runnable setRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				double ratio = (MusicPlayerService.getInstance().getCurrentTime() * 100.0) / MusicPlayerServiceController.getInstance().getDuration();
				musicProgressBar.setProgressDegree((int) (ratio * 3.6));
			}
		};
	}

	@Override
	public void updateTitle(Song song) {
		// TODO Auto-generated method stub
		String title = "";
		if (song != null) {
			title = song.getName();
		}
		TextView title_text = (TextView) rootView
				.findViewById(R.id.lite_player_title);
		title_text.setText(title);

	}

	@Override
	public void updateSubtitle(Song song) {
		String subtitle = "";
		if (song != null) {

			subtitle = song.getArtist() + " | " + song.getAlbum();
		}
		if(subtitle.equals(" | ")){
			subtitle = "";
		}
		TextView subtitle_text = (TextView) rootView
				.findViewById(R.id.lite_player_subtitle);
		subtitle_text.setText(subtitle);
	}

	@Override
	public void updateImage(Song song) {
		// TODO Auto-generated method stub

		ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
		image.setDefaultImageResId(R.drawable.ic_launcher);
		if (song != null) {
//			image.setImageUrl(song.getArtworkUrl(), mImageLoader);
		}
	}


	@Override
	protected void updateOtherInfo(Song song) {
		// TODO Auto-generated method stub
	}


	@Override
	public void stop() {
		playPauseBtn.setImageResource(android.R.drawable.ic_media_play);
	}

	@Override
	public void play() {
		super.play();
		playPauseBtn.setImageResource(android.R.drawable.ic_media_pause);
	}

	@Override
	public void pause() {
		playPauseBtn.setImageResource(android.R.drawable.ic_media_play);
	}
}
