package ngo.music.soundcloudplayer.entity;

import java.io.File;
import java.io.IOException;

import ngo.music.soundcloudplayer.api.Stream;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;

public abstract class Song implements Comparable <Song>{
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title;
	}
	/**
	 * id if get from local
	 */
	protected String id = "";
	/**
	 * Artist
	 */
	protected String artist = "";
	/**
	 * Album
	 */
	protected String album = "";
	/**
	 * link if get from local 
	 */
	protected String link = "";
	/**
	 * Title
	 */
	protected String title =  "";

	/**
	 * Gerne
	 */
	protected String genre =  "";
	/**
	 * Artwork Url
	 */
	protected String artworkUrl = "";
	/**
	 * TagList
	 */
	protected String tagList;
	
	public void setArtworkUrl (String artworkUrl){
		this.artworkUrl = artworkUrl;
	}
	
	public String getArtworkUrl (){
		return artworkUrl;
	}
	
	
	
	
	
	public Song() {
		// TODO Auto-generated constructor stub
	}
	

	public Song(String id, String title, String artist,String album, String link) {
		// TODO Auto-generated constructor stub
		setId(id);
		setTitle(title);
		setArtist(artist);
		setLink(link);
		setAlbum(album);
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * @return the link
	 * @throws IOException 
	 */
	public abstract String getLink();


	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}




	public String getArtist() {
		return artist;
	}


	public void setArtist(String artist) {
		this.artist = artist;
	}


	public String getAlbum() {
		return album;
	}


	public void setAlbum(String album) {
		this.album = album;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
	

	
	/**
	 * @return the tagList
	 */
	public String getTagList() {
		return tagList;
	}


	/**
	 * @param tagList the tagList to set
	 */
	public void setTagList(String tagList) {
		this.tagList = tagList;
	}
}
