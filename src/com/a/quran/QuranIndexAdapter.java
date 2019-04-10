package com.a.quran;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class QuranIndexAdapter extends BaseExpandableListAdapter {

	private Activity context;
	private Map<String, List<QuranIndexModel>> surahCollection;
	private List<String> jozeList;

	private String sql = "SELECT JozeID, SuraID, FirstVerse, LastVerse, SuraName"
			+ " FROM QuranIndex WHERE JozeID = ? ORDER BY SuraID";

	private String jozeNames[] = { "یکم", "دوم", "سوم", "چهارم", "پنجم", "ششم",
			"هفتم", "هشتم", "نهم", "دهم", "یازدهم", "دوازدهم", "سیزدهم",
			"چهاردهم", "پانزدهم", "شانزدهم", "هفدهم", "هژدهم", "نونزدهم",
			"بیستم", "بیست‌یکم", "بیست‌دوم", "بیست‌سوم", "بیست‌چهارم",
			"بیست‌پنجم", "بیست‌ششم", "بیست‌هفتم", "بیست‌هشتم", "بیست‌نهم",
			"سی‌ام" };

	Typeface tf;

	public QuranIndexAdapter(Activity context, SQLiteDatabase db) {

		surahCollection = new LinkedHashMap<String, List<QuranIndexModel>>();
		jozeList = new ArrayList<String>();

		for (int j = 1; j <= 30; j++) {
			Cursor c = db.rawQuery(this.sql,
					new String[] { String.format("%d", j) });
			String tempJozename = "جزء " + jozeNames[j - 1];
			c.moveToFirst();
			List<QuranIndexModel> tempList = new ArrayList<QuranIndexModel>();
			for (int i = 0; i < c.getCount(); i++) {
				QuranIndexModel temp = new QuranIndexModel();
				temp.setJozeID(c.getInt(0));
				temp.setSuraID(c.getInt(1));
				temp.setFirstVerse(c.getInt(2));
				temp.setLastVerse(c.getInt(3));
				temp.setSuraName(c.getString(4));
				tempList.add(temp);
				c.moveToNext();
			}

			surahCollection.put(tempJozename, tempList);

			jozeList.add(tempJozename);
		}
		this.context = context;
	}

	@Override
	public Object getChild(int gPos, int cPos) {
		// gPos = 5
		return surahCollection.get(jozeList.get(gPos)).get(cPos);
	}

	@Override
	public long getChildId(int gPos, int cPos) {
		return cPos;
	}

	@SuppressLint("NewApi")
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final QuranIndexModel sura = (QuranIndexModel) getChild(groupPosition,
				childPosition);

		LayoutInflater inflater = context.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.sura_item, null);
		}

		ImageView hasAudio = (ImageView) convertView
				.findViewById(R.id.hasAudio);

		TextView item = (TextView) convertView.findViewById(R.id.sura);
		TextView detail = (TextView) convertView.findViewById(R.id.detail);

		ImageButton openButton = (ImageButton) convertView
				.findViewById(R.id.open);
		openButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				QuranActivity.QuranIndexModel = surahCollection.get(
						jozeList.get(groupPosition)).get(childPosition);
				Intent quran = new Intent(context, QuranActivity.class);
				context.startActivity(quran);
			}
		});

		detail.setText(String.format("آیه‌های %s،%s",
				Globals.toArabicDigits(sura.getFirstVerse()),
				Globals.toArabicDigits(sura.getLastVerse())));
		tf = Typeface.createFromAsset(context.getAssets(), "me_quran.ttf");
		item.setTypeface(tf);
		item.setTextSize(17);

		item.setText(sura.getSuraName());

		File root = Globals.getRootOfAudioPath(context, sura.getSuraID());

		if (!root.exists()) {
			hasAudio.setBackground(null);
		}

		return convertView;
	}

	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".dat"));
		}
	}

	public int getChildrenCount(int gPos) {
		return surahCollection.get(jozeList.get(gPos)).size();
	}

	public Object getGroup(int gPos) {
		return jozeList.get(gPos);
	}

	public int getGroupCount() {

		return jozeList.size();

	}

	public long getGroupId(int gPos) {
		return gPos;
	}

	public View getGroupView(int gPos, boolean isExpanded, View convertView,
			ViewGroup parent) {
		String jozeName = (String) getGroup(gPos);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.group_item, null);
		}
//		convertView.setBackgroundColor(0xff808080);
		TextView item = (TextView) convertView.findViewById(R.id.sura);
		tf = Typeface.createFromAsset(context.getAssets(), "qur_std.ttf");

		item.setTypeface(tf, Typeface.ITALIC);
		item.setTextSize(13);
		item.setText(jozeName);

		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
