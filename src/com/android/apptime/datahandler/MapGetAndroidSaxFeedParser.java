package com.android.apptime.datahandler;

import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class MapGetAndroidSaxFeedParser extends MapGetBaseFeedParser {

	static final String RSS = "plist";
	public MapGetAndroidSaxFeedParser(String feedUrl) {
		super(feedUrl);
	}

	public List<MapData> parse() {
		final MapData currentMessage = new MapData();
		RootElement root = new RootElement(RSS);
		final List<MapData> messages = new ArrayList<MapData>();
		
		Element array = root.getChild(ARRAY);
		Element dict = array.getChild(DICT);
		
		dict.setEndElementListener(new EndElementListener(){
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		dict.getChild(TITLE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setTitle(body);
			}
		});
		
		dict.getChild(HORIZONTAL).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setHorizontal(body);
			}
		});
		dict.getChild(VERTICAL).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setTitle(body);
			}
		});
		dict.getChild(LONGITUDE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setTitle(body);
			}
		});
		dict.getChild(LATITUDE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setTitle(body);
			}
		});
		dict.getChild(LINK).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setTitle(body);
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