package com.android.apptime.datahandler;

import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class IvleAcadSemesterInfoGetAndroidSaxFeedParser extends IvleAcadSemesterInfoGetBaseFeedParser{
	static final String RSS = "APIDataOfData_AcadSemesterInfoMTRdQN6P";
	static final String NAME_SPACE = "http://schemas.datacontract.org/2004/07/";
	public IvleAcadSemesterInfoGetAndroidSaxFeedParser(String feedUrl) {
		super(feedUrl);
	}

	public List<IvleAcadSemesterInfoData> parse() {
		final IvleAcadSemesterInfoData currentMessage = new IvleAcadSemesterInfoData();
		RootElement root = new RootElement(RSS);
		final List<IvleAcadSemesterInfoData> messages = new ArrayList<IvleAcadSemesterInfoData>();
		
		Element results = root.getChild(NAME_SPACE, RESULTS);
		Element data_acad_semester_info = results.getChild(DATA_ACAD_SEMESTER_INFO);
		
		data_acad_semester_info.setEndElementListener(new EndElementListener(){
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		data_acad_semester_info.getChild(ACAD_YEAR).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setAcadYear(body);
			}
		});
		data_acad_semester_info.getChild(EVEN_ODD_WEEK).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setEvenOddWeek(body);
			}
		});
		data_acad_semester_info.getChild(LECTURE_START_DATE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setLectureStartDate(body);
			}
		});
		data_acad_semester_info.getChild(SEMESTER).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setSemester(body);
			}
		});
		data_acad_semester_info.getChild(SEMESTER_END_DATE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setSemesterEndDate(body);
			}
		});
		data_acad_semester_info.getChild(SEMESTER_START_DATE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setSemesterStartDate(body);
			}
		});
		data_acad_semester_info.getChild(TUTORIAL_START_DATE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setTutorialStartDate(body);
			}
		});
		data_acad_semester_info.getChild(TYPE_NAME).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setTypeName(body);
			}
		});
		data_acad_semester_info.getChild(WEEK_TYPE_END_DATE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setWeekTypeEndDate(body);
			}
		});
		data_acad_semester_info.getChild(WEEK_TYPE_START_DATE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setWeekTypeStartDate(body);
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
