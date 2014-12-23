package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SongAdapter extends ArrayAdapter<Song> {
	private SongAdapter(Context context, int resource) {
		super(context, resource);

		songs = SongController.getInstance().getSongs();

		instance = this;
	}

	public static SongAdapter instance = null;
	private ArrayList<Song> songs;

	public static SongAdapter getInstance() {
		if (instance == null) {
			new SongAdapter(MainActivity.getActivity().getApplicationContext(),
					R.layout.tab_songs_view);
		}
		return instance;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		if(v==null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater
					.inflate(R.layout.song_view, null);
		}
		BasicFunctions.ResizeImageView(MainActivity.screenWidth / 10,
				(ImageView) v.findViewById(R.id.song_image));
		TextView title = (TextView) v.findViewById(R.id.song_title);
		title.setText(songs.get(position).getTitle());
		TextView subtitle = (TextView) v.findViewById(R.id.song_subtitle);
		subtitle.setText(songs.get(position).getArtist()+" | "+songs.get(position).getAlbum());
		return v;
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
}
