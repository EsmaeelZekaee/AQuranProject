package com.a.quran;

public class QuranIndexModel {
	
	private int iJozeID;
	private int iSuraID;
	private int iFirstVerse;
	private int iLastVerse;
	
	private String sSuraName;

	public int getJozeID() {
		return iJozeID;
	}

	public int getSuraID() {
		return iSuraID;
	}

	public int getFirstVerse() {
		return iFirstVerse;
	}

	public int getLastVerse() {
		return iLastVerse;
	}

	public String getSuraName() {
		return sSuraName;
	}

	public void setJozeID(int value) {
		iJozeID = value;
	}

	public void setSuraID(int value) {
		iSuraID = value;
	}

	public void setFirstVerse(int value) {
		iFirstVerse = value;
	}

	public void setLastVerse(int value) {
		iLastVerse = value;
	}

	public void setSuraName(String value) {
		sSuraName = value;
	}
}
