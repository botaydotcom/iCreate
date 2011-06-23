package com.android.apptime;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class IvleMyOrganizerGetBaseFeedParser implements FeedParser {

	// names of the XML tags
	static final String COMMENTS = "Comments";
	static final String LAST_UPDATE = "LastUpdate";
	static final  String RESULTS = "Results";
	static final  String DATA_ORGANIZER_EVENTS = "Data_Organizer_Events";
	static final  String DATE = "Date";
	static final  String DESCRIPTION = "Description";
	static final  String EVENT_TYPE = "EventType";
	static final  String ID = "ID";
	static final String LOCATION = "Location";
	static final String TITLE = "Title";
	
	private final URL feedUrl;

	protected IvleMyOrganizerGetBaseFeedParser(String feedUrl){
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