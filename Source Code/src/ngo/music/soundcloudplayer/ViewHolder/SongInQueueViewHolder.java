package ngo.music.soundcloudplayer.ViewHolder;

import ngo.music.soundcloudplayer.R;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.todddavies.components.progressbar.ProgressWheel;

public class SongInQueueViewHolder {
	public NetworkImageView avatar;
	public ImageView menu;
	public TextView title;
	public TextView subtitle;
	public RelativeLayout background;

	public SongInQueueViewHolder(View v) {
		// TODO Auto-generated constructor stub
		background = (RelativeLayout)v.findViewById(R.id.song_queue_background);
		avatar = (NetworkImageView) v.findViewById(R.id.song_queue_image);
		menu = (ImageView) v.findViewById(R.id.song_queue_menu);
		title = (TextView) v.findViewById(R.id.song_queue_title);
		subtitle = (TextView) v.findViewById(R.id.song_queue_subtitle);
		
	}
}