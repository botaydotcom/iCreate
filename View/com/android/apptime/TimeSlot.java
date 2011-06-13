package com.android.apptime;

public class TimeSlot {
	private int mHour, mMinute;

	public void setTime(int hour, int min) {
		mHour = hour;
		mMinute = min;
	}

	public void setHour(int hour) {
		mHour = hour;
	}

	public void setMinunte(int min) {
		mMinute = min;
	}

	public int getHour() {
		return mHour;
	}

	public int getMinute() {
		return mMinute;
	}

	public String getAPPMTimeFormatWithoutHourPadding() {
		String result = "";
		if (mHour < 12)	result = "AM";
		else
			result = "PM";
		
		int tempHour = mHour;
		if (tempHour > 12)
			tempHour -= 12;
		if (tempHour == 0)
			tempHour = 12;
		result = String.valueOf(tempHour)+ ":" + pad(mMinute) + result;
		return result;
	}
	
	public String getAPPMTimeFormatWithHourPadding() {
		String result = getAPPMTimeFormatWithoutHourPadding();
		if (result.indexOf(":") == 1) result="0"+result;
		return result;
	}
	
	public String getAPPMHourFormatWithoutHourPadding() {
		String result = "";
		if (mHour < 12)	result = "AM";
		else
			result = "PM";
		
		int tempHour = mHour;
		if (tempHour > 12)
			tempHour -= 12;
		if (tempHour == 0)
			tempHour = 12;
		result = String.valueOf(tempHour)+ result;
		return result;
	}
	
	

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

}
