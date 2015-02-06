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

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseTable extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION= 2;

	
	// Database Name
	private static final String DATABASE_NAME = "musicplayer";

	// Contacts table name
	private static final String TABLE_LOGIN = "login";
	
	private static DatabaseTable mInstance = null;

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_TOKEN = "token";
	private static final String KEY_LINK = "link";
	// private static final String KEY_RSS_LINK = "rss_link";
	private static final String KEY_DESCRIPTION = "description";
	// private static final String KEY_IMG = "img_name";
	private static final String KEY_IMG_LINK = "img_link";
	private static final String KEY_PUBLIC_DATE = "public_date";

	private static final int MAX_SIZE_DATABASE = 100;
	// String of table-name
//	private String table_name_array[] = { "home_page", "business", "cars",
//			"chat", "digital", "entertainment", "sports", "funny", "laws",
//			"life", "news", "science", "social", "travelling", "world" };

	// private long insertedRowIndex;

	private DatabaseTable(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		
		
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		//for (String table_name : table_name_array) {
			String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
					+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TOKEN + " TEXT"+ ");";
			db.execSQL(CREATE_LOGIN_TABLE);
		//}

	}
	
	

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

		// Create tables again
		onCreate(db);
	}

	
	  public static DatabaseTable getInstance(Context ctx) {

		    // Use the application context, which will ensure that you 
		    // don't accidentally leak an Activity's context.
		    // See this article for more information: http://bit.ly/6LRzfx
		    if (mInstance == null) {
		      mInstance = new DatabaseTable(ctx);
		    }
		    System.out.println ("DATA BASE  = " + mInstance);
		    return mInstance;
		  }
	  
	/**
	 * Adding a new website in websites table Function will check if a site
	 * already existed in database. If existed will update the old one else
	 * creates a new row
	 * */
//	public void addSite(WebSite site) {
//		TABLE_RSS = getTableName();
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//		values.put(KEY_TOKEN, site.getTitle()); // site title
//		values.put(KEY_LINK, site.getLink()); // site url
//		values.put(KEY_IMG_LINK, site.getImageLink()); // rss img
//		values.put(KEY_DESCRIPTION, site.getDescription()); // site description
//
//		values.put(KEY_PUBLIC_DATE, site.getPubDate()); // public date
//		// Check if row already existed in database
//		if (!isSiteExists(db, site.getLink(), site.getTitle(), site.getImageLink())) {
//			// site not existed, create a new row
//			db.insert(TABLE_RSS, null, values);
//			db.close();
//		} else {
//			// site already existed update the row
//			updateSite(site);
//			db.close();
//		}
//	}

	/**
	 * Reading all rows from database
	 * */
//	public List<WebSite> getAllSitesByID() {
//		TABLE_RSS = getTableName();
//		List<WebSite> siteList = new ArrayList<WebSite>();
//		//Log.d("DEBUG", "SQL " + TABLE_RSS);
//		// Select All Query
//		String selectQuery = "SELECT  * FROM " + TABLE_RSS + " ORDER BY "
//				+ KEY_ID + " DESC";
//
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery(selectQuery, null);
//
//		// looping through all rows and adding to list
//		if (cursor.moveToFirst()) {
//			do {
//				WebSite site = new WebSite();
//				site.setId(Integer.parseInt(cursor.getString(0)));
//				site.setTitle(cursor.getString(1));
//				site.setLink(cursor.getString(2));
//				site.setImageLink(cursor.getString(3));
//				site.setDescription(cursor.getString(4));
//				site.setPubDate(cursor.getString(5));
//				// Adding contact to list
//				//Log.d("DEBUG", "SQL " + String.valueOf(site.getId()));
//				siteList.add(site);
//			} while (cursor.moveToNext());
//		}
//		cursor.close();
//		db.close();
//
//		// return contact list
//
//		return siteList;
//	}

	// /**
	// * Reading all rows from database
	// * */
	// public List<WebSite> getAllSitesLimited(int limitedNumber) {
	// int count = 0;
	// List<WebSite> siteList = new ArrayList<WebSite>();
	// // Select All Query
	// String selectQuery = "SELECT * FROM " + TABLE_RSS
	// + " ORDER BY " + KEY_ID + " DESC";
	//
	// SQLiteDatabase db = this.getReadableDatabase();
	// Cursor cursor = db.rawQuery(selectQuery, null);
	//
	// // looping through all rows and adding to list
	// if (cursor.moveToFirst()) {
	// do {
	// count++;
	// WebSite site = new WebSite();
	// site.setId(Integer.parseInt(cursor.getString(0)));
	// site.setTitle(cursor.getString(1));
	// site.setLink(cursor.getString(2));
	// site.setImageLink(cursor.getString(3));
	// site.setDescription(cursor.getString(4));
	// site.setPubDate(cursor.getString(5));
	// // Adding contact to list
	// siteList.add(site);
	// } while (cursor.moveToNext() && count < limitedNumber);
	// }
	// cursor.close();
	// db.close();
	//
	// // return contact list
	//
	// return siteList;
	// }

	/**
	 * Updating a single row row will be identified by rss link
	 * */
	public int updateToken(String token) {
		
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TOKEN, token);
//		

		// updating row return
