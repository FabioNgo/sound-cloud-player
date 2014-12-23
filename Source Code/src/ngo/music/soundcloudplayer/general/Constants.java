package ngo.music.soundcloudplayer.general;

import java.net.URI;

/**
 * Store the contants of app
 * @author LEBAO_000
 *
 */
public interface Constants {

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
		String COMMENT_COUNT = "comment_count";
		String CREATED_WITH = "created_with";
		String ATTACHMENTS_URI = "attachments_uri";
		String ASSET_DATA = "asset_data";
		String ARTWORK_DATA  = "artwork_data";
		
		String TRACKS = "tracks";
	}
	interface MusicService {
		int MUSIC_START = 0;
		int MUSIC_STOP = 1;
		int MUSIC_PAUSE = 2;
		int MUSIC_NEW_SONG = 3;
	}
}
