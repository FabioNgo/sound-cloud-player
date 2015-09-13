package ngo.music.player.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ngo.music.player.helper.Constants;

public class DatabaseCreate extends SQLiteOpenHelper implements Constants.DatabaseConstant {

	// Database Version
		//private static final int DATABASE_VERSION= 1;

		
		// Database Name
//		private static final String DATABASE_NAME = "musicplayer";
//
//		// Contacts table name
//		private static final String LOGIN_TABLE_NAME = "login";
//		private static final String SONG_TABLE_NAME = "song";
//		private static final String ARTIST_TABLE_NAME = "artist";
//		
		private static DatabaseCreate mInstance = null;
//
//		// Contacts Table Columns names
//		private static final String LOGIN_KEY_ID = "id";
//		private static final String LOGIN_KEY_TOKEN  = "token";
//		
//		
//		/*
//		 * User table columns names
//		 */
//		private static final String SONG_KEY_ID = "id";
//		private static final String SONG_KEY_TITLE = "title";
//		private static final String SONG_KEY_ARTWORK_URL = "artwork_url";
//		private static final String SONG_KEY_STREAM_URL = "stream_url";
//
//		private static final String SONG_KEY_TAG = "tag";
//		private static final String SONG_KEY_ARTIST = "artist";
//		private static final String SONG_KEY_GERNE = "gerne";
//		private static final String SONG_KEY_DURATION = "duration";


		
		
		

		// Artist Table Columns names
//		private static final String ARTIST_KEY_ID = "id";
//		private static final String ARTIST_KEY_USERNAME = "username";
//		private static final String ARTIST_KEY_FULLNAME = "fullname";
//		private static final String ARTIST_KEY_ARTWORK_URL = "artwork";
//		private static final String ARTIST_KEY_CITY = "city";
//		private static final String ARTIST_KEY_COUNTRY = "country";
		
		public DatabaseCreate(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

			
		}

		// Creating Tables
		@Override
		public void onCreate(SQLiteDatabase db) {
			
		}
		
		public void createTables(){
			try{
				SQLiteDatabase db = this.getWritableDatabase();
				/*
				 * Create database for logged in
				 */
	//			String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
	//					+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TOKEN + " TEXT"+ ");";
				String CREATE_LOGIN_TABLE = "CREATE TABLE " + LOGIN_TABLE_NAME + "("
						+ LOGIN_KEY_ID + " INTEGER PRIMARY KEY, " + LOGIN_KEY_TOKEN + " TEXT" + ");";
				
				
				/*
				 * Create databse for artist
				 */
				String CREATE_ARTIST_TABLE = "CREATE TABLE " + ARTIST_TABLE_NAME + "("
						+ ARTIST_KEY_ID + " TEXT PRIMARY KEY, " + ARTIST_KEY_USERNAME + " TEXT, "
						+ ARTIST_KEY_ARTWORK_URL + " TEXT, " 
						+ ARTIST_KEY_CITY + " TEXT, " + ARTIST_KEY_COUNTRY + " TEXT, "+ ARTIST_KEY_FULLNAME + " TEXT);";
				
	
				
				/*
				 * Create database for song
				 */
				String CREATE_SONG_TABLE = "CREATE TABLE " + SONG_TABLE_NAME + "("
						+ SONG_KEY_ID + " TEXT PRIMARY KEY, " + SONG_KEY_TITLE + " TEXT, "
						+ SONG_KEY_ARTWORK_URL + " TEXT, " + SONG_KEY_DURATION  + " INTEGER, " 
						+ SONG_KEY_ARTIST + " TEXT, " + SONG_KEY_GERNE + " TEXT, " + SONG_KEY_STREAM_URL + " TEXT, "
						+ SONG_KEY_TAG + " TEXT);";
				db.execSQL(CREATE_SONG_TABLE);
				db.execSQL(CREATE_LOGIN_TABLE);
				db.execSQL(CREATE_ARTIST_TABLE);
			}catch (Exception e){
				return;
			}
		}
		// Upgrading database
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Drop older table if existed
			db.execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + SONG_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ARTIST_TABLE_NAME);
			// Create tables again
			createTables();
		}

		
		public static DatabaseCreate getInstance(Context ctx) {

			    // Use the application context, which will ensure that you 
			    // don't accidentally leak an Activity's context.
			    // See this article for more information: http://bit.ly/6LRzfx
			    if (mInstance == null) {
			      mInstance = new DatabaseCreate(ctx);
			    }
			    return mInstance;
		}
		
		


}
