package com.a.quran;

import java.sql.Date;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public  class QuranLog {
	public static void setLog(Context context, QuranModel quranModel) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		
		sharedPrefs.edit().putLong("Log.SuraID", quranModel.getSuraID());
		sharedPrefs.edit().putLong("Log.VerseID", quranModel.getVerseID());
		
	}

	public static void getLog(Context context)
	{
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		
	}
}
