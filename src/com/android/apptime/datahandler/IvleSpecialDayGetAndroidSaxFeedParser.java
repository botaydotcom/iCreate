package com.android.apptime.datahandler;

import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class IvleSpecialDayGetAndroidSaxFeedParser extends IvleSpecialDayGetBaseFeedParser{
	static final String RSS = "APIDataOfData_SpecialDaysMTRdQN6P";
	static final String NAME_SPACE = "http://schemas.datacontract.org/2004/07/";
	public IvleSpecialDayGetAndroidSaxFeedParser(String feedUrl) {
		super(feedUrl);
	}

	public List<IvleSpecialDayData> parse() {
		final IvleSpecialDayData currentMessage = new IvleSpecialDayData();
		RootElement root = new RootElement(NAME_SPACE, RSS);
		final List<IvleSpecialDayData> messages = new ArrayList<IvleSpecialDayData>();
		
		Element results = root.getChild(RESULTS);
		Element data_special_days = results.getChild(DATA_SPECIAL_DAYS);
		
		data_special_days.setEndElementListener(new EndElementListener(){
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		data_special_days.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setDescription(body);
			}
		});
		data_special_days.getChild(END_DATE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setEndDate(body);
			}
		});
		data_special_days.getChild(START_DATE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setStartDate(body);
			}
		});
		data_special_days.getChild(TYPE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setType(body);
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
