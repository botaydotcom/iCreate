package com.android.apptime.datahandler;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class MapGetBaseFeedParser implements MapFeedParser {

	// names of the XML tags
	static final String TITLE = "Title";
	static final String HORIZONTAL = "Horizontal";
	static final  String VERTICAL = "Vertical";
	static final  String LONGITUDE = "Longitude";
	static final  String LATITUDE = "Latitude";
	static final  String LINK = "Link";
	static final  String ARRAY = "array";
	static final String DICT = "dict";
	//static final String TITLE = "Title";
	
	private final URL feedUrl;

	protected MapGetBaseFeedParser(String feedUrl){
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