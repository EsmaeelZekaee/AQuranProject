package com.a.quran;

public class QuranModel {
	private int iSuraID;
	private int iVerseID;
	private String sAyahTranslateText;
	private String sAyahText;
	public Object tag;

	public QuranModel() {
		tag = 0;
	}

	public void setSuraID(int value) {
		iSuraID = value;
	}

	public void setVerseID(int value) {
		iVerseID = value;
	}

	public void setAyahText(String value) {
		sAyahText = value;
	}

	public void setAyahTranslateText(String value) {
		sAyahTranslateText = value;
	}

	public int getSuraID() {
		return iSuraID;
	}

	public int getVerseID() {
		return iVerseID;
	}

	public String getAyahText() {
		return sAyahText;
	}

	public String getTranslateText() {
		return sAyahTranslateText;
	}
}
