package com.a.quran;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// این دستور شمایل و ظاهر تنظیمات را ست می‌کند
		addPreferencesFromResource(R.xml.settings);

		final SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		// کی را ارسال می‌کنیم
		final ListPreference gharyListPreference = (ListPreference) findPreference("ghary");

		setGharyListPreferenceData(gharyListPreference, sharedPrefs);

		gharyListPreference.setNegativeButtonText("لغو");
		gharyListPreference
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						return false;
					}
				});

		final ListPreference fontListPreference = (ListPreference) findPreference("font");
		fontListPreference.setNegativeButtonText("لغو");
		setFontListPreferenceData(fontListPreference, sharedPrefs);
		fontListPreference
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						return false;
					}
				});

		final ListPreference transListPreference = (ListPreference) findPreference("trans_lang");
		transListPreference.setNegativeButtonText("لغو");
		setTransListPreferenceData(transListPreference, sharedPrefs);
		transListPreference
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						return false;
					}
				});
	}

	private void setTransListPreferenceData(ListPreference listPreference,
			SharedPreferences sharedPrefs) {

		CharSequence[] entries = { "ندارد", "فارسی", "انگلیسی" };
		CharSequence[] entryValues = { "0", "1", "2" };
		listPreference.setEntries(entries);
		listPreference.setDefaultValue(sharedPrefs.getString("trans_lang", "0"));
		listPreference.setEntryValues(entryValues);

	}

	protected static void setGharyListPreferenceData(
			ListPreference listPreference, SharedPreferences sharedPrefs) {

		File root = new File(Globals.audioPath);

		ArrayList<CharSequence> entries = new ArrayList<CharSequence>();
		ArrayList<CharSequence> entryValues = new ArrayList<CharSequence>();
		int len = 0;
		if (root.exists()) {

			for (File dir : root.listFiles()) {
				if (dir.isDirectory()) {
					entries.add((CharSequence) dir.getName());
					entryValues.add((CharSequence) dir.getPath());
					len++;
				}
			}
		}

		listPreference.setEntries(entries.toArray(new CharSequence[len]));
		listPreference.setDefaultValue(sharedPrefs.getString("ghary", null));
		listPreference.setEntryValues(entryValues
				.toArray(new CharSequence[len]));

	}

	protected static void setFontListPreferenceData(
			ListPreference listPreference, SharedPreferences sharedPrefs) {
		CharSequence[] entries = { "قلم 1", "قلم 2" };
		CharSequence[] entryValues = { "me_quran.ttf", "qur_std.ttf" };
		listPreference.setEntries(entries);
		listPreference.setDefaultValue(sharedPrefs.getString("font",
				"me_quran.ttf"));
		listPreference.setEntryValues(entryValues);
	}
}
