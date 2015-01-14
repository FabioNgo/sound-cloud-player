package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.todddavies.components.progressbar.ProgressWheel;

public abstract class CompositionListAdapter extends ArrayAdapter<String>
		implements Constants.Categories {
	private View v;

	Context context;
	int resource;
	private ArrayList<String> categories;

	public CompositionListAdapter(Context context, int resource) {
		super(context, resource);

		this.categories = getCategories();
		this.context = context;
		this.resource = resource;

	}

	/**
	 * get categories in list of item sets
	 * 
	 * @return categories
	 */
	protected abstract ArrayList<String> getCategories();

	/**
	 * get item from specific categories
	 * 
	 * @param cat
	 *            : category
	 * @return list of songs
	 */
	protected abstract ArrayList<Song> getItemsFromCat(String cat);

	public static CompositionListAdapter instance = null;

	public static CompositionListAdapter getInstance() {

		return instance;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.composition_view, null);

		}

		setLayoutInformation(position, v);
		return v;
	}

	/**
	 * @param position
	 * @param v
	 */
	private void setLayoutInformation(int position, View v) {
		String cat = categories.get(position);
		ArrayList<Song> songs = getItemsFromCat(cat);
		String[] titles = { "1. ", "2. ", "3. ", "4. ", "5. " };
		// final Song song = songs.get(position);
		/**
		 * Set avatar for song
		 */
		// ImageView avatar = (ImageView)
		// v.findViewById(R.id.lite_player_image);
		//
		// ImageLoader mImageLoader =
		// AppController.getInstance().getImageLoader();
		// avatar.setMinimumHeight(MainActivity.screenHeight/5);
		// avatar.setMinimumWidth(MainActivity.screenHeight/5);

		// avatar.setImageUrl(song.getArtworkUrl(), mImageLoader);
		/**
		 * set menu
		 */
		// final ImageView menu = (ImageView) v.findViewById(R.id.song_menu);
		// menu.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// PopupMenu popup = new
		// PopupMenu(MusicPlayerMainActivity.getActivity(), menu);
		// //Inflating the Popup using xml file
		// popup.getMenuInflater()
		// .inflate(R.menu.song_list_menu, popup.getMenu());
		//
		// //registering popup with OnMenuItemClickListener
		// popup.setOnMenuItemClickListener(new
		// PopupMenu.OnMenuItemClickListener() {
		//
		//
		// @Override
		// public boolean onMenuItemClick(MenuItem arg0) {
		// // TODO Auto-generated method stub
		// switch (arg0.getItemId()) {
		// case R.id.list_addQueue:
		// MusicPlayerService.getInstance().addSongToQueue(song);
		// break;
		// case R.id.list_playNext:
		// MusicPlayerService.getInstance().addToNext(song);
		// break;
		// case R.id.list_addToPlaylist:
		// break;
		// case R.id.list_delete:
		// SongController.getInstance().deleteSong(song);
		// break;
		// default:
		// break;
		// }
		//
		// return false;
		// }
		// });
		//
		// popup.show(); //showing popup menu
		// }
		// });
		/*
		 * Set song title1
		 */
		TextView item1 = (TextView) v.findViewById(R.id.item_1);

		try {
			titles[0] += songs.get(0).getTitle();
		} catch (Exception e) {

		}
		item1.setText(titles[0]);
		/*
		 * Set song title2
		 */
		TextView item2 = (TextView) v.findViewById(R.id.item_2);

		try {
			titles[1] += songs.get(1).getTitle();
		} catch (Exception e) {

		}
		item2.setText(titles[1]);
		/*
		 * Set song title3
		 */
		TextView item3 = (TextView) v.findViewById(R.id.item_3);

		try {
			titles[2] += songs.get(2).getTitle();
		} catch (Exception e) {

		}
		item3.setText(titles[2]);
		/*
		 * Set song title4
		 */
		TextView item4 = (TextView) v.findViewById(R.id.item_4);

		try {
			titles[3] += songs.get(3).getTitle();
		} catch (Exception e) {

		}
		item4.setText(titles[3]);
		/*
		 * Set song title1
		 */
		TextView item5 = (TextView) v.findViewById(R.id.item_5);

		try {
			titles[4] += songs.get(4).getTitle();
		} catch (Exception e) {

		}
		item5.setText(titles[4]);

	}

	@Override
	public String getItem(int position) {
		return categories.get(position);
	}

	@Override
	public int getCount() {
		return categories.size();
	}

}
