package com.android.apptime.datahandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Log;
import android.util.Xml;

public class IvleTimetableGetAndroidSaxFeedParser extends IvleTimetableGetBaseFeedParser{
	static final String RSS = "APIDataOfData_Timetable_StudentMTRdQN6P";
	static final String NAME_SPACE = "http://schemas.datacontract.org/2004/07/";
	private static final String TAG = "timetableparser";
	public IvleTimetableGetAndroidSaxFeedParser(String feedUrl) {
		super(feedUrl);
	}

	public List<IvleTimetableData> parse() {
		final IvleTimetableData currentMessage = new IvleTimetableData();
		RootElement root = new RootElement(NAME_SPACE, RSS);
		final List<IvleTimetableData> messages = new ArrayList<IvleTimetableData>();
		
		Element results = root.getChild(RESULTS);
		Element data_timetable_student = results.getChild(DATA_TIMETABLE_STUDENT);
		
		data_timetable_student.setEndElementListener(new EndElementListener(){
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		data_timetable_student.getChild(ACAD_YEAR).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setAcadYear(body);
			}
		});
		data_timetable_student.getChild(CLASS_NO).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setClassNo(body);
			}
		});
		data_timetable_student.getChild(DAY_CODE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setDayCode(body);
			}
		});
		data_timetable_student.getChild(DAY_TEXT).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setDayText(body);
			}
		});
		data_timetable_student.getChild(END_TIME).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setEndTime(body);
			}
		});
		data_timetable_student.getChild(LESSON_TYPE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setLessonType(body);
			}
		});
		data_timetable_student.getChild(MODULE_CODE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setModuleCode(body);
			}
		});
		data_timetable_student.getChild(SEMESTER).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setSemester(body);
			}
		});		data_timetable_student.getChild(START_TIME).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setStartTime(body);
			}
		});
		data_timetable_student.getChild(VENUE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setVenue(body);
			}
		});		data_timetable_student.getChild(WEEK_CODE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setWeekCode(body);
			}
		});
		data_timetable_student.getChild(WEEK_TEXT).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setWeekText(body);
			}
		});
		try {
//			byte [] data = new byte[2048];
//			InputStream is = this.getInputStream();
//			String s = "";
//			while (is.read(data)>0){
//				s = s+new String(data);
//			}
//			Log.d(TAG, s);
			Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
			throw new RuntimeException(e);
		}
		return messages;
	}
}
