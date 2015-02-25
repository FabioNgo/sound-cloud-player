/**
 * Function:
 * 	Add a new row in websites table
	Returns all the rows as Website class objects
	Update existing row
	Returns single row
	Deletes a single row
	Check if a website is already existed
 */
package ngo.music.soundcloudplayer.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.entity.SCSong;
import ngo.music.soundcloudplayer.general.Constants;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SCSongDatabaseTable extends SQLiteOpenHelper implements Constants.DatabaseConstant {

	// Database Version
	//private static final int DATABASE_VERSION= 2;

	
	// Database Name
	private static final String DATABASE_NAME = "musicplayer";

	// Contacts table name
	private static final String SONG_TABLE_NAME = "song";
	
	private static SCSongDatabaseTable mInstance = null;

	// Contacts Table Columns names
//	private static final String SONG_KEY_ID = "id";
//	private static final String SONG_KEY_TITLE = "title";
//	private static final String KEY_ARTWORK_URL = "artwork_url";
//	private static final String KEY_STREAM_URL = "stream_url";
//
//	private static final String KEY_TAG = "tag";
//	private static final String KEY_ARTIST = "artist";
//	private static final String KEY_GERNE = "gerne";
//	private static final String KEY_DURATION = "duration";


	private SCSongDatabaseTable(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		
		
	}



	
	  public static SCSongDatabaseTable getInstance(Context ctx) {

		    // Use the application context, which will ensure that you 
		    // don't accidentally leak an Activity's context.
		    // See this article for more information: http://bit.ly/6LRzfx
		    if (mInstance == null) {
		      mInstance = new SCSongDatabaseTable(ctx);
		    }
		    //System.out.println ("DATA BASE  = " + mInstance);
		    return mInstance;
		  }
	  
	/**
	 * Adding a new song in song table Function will check if a song
	 * already existed in database. If existed will update the old one else
	 * creates a new row
	 * @throws IOException 
	 * */
	public void addSong(SCSong song) throws IOException {
		//TABLE_RSS = getTableName();
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SONG_KEY_ID, song.getId()); //song ID
		values.put(SONG_KEY_TITLE, song.getTitle()); // song title
		values.put(SONG_KEY_STREAM_URL, song.getStream().streamUrl); // song stream url
		values.put(SONG_KEY_ARTWORK_URL, song.getArtworkUrl()); // song artwork img
		values.put(SONG_KEY_ARTIST, song.getUser().getId()); // song ID OF ARTIST
		values.put(SONG_KEY_DURATION, song.getDuration());
		values.put(SONG_KEY_GERNE, song.getGenre()); // song gerne
		values.put(SONG_KEY_TAG, song.getTagList()); // song tag list
		System.out.println ("DATABASE = " + song.getId() );
		// Check if row already existed in database
		if (!isSongExists(db, song.getId())) {
			// site not existed, create a new row
			db.insert(SONG_TABLE_NAME, null, values);
			db.close();
		} else {
			// site already existed update the row
			updateSong(song);
			db.close();
		}
		
		ArtistSCDatabaseTable artistSCDatabaseTable = ArtistSCDatabaseTable.getInstance(MusicPlayerMainActivity.getActivity());
		artistSCDatabaseTable.addArtist(song.getUser());
	}



	/**
	 * Updating a single row row will be identified by rss link
	 * */
	public int updateSong(SCSong song) {
		
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SONG_KEY_ID, song.getId()); //song ID
		values.put(SONG_KEY_TITLE, song.getTitle()); // song title
		values.put(SONG_KEY_STREAM_URL, song.getStream().streamUrl); // song stream url
		values.put(SONG_KEY_ARTWORK_URL, song.getArtworkUrl()); // song artwork img
		values.put(SONG_KEY_ARTIST, song.getUserId()); // song ID OF ARTIST
		values.put(SONG_KEY_DURATION, song.getDuration());
		values.put(SONG_KEY_GERNE, song.getGenre()); // song gerne
		values.put(SONG_KEY_TAG, song.getTagList()); // song tag list
//		

		// updating row return
		int update = db.update(SONG_TABLE_NAME, values, SONG_KEY_ID + " = ?",
				new String[] { song.getId() });
