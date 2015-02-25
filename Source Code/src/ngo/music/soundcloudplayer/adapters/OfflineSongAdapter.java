package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.ViewHolder.SongInListViewHolder;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.CategoryAddingFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.PlaylistAddingFragment;
import ngo.music.soundcloudplayer.controller.MenuController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
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

public class OfflineSongAdapter extends LiteListSongAdapter {
	
	public static OfflineSongAdapter instance = null;

	public static OfflineSongAdapter getInstance() {

		if (instance == null) {
			instance = createNewInstance();
		}
		return instance;
	}
	public OfflineSongAdapter(Context context, int resource) {
		super(context, resource);


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
	public ArrayList<Song> getSongs() {
		// TODO Auto-generated method stub
		return SongController.getInstance().getOfflineSongs(true);
	}
	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
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
	
}
