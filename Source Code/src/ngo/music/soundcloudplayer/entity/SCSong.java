package ngo.music.soundcloudplayer.entity;

import java.io.File;
import java.io.IOException;

import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Stream;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.database.ArtistSCDatabaseTable;
import ngo.music.soundcloudplayer.database.SCSongDatabaseTable;
import ngo.music.soundcloudplayer.general.Constants;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;

public class SCSong extends Song{


	public SCSong(String id , String title, String album,long duration){
		super (id, title,"",album,"",duration);
		
	}
	public SCSong(String id, String title, String artist, String album,
			String link,long duration) {
		
		super(id, title, artist, album, link,duration);
		//System.out.println (id + " " + title + " " );

		// TODO Auto-generated constructor stub
	}

	/**
	 * timestamp of creation
	 */
	private String createdAt =  "";
	
	/**
	 * user-id of the owner
	 */
	private String userId = "" ;
	/**
	 * mini user representation of the owner
	 */
	private User user;
	
	
	private boolean commentable;
	private String sharing =  "";
	/**
	 * permalink of the resource
	 */
	private String permalink =  "";
	private String description =  "";
	private boolean streamable;
	private boolean downloadable;
	private String release =  "";
	private String purchaseUrl =  "";
	private int labelID =  0;
	private String labelName =  "";
	private String videoUrl =  "";
	private String trackType =  "";
	private String keySignature =  "";
	private String artwork_url = "";
	private String privacy = "";
	/**
	 * beats per minute
	 */
	private int bpm;
	
	/**
	 * file format of the original file
	 */
	private String format;
	private int releaseYear;
	private int releaseMonth;
	private int releaseDay;
	
	/**
	 * size in bytes of the original file
	 */
	private long contentSize;
	private String license;
	private String uri;
	private String permalinkUrl;
	private String waveformUrl;
	private String streamUrl = null;
	private String resolvedStreamUrl;
	private String downloadUrl;
	private int playbackCount;
	private int downloadCount;
	private int favoriteCount;
	private int likesCount;
	private int commentCount;
	private Stream stream = null;
	/**
	 * binary data of the audio file
	 * only for uploading
	 */
	private File assetData;
	
	/**
	 * binary data of the artwork image
	 * only for uploading
	 */
	private File artworkData;
	@Override
	public String getLink() throws IOException {
		// TODO Auto-generated method stub
		/*
		 * If song is already in database
		 */
		System.out.println ("GET LINK ID = " + id) ;
		SCSongDatabaseTable songDb = SCSongDatabaseTable.getInstance(MusicPlayerMainActivity.getActivity());
		SCSong song = songDb.getSong(id);
		if (song != null){
			return songDb.getLink(id);
		}
		//String link = songDb.getSong(String.valueOf(id));
//		if (onlineSong != null){
//			return onlineSong;
//		}
		if (stream != null){
			
		}else{
		
//			String streamlink =  songDb.getLink(getId());
//			if (streamlink != null){
//				//link = streamlink;
//				System.out.println (streamlink);
//				return streamlink;
//			}
				
			
			SCUserController userController = SCUserController.getInstance();
			ApiWrapper wrapper = userController.getApiWrapper();
			wrapper.setToken(new Token(Constants.DEFAULT_TOKEN,"refresh_token"));
			
			
			stream = wrapper.resolveStreamUrl(link,false);
			//link = stream.streamUrl;
			songDb.addSong(this);
			
			
		}
		//System.out.println(stream.streamUrl);
		return stream.streamUrl;
		
		
	}
	