//		int insert = (int) db.insert(SONG_TABLE_NAME, null, values);
		db.close();
		return update;

	}

	/**
	 * Reading a song (row) 
	 * */
	public SCSong getSong(String id) {
		
		SQLiteDatabase db = this.getReadableDatabase();

//		Cursor cursor = db.query(SONG_TABLE_NAME, new String[] { SONG_KEY_ID, SONG_KEY_TITLE, SONG_KEY_ARTIST, SONG_KEY_ARTWORK_URL, SONG_KEY_DURATION, SONG_KEY_GERNE, SONG_KEY_STREAM_URL, SONG_KEY_TAG },
//				SONG_KEY_ID + "=?", new String[] { id}, null, null,
//				null, null);
		
		String query = "SELECT * FROM " + SONG_TABLE_NAME + " WHERE " + SONG_KEY_ID + "=" + id;
		Cursor cursor = db.rawQuery(query, new String[]{});
		if (cursor == null || cursor.getCount() == 0){
			db.close();
			return null;
		}
		if (cursor != null) {
			cursor.moveToFirst();
		}
		SCSong onlineSong = new SCSong(
				cursor.getString(cursor.getColumnIndex(SONG_KEY_ID)),
				cursor.getString(cursor.getColumnIndex(SONG_KEY_TITLE)),
				"",
				"soundcloud.com",
				cursor.getString(cursor.getColumnIndex(SONG_KEY_STREAM_URL)),
				Long.parseLong(cursor.getString(cursor.getColumnIndex(SONG_KEY_DURATION))));
		
		onlineSong.setGenre(cursor.getString(cursor.getColumnIndex(SONG_KEY_GERNE)));
		onlineSong.setTagList(cursor.getString(cursor.getColumnIndex(SONG_KEY_TAG)));
		onlineSong.setUserId(cursor.getString(cursor.getColumnIndex(SONG_KEY_ARTIST)));
		
		cursor.close();
		db.close();
		return onlineSong;
	}

	/**
	 * Reading a song (row) 
	 * */
	public String getLink(String id) {
		try{
			SQLiteDatabase db = this.getReadableDatabase();
	
	//		Cursor cursor = db.query(SONG_TABLE_NAME, new String[] { SONG_KEY_STREAM_URL },
	//				SONG_KEY_ID + "=?", new String[] { }, null, null,
	//				null, null);
			//System.out.println ("GET LINK DATABASE = " + id);
			String query = "SELECT " + SONG_KEY_STREAM_URL + " FROM " + SONG_TABLE_NAME + " WHERE " + SONG_KEY_ID + "=" + id;
			Cursor cursor = db.rawQuery(query, new String[]{});
			
			if (cursor == null || cursor.getCount() == 0){
				db.close();
				return null;
			}
			if (cursor != null) {
				cursor.moveToFirst();
			}
			
			String link = cursor.getString(cursor.getColumnIndex(SONG_KEY_STREAM_URL));
			
			
			cursor.close();
			db.close();
			return link;
		}catch(Exception e){
			return null;
		}
	}


	/**
	 * Deleting single song by its ID
	 * */
	public void deleteSong(String id) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		//System.out.println ("GET WRITE = " + db);
		db.delete(SONG_TABLE_NAME, SONG_KEY_ID + " = ?",
				new String[] { id });
		db.close();
	}
	
	public void clearTable(){
		SQLiteDatabase db = mInstance.getWritableDatabase();
		db.execSQL("DELETE FROM " + SONG_TABLE_NAME);
		db.close();
	}

	/**
	 * Checking whether a song is already existed check is done by matching id
	 * link
	 * */
	public boolean isSongExists(SQLiteDatabase db, String id) {
		
		boolean exists = false;
		Cursor cursor;
		//has the same link
		cursor = db.rawQuery("SELECT * FROM " + SONG_TABLE_NAME
				+ " WHERE id = '" + id + "'", new String[] {});
		exists = (cursor.getCount() > 0);
		if (exists){
			return true;
		}
		return false;
		

		
	}

	/**
	 * Get the size of the database
	 */
	public int getDatabaseSize() {

		String countQuery = "SELECT  * FROM " + SONG_TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();
		return cnt;
	}




	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + LOGIN_TABLE_NAME + "("
				+ LOGIN_KEY_ID + " INTEGER PRIMARY KEY, " + LOGIN_KEY_TOKEN + " TEXT" + ");";
		db.execSQL(CREATE_LOGIN_TABLE);
		
	}




	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + SONG_TABLE_NAME);
		
		// Create tables again
		onCreate(db);
		
	}

	
	
}
