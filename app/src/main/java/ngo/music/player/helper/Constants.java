package ngo.music.player.helper;

import java.net.URI;

/**
 * Store the contants of app
 * @author LEBAO_000
 *
 */
public interface Constants {

	String CLIENT_ID ="2ae2cf32fdd87967a353b7e718d7f3cd";
	String CLIENT_SECRET = "acfe1a475e4d004214fed3326a01eb79";
	URI REDIRECT_URI     = URI.create("http://vnntu.com/home/callback.html");
	String DEFAULT_TOKEN  = "1-89385-17404897-36e93ca8ded37dc";
	
	
	String TRACK_LINK = "https://api.soundcloud.com/tracks";
	String USER_LINK = "http://api.soundcloud.com/users";
	String ME_FAVORITES = "/me/favorites";
	String ME_MY_STREAM = "/me/tracks";
	String PLAYLISTS_LINK = "api.soundcloud.com/playlists";
	String ME_PLAYLISTS = "/me/playlists";
	String ME_FOLLOWERS = "/me/followers";
	String ME_FOLLOWINGS = "/me/followings";
	
	int FACEBOOK_USER = 0;
	int GOOGLE_PLUS_USER = 1;
	int SOUNDCLOUD_USER = 2;
	
	interface UserContant{
		String USER  = "user";
		String USERNAME = "username";
		String ID = "id";
		String PERMALINK = "permalink";
		String URI_LINK = "uri";
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
		
		int LOGGED_IN = 0;
		int NOT_LOGGED_IN = 1;
		
	}
	interface XMLConstant{
		String XML_TAG_SONG = "song";
		String XML_TAG_ID = "id";
		String XML_TAG_TITLE = "title";
		String XML_TAG_ARTIST = "artist";
		String XML_TAG_LINK = "link";
		String XML_TAG_ALBUM = "author";
	}
	interface UIContant{
		String LAYOUT_WIDTH = "layout_width";
		String LAYOUT_HEIGHT = "layout_height";
		int TAB_PAGE_CONTAINER_ID = 1;
	}
	interface MusicProgressBar{
		int FULL_PLAYER_PROGRESSBAR_ID = 1;
		int LITE_PLAYER_PROGRESSBAR_ID = 2;
		int IN_LIST_VIEW_PLAYER_PROGRESSBAR_ID = 3;
	}
	
	interface SongConstants{
		String ID = "id";
		String CREATED_AT =   "created_at";
		String USER_ID = "user_id";
		String DURATION =   "duration";
		String COMMENTABLE = "commentable";
		String STATE =  "state";
		String SHARING = "sharing";
		String TAG_LIST = "tag_list";
		String PERMALINK = "permalink";
		String DESCRIPTION = "description";
		String STREAMABLE = "streamable";
		String DOWNLOADABLE = "downloadable";
		String GENRE = "genre";
		String RELEASE = "release";
		String PURCHASE_URL = "purchase_url";
		String LABEL_ID = "label_id";
		String LABEL_NAME = "label_name";
		String ISRC = "isrc";
		String VIDEO_URL = "video_url";
		String TRACK_TYPE = "track_type";
		String KEY_SIGNATURE = "key_signature";
		String BPM =   "bpm";
		String TITLE  =  "title";
		String RELEASE_YEAR = "release_year";
		String RELEASE_MONTH = "release_month";
		String RELEASE_DAY = "release_day";
		String FORMAT = "original_format";
		String CONTENT_SIZE = "original_content_size";
		String LICENSE = "license";
		String URI = "uri";
		String PERMALINK_URL = "permalink_url";
		String ARTWORK_URL = "artwork_url";
		String WAVEFORM_URL = "waveform_url";
		String USER = "user";
		String STREAM_URL = "stream_url";
		String DOWNLOAD_URL =   "download_url";
		String PLAYBACK_COUNT = "playback_count";
		String DOWNLOAD_COUNT = "download_count";
		String FOVORITINGS_COUNT = "favoritings_count";
		String LIKES_COUNT = "likes_count";
		String COMMENT_COUNT = "comment_count";
		String CREATED_WITH = "created_with";
		String ATTACHMENTS_URI = "attachments_uri";
		String ASSET_DATA = "asset_data";
		String ARTWORK_DATA  = "artwork_data";
		
		String TRACKS = "tracks";
	}
	interface Data{
		/**
		 * DATA CHANGE
		 */
		int QUEUE_CHANGED = 0;
		int OFFLINE_SONG_CHANGED = 1;
		int PLAYLIST_CHANGED = 2;
		int ALBUM_CHANGED = 3;
		int ITEM_IN_PLAYLIST_CHANGED = 4;
		int ITEM_IN_ALBUM_CHANGED = 5;
		int SC_PLAYLIST_CHANGED = 6;
		int ITEM_IN_SC_PLAYLIST_CHANGED = 7;

		int SC_SEARCH_PLAYLIST_CHANGED = 8;
		int ITEM_IN_SC_SEARCH_PLAYLIST_CHANGED = 9;

		int ARTIST_CHANGED = 10;
		int ITEM_IN_ARTIST_CHANGED = 11;

		
	}
	interface MusicService {
		/**
		 * MUSIC CONSTANTS
		 */
		int MUSIC_PLAYING = 0;
		int MUSIC_STOPPED = 1;
		int MUSIC_PAUSE = 2;
		int MUSIC_INTERUPTED = 3;
		int MUSIC_FREE = 4;
		int MUSIC_CUR_POINT_CHANGED = 5;
		int SERVICE_STOP = 6;
		int MUSIC_PROGRESS = 7;
		/**
		 * LOOP CONSTANTS
		 */
		int MODE_LOOP_ALL = 0;
		int MODE_LOOP_ONE = 1;
		/**
		 * NOTIFICATION ACTION
		 */
		String NOTI_ACTION_CANCEL = "cancel";
		String NOTI_ACTION_PLAY_PAUSE = "play/pause";
		String NOTI_ACTION_PREV = "prev";
		String NOTI_ACTION_NEXT = "next";
		String NOTI_ACTION_REW = "rewind";
		String NOTI_ACTION_FF = "fast forward";
		
		
	}
	interface Appplication{
		int APP_RUNNING =0;
		int APP_STOPPED = 1;
	}
	


	interface Models {

		int ARTIST = 3;
		int PLAYLIST = 1;
		int ALBUM = 2;
		int OFFLINE = 0;
		int QUEUE = 4;
		int ZING = 5;
		//		int FAVORITE = 5;
		int SIZE = 6;
		//int ZING = 6;
	}

}
