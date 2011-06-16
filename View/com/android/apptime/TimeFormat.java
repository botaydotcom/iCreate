package com.android.apptime;

import java.util.Date;

public class TimeFormat {

	public static String getAPPMTimeFormatWithoutHourPadding(Date time) {
		String result = "";
		if (time.getHours() < 12)	result = "AM";
		else
			result = "PM";
		
		int tempHour = time.getHours();
		if (tempHour > 12)
			tempHour -= 12;
		if (tempHour == 0)
			tempHour = 12;
		result = String.valueOf(tempHour)+ ":" + pad(time.getMinutes()) + result;
		return result;
	}
	
	public static String getAPPMTimeFormatWithHourPadding(Date time) {
		String result = getAPPMTimeFormatWithoutHourPadding(time);
		if (result.indexOf(":") == 1) result="0"+result;
		return result;
	}
	
	public static String getAPPMHourFormatWithoutHourPadding(Date time) {
		String result = "";
		if (time.getHours() < 12)	result = "AM";
		else
			result = "PM";
		
		int tempHour = time.getHours();
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
