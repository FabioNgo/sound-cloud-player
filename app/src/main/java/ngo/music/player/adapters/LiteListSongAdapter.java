/**
 * Display Single song in song list view
 */
package ngo.music.player.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.volley.api.AppController;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ngo.music.player.Controller.MenuController;
import ngo.music.player.Model.Song;
import ngo.music.player.R;
import ngo.music.player.ViewHolder.SongInListViewHolder;
import ngo.music.player.boundary.MusicPlayerMainActivity;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;
import ngo.music.player.service.MusicPlayerService;

public abstract class LiteListSongAdapter extends ArrayAdapter<Song> implements Constants.Models,Observer {
	protected View rootView;
	protected Context context;
	protected int resource;
	protected Song[] songs;
	SongInListViewHolder viewHolder = null;

	public LiteListSongAdapter(Context context, int resource) {
		super(context, resource);
		this.context = context;
		this.resource = resource;
		songs = getSongs();
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		rootView = convertView;
		if (rootView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rootView = inflater.inflate(R.layout.song_in_list, parent, false);
			viewHolder = new SongInListViewHolder(rootView);
			rootView.setTag(viewHolder);
		} else {
			viewHolder = (SongInListViewHolder) rootView.getTag();
		}

		setLayoutInformation(position, viewHolder, rootView);
		return rootView;
	}

	/**
	 * @param position
	 * @param v
	 */
	public void setLayoutInformation(int position,
			final SongInListViewHolder viewHolder, View v) {
		final Song song = songs[position];
		this.viewHolder = viewHolder;
		/**
		 * Set avatar for song
		 */

		ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

		Helper.setImageViewSize(
				Helper.dpToPx(50, getContext()),
				Helper.dpToPx(50, getContext()), viewHolder.avatar);
		String artworkUrl = "";
		if (artworkUrl.equals("")) {
			Log.i("AVA", "no image url");
			Drawable drawable = context.getResources().getDrawable(
					R.drawable.ic_launcher);

			viewHolder.avatar.setImageDrawable(drawable);
		}
		/**
		 * set menu
		 */

		viewHolder.menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PopupMenu popup = new PopupMenu(MusicPlayerMainActivity
						.getActivity(), v);
				// Inflating the Popup using xml file
				popup.getMenuInflater().inflate(getSongMenuId(),
						popup.getMenu());

				// registering popup with OnMenuItemClickListener
				Song[] songs = new Song[1];
				songs[0] = song;
				popup.setOnMenuItemClickListener(MenuController
						.getInstance(songs));

				popup.show(); // showing popup menu
			}
		});
		/*
		 * Set title
		 */

		viewHolder.title.setText(song.getAttribute("title"));

		/*
		 * Set sub title
		 */

		viewHolder.subtitle.setText(song.getAttribute("artist"));
		/**
		 * Set background , to indicate which song is playing
		 */
		if (MusicPlayerService.getInstance().getCurrentSongId()
				.compareTo(songs[position].getId()) == 0) {

			viewHolder.background.setBackgroundResource(R.color.primary_light);
		} else {

			viewHolder.background
					.setBackgroundResource(R.color.background_material_light);

		}
		/**
		 * Set time
		 */

		viewHolder.duration.setText(Helper.toFormatedTime(Long.parseLong(songs[position].getAttribute("duration"))));

	}

	@Override
	public Song getItem(int position) {
		return songs[position];
	}

	@Override
	public int getCount() {
		return songs.length;
	}

	public void updateSongs() {
		// TODO Auto-generated method stub
		songs = getSongs();
		this.notifyDataSetChanged();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		songs = new Song[0];
	}

	public ArrayList<String> getSongIds() {
		ArrayList<String> result = new ArrayList<String>();
		for (Song song : songs) {
			result.add(song.getId());
		}
		return result;

	}

	@Override
	public void update(Observable observable, Object data) {
		ArrayList<Song> newData = (ArrayList<Song>) data;
		newData.toArray(this.songs);
		notifyDataSetChanged();

	}

	public abstract Song[] getSongs();
	public abstract int getSongMenuId();
}
