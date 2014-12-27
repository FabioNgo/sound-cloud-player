package ngo.music.soundcloudplayer.boundary;

import com.todddavies.components.progressbar.ProgressWheel;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.UpdateUiFromServiceController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Choreographer.FrameCallback;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

public class LitePlayerUI extends PlayerUI {

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
		
		ImageView image = (ImageView) rootView.findViewById(R.id.lite_player_image);
		BasicFunctions.ResizeImageView(container.getLayoutParams().height, image);
		UpdateUiFromServiceController.getInstance().addUiFragment(this);
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
		String subtitle = song.getArtist()+" | "+song.getAlbum();
		TextView subtitle_text = (TextView) rootView
				.findViewById(R.id.lite_player_subtitle);
		subtitle_text.setText(subtitle);
	}
	
	

}
