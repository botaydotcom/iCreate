package com.android.apptime.datahandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.android.apptime.Main;
import com.android.apptime.R;

public class Ivle {
	private static Ivle theInstance = null;
	private static String IVLE_Token;
	private List<IvleAcadSemesterInfoData> ivleAcadSemesterInfoList;
	private List<IvleEventData> ivleEventList;
	private List<IvleSpecialDayData> ivleSpecialDayList;
	private List<IvleTimetableData> ivleTimetableList;

	private Ivle() {
		ivleAcadSemesterInfoList = new ArrayList();
		ivleEventList = new ArrayList();
		ivleSpecialDayList = new ArrayList();
		ivleTimetableList = new ArrayList();
	}

	public static Ivle createInstance(Context context) {
		if (theInstance == null) {
			theInstance = new Ivle();
			theInstance.context = context;
			theInstance.IVLE_Token = context.getSharedPreferences(
					Main.SHARED_PREF_FILE, 0).getString(Main.IVLE_TOKEN_FIELD,
					"");
		}
		return theInstance;
	}

	public static Ivle getInstance() {
		return theInstance;
	}

	public String GetIVLEToken() {
		return IVLE_Token;
	}

	private Context context = null;

	// input start and end date as a string in the following format: dd/mm/yyyy
	private void getEvents(String start_date, String end_date) {
		ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();

		listParams.add(new BasicNameValuePair(context
				.getString(R.string.apikeyfield), context
				.getString(R.string.apikey)));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.authfield), IVLE_Token));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.startdatefield), start_date));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.enddatefield), end_date));

		StringBuilder sb = new StringBuilder(
				context.getString(R.string.my_organizer_uri));

		sb.append("?");
		sb.append(listParams);

		String feedURL = sb.toString();

		IvleEventGetAndroidSaxFeedParser newParser = new IvleEventGetAndroidSaxFeedParser(
				feedURL);

		ivleEventList = newParser.parse();
	}

	public List<IvleEventData> getEventList(String start_date, String end_date) {
		this.getEvents(start_date, end_date);
		return ivleEventList;
	}

	// posting a new personal event to ivle
	/*
	 * APIKey: The pre allocated API key AuthToken: The authentication token
	 * generated by the login page. This determines the current user.
	 * EventTitle: Title of the personal event. Venue: Venue of the personal
	 * event. EventDateTime: DateTime of the event. Format: dd-mmm-yyyy h24:mi.
	 * Example: 01-Apr-2011 13:30. Default to current datetime if not specified.
	 * Description: Event description. RecurType: N = No Recurrence; W = Weekly
	 * Recurrence. Default is 'N', ie. no recurrence. WeeklyRecurEvery: 1 =
	 * Every Week; 2 = Every Other Week. (Applicable only for RecurType = W).
	 * Default is 1, ie. Every Week. strDays: list of days, delimited by commas.
	 * 1=Monday; 2=Tuesday; 3=Wednesday; 4=Thursday; 5=Friday; 6=Saturday;
	 * 7=Sunday. If recur on Monday, Wednesday and Friday, then the value pass
	 * in should be 1,3,5. (Applicable only for RecurType = W) RecurTillDate:
	 * Recur until which date. Format: dd-mmm-yyyy. Example: 01-Apr-2011.
	 * (Applicable only for RecurType = W)
	 */

	public String postEvent(String event_title, String venue,
			String event_date_time, String description, String recur_type,
			String weekly_recur_every, String strDays, String recur_till_date) {
		ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();

		listParams.add(new BasicNameValuePair(context
				.getString(R.string.apikeyfield), context
				.getString(R.string.apikey)));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.authfield), IVLE_Token));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventtitle), event_title));
		listParams.add(new BasicNameValuePair(
				context.getString(R.string.venue), venue));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventdatetime), event_date_time));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.description), description));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.recurtype), recur_type));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.weeklyrecurevery), weekly_recur_every));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.recurtilldate), recur_till_date));

		String result = DataHandler
				.sendRequestUsingPost(
						context.getString(R.string.my_organizer_add_personal_event_uri),
						listParams);

		return result;
	}

	// overload - parameters until recur type
	public String postEvent(String event_title, String venue,
			String event_date_time, String description, String recur_type) {
		ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();

		listParams.add(new BasicNameValuePair(context
				.getString(R.string.apikeyfield), context
				.getString(R.string.apikey)));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.authfield), IVLE_Token));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventtitle), event_title));
		listParams.add(new BasicNameValuePair(
				context.getString(R.string.venue), venue));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventdatetime), event_date_time));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.description), description));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.recurtype), recur_type));

		String result = DataHandler
				.sendRequestUsingPost(
						context.getString(R.string.my_organizer_add_personal_event_uri),
						listParams);

		return result;
	}

	// overload - parameters until description
	public String postEvent(String event_title, String venue,
			String event_date_time, String description) {
		ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();

		listParams.add(new BasicNameValuePair(context
				.getString(R.string.apikeyfield), context
				.getString(R.string.apikey)));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.authfield), IVLE_Token));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventtitle), event_title));
		listParams.add(new BasicNameValuePair(
				context.getString(R.string.venue), venue));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventdatetime), event_date_time));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.description), description));

		String result = DataHandler
				.sendRequestUsingPost(
						context.getString(R.string.my_organizer_add_personal_event_uri),
						listParams);

		return result;
	}

	// updating a personal event to ivle
	/*
	 * APIKey: The pre allocated API key AuthToken: The authentication token
	 * generated by the login page. This determines the current user. EventID:
	 * ID obtained from the [MyOrganizer_Personal] function EventTitle: Title of
	 * the personal event. Venue: Venue of the personal event. EventDateTime:
	 * DateTime of the event. Format: dd-mmm-yyyy h24:mi. Example: 01-Apr-2011
	 * 13:30. Default to current datetime if not specified. Description: Event
	 * description. RecurType: N = No Recurrence; W = Weekly Recurrence. Default
	 * is 'N', ie. no recurrence. WeeklyRecurEvery: 1 = Every Week; 2 = Every
	 * Other Week. (Applicable only for RecurType = W). Default is 1, ie. Every
	 * Week. strDays: list of days, delimited by commas. 1=Monday; 2=Tuesday;
	 * 3=Wednesday; 4=Thursday; 5=Friday; 6=Saturday; 7=Sunday. If recur on
	 * Monday, Wednesday and Friday, then the value pass in should be 1,3,5.
	 * (Applicable only for RecurType = W) RecurTillDate: Recur until which
	 * date. Format: dd-mmm-yyyy. Example: 01-Apr-2011. (Applicable only for
	 * RecurType = W)
	 */

	public String updateEvent(String event_id, String event_title,
			String venue, String event_date_time, String description,
			String recur_type, String weekly_recur_every, String strDays,
			String recur_till_date) {
		ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();

		listParams.add(new BasicNameValuePair(context
				.getString(R.string.apikeyfield), context
				.getString(R.string.apikey)));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.authfield), IVLE_Token));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventid), event_id));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventtitle), event_title));
		listParams.add(new BasicNameValuePair(
				context.getString(R.string.venue), venue));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventdatetime), event_date_time));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.description), description));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.recurtype), recur_type));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.weeklyrecurevery), weekly_recur_every));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.recurtilldate), recur_till_date));

		String result = DataHandler
				.sendRequestUsingPost(
						context.getString(R.string.my_organizer_add_personal_event_uri),
						listParams);

		return result;
	}

	// overload - parameters until recur type
	public String updateEvent(String event_id, String event_title,
			String venue, String event_date_time, String description,
			String recur_type) {
		ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();

		listParams.add(new BasicNameValuePair(context
				.getString(R.string.apikeyfield), context
				.getString(R.string.apikey)));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.authfield), IVLE_Token));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventid), event_id));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventtitle), event_title));
		listParams.add(new BasicNameValuePair(
				context.getString(R.string.venue), venue));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventdatetime), event_date_time));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.description), description));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.recurtype), recur_type));

		String result = DataHandler
				.sendRequestUsingPost(
						context.getString(R.string.my_organizer_add_personal_event_uri),
						listParams);

		return result;
	}

	// overload - parameters until description
	public String updateEvent(String event_id, String event_title,
			String venue, String event_date_time, String description) {
		ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();

		listParams.add(new BasicNameValuePair(context
				.getString(R.string.apikeyfield), context
				.getString(R.string.apikey)));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.authfield), IVLE_Token));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventid), event_id));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventtitle), event_title));
		listParams.add(new BasicNameValuePair(
				context.getString(R.string.venue), venue));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventdatetime), event_date_time));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.description), description));

		String result = DataHandler
				.sendRequestUsingPost(
						context.getString(R.string.my_organizer_add_personal_event_uri),
						listParams);

		return result;
	}

	// delete event
	/*
	 * APIKey: The pre allocated API key AuthToken: The authentication token
	 * generated by the login page. This determines the current user. EventID:
	 * ID obtained from the [MyOrganizer_Personal] function.
	 * DeleteAllRecurrence: Whether to delete all recurrence. Default is set to
	 * false. true=delete only this event; false=delete all recurrence of the
	 * event.
	 */
	public String deleteEvent(String event_id, String delete_all_recurrence) {
		ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();

		listParams.add(new BasicNameValuePair(context
				.getString(R.string.apikeyfield), context
				.getString(R.string.apikey)));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.authfield), IVLE_Token));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventid), event_id));
		listParams
				.add(new BasicNameValuePair(context
						.getString(R.string.deleteallrecurrence),
						delete_all_recurrence));

		String result = DataHandler.sendRequestUsingPost(
				context.getString(R.string.my_organizer_delete_personal_event),
				listParams);

		return result;
	}

	// delete event overload without recurrence
	public String deleteEvent(String event_id) {
		ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();

		listParams.add(new BasicNameValuePair(context
				.getString(R.string.apikeyfield), context
				.getString(R.string.apikey)));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.authfield), IVLE_Token));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.eventid), event_id));

		String result = DataHandler.sendRequestUsingPost(
				context.getString(R.string.my_organizer_delete_personal_event),
				listParams);

		return result;
	}

	/*
	 * AcadYear: Academic Year. Eg. 2010/2011 Semester: Academic Semester. Eg.
	 * 1, 2, 3 or 4 ClassNo: Class Number DayCode: Day Code. 1, 2, 3, 4, 5, 6 or
	 * 7. DayText: Day description. StartTime: Starting time for the lesson.
	 * EndTime: Ending time for the lesson. Venue: Venue of the slot WeekCode:
	 * Week Code. Refer to [CodeTable_WeekTypes] function for the full list.
	 * WeekText: Week description. Refer to [CodeTable_WeekTypes] function for
	 * the full list. LessonType: Type of lesson. Example: Lecture, Tutorial and
	 * so on.
	 */
	private void getTimetable(String acad_year, String semester) {
		ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();

		listParams.add(new BasicNameValuePair(context
				.getString(R.string.apikeyfield), context
				.getString(R.string.apikey)));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.authfield), IVLE_Token));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.acadyear), acad_year));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.semester), semester));

		StringBuilder sb = new StringBuilder(
				context.getString(R.string.my_organizer_get_timetable_student_uri));

		sb.append("?");
		sb.append(listParams);

		String feedURL = sb.toString();

		IvleTimetableGetAndroidSaxFeedParser newParser = new IvleTimetableGetAndroidSaxFeedParser(
				feedURL);

		ivleTimetableList = newParser.parse();
	}

	public List<IvleTimetableData> getTimetableList(String acad_year,
			String semester) {
		this.getTimetable(acad_year, semester);
		return ivleTimetableList;
	}

	/*
	 * StartDate: Starting date. Format: dd-mmm-yyyy EndDate: Ending date.
	 * Format: dd-mmm-yyyy If StartDate or EndDate is empty, it default to
	 * today's date.
	 */

	private void getSpecialDays(String start_date, String end_date) {
		ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();

		listParams.add(new BasicNameValuePair(context
				.getString(R.string.apikeyfield), context
				.getString(R.string.apikey)));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.authfield), IVLE_Token));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.startdatefield), start_date));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.enddatefield), end_date));

		StringBuilder sb = new StringBuilder(
				context.getString(R.string.my_organizer_get_special_days_uri));

		sb.append("?");
		sb.append(listParams);

		String feedURL = sb.toString();

		IvleSpecialDayGetAndroidSaxFeedParser newParser = new IvleSpecialDayGetAndroidSaxFeedParser(
				feedURL);

		ivleSpecialDayList = newParser.parse();
	}

	public List<IvleSpecialDayData> getSpecialDayList(String start_date,
			String end_date) {
		this.getSpecialDays(start_date, end_date);
		return ivleSpecialDayList;
	}

	/*
	 * AcadYear: Academic Year. Eg. 2010/2011 Semester: Academic Semester. Eg.
	 * 1, 2, 3 or 4
	 */

	private void getAcadSemesterInfo(String acad_year, String semester) {
		ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();

		listParams.add(new BasicNameValuePair(context
				.getString(R.string.apikeyfield), context
				.getString(R.string.apikey)));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.authfield), IVLE_Token));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.acadyear), acad_year));
		listParams.add(new BasicNameValuePair(context
				.getString(R.string.semester), semester));

		StringBuilder sb = new StringBuilder(
				context.getString(R.string.my_organizer_get_acad_semester_info_uri));

		sb.append("?");
		sb.append(listParams);

		String feedURL = sb.toString();

		IvleAcadSemesterInfoGetAndroidSaxFeedParser newParser = new IvleAcadSemesterInfoGetAndroidSaxFeedParser(
				feedURL);

		ivleAcadSemesterInfoList = newParser.parse();
	}

	public List<IvleAcadSemesterInfoData> getAcadSemesterInfoList(
			String acad_year, String semester) {
		this.getAcadSemesterInfo(acad_year, semester);
		return ivleAcadSemesterInfoList;
	}
}