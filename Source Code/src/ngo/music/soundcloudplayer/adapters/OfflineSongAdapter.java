package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.ViewHolder.SongInListViewHolder;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.boundary.fragments.CategoryAddingFragment;
import ngo.music.soundcloudplayer.boundary.fragments.PlaylistAddingFragment;
import ngo.music.soundcloudplayer.controller.MenuController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Context;
import android.support.v4.app.FragmentManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.todddavies.components.progressbar.ProgressWheel;

public class OfflineSongAdapter extends ArrayAdapter<Song> {
	private View v;

	Context context;
	int resource;

	public OfflineSongAdapter(Context context, int resource) {
		super(context, resource);

		songs = SongController.getInstance().getOfflineSongs(true);
		this.context = context;
		this.resource = resource;

	}

	public static OfflineSongAdapter instance = null;
	private ArrayList<Song> songs;

	public static OfflineSongAdapter getInstance() {

		if (instance == null) {
			instance = createNewInstance();
		}
		return instance;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SongInListViewHolder viewHolder = null;
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
	private void setLayoutInformation(int position,final SongInListViewHolder viewHolder, View v) {
		
		final Song song = songs.get(position);
		/**
		 * Set avatar for song
		 */
//		 ImageView avatar = (ImageView)
//		 v.findViewById(R.id.song_image);
		//
		// ImageLoader mImageLoader =
		// AppController.getInstance().getImageLoader();
		// avatar.setMinimumHeight(MainActivity.screenHeight/5);
		// avatar.setMinimumWidth(MainActivity.screenHeight/5);

		// avatar.setImageUrl(song.getArtworkUrl(), mImageLoader);
		/**
		 * set menu
		 */
		
		viewHolder.menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PopupMenu popup = new PopupMenu(MusicPlayerMainActivity.getActivity(), viewHolder.menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                    .inflate(R.menu.song_list_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    

					@Override
					public boolean onMenuItemClick(MenuItem arg0) {
						// TODO Auto-generated method stub
						switch (arg0.getItemId()) {
						case R.id.list_addQueue:
							MusicPlayerService.getInstance().addSongToQueue(song);
							break;
						case R.id.list_playNext:
							MusicPlayerService.getInstance().addToNext(song);
							break;
						case R.id.list_addToPlaylist:
							ArrayList<Song> songs = new ArrayList<Song>();
							songs.add(song);
							MenuController.getInstance().addToPlaylist(songs);
							break;
						case R.id.list_delete:
							SongController.getInstance().deleteSong(song);
							break;
						default:
							break;
						}
						
						return false;
					}
                });

                popup.show(); //showing popup menu
			}
		});
		/*
		 * Set title
		 */
		
		viewHolder.title.setText(song.getTitle());

		/*
		 * Set sub title
		 */
		
		viewHolder.subtitle.setText(song.getArtist() + " | " + song.getAlbum());
		/**
		 * Set progress bar
		 */
		if (MusicPlayerService.getInstance().getCurrentSongId()
				.compareTo(songs.get(position).getId()) == 0) {
			
			viewHolder.background.setBackgroundResource(R.color.primary_light);
		} else {
			
			viewHolder.background.setBackgroundResource(R.color.background_material_light);

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

	public ArrayList<Song> getSongs() {
		return songs;

	}

	public ArrayList<String> getSongIds() {
		ArrayList<String> result = new ArrayList<String>();
		for (Song song : songs) {
			result.add(song.getId());
		}
		return result;

	}

	public static OfflineSongAdapter createNewInstance() {
		// TODO Auto-generated method stub
		instance = new OfflineSongAdapter(MusicPlayerMainActivity.getActivity()
				.getApplicationContext(), R.layout.list_view);
		return instance;
	}

	@Override
	public void add(Song song) {
		// TODO Auto-generated method stub
		if (!(song instanceof OfflineSong)) {

		} else {
			songs.add((OfflineSong) song);
		}

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		songs.clear();
	}

	public void updateSongs() {
		// TODO Auto-generated method stub
		songs = SongController.getInstance().getOfflineSongs(true);
		this.notifyDataSetChanged();
	}
	
}
