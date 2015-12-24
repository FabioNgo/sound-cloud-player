package ngo.music.player.adapters;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.CategoryManager;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.R;
import ngo.music.player.boundary.MusicPlayerMainActivity;

public class QueueSongAdapter extends LiteListSongAdapter {


	public static QueueSongAdapter instance = null;

	public QueueSongAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}

	public static QueueSongAdapter getInstance() {

		if (instance == null) {
			instance = new QueueSongAdapter(MusicPlayerMainActivity
					.getActivity().getApplicationContext(),
					R.layout.song_in_list);
		}
		return instance;
	}

	@Override
	public Song[] getSongs() {

		JSONObject[] array = ((CategoryManager) ModelManager.getInstance(QUEUE)).getSongsFromCategory("queue");
		Song[] result = new Song[array.length];

		for (int i = 0; i < array.length; i++) {
			try {
				result[i] = (Song) ModelManager.getInstance(QUEUE).get(array[i].getString("id"));
			} catch (JSONException e) {
				continue;
			}
		}
		return result;
	}

	@Override
	public int getSongMenuId() {
		// TODO Auto-generated method stub
		return R.menu.song_queue_menu;
	}



}
