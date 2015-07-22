package com.emanuelef.linkspocket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

class PocketDB {
	public static String DB_FILENAME = "pocketdb";
	public static int DB_CURRENT_VERSION = 1;
	public static final String FOLDERS_TABLE = "folders";
		public static final String FOLDERS_COLUMN_ID = "_id";
		public static final String FOLDERS_COLUMN_NAME = "name";
	public static final String LINKS_TABLE = "links";
		public static final String LINKS_COLUMN_ID = "_id";
		public static final String LINKS_COLUMN_TITLE = "title";
		public static final String LINKS_COLUMN_VALUE = "value";
		public static final String LINKS_COLUMN_FOLDER_ID = "fid";
	
	private SQLiteDatabase mDB;
	
	// Singleton object
	static private PocketDB instance = null;
	static PocketDB getPocketDB(Context context) {
		if (instance == null)
			instance = new PocketDB(context);
		return instance;
	}
	private PocketDB(Context context){
		// Initialize database
		SQLiteOpenHelper helper = new OpenHelper(context, DB_FILENAME, null, DB_CURRENT_VERSION);
		mDB = helper.getWritableDatabase();
		
		/*
		ContentValues values = new ContentValues();
		values.put(FOLDERS_COLUMN_NAME, "Unread book");
		mDB.insert(FOLDERS_TABLE, null, values);
		values = new ContentValues();
		values.put(FOLDERS_COLUMN_NAME, "Painful look");
		mDB.insert(FOLDERS_TABLE, null, values);
		
		values = new ContentValues();
		values.put(LINKS_COLUMN_TITLE, "De Andrè");
		values.put(LINKS_COLUMN_VALUE, "http://google.it");
		values.put(LINKS_COLUMN_FOLDER_ID, 1);
		mDB.insert(LINKS_TABLE, null, values);
		
		values = new ContentValues();
		values.put(LINKS_COLUMN_TITLE, "Hacker News");
		values.put(LINKS_COLUMN_VALUE, "http://google.it");
		values.put(LINKS_COLUMN_FOLDER_ID, 2);
		mDB.insert(LINKS_TABLE, null, values);*/
	}
	
	public Cursor getFolders() {
		return mDB.query(FOLDERS_TABLE, null, null, null, null, null, FOLDERS_COLUMN_ID);
	}
	
	public Cursor getLinks(int folderid) {
		return mDB.query(LINKS_TABLE, null, LINKS_COLUMN_FOLDER_ID + " = " + folderid, null, null, null, LINKS_COLUMN_ID);
	}
	
	public void createFolder(String foldname) {
		ContentValues values = new ContentValues();
		values.put(FOLDERS_COLUMN_NAME, foldname);
		mDB.insert(FOLDERS_TABLE, null, values);
	}
	
	public void addLink(String title, String target, int folder) {
		ContentValues values = new ContentValues();
		values.put(LINKS_COLUMN_TITLE, title);
		values.put(LINKS_COLUMN_VALUE, target);
		values.put(LINKS_COLUMN_FOLDER_ID, folder);
		mDB.insert(LINKS_TABLE, null, values);
	}
	
	public void removeLink(int id) {
		mDB.delete(LINKS_TABLE, LINKS_COLUMN_ID + " = " + id, null);
	}
	
	// Class containers
	private class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String createFolders = "CREATE TABLE " + FOLDERS_TABLE + " (" +
					FOLDERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
					FOLDERS_COLUMN_NAME + " VARCHAR(100) );";
			String createLinks = "CREATE TABLE " + LINKS_TABLE + " (" +
					LINKS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
					LINKS_COLUMN_TITLE + " VARCHAR(100)," +
					LINKS_COLUMN_VALUE + " VARCHAR(1000)," + 
					LINKS_COLUMN_FOLDER_ID + " INTEGER," +
					"FOREIGN KEY (" + LINKS_COLUMN_FOLDER_ID + ") REFERENCES " + FOLDERS_TABLE + "(" + FOLDERS_COLUMN_ID + ") );";
			
			db.execSQL(createFolders);
			db.execSQL(createLinks);
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			throw new Error("onUpgrade not implemented");
		}
		
	}
}