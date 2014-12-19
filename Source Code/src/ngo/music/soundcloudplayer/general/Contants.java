package ngo.music.soundcloudplayer.general;

import java.net.URI;

/**
 * Store the contants of app
 * @author LEBAO_000
 *
 */
public interface Contants {

	public static final String CLIENT_ID ="2ae2cf32fdd87967a353b7e718d7f3cd";
	public static final String CLIENT_SECRET = "acfe1a475e4d004214fed3326a01eb79";
	public static final URI REDIRECT_URI     = URI.create("http://localhost:8000");
	
	public static final int FACEBOOK_USER = 0;
	public static final int GOOGLE_PLUS_USER = 1;
	public static final int SOUNDCLOUD_USER = 2;
	
	interface UserContant{
		String USER  = "user";
		String USERNAME = "username";
		String ID = "id";
		String PERMALINK = "permalink";
		String URI = "uri";
		String PERMALINK_URL = "permalink_url";
		String AVATAR_URL = "avatar_url";
		String COUNTRY = "country";
		String CITY = "city";
		String FULLNAME = "full_name";
		String DESCRIPTION = "description";
		String DISCOGS_NAME = "discogs_name";
		String MYSPACE_NAME = "myspace_name";
		String WEBSITE = "website";
		String WEBSITE_TITLE = "website_title";
		String ONLINE = "online";
		String TRACK_COUNT = "track_count";
		String PLAYLIST_COUNT = "playlist_count";
		String FOLLOWERS_COUNT = "followers_count";
		String FOLLOWINGS_COUNT = "followings_count";
		String PUBLIC_FAVORITES_COUNT  = "public_favorites_count";
		String AVATAR_DATA = "avatar_data";
		String PLAN = "plan";
		String PRIVATE_TRACK_COUNT = "private_tracks_count";
		String PRIVATE_PLAYLISTS_COUNT = "private_playlists_count";
		String PRIMARY_EMAIL_CONFIRMED = "primary_email_confirmed";
		
	}
	
	interface UIContant{
		String LAYOUT_WIDTH = "layout_width";
		String LAYOUT_HEIGHT = "layout_height";
	}
}
