package com.android.apptime.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class IvleSpecialDayGetBaseFeedParser implements IvleSpecialDayFeedParser{

	// names of the XML tags
	static final String COMMENTS = "Comments";
	static final String LAST_UPDATE = "LastUpdate";
	static final String RESULTS = "Results";
	static final String DATA_SPECIAL_DAYS = "Data_SpecialDays";
	static final String DESCRIPTION = "Description";
	static final String END_DATE = "EndDate";
	static final String START_DATE = "StartDate";
	static final String TYPE = "Type";
	
	private final URL feedUrl;

	protected IvleSpecialDayGetBaseFeedParser(String feedUrl){
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
