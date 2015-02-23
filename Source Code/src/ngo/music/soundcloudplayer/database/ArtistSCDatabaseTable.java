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

import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.entity.SCSong;
import ngo.music.soundcloudplayer.entity.SCAccount;
import ngo.music.soundcloudplayer.general.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ArtistSCDatabaseTable implements Constants.DatabaseConstant{


	
	private static ArtistSCDatabaseTable mInstance = null;
	static Context context;
	// private long insertedRowIndex;

//	private ArtistSCDatabaseTable(Context context) {
//		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//		
//		
//		
//	}

//	// Creating Tables
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//
////		//for (String table_name : table_name_array) {
//		String CREATE_ARTIST_TABLE = "CREATE TABLE " + ARTIST_TABLE_NAME + "("
//				+ ARTIST_KEY_ID + " TEXT PRIMARY KEY, " + ARTIST_KEY_USERNAME + " TEXT, "
//				+ ARTIST_KEY_ARTWORK_URL + " TEXT, " 
//				+ ARTIST_KEY_CITY + "TEXT, " + ARTIST_KEY_COUNTRY + "TEXT, "+ ARTIST_KEY_FULLNAME + "TEXT);";
//			db.execSQL(CREATE_ARTIST_TABLE);
//		//}
//
//	}
//	
//	
//
//	// Upgrading database
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		// Drop older table if existed
//		db.execSQL("DROP TABLE IF EXISTS " + ARTIST_TABLE_NAME);
//
//		// Create tables again
//		onCreate(db);
//	}

	
	  public static ArtistSCDatabaseTable getInstance(Context ctx) {

		    // Use the application context, which will ensure that you 
		    // don't accidentally leak an Activity's context.
		    // See this article for more information: http://bit.ly/6LRzfx
		    if (mInstance == null) {
		    	context = ctx;
		      mInstance = new ArtistSCDatabaseTable();
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
	public void addSong(SCAccount scAccount) throws IOException {
		//TABLE_RSS = getTableName();
		SQLiteDatabase db = DatabaseCreate.getInstance(context).getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ARTIST_KEY_ID, scAccount.getId()); //user ID
		values.put(ARTIST_KEY_USERNAME, scAccount.getUsername()); // username
		values.put(ARTIST_KEY_FULLNAME, scAccount.getFullName()); // fullname
		values.put(ARTIST_KEY_ARTWORK_URL, scAccount.getAvatarUrl()); // song artwork img
		values.put(ARTIST_KEY_COUNTRY, scAccount.getCountry()); // song ID OF ARTIST

		values.put(ARTIST_KEY_CITY, scAccount.getCity()); // song gerne
		
		// Check if row already existed in database
		if (!isArtistExists(db, scAccount.getId())) {
			// site not existed, create a new row
			db.insert(ARTIST_TABLE_NAME, null, values);
			db.close();
		} else {
			// site already existed update the row
			updateArtist(scAccount);
			db.close();
		}
	}


	/**
	 * Updating a single row
	 * */
	public int updateArtist(SCAccount scAccount) {
		
		SQLiteDatabase db = DatabaseCreate.getInstance(context).getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ARTIST_KEY_ID, scAccount.getId()); //user ID
		values.put(ARTIST_KEY_USERNAME, scAccount.getUsername()); // username
		values.put(ARTIST_KEY_FULLNAME, scAccount.getFullName()); // fullname
		values.put(ARTIST_KEY_ARTWORK_URL, scAccount.getAvatarUrl()); // song artwork img
		values.put(ARTIST_KEY_COUNTRY, scAccount.getCountry()); // song ID OF ARTIST

		values.put(ARTIST_KEY_CITY, scAccount.getCity()); // song gerne
//		

		// updating row return
//		int update = db.update(TABLE_LOGIN, values, KEY_TOKEN + " = ?",
//				new String[] { token });
		int insert = (int) db.insert(ARTIST_TABLE_NAME, null, values);
		db.close();
		return insert;

	}

	/**
	 * Reading a song (row) 
	 * */
	public SCAccount getArtist(String id) {
		//System.out.println ("DATABASE GET ARTIST" );
		SQLiteDatabase db = DatabaseCreate.getInstance(context).getReadableDatabase();
		
		Cursor cursor = db.query(ARTIST_TABLE_NAME, new String[] { ARTIST_KEY_ID, ARTIST_KEY_USERNAME, ARTIST_KEY_FULLNAME, ARTIST_KEY_ARTWORK_URL, ARTIST_KEY_CITY, ARTIST_KEY_COUNTRY },
				ARTIST_KEY_ID + "=?", new String[] { }, null, null,
				null, null);
		
		if (cursor == null || cursor.getCount() == 0){
			db.close();
			return null;
		}
		if (cursor != null) {
			cursor.moveToFirst();
		}
//		OnlineSong onlineSong = new OnlineSong(
//				cursor.getString(cursor.getColumnIndex(KEY_ID)),
//				cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
//				cursor.getString(cursor.getColumnIndex(KEY_ARTIST)),
//				"soundcloud.com",
//				cursor.getString(cursor.getColumnIndex(KEY_STREAM_URL)));
		SCAccount scAcount = new SCAccount();
		scAcount.setAvatarUrl(cursor.getString(cursor.getColumnIndex(ARTIST_KEY_ARTWORK_URL)));
		scAcount.setId(cursor.getString(cursor.getColumnIndex(ARTIST_KEY_ID)));
		scAcount.setUsername(cursor.getString(cursor.getColumnIndex(ARTIST_KEY_USERNAME)));
		scAcount.setFullName(cursor.getString(cursor.getColumnIndex(ARTIST_KEY_FULLNAME)));
		scAcount.setCountry(cursor.getString(cursor.getColumnIndex(ARTIST_KEY_COUNTRY)));
		scAcount.setCity(cursor.getString(cursor.getColumnIndex(ARTIST_KEY_CITY)));
		
		
		cursor.close();
		db.close();
		return scAcount;
	}



	/**
	 * Deleting single row
	 * */
	public void deleteArtist(String id) {
		
		SQLiteDatabase db = DatabaseCreate.getInstance(context).getWritableDatabase();
		//System.out.println ("GET WRITE = " + db);
		db.delete(ARTIST_TABLE_NAME, ARTIST_KEY_ID + " = ?",
				new String[] { id });
		db.close();
	}
	
	public void clearTable(){
		SQLiteDatabase db = DatabaseCreate.getInstance(context).getWritableDatabase();
		db.execSQL("DELETE FROM " + ARTIST_TABLE_NAME);
		db.close();
	}

	/**
	 * Checking whether a site is already existed check is done by matching rss
	 * link
	 * */
	public boolean isArtistExists(SQLiteDatabase db, String id) {
		
		boolean exists = false;
		Cursor cursor;
		//has the same link
		cursor = db.rawQuery("SELECT * FROM " + ARTIST_TABLE_NAME
				+ " WHERE id = '" + id + "'", new String[] {});
		exists = (cursor.getCount() > 0);
		if (exists){
			return true;
		}
		return false;
		
//		//has the same title
//		cursor = db.rawQuery("SELECT * FROM " + TABLE_RSS
//				+ " WHERE title = '" + title + "'", new String[] {});
//		exists = (cursor.getCount() > 0);
//		if (exists){
//			return true;
//		}
		
	}

	/**
	 * Get the size of the database
	 */
	public int getDatabaseSize() {

		String countQuery = "SELECT  * FROM " + ARTIST_TABLE_NAME;
		SQLiteDatabase db = DatabaseCreate.getInstance(context).getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();
		return cnt;
	}

//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		// TODO Auto-generated method stub
//		
//	}

	
	
}
