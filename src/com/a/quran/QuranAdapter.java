package com.a.quran;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QuranAdapter extends BaseAdapter {

	private Activity context;
	private List<QuranModel> ayahList;
	
	private String sql1 = "SELECT  SuraID, VerseID, AyahText FROM Quran WHERE "
			+ "SuraID = ? AND VerseID >= ? AND VerseID <= ?";

	private String sql2 = "SELECT  SuraID, VerseID, AyahText FROM %s WHERE "
			+ "SuraID = ? AND VerseID >= ? AND VerseID <= ?";

	private float textSize;
	private String fontName;
	private SharedPreferences sharedPrefs;
	private TextView selectedRow = null;
	
	public static QuranModel selectedAyah;

	public QuranAdapter(Activity context, SQLiteDatabase db, int suraID,
			int firstVerse, int lastVerse) {
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

		fontName = sharedPrefs.getString("font", "me_quran.ttf");

		ayahList = new ArrayList<QuranModel>();
		Cursor c1 = db.rawQuery(
				sql1,// پارامتر های دستور SQL
				new String[] { String.format("%d", suraID),
						String.format("%d", firstVerse),
						String.format("%d", lastVerse) });

		Cursor c2 = null;

		String lang = sharedPrefs.getString("trans_lang", "0");
		if (lang != "0") {
			sql2 = String.format(sql2, lang == "1" ? "QuranFarsi"
					: "QuranEnglish");
			c2 = db.rawQuery(
					sql2,// پارامتر های دستور SQL
					new String[] { String.format("%d", suraID),
							String.format("%d", firstVerse),
							String.format("%d", lastVerse) });
		}
		c1.moveToFirst();
		if (c2 != null)
			c2.moveToFirst();
		for (int i = 0; i < c1.getCount(); i++) {
			QuranModel temp = new QuranModel();
			temp.setSuraID(c1.getInt(0)); // ستون نخست دستور اس.کیو.ال که از نوع
			temp.setVerseID(c1.getInt(1));
			temp.setAyahText(c1.getString(2));
			if (c2 != null) {
				temp.setAyahTranslateText(c2.getString(2));
				c2.moveToNext();
			} else
				temp.setAyahTranslateText("");
			ayahList.add(temp);
			c1.moveToNext();

		}
		c1.close();
		if (c2 != null)
			c2.close();
		this.setTextSize(15);
		this.context = context;
	}

	@Override
	public int getCount() {
		return ayahList.size();
	}

	@Override
	public QuranModel getItem(int index) {
		return ayahList.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	public void setItem(int index, QuranModel item) {
		ayahList.set(index, item);
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.ayah_item, null);
		}

		convertView.setBackgroundColor(0xffffd800);
		
		Typeface tf = Typeface.createFromAsset(context.getAssets(), fontName);
		final TextView arabic = (TextView) convertView
				.findViewById(R.id.arabic);
		
		final TextView trans = (TextView) convertView.findViewById(R.id.trans);

		final TextView number = (TextView) convertView
				.findViewById(R.id.number);

		final QuranModel ayah = (QuranModel) ayahList.get(pos);
		
		arabic.setTypeface(tf);
		arabic.setBackgroundColor(0xffffd800);
		arabic.setTextSize(this.getTextSize());
		arabic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedAyah = ayah;
				if (selectedRow != null) {
					selectedRow.setTextColor(0xFF0000FF);
				}
				arabic.setTextColor(0xFF000000);
				selectedRow = arabic;
			}
		});

		trans.setBackgroundColor(0xffffd800);
		
		arabic.setText(Html.fromHtml(ayah.getAyahText()));
		trans.setText((CharSequence) ayah.getTranslateText());
		
		number.setTextColor(0xff0094ff);
		number.setTextSize(20);
		number.setText(Globals.toArabicDigits(ayah.getVerseID(), true));
		number.setTypeface(tf);
		return convertView;
	}

	public void setTextSize(float value) {
		textSize = value;
	}

	public float getTextSize() {
		return textSize;
	}
}
