package com.a.quran;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class QuranPlayer {
	private int iSura;
	private int iAyah;

	private File mp3;
	private MediaPlayer player;
	private File root;

	private OnNextListener onNextListener = null;

	private boolean hasNext() {
		String sura = String.format("%s%d", iSura < 10 ? "00"
				: iSura < 100 ? "0" : "", iSura);
		String ayah = String.format("%s%d", iAyah < 10 ? "00"
				: iAyah < 100 ? "0" : "", iAyah);
		return (new File(String.format("%s/%s%s.dat", root.getPath(), sura,
				ayah))).exists();
	}

	public void setOnNext(OnNextListener onNext) {
		this.onNextListener = onNext;
	}

	public QuranPlayer(Context context) {
		root = Globals.getRootOfAudioPath(context,
				QuranActivity.QuranIndexModel.getSuraID());
		player = new MediaPlayer();
		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer player) {
				if (hasNext()) {
					next();
					if (onNextListener != null) {
						onNextListener.OnNext(iAyah);
					}
				}
			}
		});
	}

	public void init() {
		if (QuranAdapter.selectedAyah != null) {
			iSura = QuranAdapter.selectedAyah.getSuraID();
			iAyah = QuranAdapter.selectedAyah.getVerseID();
		}
	}

	public void play() {

		String sura = String.format("%s%d", iSura < 10 ? "00"
				: iSura < 100 ? "0" : "", iSura);
		String ayah = String.format("%s%d", iAyah < 10 ? "00"
				: iAyah < 100 ? "0" : "", iAyah);
		mp3 = new File(String.format("%s/%s%s.dat", root.getPath(), sura, ayah));

		if (mp3.exists()) {

			try {
				player.reset();
				player.setDataSource(mp3.getPath());
				player.prepare();
				player.start();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void next() {
		iAyah++;
		play();
	}

	public void stop() {
		player.stop();
	}
}
