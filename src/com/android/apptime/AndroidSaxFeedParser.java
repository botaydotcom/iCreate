package com.android.apptime;

import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class AndroidSaxFeedParser extends BaseFeedParser {

	static final String RSS = "APIDataOfData_Organizer_EventsMTRdQN6P";
	public AndroidSaxFeedParser(String feedUrl) {
		super(feedUrl);
	}

	public List<Message> parse() {
		final Message currentMessage = new Message();
		RootElement root = new RootElement(RSS);
		final List<Message> messages = new ArrayList<Message>();
		
		//Element comments = root.getChild(COMMENTS);
		//Element last_update = root.getChild(LAST_UPDATE);
		Element results = root.getChild(RESULTS);
		Element data_organizer_events = results.getChild(DATA_ORGANIZER_EVENTS);
		
		data_organizer_events.setEndElementListener(new EndElementListener(){
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		data_organizer_events.getChild(DATE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setDate(body);
			}
		});
		data_organizer_events.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setDescription(body);
			}
		});
		data_organizer_events.getChild(EVENT_TYPE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setEventType(body);
			}
		});
		data_organizer_events.getChild(ID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setID(body);
			}
		});
		data_organizer_events.getChild(LOCATION).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setLocation(body);
			}
		});
		data_organizer_events.getChild(TITLE).setEndTextElementListener(new EndTextElementListener(){
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