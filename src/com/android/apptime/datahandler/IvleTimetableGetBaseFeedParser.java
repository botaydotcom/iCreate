package com.android.apptime.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class IvleTimetableGetBaseFeedParser implements IvleTimetableFeedParser{
	// names of the XML tags
	static final String COMMENTS = "Comments";
	static final String LAST_UPDATE = "LastUpdate";
	static final  String RESULTS = "Results";
	static final  String DATA_TIMETABLE_STUDENT = "Data_Timetable_Student";
	static final  String ACAD_YEAR = "AcadYear";
	static final  String CLASS_NO = "ClassNo";
	static final  String DAY_CODE = "DayCode";
	static final  String DAY_TEXT = "DayText";
	static final String END_TIME = "EndTime";
	static final String LESSON_TYPE = "LessonType";
	static final  String MODULE_CODE = "ModuleCode";
	static final  String SEMESTER = "Semester";
	static final  String START_TIME = "StartTime";
	static final  String VENUE = "Venue";
	static final String WEEK_CODE = "WeekCode";
	static final String WEEK_TEXT = "WeekText";
	
	private final URL feedUrl;

	protected IvleTimetableGetBaseFeedParser(String feedUrl){
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream getInputStream() {
		try {
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
