package ngo.music.soundcloudplayer.general;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseTable {

	public static final String LOGIN_TABLE = "FTS";
	public static final String COL_WORD = "WORD";
	public static final String COL_DEFINITION = "DEFINITION";
	private static final String DATABASE_NAME = "SOUNDCLOUD";
	public static final int DATABASE_VERSION = 1;
	
	private final DatabaseOpenHelper mDatabaseOpenHelper;
	public DatabaseTable(Context context) {
		// TODO Auto-generated constructor stub
		mDatabaseOpenHelper = new DatabaseOpenHelper(context);
		
		
	}
	
	private static class DatabaseOpenHelper extends SQLiteOpenHelper{
		private final Context mHelperContext;
		private SQLiteDatabase mDatabase;
		
		private static final String FTS_TABLE_CREATE = 
				"CREATE VIRTUAL TABLE " + LOGIN_TABLE + 
				" USING fts3 ("
				+ COL_WORD + ", " +
				COL_DEFINITION + ")";
		
		public DatabaseOpenHelper(Context context) {
			// TODO Auto-generated constructor stub
			super (context, DATABASE_NAME, null, DATABASE_VERSION);
			mHelperContext = context;
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			mDatabase = db;
			mDatabase.execSQL(FTS_TABLE_CREATE);
			
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE);
			onCreate(db);
			
		}
	
		
	}

}
