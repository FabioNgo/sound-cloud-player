package ngo.music.soundcloudplayer.entity;

import java.io.IOException;
import java.util.ArrayList;

import ngo.music.soundcloudplayer.controller.SongController;

public class Category {
	protected ArrayList<Song> songs;

	protected String title;
	private static final String separator = "\1";

	public Category(String title, ArrayList<Song> songs) {

		// TODO Auto-generated constructor stub

		this.title = title;
		this.songs = songs;
	}
	public Category(String title,Song song) {

		// TODO Auto-generated constructor stub

		this.title = title;
		this.songs = new ArrayList<Song>();
		songs.add(song);
	}
	public Category(String string) {

		String[] temp = string.split(separator);

		this.title = temp[0];
		this.songs = new ArrayList<Song>();
		for (int i = 1; i < temp.length; i++) {
			if (!temp[i].equals("")) {
				Song song = SongController.getInstance().getSong(temp[i]);
				if (song != null) {
					songs.add(song);
				}
			}
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub

		String titlePart = getTitle();
		String songsPart = "";
		if (!songs.isEmpty()) {
			for (int i = 0; i < songs.size(); i++) {
				songsPart += songs.get(i).getTitle();

				songsPart += separator;

			}
		}
		return titlePart + separator + songsPart;
	}

	public String toStoredString() {
		// TODO Auto-generated method stub

		String titlePart = title;
		String songsPart = "";
		for (int i = 0; i < songs.size(); i++) {
			songsPart += songs.get(i).getId();

			songsPart += separator;

		}
		return titlePart + separator + songsPart;
	}

	public ArrayList<Song> getSongs() {

		return songs;
	}

	public void addSongs(ArrayList<Song> songs) throws NumberFormatException, IOException {
		for (Song song1 : songs) {
			if (!this.songs.contains(song1)) {
				this.songs.add(song1);
			}
		}
	}

	public void addSong(Song song) throws NumberFormatException, IOException {

		if (!this.songs.contains(song)) {
			this.songs.add(song);
		}

	}

	public String getTitle() {
		return title;
	}

	public void removeSong(Song song) {
		for (int i = 0; i < songs.size(); i++) {
			if (songs.get(i).getId().equals(song.getId())) {
				songs.remove(i);
				return;
			}
		}
	}
	public void setTitle(String newName) {
		// TODO Auto-generated method stub
		this.title = newName;
	}
}
