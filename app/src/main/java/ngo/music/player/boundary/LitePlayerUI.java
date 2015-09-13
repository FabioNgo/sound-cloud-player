package ngo.music.player.boundary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.volley.api.AppController;

import ngo.music.player.controller.UIController;
import ngo.music.player.entity.Song;
import ngo.music.player.helper.BasicFunctions;
import ngo.music.player.helper.Constants;
import ngo.music.player.R;

public class LitePlayerUI extends PlayerUI implements Constants.MusicService {

	NetworkImageView image;

	


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		numberPlayerLoading++;
		rootView = inflater.inflate(R.layout.liteplayer, container, false);
		iniMusicProgressBar();

		image = (NetworkImageView) rootView
				.findViewById(R.id.lite_player_image);
		BasicFunctions.setImageViewSize(container.getLayoutParams().height,
				container.getLayoutParams().height, image);

		UIController.getInstance().addPlayerUiFragment(this);
		numberPlayerLoading--;
		return rootView;
	}

	@Override
	public void updateTitle(Song song) {
		// TODO Auto-generated method stub
		String title = "";
		if (song != null) {
			title = song.getTitle();
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
			image.setImageUrl(song.getArtworkUrl(), mImageLoader);
		}
	}

	@Override
	protected void updateOtherInfo(Song song) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean hasTextTime() {
		// TODO Auto-generated method stub
		return false;
	}

}
