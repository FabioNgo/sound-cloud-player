package ngo.music.soundcloudplayer.database;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Database Version
		private static final int DATABASE_VERSION= 3;

		
		// Database Name
		private static final String DATABASE_NAME = "soundcloud";

		// Contacts table name
		private static String TABLE_NAME = "login_info";
		
		private static DatabaseHandler mInstance = null;

		// Contacts Table Columns names
		private static final String KEY_ID = "id";
		private static final String KEY_TOKEN  = "token";
		private static final String KEY_USERNAME = "username";
		private static final String KEY_PASSWORD = "password";


		private DatabaseHandler(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

			
		}

		// Creating Tables
		@Override
		public void onCreate(SQLiteDatabase db) {

			
				String CREATE_RSS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
						+ KEY_ID + " INTEGER PRIMARY KEY, " 
						+ KEY_TOKEN + " TEXT" + ");";
				db.execSQL(CREATE_RSS_TABLE);
			

		}
		
		// Upgrading database
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Drop older table if existed
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

			// Create tables again
			onCreate(db);
		}

		
		public static DatabaseHandler getInstance(Context ctx) {

			    // Use the application context, which will ensure that you 
			    // don't accidentally leak an Activity's context.
			    // See this article for more information: http://bit.ly/6LRzfx
			    if (mInstance == null) {
			      mInstance = new DatabaseHandler(ctx);
			    }
			    return mInstance;
		}
		
		/**
		 * Adding a new username into database
		 * */
		public void addLoginInfo(String token) {
			refreshDatabase();
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(KEY_TOKEN, token); // username
			
			// Check if row already existed in database
			if (!isUserLoggedIn()) {
				// site not existed, create a new row
				db.insert(TABLE_NAME, null, values);
				db.close();
			} 
				// site already existed update the row
			return;
		}
		
		/**
		 * Checking whether a user is already existed
		 * 
		 * */
		public boolean isUserLoggedIn() {
			SQLiteDatabase db = this.getReadableDatabase();
			boolean exists = false;
			Cursor cursor;
			//has the same link
			cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
			
			if (cursor.getCount() > 0){
				return true;
			}else{
				return false;
			}
	
		}
		
		/**
		 * Deleting single row
		 * */
		public void refreshDatabase() {
			
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("DELETE FROM " + TABLE_NAME);
			db.close();
		}
		
		/**
		 * Reading a row (username) row is identified by row user
		 * */
		public String getUserInfo() {
			SQLiteDatabase db = this.getReadableDatabase();
			String userInfo = new String();
			Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
			if (cursor != null) {
				cursor.moveToFirst();
			}
			
			/*
			 * getToken
			 */
			userInfo = cursor.getString(1);
			System.out.println ("TOKEN INFO = " + userInfo);
			
			cursor.close();
			db.close();
			return userInfo;
		}

}
