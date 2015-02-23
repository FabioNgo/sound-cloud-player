package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.volley.api.AppController;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.ViewHolder.SongInListViewHolder;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.MenuController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

public abstract class LiteListSongAdapter extends ArrayAdapter<Song> implements OnMenuItemClickListener,OnClickListener {
	protected View v;
	protected Context context;
	protected int resource;
	protected ArrayList<Song> songs;
	protected Song song;
	SongInListViewHolder viewHolder=null;
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
		v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.song_in_list, parent,false);
			viewHolder = new SongInListViewHolder(v);
			v.setTag(viewHolder);
		}else{
			viewHolder = (SongInListViewHolder) v.getTag();
		}

		setLayoutInformation(position,viewHolder, v);
		return v;
	}

	/**
	 * @param position
	 * @param v
	 */
	public void setLayoutInformation(int position,final SongInListViewHolder viewHolder, View v) {
		 song = songs.get(position);
		 this.viewHolder = viewHolder;
		/**
		 * Set avatar for song
		 */

		ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

		BasicFunctions.setImageViewSize(
				BasicFunctions.dpToPx(50, getContext()),
				BasicFunctions.dpToPx(50, getContext()), viewHolder.avatar);
		String artworkUrl = song.getArtworkUrl();
		if (artworkUrl.equals("")) {
			Log.i("AVA", "no image url");
			Drawable drawable = context.getResources().getDrawable(R.drawable.ic_launcher);
			
			viewHolder.avatar.setImageDrawable(drawable);
		} else {
			
			viewHolder.avatar.setImageUrl(song.getArtworkUrl(), mImageLoader);
		}
		/**
		 * set menu
		 */
		
		viewHolder.menu.setOnClickListener(this);
		/*
		 * Set title
		 */
		
		viewHolder.title.setText(song.getTitle());

		/*
		 * Set sub title
		 */
		
		viewHolder.subtitle.setText(song.getArtist());
		/**
		 * Set background , to indicate which song is playing
		 */
		if (MusicPlayerService.getInstance().getCurrentSongId()
				.compareTo(songs.get(position).getId()) == 0) {
			
			viewHolder.background.setBackgroundResource(R.color.primary_light);
		} else {
			
			viewHolder.background.setBackgroundResource(R.color.background_material_light);

		}
		/**
		 * Set time
		 */
		String html = "<b>" + BasicFunctions.milisecondToMinuteOffset(song.getDuration())
		+ "</b>" + "<br>" + BasicFunctions.milisecondToSecondOffset(song.getDuration());
		viewHolder.duration.setText(Html.fromHtml(html));
		
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
		ArrayList<String> result = new ArrayList<String>();
		for (Song song : songs) {
			result.add(song.getId());
		}
		return result;

	}
	public abstract ArrayList<Song> getSongs();
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		PopupMenu popup = new PopupMenu(MusicPlayerMainActivity.getActivity(), viewHolder.menu);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
            .inflate(R.menu.song_list_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);

        popup.show(); //showing popup menu
	}
}
