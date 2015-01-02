package ngo.music.soundcloudplayer.entity;

import java.io.File;

import ngo.music.soundcloudplayer.api.Stream;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;

public class OfflineSong extends Song{
	public OfflineSong(Cursor c) {
		super();
		setTitle(c.getString(c.getColumnIndex(Media.TITLE)));
		setId((c.getString(c.getColumnIndex(Media._ID))));
		setLink(c.getString(c.getColumnIndex(Media.DATA)));
		setArtist(c.getString(c.getColumnIndex(Media.ARTIST)));
		setAlbum(c.getString(c.getColumnIndex(Media.ALBUM)));
	}

	public OfflineSong(String id, String title, String artist, String album,
			String link) {
		super(id,title,artist,album,link);
		// TODO Auto-generated constructor stub
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
