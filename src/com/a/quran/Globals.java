package com.a.quran;

import java.io.File;

import android.R.bool;
import android.R.string;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Globals {
	public static String toArabicDigits(int value, boolean rtl) {
		char base = '\u0660';

		StringBuilder sb = new StringBuilder();

		do {
			sb.insert(0, String.format("%c", base + (value % 10)));
		} while ((value = value / 10) > 0);

		if (rtl) {
			sb.insert(0, '\uFD3E');
			sb.append('\uFD3F');
		} else {
			sb.insert(0, '\uFD3F');
			sb.append('\uFD3E');
		}
		return sb.toString();
	}

	public static String toArabicDigits(int value) {
		char base = '\u0660';
		StringBuilder sb = new StringBuilder();

		do {
			sb.insert(0, String.format("%c", base + (value % 10)));
		} while ((value = value / 10) > 0);
		
		return sb.toString();

	}

	@SuppressLint("SdCardPath")
	public static String audioPath = new String("/sdcard/aQuran/audio");

	public static File getRootOfAudioPath(Context context, int suraId) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);

		return new File(String.format("%s/%d/",
				sharedPref.getString("ghary", null), suraId));

	}

	public static String normalizeText(String text) {
		char[] erab = { '\u036D', '\u036E', '\u036F', '\u064D', '\u064C',
				'\u064D', '\u064E', '\u064F', '\u0650', '\u0651', '\u0652',
				'\u0653', '\u0654', '\u0655', '\u0656', '\u0657', '\u0658',
				'\u0659', '\u065A', '\u065B', '\u065C', '\u065D', '\u065E',
				'\u065F', '\u06DF', '\u06E0', '\u06E1', '\u06E2', '\u06E3',
				'\u06E4', '\u06E5', '\u06E6', '\u06E7', '\u06E8', '\u06ED',
				'\u0670', '\u06D6', '\u06DF', '\u06E0', '\u0651' };
		text = remove(text, erab);

		char[] orginal1 = { '\u0622', '\u0623', '\u0625', '\u0671', '\uFB50',
				'\uFE83', '\uFE87', '\uFE81' };
		text = replace(text, orginal1, '\uFE8D');

		char[] orginal2 = { '\uFEF0', '\uFEF2' };
		text = replace(text, orginal2, '\uFEF1');

		char[] orginal3 = { '\u0629' };
		text = replace(text, orginal3, '\u0647');

		return text;
	}

	public static String replace(String text, char[] orginal, char c) {
		StringBuilder sb = new StringBuilder();

		for (char ch : text.toCharArray()) {
			Boolean flag = true;
			for (char character : orginal) {
				if (ch == character) {
					flag = false;
					break;
				}
			}

			sb.append(flag ? ch : c);

		}

		return sb.toString();
	}

	public static String remove(String text, char[] erab) {
		StringBuilder sb = new StringBuilder();

		for (char ch : text.toCharArray()) {
			Boolean flag = true;
			for (char c : erab) {
				if (ch == c) {
					flag = false;
					break;
				}
			}
			if (flag)
				sb.append(ch);
		}

		return sb.toString();
	}

	public static String removePrefix(String text) {
		String[] perfix1 = { "ب", "م", "ل", "و" };
		String[] perfix2 = { "لا", "فك", "فس", "لل", "اس", "ال", "اف" };
		String[] perfix3 = { "كال", "اال", "فال", "است", "بال", "فلل" };
		String[] perfix4 = { "فبال", "فكال" };

		for (String string : perfix1) {
			if (text.indexOf(string) == 0)
				return text.replaceFirst(string, "");
		}

		for (String string : perfix2) {
			if (text.indexOf(string) == 0)
				return text.replaceFirst(string, "");
		}

		for (String string : perfix3) {
			if (text.indexOf(string) == 0)
				return text.replaceFirst(string, "");
		}

		for (String string : perfix4) {
			if (text.indexOf(string) == 0)
				return text.replaceFirst(string, "");
		}

		return text;
	}

	public static String removeSuffix(String text) {
		String[] sufix1 = { "ه", "ا", "ي", "ت", "ة" };
		String[] sufix2 = { "ني", "یھ", "تھ", "تي", "ان", "ون", "ون", "ین",
				"ھم", "ھن", "ھا", "نا", "وا", "ام", "ان", "ات" };

		text = new StringBuffer(text).reverse().toString();
		for (String string : sufix1) {
			if (text.indexOf(new StringBuffer(string).reverse().toString()) == 0)
				return new StringBuffer(text.replaceFirst(string, ""))
						.reverse().toString();
		}

		for (String string : sufix2) {
			if (text.indexOf(new StringBuffer(string).reverse().toString()) == 0)
				return new StringBuffer(text.replaceFirst(string, ""))
						.reverse().toString();
		}

		return new StringBuffer(text).reverse().toString();
	}
}