	/**
	 * @return the createdAt
	 */
	public String getCreatedAt() {
		return createdAt;
	}


	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}


	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}


	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}


	/**
	 * @return the user
	 */
	public SCAccount getUser() {
		return (SCAccount)user;
	}


	/**
	 * @param user the user to set
	 */
	public void setUser(SCAccount user) {
		this.user = user;
	}


	


	/**
	 * @return the commentable
	 */
	public boolean isCommentable() {
		return commentable;
	}


	/**
	 * @param commentable the commentable to set
	 */
	public void setCommentable(boolean commentable) {
		this.commentable = commentable;
	}


	/**
	 * @return the sharing
	 */
	public String getSharing() {
		return sharing;
	}


	/**
	 * @param sharing the sharing to set
	 */
	public void setSharing(String sharing) {
		this.sharing = sharing;
	}


	


	/**
	 * @return the permalink
	 */
	public String getPermalink() {
		return permalink;
	}


	/**
	 * @param permalink the permalink to set
	 */
	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the streamable
	 */
	public boolean isStreamable() {
		return streamable;
	}


	/**
	 * @param streamable the streamable to set
	 */
	public void setStreamable(boolean streamable) {
		this.streamable = streamable;
	}


	/**
	 * @return the downloadable
	 */
	public boolean isDownloadable() {
		return downloadable;
	}


	/**
	 * @param downloadable the downloadable to set
	 */
	public void setDownloadable(boolean downloadable) {
		this.downloadable = downloadable;
	}


	


	/**
	 * @return the release
	 */
	public String getRelease() {
		return release;
	}


	/**
	 * @param release the release to set
	 */
	public void setRelease(String release) {
		this.release = release;
	}


	/**
	 * @return the purchaseUrl
	 */
	public String getPurchaseUrl() {
		return purchaseUrl;
	}


	/**
	 * @param purchaseUrl the purchaseUrl to set
	 */
	public void setPurchaseUrl(String purchaseUrl) {
		this.purchaseUrl = purchaseUrl;
	}


	/**
	 * @return the labelID
	 */
	public int getLabelID() {
		return labelID;
	}


	/**
	 * @param labelID the labelID to set
	 */
	public void setLabelID(int labelID) {
		this.labelID = labelID;
	}


	/**
	 * @return the labelName
	 */
	public String getLabelName() {
		return labelName;
	}


	/**
	 * @param labelName the labelName to set
	 */
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}


	/**
	 * @return the videoUrl
	 */
	public String getVideoUrl() {
		return videoUrl;
	}


	/**
	 * @param videoUrl the videoUrl to set
	 */
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}


	/**
	 * @return the trackType
	 */
	public String getTrackType() {
		return trackType;
	}


	/**
	 * @param trackType the trackType to set
	 */
	public void setTrackType(String trackType) {
		this.trackType = trackType;
	}


	/**
	 * @return the keySignature
	 */
	public String getKeySignature() {
		return keySignature;
	}


	/**
	 * @param keySignature the keySignature to set
	 */
	public void setKeySignature(String keySignature) {
		this.keySignature = keySignature;
	}


	/**
	 * @return the bpm
	 */
	public int getBpm() {
		return bpm;
	}


	/**
	 * @param bpm the bpm to set
	 */
	public void setBpm(int bpm) {
		this.bpm = bpm;
	}


	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}


	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}


	/**
	 * @return the releaseYear
	 */
	public int getReleaseYear() {
		return releaseYear;
	}


	/**
	 * @param releaseYear the releaseYear to set
	 */
	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}


	/**
	 * @return the releaseMonth
	 */
	public int getReleaseMonth() {
		return releaseMonth;
	}


	/**
	 * @param releaseMonth the releaseMonth to set
	 */
	public void setReleaseMonth(int releaseMonth) {
		this.releaseMonth = releaseMonth;
	}


	/**
	 * @return the releaseDay
	 */
	public int getReleaseDay() {
		return releaseDay;
	}


	/**
	 * @param releaseDay the releaseDay to set
	 */
	public void setReleaseDay(int releaseDay) {
		this.releaseDay = releaseDay;
	}


	/**
	 * @return the contentSize
	 */
	public long getContentSize() {
		return contentSize;
	}


	/**
	 * @param contentSize the contentSize to set
	 */
	public void setContentSize(long contentSize) {
		this.contentSize = contentSize;
	}


	/**
	 * @return the license
	 */
	public String getLicense() {
		return license;
	}


	/**
	 * @param license the license to set
	 */
	public void setLicense(String license) {
		this.license = license;
	}


	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}


	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}


	/**
	 * @return the permalinkUrl
	 */
	public String getPermalinkUrl() {
		return permalinkUrl;
	}


	/**
	 * @param permalinkUrl the permalinkUrl to set
	 */
	public void setPermalinkUrl(String permalinkUrl) {
		this.permalinkUrl = permalinkUrl;
	}


	/**
	 * @return the waveformUrl
	 */
	public String getWaveformUrl() {
		return waveformUrl;
	}


	/**
	 * @param waveformUrl the waveformUrl to set
	 */
	public void setWaveformUrl(String waveformUrl) {
		this.waveformUrl = waveformUrl;
	}


	/**
	 * @return the streamUrl
	 */
	public String getStreamUrl() {
		return streamUrl;
	}


	/**
	 * @param streamUrl the streamUrl to set
	 */
	public void setStreamUrl(String streamUrl) {
		this.streamUrl = streamUrl;
	}


	/**
	 * @return the downloadUrl
	 */
	public String getDownloadUrl() {
		return downloadUrl;
	}


	/**
	 * @param downloadUrl the downloadUrl to set
	 */
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}


	/**
	 * @return the playbackCount
	 */
	public int getPlaybackCount() {
		return playbackCount;
	}


	/**
	 * @param playbackCount the playbackCount to set
	 */
	public void setPlaybackCount(int playbackCount) {
		this.playbackCount = playbackCount;
	}


	/**
	 * @return the downloadCount
	 */
	public int getDownloadCount() {
		return downloadCount;
	}


	/**
	 * @param downloadCount the downloadCount to set
	 */
	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}


	/**
	 * @return the favoriteCount
	 */
	public int getFavoriteCount() {
		return favoriteCount;
	}


	/**
	 * @param favoriteCount the favoriteCount to set
	 */
	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}


	/**
	 * @return the commentCount
	 */
	public int getCommentCount() {
		return commentCount;
	}


	/**
	 * @param commentCount the commentCount to set
	 */
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}


	/**
	 * @return the assetData
	 */
	public File getAssetData() {
		return assetData;
	}


	/**
	 * @param assetData the assetData to set
	 */
	public void setAssetData(File assetData) {
		this.assetData = assetData;
	}


	/**
	 * @return the artworkData
	 */
	public File getArtworkData() {
		return artworkData;
	}


	/**
	 * @param artworkData the artworkData to set
	 */
	public void setArtworkData(File artworkData) {
		this.artworkData = artworkData;
	}

	@Override
	public String getArtist(){
		try{
			if (user.getFullName() .compareTo("") != 0){
				return user.getFullName();
			}else{
				return user.getUsername();	
			}
		}catch (Exception e){
			//e.printStackTrace();
			return "";
		}
		
	}


	/**
	 * @return the likesCount
	 */
	public int getLikesCount() {
		return likesCount;
	}


	/**
	 * @param likesCount the likesCount to set
	 */
	public void setLikesCount(int likesCount) {
		this.likesCount = likesCount;
	}
	

	public String getPlaybackCountString(){
		String temp = "";
		String tempStr = String.valueOf(playbackCount);
		int start = tempStr.length()-1;
		
		while (start > 3){
			temp = "," + tempStr.substring(start-3,start ) + temp;
			start = start -3;
		}
		temp = tempStr.substring(0, start) + temp;

		return temp;
		
	}
	
	public String getLikeCountString(){
		String temp = "";
		String tempStr = String.valueOf(likesCount);
		int start = tempStr.length()-1;
		
		while (start > 3){
			temp = "," + tempStr.substring(start-3,start ) + temp;
			start = start -3;
		}
		temp = tempStr.substring(0, start) + temp;

		return temp;
		
	}


	@Override
	public String getId (){
		return id;
	}
	
	public void setId (int soundcloudID){
		id = String.valueOf(soundcloudID);
	}
	
	
	@Override
	public int compareTo(Song arg0) {
		// TODO Auto-generated method stub
		
		return id.compareTo(arg0.getId());
	}
	/**
	 * @return the privacy
	 */
	public String getPrivacy() {
		return privacy;
	}
	/**
	 * @param privacy the privacy to set
	 */
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	/**
	 * @return the resolvedStreamUrl
	 */
	public String getResolvedStreamUrl() {
		return resolvedStreamUrl;
	}
	/**
	 * @param resolvedStreamUrl the resolvedStreamUrl to set
	 */
	public void setResolvedStreamUrl(String resolvedStreamUrl) {
		this.resolvedStreamUrl = resolvedStreamUrl;
	}
	
	public Stream getStream(){
		return stream;
	}
	

}
