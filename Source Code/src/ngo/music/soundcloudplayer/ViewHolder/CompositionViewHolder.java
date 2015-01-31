package ngo.music.soundcloudplayer.ViewHolder;

import ngo.music.soundcloudplayer.R;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CompositionViewHolder {

	// TODO Auto-generated constructor stubpublic class ViewHolder {

	public TextView[] items;
	public ImageView menu;
	public CompositionViewHolder(int size,View v) {
		items = new TextView[size + 1];
		items[0] = (TextView) v
				.findViewById(R.id.composition_view_cat);
		items[1] = (TextView) v.findViewById(R.id.item_1);
		items[2] = (TextView) v.findViewById(R.id.item_2);
		items[3] = (TextView) v.findViewById(R.id.item_3);
		items[4] = (TextView) v.findViewById(R.id.item_4);
		items[5] = (TextView) v.findViewById(R.id.item_5);
		menu = (ImageView)v.findViewById(R.id.composition_view_menu);
	}

}