//		int update = db.update(TABLE_LOGIN, values, KEY_TOKEN + " = ?",
//				new String[] { token });
		int insert = (int) db.insert(TABLE_LOGIN, null, values);
		db.close();
		return insert;

	}

	/**
	 * Reading a token (row) 
	 * */
	public String getToken() {
		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_LOGIN, new String[] { KEY_ID, KEY_TOKEN },
				KEY_ID + "=?", new String[] { }, null, null,
				null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		String token = cursor.getString(1);
		cursor.close();
		db.close();
		return token;
	}

	/**
	 * Reading a row (website) row is identified by link
	 * */
//	public WebSite getSiteByLink(String link) {
//		TABLE_RSS = getTableName();
//		SQLiteDatabase db = this.getReadableDatabase();
//
//		Cursor cursor = db.query(TABLE_RSS, new String[] { KEY_ID, KEY_TOKEN,
//				KEY_LINK, KEY_IMG_LINK, KEY_DESCRIPTION, KEY_PUBLIC_DATE },
//				KEY_LINK + "=?", new String[] { link }, null, null, null, null);
//		if (cursor != null) {
//			cursor.moveToFirst();
//		}
//
//		WebSite site = new WebSite(cursor.getString(cursor.getColumnIndex(KEY_TOKEN)),
//									cursor.getString(cursor.getColumnIndex(KEY_LINK)),
//									cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
//									cursor.getString(cursor.getColumnIndex(KEY_PUBLIC_DATE)),
//									cursor.getString(cursor.getColumnIndex(KEY_IMG_LINK)));
//
//		site.setId(cursor.getInt((cursor.getColumnIndex(KEY_ID))));
//
//		cursor.close();
//		db.close();
//		return site;
//	}

	/**
	 * Deleting single row
	 * */
	public void deleteRow(String token) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		System.out.println ("GET WRITE = " + db);
		db.delete(TABLE_LOGIN, KEY_TOKEN + " = ?",
				new String[] { token });
		db.close();
	}
	
	public void clearTable(){
		SQLiteDatabase db = mInstance.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_LOGIN);
		db.close();
	}
//	/**
//	 * Deleting many rows
//	 * */
//	public void cleanDatabase() {
//		
//		SQLiteDatabase db = this.getWritableDatabase();
//		int numberOfDelete = getDatabaseSize() - MAX_SIZE_DATABASE;
//		Log.d("DEBUG" , "COUNT " + String.valueOf(numberOfDelete));
//		int count = 0;
//		
//		if (numberOfDelete <= 0) return;
//		else{
//			String selectQuery = "SELECT * FROM " + TABLE_RSS + " ORDER BY "
//					+ KEY_ID + " ASC";
//			Cursor cursor = db.rawQuery(selectQuery, null);
//			// looping through all rows and adding to list
//			if (cursor.moveToFirst()) {
//				do {
//					db.delete(TABLE_RSS, KEY_ID + " = ?",
//							new String[] { cursor.getString(0)});
//					count++;
//					if (count > numberOfDelete) break;
//					Log.d("DEBUG" , "COUNT " + String.valueOf(count));
//					//WebSite site = new WebSite();
//					//site.setId(Integer.parseInt(cursor.getString(0)));
////					site.setTitle(cursor.getString(1));
////					site.setLink(cursor.getString(2));
////					site.setImageLink(cursor.getString(3));
////					site.setDescription(cursor.getString(4));
////					site.setPubDate(cursor.getString(5));
//					// Adding contact to list
//					//Log.d("DEBUG", "SQL " + String.valueOf(site.getId()));
//					//siteList.add(site);
//				} while (cursor.moveToNext());
//			}
//		
//		}
//		db.close();
//		
//		//SELECT TOP 2 * FROM Customers;
//		
//		
//	}

//	/**
//	 * 
//	 */
//	public int getLatestId(){
//		TABLE_RSS = getTableName();
//		SQLiteDatabase db = this.getWritableDatabase();
//		String selectQuery = "SELECT * FROM " + TABLE_RSS + " ORDER BY "
//				+ KEY_ID + " DESC";
//		Cursor cursor = db.rawQuery(selectQuery, null);
//		cursor.moveToFirst();
//		return cursor.getInt(0);
//	}
	/**
	 * Checking whether a site is already existed check is done by matching rss
	 * link
	 * */
	public boolean isSiteExists(SQLiteDatabase db, String token) {
		
		boolean exists = false;
		Cursor cursor;
		//has the same link
		cursor = db.rawQuery("SELECT * FROM " + TABLE_LOGIN
				+ " WHERE token = '" + token + "'", new String[] {});
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

		String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();
		return cnt;
	}

	
	
}
