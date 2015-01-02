package ngo.music.soundcloudplayer.entity;

import java.io.File;
import java.io.IOException;

import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Stream;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;

public class OnlineSong extends Song{

	/**
	 * timestamp of creation
	 */
	private String createdAt =  "";
	
	/**
	 * user-id of the owner
	 */
	private int userId =  0;
	/**
	 * mini user representation of the owner
	 */
	private User user;
	
	/**
	 * duration in milliseconds
	 */
	private long duration =  0;
	private boolean commentable;
	private String sharing =  "";
	private String tagList =  "";
	/**
	 * permalink of the resource
	 */
	private String permalink =  "";
	private String description =  "";
	private boolean streamable;
	private boolean downloadable;
	private String gerne =  "";
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
	private String streamUrl;
	private String resolvedStreamUrl;
	private String downloadUrl;
	private int playbackCount;
	private int downloadCount;
	private int favoriteCount;
	private int likesCount;
	private int commentCount;
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
	public String getLink() {
		// TODO Auto-generated method stub
		SoundCloudUserController userController = SoundCloudUserController.getInstance();
		ApiWrapper wrapper = userController.getApiWrapper();
		
		Stream stream = null;
		try {
			stream = wrapper.resolveStreamUrl(streamUrl,false);
			return stream.streamUrl;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
	public int getUserId() {
		return userId;
	}


	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}


	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}


	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}


	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}


	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
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
	 * @return the gerne
	 */
	public String getGerne() {
		return gerne;
	}


	/**
	 * @param gerne the gerne to set
	 */
	public void setGerne(String gerne) {
		this.gerne = gerne;
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
		if (user.getFullName() .compareTo("") != 0){
			return user.getFullName();
		}else{
			return user.getUsername();	
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


	/**
	 * @return the artwork_url
	 */
	public String getArtworkUrl() {
		return artwork_url;
	}


	/**
	 * @param artwork_url the artwork_url to set
	 */
	public void setArtworkUrl(String artwork_url) {
		this.artwork_url = artwork_url;
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

}
