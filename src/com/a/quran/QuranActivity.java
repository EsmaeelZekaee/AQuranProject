package com.a.quran;

import java.io.File;
import java.io.IOException;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.ToggleButton;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ZoomControls;

public class QuranActivity extends Activity {
	public static QuranIndexModel QuranIndexModel;
	private QuranPlayer quranPlayer;
	private ListView quranView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Context self = this;
		setContentView(R.layout.quran_page_layout);
		SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(this);

		quranView = (ListView) findViewById(R.id.quran);

		final ZoomControls pageZoom = (ZoomControls) findViewById(R.id.zoomFace);

		final Typeface tf = Typeface.createFromAsset(getAssets(),
				"me_quran.ttf");
		TextView headerView = (TextView) findViewById(R.id.header);

		headerView.setTypeface(tf);
		headerView.setText(QuranActivity.QuranIndexModel.getSuraName());
		final QuranAdapter quranAdapter = new QuranAdapter(this,
				dbHelper.getReadableDatabase(),
				QuranActivity.QuranIndexModel.getSuraID(),
				QuranActivity.QuranIndexModel.getFirstVerse(),
				QuranActivity.QuranIndexModel.getLastVerse());

		quranView.setAdapter(quranAdapter);

		pageZoom.setOnZoomInClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				QuranAdapter quranAdapter = (QuranAdapter) quranView
						.getAdapter();
				zoomText(quranAdapter, pageZoom, 1.25f);
				quranView.invalidateViews();
			}
		});

		pageZoom.setOnZoomOutClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				QuranAdapter quranAdapter = (QuranAdapter) quranView
						.getAdapter();
				zoomText(quranAdapter, pageZoom, .8f);
				quranView.invalidateViews();
			}
		});

		final Button gotoButton = (Button) findViewById(R.id.go_to);
		final Dialog gotoDialog = new Dialog(self);

		gotoDialog.setContentView(R.layout.goto_layout);
		gotoDialog.setTitle("انتخاب آیه");
		final SeekBar seekBar = (SeekBar) gotoDialog
				.findViewById(R.id.gotoSeekBar);

		seekBar.setMax(QuranActivity.QuranIndexModel.getLastVerse()
				- QuranActivity.QuranIndexModel.getFirstVerse());

		final TextView textView = (TextView) gotoDialog
				.findViewById(R.id.ayahNumber);

		seekBar.setProgress(quranView.getSelectedItemPosition());

		textView.setText(Globals.toArabicDigits(
				QuranActivity.QuranIndexModel.getFirstVerse()
						+ seekBar.getProgress(), true));
		textView.setTypeface(tf);

		Button okeyButton = (Button) gotoDialog.findViewById(R.id.okey);

		okeyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				quranView.setSelection(seekBar.getProgress());

			}
		});
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				textView.setText(Globals.toArabicDigits(
						QuranActivity.QuranIndexModel.getFirstVerse() + arg1,
						true));
			}
		});

		gotoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gotoDialog.show();
			}
		});

		quranPlayer = new QuranPlayer(this);
		final ToggleButton play_stop = (ToggleButton) findViewById(R.id.paly_stop);

		final File root = Globals.getRootOfAudioPath(this,
				QuranActivity.QuranIndexModel.getSuraID());

		quranPlayer.setOnNext(new OnNextListener() {

			@Override
			public void OnNext(int nextAyah) {
				int index = nextAyah
						- QuranActivity.QuranIndexModel.getFirstVerse();

				quranView.setSelection(index);

				View row = (View) quranView.getChildAt(index
						- quranView.getFirstVisiblePosition());
				if (row != null) {
					TextView arabic = (TextView) row.findViewById(R.id.arabic);
					if (arabic != null) {
						arabic.performClick();
					}
				}
			}
		});

		play_stop.setEnabled(root.exists());
		play_stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (play_stop.getText() == play_stop.getTextOn()) {
					quranPlayer.init();
					quranPlayer.play();
				} else {
					quranPlayer.stop();
				}

			}
		});

		final Dialog searchDialog = new Dialog(self);

		searchDialog.setContentView(R.layout.search_layout);
		searchDialog.setTitle("جستجو");
		Button find = (Button) searchDialog.findViewById(R.id.find);
		final ListView report = (ListView) searchDialog
				.findViewById(R.id.report);
		find.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				ArrayAdapter<QuranModel> reportAdapter = new ArrayAdapter<QuranModel>(
						self, R.id.report) {
					@Override
					public View getView(int position, View convertView,
							ViewGroup parent) {
						// TODO Auto-generated method stub
						if (convertView == null) {
							LayoutInflater infalInflater = (LayoutInflater) getContext()
									.getSystemService(
											Context.LAYOUT_INFLATER_SERVICE);
							convertView = infalInflater.inflate(
									R.layout.report_item, null);
						}
						TextView textView = (TextView) convertView
								.findViewById(R.id.report_det);
						QuranModel item = getItem(position);
						StringBuilder sb = new StringBuilder();
						sb.append(Globals.toArabicDigits((Integer) item.tag))
								.append("مورد در آیه")
								.append(Globals.toArabicDigits(item
										.getVerseID())).append("پیدا شد.");
						textView.setText(Html.fromHtml(sb.toString()));
						textView.setTag(item);
						textView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View view) {
								QuranModel qm = (QuranModel) view.getTag();
								int index = qm.getVerseID()
										- QuranActivity.QuranIndexModel
												.getFirstVerse();
								quranView.setSelection(index);
								searchDialog.dismiss();
							}
						});
						return convertView;
					}
				};

				TextView searchText = (TextView) searchDialog
						.findViewById(R.id.searchText);
				searchText.setText("الرسل");// برای اینکه کی برد فارسی ندارد

				String searchKey = searchText.getText().toString();
				searchKey = Globals.removePrefix(searchKey);
				searchKey = Globals.removeSuffix(searchKey);

				for (int index = 0; index < quranAdapter.getCount(); index++) {
					QuranModel qm = quranAdapter.getItem(index);
					qm.tag = 0;
					String otext = qm.getAyahText();
					otext = otext.replace(" <strong><u>", "").replace(
							"</u></strong> ", " ");
					String ntext = Globals.normalizeText(otext);
					if (ntext.contains((CharSequence) searchKey)) {

						// محل وقوع را پیدا می‌کنیم
						StringBuilder sb = new StringBuilder();

						String[] nwords = ntext.split(" ");
						String[] owords = otext.split(" ");

						for (int i = 0; i < owords.length; i++) {
							if (nwords[i].contains((CharSequence) searchKey)) {

								sb.append(" <strong><u>").append(owords[i])
										.append("</u></strong> ");
								qm.tag = (Object)((Integer) qm.tag + 1);
								continue;
							}
							sb.append(owords[i]).append(" ");
						}

						qm.setAyahText(sb.toString());
						quranAdapter.setItem(index, qm);
						quranView.invalidateViews();
						reportAdapter.add(qm);
					}
				}
				report.setAdapter(reportAdapter);
			}
		});

		((Button) searchDialog.findViewById(R.id.clear))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						for (int index = 0; index < quranAdapter.getCount(); index++) {
							QuranModel qm = quranAdapter.getItem(index);

							String otext = qm.getAyahText();
							otext = otext.replace(" <strong><u>", "").replace(
									"</u></strong> ", " ");
							qm.setAyahText(otext.toString());
							quranAdapter.setItem(index, qm);
						}
						quranView.invalidateViews();
						report.setAdapter(null);
						report.invalidateViews();
						searchDialog.dismiss();
					}
				});

		Button search = (Button) findViewById(R.id.search_button);
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				searchDialog.show();
			}
		});
	}

	public View getViewByPosition(int position, ListView listView) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition
				+ listView.getChildCount() - 1;

		if (position < firstListItemPosition || position > lastListItemPosition) {
			return listView.getAdapter().getView(position, null, listView);
		} else {
			final int childIndex = position - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}

	private void zoomText(QuranAdapter quranAdapter, ZoomControls zoomControls,
			float zoom) {
		quranAdapter.setTextSize(quranAdapter.getTextSize() * zoom);
		zoomControls
				.setIsZoomInEnabled(quranAdapter.getTextSize() < 29.296875f);
		zoomControls.setIsZoomOutEnabled(quranAdapter.getTextSize() > 12f);
	}

	@Override
	protected void onPause() {
		quranPlayer.stop();
		super.onPause();
	}
}
