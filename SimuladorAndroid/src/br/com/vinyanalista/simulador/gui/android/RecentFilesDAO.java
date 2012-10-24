package br.com.vinyanalista.simulador.gui.android;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecentFilesDAO {

	private static final boolean fileExists(String filePath) {
		// http://www.mkyong.com/java/how-to-check-if-a-file-exists-in-java/
		return new File(filePath).exists();
	}

	private class RecentFile {
		long id;
		String filePath;

		public RecentFile(long id, String filePath) {
			this.id = id;
			this.filePath = filePath;
		}
	}

	private static final String DATABASE_NAME = "aesdata";
	private static final String TABLE_NAME = "recent";

	public static final String KEY_ID = "_id";
	public static final String KEY_PATH = "path";

	private static final String CREATE_STATEMENT = "create table " + TABLE_NAME
			+ " (" + KEY_ID + " integer primary key autoincrement, " + KEY_PATH
			+ " text not null);";

	private DatabaseHelper databaseHelper;
	private SQLiteDatabase database;
	private final Context context;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_STATEMENT);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}

	public RecentFilesDAO(Context context) {
		this.context = context;
	}

	public RecentFilesDAO open() throws SQLException {
		databaseHelper = new DatabaseHelper(context);
		database = databaseHelper.getWritableDatabase();
		return this;
	}

	public List<String> fetchAllRecentFiles() {
		List<String> recentFiles = new ArrayList<String>();
		Cursor cursor = database.query(TABLE_NAME, new String[] { KEY_ID,
				KEY_PATH }, null, null, null, null, KEY_ID + " desc");
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			String filePath = cursor.getString(1);
			if (fileExists(filePath))
				recentFiles.add(cursor.getString(1));
			else
				deleteRecentFile(filePath);
			cursor.moveToNext();
		}
		return recentFiles;
	}

	private RecentFile findRecentFile(String filepath) {
		try {
			Cursor cursor = database.query(true, TABLE_NAME, new String[] {
					KEY_ID, KEY_PATH }, KEY_PATH + "='" + filepath + "'", null,
					null, null, null, "1");
			if (cursor != null) {
				cursor.moveToFirst();
				return new RecentFile(cursor.getLong(0), cursor.getString(1));
			}
		} catch (Exception e) {
		}
		return null;
	}

	public long addRecentFile(String filePath) {
		RecentFile foundFile = findRecentFile(filePath);
		if (foundFile != null)
			deleteRecentFile(filePath);
		ContentValues values = new ContentValues();
		values.put(KEY_PATH, filePath);
		return database.insert(TABLE_NAME, null, values);
	}

	public boolean deleteRecentFile(String filePath) {
		return database.delete(TABLE_NAME, KEY_PATH + "='" + filePath + "'",
				null) > 0;
	}

	public void close() {
		databaseHelper.close();
	}

}