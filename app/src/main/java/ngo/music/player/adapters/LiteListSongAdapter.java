/**
 * Display Single song in song list view
 */
package ngo.music.player.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.volley.api.AppController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ngo.music.player.Controller.MenuController;
import ngo.music.player.Controller.MusicPlayerServiceController;
import ngo.music.player.Model.Model;
import ngo.music.player.Model.OfflineSong;
import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.R;
import ngo.music.player.View.MusicPlayerMainActivity;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;
import ngo.music.player.service.MusicPlayerService;

public abstract class LiteListSongAdapter extends RecyclerView.Adapter<LiteListSongAdapter.SongInListViewHolder> implements Constants.Models,Observer {
	public static RecyclerView.Adapter getInstance(int type) {
		switch (type){
			case OFFLINE:
				if(OfflineSongAdapter.instance == null){
					createInstance(type);
				}
				return OfflineSongAdapter.instance;
			case QUEUE:
				if(QueueSongAdapter.instance == null){
					createInstance(type);
				}
				return QueueSongAdapter.instance;
			case ZING:
				if(ZingSongAdapter.instance == null){
					createInstance(type);
				}
				return ZingSongAdapter.instance;
			default:
				return  null;
		}
	}
	public static RecyclerView.Adapter createInstance(int type) {
		switch (type){
			case OFFLINE:
				OfflineSongAdapter.instance = new OfflineSongAdapter();
				return OfflineSongAdapter.instance;
			case QUEUE:
				QueueSongAdapter.instance = new QueueSongAdapter();
				return QueueSongAdapter.instance;
			case ZING:
				ZingSongAdapter.instance = new ZingSongAdapter();
				return ZingSongAdapter.instance;
			default:
				return  null;
		}
	}

	public class SongInListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		public TextView position;
		public ImageView menu;
		public TextView title;
		public TextView subtitle;
		public TextView duration;
		
		private int menuId;
		private Song item;

		public SongInListViewHolder(View v, int menuID) {
			super(v);
			// TODO Auto-generated constructor stub
//			background = (AppCompatImageView)v.findViewById(R.id.song_background);

			position = (TextView) v.findViewById(R.id.song_position);
			menu = (ImageView) v.findViewById(R.id.song_menu);
			title = (TextView) v.findViewById(R.id.song_title);
			subtitle = (TextView) v.findViewById(R.id.song_subtitle);
			duration = (TextView)v.findViewById(R.id.song_duration);
			this.menuId =menuID;
			v.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			
			MusicPlayerService.getInstance().playNewSong(getAdapterPosition(),songs);
			return;
		}

		public void setItem(final Song item, int pos) {
			this.item = item;
			position.setText(String.valueOf(pos+1));
			title.setText(item.getName());
			subtitle.setText(item.getArtist());
			/**
			 * Set time
			 */
			try{
				duration.setText(Helper.toFormatedTime(item.getDuration()));
			}catch (Exception e){
				e.printStackTrace();
			}
			menu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					PopupMenu popup = new PopupMenu(MusicPlayerMainActivity
							.getActivity(), v);
					// Inflating the Popup using xml file
					popup.getMenuInflater().inflate(menuId,
							popup.getMenu());

					// registering popup with OnMenuItemClickListener
					ArrayList<Song> songs = new ArrayList<>();
					songs.add(item);
					popup.setOnMenuItemClickListener(MenuController
							.getInstance(songs));

					popup.show(); // showing popup menu
				}
			});
			/**
			 * Set background , to indicate which song is playing
			 */

			if (item.equals(MusicPlayerServiceController.getInstance().getCurrentSong())) {
				position.setTextColor(MusicPlayerMainActivity.getActivity().getResources().getColor(R.color.primary));
				position.setTypeface(Typeface.create("",Typeface.BOLD));

			} else {

				position.setTextColor(MusicPlayerMainActivity.getActivity().getResources().getColor(R.color.primary_text));
				position.setTypeface(Typeface.create("", Typeface.NORMAL));
			}


		}
	}
	LiteListSongAdapter(){
		type = setType();
		MusicPlayerServiceController.getInstance().addObserver(this);
		ModelManager.getInstance(type).addObserver(this);
		this.songs = getSongs();
	}

	protected abstract int setType();

	public ArrayList<Song> getSongs() {
		ArrayList<Model> temp = ModelManager.getInstance(type).getAll();
		//System.out.println ("LIST MODEL SIZE = " + temp.size());
		ArrayList<Song> songs = new ArrayList<>();
		for (Model model:temp) {
			songs.add((Song) model);
		}
		return songs;
	}

	protected ArrayList<Song> songs;
	private int type = -1;


	@Override
	public SongInListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = (LayoutInflater) parent.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rootView = inflater.inflate(R.layout.song_in_list, parent, false);
		SongInListViewHolder viewHolder = new SongInListViewHolder(rootView, getSongMenuId());
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(SongInListViewHolder holder, int position) {
		holder.setItem(songs.get(holder.getAdapterPosition()),position);
	}
	public void update(Observable observable, Object data) {
		if(observable instanceof ModelManager) {
			this.songs = getSongsFromData(data);
			notifyDataSetChanged();

		}

	}

	protected abstract ArrayList<Song> getSongsFromData(Object data);
	public abstract int getSongMenuId();

	@Override
	public int getItemCount() {
		return songs.size();
	}
}
