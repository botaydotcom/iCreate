package com.android.apptime.datahandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class MapGetAndroidSaxFeedParser extends MapGetBaseFeedParser {

	
	/*
	 To use this, call:
	 Uri pathUri = Uri.parse("android.resource://"
				+ getApplicationContext().getPackageName() + "/"
				+ R.raw.buildinglist);
	 String path = pathUri.toString();
	 */
	
	static final String RSS = "plist";
	public MapGetAndroidSaxFeedParser(InputStream feedUrl) {
		super(feedUrl);
	}

	public List<MapData> parse() {
		final MapData currentMessage = new MapData();
		final String[] currentElement = new String[1];
		RootElement root = new RootElement(RSS);
		final List<MapData> messages = new ArrayList<MapData>();
		
		
		Element array = root.getChild(ARRAY);
		Element dict = array.getChild(DICT);
		
		dict.setEndElementListener(new EndElementListener(){
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		dict.getChild(KEY).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentElement[0] = body;
			}
		});
		
		dict.getChild(INTEGER).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				if (HORIZONTAL.equals(currentElement[0])) {
					currentMessage.setHorizontal(body);
				} else if (VERTICAL.equals(currentElement[0])) {
					currentMessage.setVertical(body);
				} 
			}
		});
		dict.getChild(REAL).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				if (LATITUDE.equals(currentElement[0])) {
					currentMessage.setLatitude(body);
				} else if (LONGITUDE.equals(currentElement[0])) {
					currentMessage.setLongitude(body);
				} 
			}
		});
		dict.getChild(STRING).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				if (TITLE.equals(currentElement[0])) {
					currentMessage.setTitle(body);
				} else if (LINK.equals(currentElement[0])) {
					currentMessage.setLink(body);
				} 
			}
		});		
		
		try {
			Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return messages;
	}
}