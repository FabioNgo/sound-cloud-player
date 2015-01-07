package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.volley.api.AppController;

public class LitePlayerUI extends PlayerUI {

	NetworkImageView image;

	public LitePlayerUI() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		hasTextTime = false;
		musicProgressBar_id = R.id.lite_player_progress_bar;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.liteplayer, container, false);
		iniMusicProgressBar();

		image = (NetworkImageView) rootView
				.findViewById(R.id.lite_player_image);
		BasicFunctions.setImageViewSize(container.getLayoutParams().height,
				container.getLayoutParams().height, image);

		UIController.getInstance().addUiFragment(this);
		return rootView;
	}

	@Override
	public void updateTitle(Song song) {
		// TODO Auto-generated method stub
		String title = song.getTitle();
		TextView title_text = (TextView) rootView
				.findViewById(R.id.lite_player_title);
		title_text.setText(title);
	}

	@Override
	public void updateSubtitle(Song song) {
		// TODO Auto-generated method stub
		String subtitle = song.getArtist() + " | " + song.getAlbum();
		TextView subtitle_text = (TextView) rootView
				.findViewById(R.id.lite_player_subtitle);
		subtitle_text.setText(subtitle);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

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

}
