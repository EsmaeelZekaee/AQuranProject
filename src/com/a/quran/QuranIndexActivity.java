package com.a.quran;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

public class QuranIndexActivity extends Activity {
	ExpandableListView expQuranIndex;
	private static final int RESULT_SETTINGS = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.quran_index_layout);
		expQuranIndex = (ExpandableListView) findViewById(R.id.quran_index);
		SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(this);

		final QuranIndexAdapter quranIndexAdapter = new QuranIndexAdapter(this,
				dbHelper.getReadableDatabase());

		expQuranIndex.setAdapter(quranIndexAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivityForResult(intent, RESULT_SETTINGS);
			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_SETTINGS:
			upDateSetting();
			break;
		}
	}

	private void upDateSetting() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		
	}
}
