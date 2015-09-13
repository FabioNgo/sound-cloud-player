package ngo.music.player.entity;

public abstract class User {

	/*
	 * username
	 */
	protected String username;
	
	/*
	 * Interger ID
	 */
	protected String id;
	
	/*
	 *Permalink of resource 
	 */
	protected String permalink;
	
	/*
	 * API resource URL
	 */
	protected String uri;
	
	/*
	 * URL to the SoundCloud.com page
	 */
	protected String permalinkUrl;
	
	/*
	 * URL to a JPEG image
	 */
	protected String avatarUrl;
	
	/*
	 * country
	 */
	protected String country= " ";
	
	/*
	 * first and last name	
	 */
	protected String fullName = " ";
	
	/*
	 * city
	 */
	protected String city = " ";
	
	/*
	 * description
	 */
	protected String description = "";
	
	/*
	 * Discogs name
	 */
	protected String discogsName = "";
	
	/*
	 * MySpace name
	 */
	protected String mySpaceName = "";
	
	/*
	 * a URL to the website
	 */
	protected String website = "";
	
	/*
	 * a custom title for the website
	 */
	protected String websiteTitle = "";
	
	/*
	 * online status (boolean)
	 */
	protected boolean online ;
	
	/*
	 * number of public tracks
	 */
	protected int trackCount;
	
	/*
	 * number of public playlist
	 */
	protected int playlistCount;
	
	/*
	 * number of followers
	 */
	protected int followersCount;
	
	/*
	 * number of followed user
	 */
	protected int followingCount;
	
	/*
	 * number of favorited public tracks
	 */
	protected int publicFavoriteCount;
	
	/*
	 * binary data of user avatar (only for uploading)
	 */
	protected byte avataData;
	
	/*
	 * subscription plan of the user
	 */
	protected String plan;
	
	/*
	 * number of protected tracks
	 */
	protected int privateTracksCount;
	
	/*
	 * number of protected playlists
	 */
	protected int privatePlaylistsCount;
	
	/*
	 * boolean if email is confirmed
	 */
	protected boolean primaryEmailConfirmed;
	
	public User() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param string the id to set
	 */
	public void setId(String string) {
		this.id = string;
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
	 * @return the avatarUrl
	 */
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
	 * @param avatarUrl the avatarUrl to set
	 */
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		if (country.compareTo("null") == 0 || country == null){
			return " ";
		}
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		if (fullName.compareTo("") == 0){
			return username;
		}else{
			return fullName;
		}
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		if (city.compareTo("null") == 0 || city == null){
			return " ";
		}
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
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
	 * @return the discogsName
	 */
	public String getDiscogsName() {
		return discogsName;
	}

	/**
	 * @param discogsName the discogsName to set
	 */
	public void setDiscogsName(String discogsName) {
		this.discogsName = discogsName;
	}

	/**
	 * @return the mySpaceName
	 */
	public String getMySpaceName() {
		return mySpaceName;
	}

	/**
	 * @param mySpaceName the mySpaceName to set
	 */
	public void setMySpaceName(String mySpaceName) {
		this.mySpaceName = mySpaceName;
	}

	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * @return the websiteTitle
	 */
	public String getWebsiteTitle() {
		return websiteTitle;
	}

	/**
	 * @param websiteTitle the websiteTitle to set
	 */
	public void setWebsiteTitle(String websiteTitle) {
		this.websiteTitle = websiteTitle;
	}

	/**
	 * @return the online
	 */
	public boolean isOnline() {
		return online;
	}

	/**
	 * @param online the online to set
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}

	/**
	 * @return the trackCount
	 */
	public int getTrackCount() {
		return trackCount;
	}

	/**
	 * @param trackCount the trackCount to set
	 */
	public void setTrackCount(int trackCount) {
		this.trackCount = trackCount;
	}

	/**
	 * @return the playlistCount
	 */
	public int getPlaylistCount() {
		return playlistCount;
	}

	/**
	 * @param playlistCount the playlistCount to set
	 */
	public void setPlaylistCount(int playlistCount) {
		this.playlistCount = playlistCount;
	}

	/**
	 * @return the followersCount
	 */
	public int getFollowersCount() {
		return followersCount;
	}

	/**
	 * @param followersCount the followersCount to set
	 */
	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	/**
	 * @return the followingCount
	 */
	public int getFollowingCount() {
		return followingCount;
	}

	/**
	 * @param followingCount the followingCount to set
	 */
	public void setFollowingCount(int followingCount) {
		this.followingCount = followingCount;
	}

	/**
	 * @return the publicFavoriteCount
	 */
	public int getPublicFavoriteCount() {
		return publicFavoriteCount;
	}

	/**
	 * @param publicFavoriteCount the publicFavoriteCount to set
	 */
	public void setPublicFavoriteCount(int publicFavoriteCount) {
		this.publicFavoriteCount = publicFavoriteCount;
	}

	/**
	 * @return the avataData
	 */
	public byte getAvataData() {
		return avataData;
	}

	/**
	 * @param avataData the avataData to set
	 */
	public void setAvataData(byte avataData) {
		this.avataData = avataData;
	}

	/**
	 * @return the plan
	 */
	public String getPlan() {
		return plan;
	}

	/**
	 * @param plan the plan to set
	 */
	public void setPlan(String plan) {
		this.plan = plan;
	}

	/**
	 * @return the privateTracksCount
	 */
	public int getPrivateTracksCount() {
		return privateTracksCount;
	}

	/**
	 * @param privateTracksCount the privateTracksCount to set
	 */
	public void setPrivateTracksCount(int privateTracksCount) {
		this.privateTracksCount = privateTracksCount;
	}

	/**
	 * @return the privatePlaylistsCount
	 */
	public int getPrivatePlaylistsCount() {
		return privatePlaylistsCount;
	}

	/**
	 * @param privatePlaylistsCount the privatePlaylistsCount to set
	 */
	public void setPrivatePlaylistsCount(int privatePlaylistsCount) {
		this.privatePlaylistsCount = privatePlaylistsCount;
	}

	/**
	 * @return the primaryEmailConfirmed
	 */
	public boolean isPrimaryEmailConfirmed() {
		return primaryEmailConfirmed;
	}

	/**
	 * @param primaryEmailConfirmed the primaryEmailConfirmed to set
	 */
	public void setPrimaryEmailConfirmed(boolean primaryEmailConfirmed) {
		this.primaryEmailConfirmed = primaryEmailConfirmed;
	}
	
	public String getNumFollowerString (){
		String temp = "";
		String tempStr = String.valueOf(followersCount);
		int start = tempStr.length()-1;
		
		while (start > 3){
			temp = "," + tempStr.substring(start-3,start ) + temp;
			start = start -3;
		}
		temp = tempStr.substring(0, start) + temp;

		return temp;
	}

	
}
