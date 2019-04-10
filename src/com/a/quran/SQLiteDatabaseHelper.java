package com.a.quran;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteDatabaseHelper

extends SQLiteOpenHelper {

	public SQLiteDatabaseHelper(Context context) {
		super(context, QuranDatabase.DB_NAME, null, QuranDatabase.DB_VER);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		QuranDatabase.createStructur(db);
		QuranDatabase.insertAllSura(db);
		QuranDatabase.insertAllTranslate(db);
		QuranDatabase.insertAllQuranEnglish(db);
		QuranDatabase.insertAllAyah(db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteDatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		onCreate(db);
	}

}
