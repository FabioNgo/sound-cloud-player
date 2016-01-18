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
import ngo.music.player.Controller.MusicPlayerServiceController;
import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.R;
import ngo.music.player.ViewHolder.SongInListViewHolder;
import ngo.music.player.View.MusicPlayerMainActivity;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;
import ngo.music.player.service.MusicPlayerService;

public abstract class LiteListSongAdapter extends ArrayAdapter<Song> implements Constants.Models,Observer {
	protected View rootView;
	protected Context context;
	protected int resource;
	protected ArrayList<Song> songs;
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
		final Song song = songs.get(position);
		this.viewHolder = viewHolder;
		/**
		 * Set avatar for song
		 */

		ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

		Helper.setImageViewSize(
				Helper.dpToPx(50, getContext()),
				Helper.dpToPx(50, getContext()), viewHolder.avatar);
//		String artworkUrl = "";

//		Drawable drawable = context.getResources().getDrawable(
//				R.drawable.ic_launcher);
//
//		viewHolder.avatar.

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

		viewHolder.title.setText(song.getName());

		/*
		 * Set sub title
		 */

		viewHolder.subtitle.setText(song.getArtist());
		/**
		 * Set background , to indicate which song is playing
		 */
		if (song.equals(MusicPlayerServiceController.getInstance().getCurrentSong())) {

			viewHolder.background.setBackgroundResource(R.color.primary_light);
		} else {

			viewHolder.background
					.setBackgroundResource(R.color.background_material_light);

		}
		/**
		 * Set time
		 */
		try{
			viewHolder.duration.setText(Helper.toFormatedTime(Long.parseLong(songs.get(position).getDuration())));
		}catch (Exception e){
			e.printStackTrace();
		}


	}

	@Override
	public Song getItem(int position) {
		return songs.get(position);
	}

	@Override
	public int getCount() {
		return songs.size();
	}

	public void updateSongs() {
		// TODO Auto-generated method stub
		songs = getSongs();
		this.notifyDataSetChanged();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		songs.clear();
	}

	public ArrayList<String> getSongIds() {
		ArrayList<String> result = new ArrayList<>();
		for (Song song : songs) {
			result.add(song.getId());
		}
		return result;

	}


	public void update(Observable observable, Object data) {
		if(observable instanceof ModelManager) {
			this.songs = getSongsFromData(data);
			notifyDataSetChanged();
		}

	}

	protected abstract ArrayList<Song> getSongsFromData(Object data);

	public abstract ArrayList<Song> getSongs();
	public abstract int getSongMenuId();
}
