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
		int MUSIC_NEW_SONG = 3;
		
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
	
	interface TabContant{
		int ARTISTS = 0;
		int ALBUMS = 1;
		int SONGS = 2;
		int PLAYLISTS = 3;
		int GENRES = 4;
		int SUB_GENRES = 5;
		int USERS = 6;
		int ABOUT_US = 7;
		String DEFAULT_ID = "deafault_id";
	}

	interface Models {

		int ARTIST = 0;
		int PLAYLIST = 1;
		int ALBUM = 2;
		int OFFLINE = 3;
		int QUEUE = 4;
		//		int FAVORITE = 5;
		int SIZE = 5;
	}
	interface SoundCloudExploreConstant{
		
		
		String TRENDING_MUSIC_LINK = "https://api-v2.soundcloud.com/explore/Popular%2BMusic?tag=out-of-experiment&limit=10&offset=0&linked_partitioning=1";
		String TRENDING_AUDIO_LINK = "https://api-v2.soundcloud.com/explore/popular%2Baudio?tag=out-of-experiment&limit=10&offset=0&linked_partitioning=1";
		String ALTERNATIVE_ROCK_LINK ="https://api-v2.soundcloud.com/explore/alternative%2Brock?tag=out-of-experiment&limit=10&offset=0&linked_partitioning=1";
		String AMBIENT_LINK = "https://api-v2.soundcloud.com/explore/ambient?tag=out-of-experiment&limit=10&offset=0&linked_partitioning=1";
		String CLASSICAL_LINK = "https://api-v2.soundcloud.com/explore/classical?tag=out-of-experiment&limit=10&offset=0&linked_partitioning=1";
		String COUNTRY_LINK  = "https://api-v2.soundcloud.com/explore/country?tag=out-of-experiment&limit=10&offset=0&linked_partitioning=1";
		String DANCE_EDM_LINK = "https://api-v2.soundcloud.com/explore/dance%2B%26%2Bedm?tag=out-of-experiment&limit=10&offset=0&linked_partitioning=1";
		String DEEP_HOUSE_LINK = "https://api-v2.soundcloud.com/explore/deep%2Bhouse?tag=out-of-experiment&limit=10&offset=0&linked_partitioning=1";
		String DISCO_LINK = "https://api-v2.soundcloud.com/explore/disco?tag=out-of-experiment&limit=10&offset=0&linked_partitioning=1";
		String DRUM_BASS_LINK = "https://api-v2.soundcloud.com/explore/drum%2B%26%2Bbass?tag=out-of-experiment&limit=10&offset=0&linked_partitioning=1";
		String DUBSTEP_LINK = "https://api-v2.soundcloud.com/explore/dubstep?tag=out-of-experiment&limit=10&offset=0&linked_partitioning=1";
		String DANCE_HALL_LINK = "https://api-v2.soundcloud.com/explore/dancehall?tag=out-of-experiment&limit=10&offset=0&linked_partitioning=1";
		String ELECTRONIC_LINK = "https://api-v2.soundcloud.com/explore/electronic?tag=out-of-experiment&limit=10&offset=0&linked_partitioning=1";
		String FOLK_LINK = "https://api-v2.soundcloud.com/explore/folk%2B%26%2Bsinger-songwriter?tag=out-of-experiment&limit=10&offset=0&linked_partitioning=1";
	}
	
	interface PlaylistConstant{
		String PLAYLIST_ID = "id";
		String PLAYLIST_TITLE = "title";
		String PLAYLIST_CREATED_AT = "created_at";
		String PLAYLIST_CREATOR_ID = "user_id";
		String PLAYLIST_CREATOR = "user";
		String PLAYLIST_PERMALINK_URL = "permalink_url";
		String PLAYLIST_ARTWORK_URL = "artwork_url";
		String PLAYLIST_DESCRIPTION = "description";
		String PLAYLIST_DURATION = "duration";
		
				
		
	}
	
	interface DatabaseConstant{
		int DATABASE_VERSION = 1;
		 String DATABASE_NAME = "musicplayer";
		// Contacts table name
				String LOGIN_TABLE_NAME = "login";
				 String SONG_TABLE_NAME = "song";
				 String ARTIST_TABLE_NAME = "artist";
				
				

				// Contacts Table Columns names
				 String LOGIN_KEY_ID = "id";
				 String LOGIN_KEY_TOKEN  = "token";
				
				
				/*
				 * User table columns names
				 */
				 String SONG_KEY_ID = "id";
				 String SONG_KEY_TITLE = "title";
				 String SONG_KEY_ARTWORK_URL = "artwork_url";
				 String SONG_KEY_STREAM_URL = "stream_url";

				 String SONG_KEY_TAG = "tag";
				 String SONG_KEY_ARTIST = "artist";
				 String SONG_KEY_GERNE = "gerne";
				 String SONG_KEY_DURATION = "duration";


				
				
				

				// Artist Table Columns names
				 String ARTIST_KEY_ID = "id";
				 String ARTIST_KEY_USERNAME = "username";
				 String ARTIST_KEY_FULLNAME = "fullname";
				 String ARTIST_KEY_ARTWORK_URL = "artwork";
				 String ARTIST_KEY_CITY = "city";
				 String ARTIST_KEY_COUNTRY = "country";
				
	}
}
