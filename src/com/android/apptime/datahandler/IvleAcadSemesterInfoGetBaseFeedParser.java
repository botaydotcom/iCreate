package com.android.apptime.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class IvleAcadSemesterInfoGetBaseFeedParser implements IvleAcadSemesterInfoFeedParser{

	// names of the XML tags
	static final String COMMENTS = "Comments";
	static final String LAST_UPDATE = "LastUpdate";
	static final String RESULTS = "Results";
	static final String DATA_ACAD_SEMESTER_INFO = "Data_AcadSemesterInfo";
	static final String ACAD_YEAR = "AcadYear";
	static final String EVEN_ODD_WEEK = "EvenOddWeek";
	static final String LECTURE_START_DATE = "LectureStartDate";
	static final String SEMESTER = "Semester";
	static final String SEMESTER_END_DATE = "SemesterEndDate";
	static final String SEMESTER_START_DATE = "SemesterStartDate";
	static final String TUTORIAL_START_DATE = "TutorialStartDate";
	static final String TYPE_NAME = "TypeName";
	static final String WEEK_TYPE_END_DATE = "WeekTypeEndDate";
	static final String WEEK_TYPE_START_DATE = "WeekTypeStartDate";
	
	private final URL feedUrl;

	protected IvleAcadSemesterInfoGetBaseFeedParser(String feedUrl){
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
