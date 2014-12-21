package ngo.music.soundcloudplayer.entity;

import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;

public class Song {
	public Song(Cursor c) {
		super();
		setTitle(c.getString(c.getColumnIndex(Media.TITLE)));
		setId((c.getString(c.getColumnIndex(Media._ID))));
		setLink(c.getString(c.getColumnIndex(Media.DATA)));
	}
	private String link;
	private String title;
	private String id;
	public Song() {
		// TODO Auto-generated constructor stub
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
