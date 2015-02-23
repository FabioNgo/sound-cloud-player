package ngo.music.soundcloudplayer.controller;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.adapters.ListSongAdapter;
import ngo.music.soundcloudplayer.entity.SCSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


public class ListViewOnItemClickHandler implements OnItemClickListener  {

	public ListViewOnItemClickHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
		// TODO Auto-generated method stub
		ListSongAdapter adapter = (ListSongAdapter) arg0.getAdapter();
		adapter.notifyDataSetChanged();
		//System.out.println ("ON CLICK");
		//Song songSelected = (Song) songsList.getAdapter().getItem(position);
		ArrayList<Song> songs = adapter.getSongs();
		//SongController songController = SongController.getInstance();
		//songs = songController.resolvedPlaylist(songs);
		//String streamUrl = songController.getStreamUrl(songs.get(position));

//		MusicPlayerService.getInstance().setSongsPlaying(songs);
		MusicPlayerService.getInstance().playNewSong(position,songs);

		
	}
	

}
