package ngo.music.soundcloudplayer.ViewHolder;

import com.android.volley.toolbox.NetworkImageView;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SCSongViewHolder {
	public RelativeLayout songDetail;
	public TextView likeCount;
	public RelativeLayout likeCountLayout;
	public TextView playBack;
	public RelativeLayout download;
	public RelativeLayout share;
	public TextView title;
	public TextView subtitle;
	public TextView gerne;
	public ImageView menu;
	public NetworkImageView avatar;

	public SCSongViewHolder(View v) {
		// TODO Auto-generated constructor stub
		songDetail = (RelativeLayout) v.findViewById(R.id.song_info_field);
		likeCount = (TextView) v.findViewById(R.id.like_count_id);
		likeCountLayout = (RelativeLayout) v
				.findViewById(R.id.likes_count_field);
		playBack = (TextView) v.findViewById(R.id.play_count_id);
		download = (RelativeLayout) v.findViewById(R.id.download_field);
		share = (RelativeLayout) v.findViewById(R.id.share_field);
		title = (TextView) v.findViewById(R.id.song_title);
		subtitle = (TextView) v.findViewById(R.id.song_subtitle);
		gerne = (TextView) v.findViewById(R.id.song_gerne);
		menu = (ImageView) v.findViewById(R.id.song_menu);
		/**
		 * avatar
		 */
		avatar = (NetworkImageView) v.findViewById(R.id.song_image);
		avatar.setDefaultImageResId(R.drawable.ic_launcher);
		avatar.setMinimumHeight(MusicPlayerMainActivity.screenHeight/5);
		avatar.setMinimumWidth(MusicPlayerMainActivity.screenHeight/5);
		avatar.setMaxHeight(MusicPlayerMainActivity.screenHeight/5);
		avatar.setMaxWidth(MusicPlayerMainActivity.screenHeight/5);
	}
}
