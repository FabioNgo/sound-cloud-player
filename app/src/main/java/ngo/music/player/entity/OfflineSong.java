package ngo.music.player.entity;


import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;

public class OfflineSong extends Song {
	public OfflineSong(Cursor c) {
		super(c.getString(c.getColumnIndex(Media._ID)),
				c.getString(c.getColumnIndex(Media.TITLE)),
				c.getString(c.getColumnIndex(Media.ARTIST)),
				c.getString(c.getColumnIndex(Media.ALBUM)),
				c.getString(c.getColumnIndex(Media.DATA)),
				c.getLong(c.getColumnIndex(Media.DURATION)));

	}

	public OfflineSong(String id, String title, String artist, String album,
			String link,long duration) {
		super(id, title, artist, album, link,duration);
		// TODO Auto-generated constructor stub
		setArtworkUrl("");
	}

	@Override
	public int compareTo(Song arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLink() {
		// TODO Auto-generated method stub
		return link;
	}

}
