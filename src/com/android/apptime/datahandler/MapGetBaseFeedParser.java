package com.android.apptime.datahandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import android.net.Uri;

import com.android.apptime.R;

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
	
	private final String fileName;

	protected MapGetBaseFeedParser(String feedUri){
		try {
			this.fileName = feedUri;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream getInputStream() {
		try {
			return new FileInputStream(new File(fileName)); 
			//feedUri.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}