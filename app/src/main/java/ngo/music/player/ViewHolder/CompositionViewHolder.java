package ngo.music.player.ViewHolder;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ngo.music.player.R;

public class CompositionViewHolder {

	// TODO Auto-generated constructor stubpublic class ViewHolder {

	public TextView[] items;
	public ImageView menu;
	public EditText editText;
	public ImageView editBtn;
	public ImageView submitBtn;
	public ImageView clearBtn;
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
		editText = (EditText)v.findViewById(R.id.composition_view_cat_edit);
		editBtn = (ImageView)v.findViewById(R.id.composition_view_edit);
		submitBtn = (ImageView)v.findViewById(R.id.composition_view_submit);
		clearBtn = (ImageView)v.findViewById(R.id.composition_view_cancel);
	}

}
