package com.android.apptime.datahandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.apptime.Main;
import com.android.apptime.R;

public class Ivle extends Activity  {
	private String IVLE_Token;
	
	// return token
	public String GetIVLEToken(){
    	return IVLE_Token;
    }
	
    /* Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.login);  
    	// log in to the IVLE Network
    	loginIVLE();
    }
    
    // Displays a new WebView to accept IVLE credentials and update the auth Token
    public void loginIVLE(){
        WebView wv = (WebView) findViewById(R.id.WebViewLogin);                  
        wv.getSettings().setJavaScriptEnabled(true);
        
        /* Register a new JavaScript interface called HTMLOUT */
        wv.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        wv.setWebViewClient(new WebViewClient() 
        {               
        	@Override               
        	public void onPageFinished(WebView view, String url) 
        	{     
        		// when login is complete, the url will be login_result.ashx?r=0
        		if (url.indexOf("/api/login/login_result.ashx") > 0)
        		{
        			// When login is successful, there will be a &r=0 in the url. It also means the return data is the token itself.
        			if (url.indexOf("&r=0") > 0)
        			{
        				Intent startMain = new Intent (getApplicationContext(), Main.class);
        				startActivity(startMain);
        				view.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('body')[0].innerHTML);");        				
        			}
        		}
        	}
        });
    
        wv.loadUrl(getString(R.string.login_uri)+"?"+getString(R.string.login_apikeyfield)+"="+getString(R.string.apikey));
    }
    
    final Context myApp = this;

    /* An instance of this class will be registered as a JavaScript interface */
    class MyJavaScriptInterface
    {
        @SuppressWarnings("unused")
        public void processHTML(String html)
        {
        	IVLE_Token = html;
        	
            // process the html as needed by the app
        	
        	/*new AlertDialog.Builder(myApp)  
    	    .setTitle("Token Value")  
    	    .setMessage(IVLE_Token)  
    	    .setPositiveButton(android.R.string.ok, null)  
    	    .setCancelable(false)
    	    .create().show();  
    	    
        	ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
        	listParams.add(new BasicNameValuePair(getString(R.string.apikeyfield),"AMie2EgILFdcDJP06BAOZ"));
        	listParams.add(new BasicNameValuePair(getString(R.string.authfield),IVLE_Token));
        	listParams.add(new BasicNameValuePair(getString(R.string.startdatefield),"01/06/2011"));
        	listParams.add(new BasicNameValuePair(getString(R.string.enddatefield),"10/08/2011"));
        	
        	String result = DataHandler.sendRequestUsingGet(getString(R.string.my_organizer_uri), listParams);
        	
        	//print out the token or save for next logon or to navigate to next API call.
        	Log.i("Token", result);*/
        }
    }
    
    // input start and end date as a string in the following format: dd/mm/yyyy
    public List<IvleEventData> getEvents(String start_date, String end_date){
    	ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
    	
    	listParams.add(new BasicNameValuePair(getString(R.string.apikeyfield), getString(R.string.apikey)));
    	listParams.add(new BasicNameValuePair(getString(R.string.authfield), IVLE_Token));
    	listParams.add(new BasicNameValuePair(getString(R.string.startdatefield), start_date));
    	listParams.add(new BasicNameValuePair(getString(R.string.enddatefield), end_date));
    	
    	StringBuilder sb = new StringBuilder(getString(R.string.my_organizer_uri));
    	
    	sb.append("?");
    	sb.append(listParams);
    	
    	String feedURL = sb.toString();
    	
    	IvleEventGetAndroidSaxFeedParser newParser = new IvleEventGetAndroidSaxFeedParser(feedURL);
    	
    	return newParser.parse();
    }
    
    // posting a new personal event to ivle
    /*APIKey: The pre allocated API key
    AuthToken: The authentication token generated by the login page. This determines the current user.
    EventTitle: Title of the personal event.
    Venue: Venue of the personal event.
    EventDateTime: DateTime of the event. Format: dd-mmm-yyyy h24:mi. Example: 01-Apr-2011 13:30. Default to current datetime if not specified.
    Description: Event description.
    RecurType: N = No Recurrence; W = Weekly Recurrence. Default is 'N', ie. no recurrence.
    WeeklyRecurEvery: 1 = Every Week; 2 = Every Other Week. (Applicable only for RecurType = W). Default is 1, ie. Every Week.
    strDays: list of days, delimited by commas. 1=Monday; 2=Tuesday; 3=Wednesday; 4=Thursday; 5=Friday; 6=Saturday; 7=Sunday. If recur on Monday, Wednesday and Friday, then the value pass in should be 1,3,5. (Applicable only for RecurType = W)
    RecurTillDate: Recur until which date. Format: dd-mmm-yyyy. Example: 01-Apr-2011. (Applicable only for RecurType = W)*/
    
    public String postEvent(String event_title, String venue, String event_date_time, String description, String recur_type, String weekly_recur_every, String strDays, String recur_till_date){
    	ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
    	
    	listParams.add(new BasicNameValuePair(getString(R.string.apikeyfield), getString(R.string.apikey)));
    	listParams.add(new BasicNameValuePair(getString(R.string.authfield), IVLE_Token));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventtitle), event_title));
    	listParams.add(new BasicNameValuePair(getString(R.string.venue), venue));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventdatetime), event_date_time));
    	listParams.add(new BasicNameValuePair(getString(R.string.description), description));
    	listParams.add(new BasicNameValuePair(getString(R.string.recurtype), recur_type));
    	listParams.add(new BasicNameValuePair(getString(R.string.weeklyrecurevery), weekly_recur_every));
    	listParams.add(new BasicNameValuePair(getString(R.string.recurtilldate), recur_till_date));
    	
    	String result = DataHandler.sendRequestUsingPost(getString(R.string.my_organizer_add_personal_event_uri), listParams);
    	
    	return result;
    }
    
    // overload - parameters until recur type
    public String postEvent(String event_title, String venue, String event_date_time, String description, String recur_type){
    	ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
    	
    	listParams.add(new BasicNameValuePair(getString(R.string.apikeyfield), getString(R.string.apikey)));
    	listParams.add(new BasicNameValuePair(getString(R.string.authfield), IVLE_Token));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventtitle), event_title));
    	listParams.add(new BasicNameValuePair(getString(R.string.venue), venue));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventdatetime), event_date_time));
    	listParams.add(new BasicNameValuePair(getString(R.string.description), description));
    	listParams.add(new BasicNameValuePair(getString(R.string.recurtype), recur_type));
    	
    	String result = DataHandler.sendRequestUsingPost(getString(R.string.my_organizer_add_personal_event_uri), listParams);
    	
    	return result;
    }
    
    // overload - parameters until description
    public String postEvent(String event_title, String venue, String event_date_time, String description){
    	ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
    	
    	listParams.add(new BasicNameValuePair(getString(R.string.apikeyfield), getString(R.string.apikey)));
    	listParams.add(new BasicNameValuePair(getString(R.string.authfield), IVLE_Token));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventtitle), event_title));
    	listParams.add(new BasicNameValuePair(getString(R.string.venue), venue));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventdatetime), event_date_time));
    	listParams.add(new BasicNameValuePair(getString(R.string.description), description));
    	
    	String result = DataHandler.sendRequestUsingPost(getString(R.string.my_organizer_add_personal_event_uri), listParams);
    	
    	return result;
    }
    
    // updating a personal event to ivle
    /*APIKey: The pre allocated API key
    AuthToken: The authentication token generated by the login page. This determines the current user.
    EventID: ID obtained from the [MyOrganizer_Personal] function
    EventTitle: Title of the personal event.
    Venue: Venue of the personal event.
    EventDateTime: DateTime of the event. Format: dd-mmm-yyyy h24:mi. Example: 01-Apr-2011 13:30. Default to current datetime if not specified.
    Description: Event description.
    RecurType: N = No Recurrence; W = Weekly Recurrence. Default is 'N', ie. no recurrence.
    WeeklyRecurEvery: 1 = Every Week; 2 = Every Other Week. (Applicable only for RecurType = W). Default is 1, ie. Every Week.
    strDays: list of days, delimited by commas. 1=Monday; 2=Tuesday; 3=Wednesday; 4=Thursday; 5=Friday; 6=Saturday; 7=Sunday. If recur on Monday, Wednesday and Friday, then the value pass in should be 1,3,5. (Applicable only for RecurType = W)
    RecurTillDate: Recur until which date. Format: dd-mmm-yyyy. Example: 01-Apr-2011. (Applicable only for RecurType = W)*/
    
    public String updateEvent(String event_id, String event_title, String venue, String event_date_time, String description, String recur_type, String weekly_recur_every, String strDays, String recur_till_date){
    	ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
    	
    	listParams.add(new BasicNameValuePair(getString(R.string.apikeyfield), getString(R.string.apikey)));
    	listParams.add(new BasicNameValuePair(getString(R.string.authfield), IVLE_Token));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventid), event_id));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventtitle), event_title));
    	listParams.add(new BasicNameValuePair(getString(R.string.venue), venue));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventdatetime), event_date_time));
    	listParams.add(new BasicNameValuePair(getString(R.string.description), description));
    	listParams.add(new BasicNameValuePair(getString(R.string.recurtype), recur_type));
    	listParams.add(new BasicNameValuePair(getString(R.string.weeklyrecurevery), weekly_recur_every));
    	listParams.add(new BasicNameValuePair(getString(R.string.recurtilldate), recur_till_date));
    	
    	String result = DataHandler.sendRequestUsingPost(getString(R.string.my_organizer_add_personal_event_uri), listParams);
    	
    	return result;
    }
    
    // overload - parameters until recur type
    public String updateEvent(String event_id, String event_title, String venue, String event_date_time, String description, String recur_type){
    	ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
    	
    	listParams.add(new BasicNameValuePair(getString(R.string.apikeyfield), getString(R.string.apikey)));
    	listParams.add(new BasicNameValuePair(getString(R.string.authfield), IVLE_Token));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventid), event_id));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventtitle), event_title));
    	listParams.add(new BasicNameValuePair(getString(R.string.venue), venue));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventdatetime), event_date_time));
    	listParams.add(new BasicNameValuePair(getString(R.string.description), description));
    	listParams.add(new BasicNameValuePair(getString(R.string.recurtype), recur_type));
    	
    	String result = DataHandler.sendRequestUsingPost(getString(R.string.my_organizer_add_personal_event_uri), listParams);
    	
    	return result;
    }
    
    // overload - parameters until description
    public String updateEvent(String event_id, String event_title, String venue, String event_date_time, String description){
    	ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
    	
    	listParams.add(new BasicNameValuePair(getString(R.string.apikeyfield), getString(R.string.apikey)));
    	listParams.add(new BasicNameValuePair(getString(R.string.authfield), IVLE_Token));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventid), event_id));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventtitle), event_title));
    	listParams.add(new BasicNameValuePair(getString(R.string.venue), venue));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventdatetime), event_date_time));
    	listParams.add(new BasicNameValuePair(getString(R.string.description), description));
    	
    	String result = DataHandler.sendRequestUsingPost(getString(R.string.my_organizer_add_personal_event_uri), listParams);
    	
    	return result;
    }
    
    // delete event
    /*APIKey: The pre allocated API key
    AuthToken: The authentication token generated by the login page. This determines the current user.
    EventID: ID obtained from the [MyOrganizer_Personal] function.
    DeleteAllRecurrence: Whether to delete all recurrence. Default is set to false. true=delete only this event; false=delete all recurrence of the event.*/
    public String deleteEvent(String event_id, String delete_all_recurrence){
    	ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
    	
    	listParams.add(new BasicNameValuePair(getString(R.string.apikeyfield), getString(R.string.apikey)));
    	listParams.add(new BasicNameValuePair(getString(R.string.authfield), IVLE_Token));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventid), event_id));
    	listParams.add(new BasicNameValuePair(getString(R.string.deleteallrecurrence), delete_all_recurrence));
    	
    	String result = DataHandler.sendRequestUsingPost(getString(R.string.my_organizer_delete_personal_event), listParams);
    
    	return result;
    }
    
    // delete event overload without recurrence
    public String deleteEvent(String event_id){
    	ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
    	
    	listParams.add(new BasicNameValuePair(getString(R.string.apikeyfield), getString(R.string.apikey)));
    	listParams.add(new BasicNameValuePair(getString(R.string.authfield), IVLE_Token));
    	listParams.add(new BasicNameValuePair(getString(R.string.eventid), event_id));
    	
    	String result = DataHandler.sendRequestUsingPost(getString(R.string.my_organizer_delete_personal_event), listParams);
    
    	return result;
    }
    
    /*AcadYear: Academic Year. Eg. 2010/2011
    Semester: Academic Semester. Eg. 1, 2, 3 or 4
    ClassNo: Class Number
    DayCode: Day Code. 1, 2, 3, 4, 5, 6 or 7.
    DayText: Day description.
    StartTime: Starting time for the lesson.
    EndTime: Ending time for the lesson.
    Venue: Venue of the slot
    WeekCode: Week Code. Refer to [CodeTable_WeekTypes] function for the full list.
    WeekText: Week description. Refer to [CodeTable_WeekTypes] function for the full list.
    LessonType: Type of lesson. Example: Lecture, Tutorial and so on.*/
    public List<IvleTimetableData> getTimetable(String acad_year, String semester){
    	ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
    	
    	listParams.add(new BasicNameValuePair(getString(R.string.apikeyfield), getString(R.string.apikey)));
    	listParams.add(new BasicNameValuePair(getString(R.string.authfield), IVLE_Token));
    	listParams.add(new BasicNameValuePair(getString(R.string.acadyear), acad_year));
    	listParams.add(new BasicNameValuePair(getString(R.string.semester), semester));
    	
    	StringBuilder sb = new StringBuilder(getString(R.string.my_organizer_get_timetable_student_uri));
    	
    	sb.append("?");
    	sb.append(listParams);
    	
    	String feedURL = sb.toString();
    	
    	IvleTimetableGetAndroidSaxFeedParser newParser = new IvleTimetableGetAndroidSaxFeedParser(feedURL);
    	
    	return newParser.parse();
    }
    
    /*StartDate: Starting date. Format: dd-mmm-yyyy
    EndDate: Ending date. Format: dd-mmm-yyyy
    If StartDate or EndDate is empty, it default to today's date.*/    
    
    public List<IvleSpecialDayData> getSpecialDays(String start_date, String end_date){
    	ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
    	
    	listParams.add(new BasicNameValuePair(getString(R.string.apikeyfield), getString(R.string.apikey)));
    	listParams.add(new BasicNameValuePair(getString(R.string.authfield), IVLE_Token));
    	listParams.add(new BasicNameValuePair(getString(R.string.startdatefield), start_date));
    	listParams.add(new BasicNameValuePair(getString(R.string.enddatefield), end_date));
    	
    	StringBuilder sb = new StringBuilder(getString(R.string.my_organizer_get_special_days_uri));
    	
    	sb.append("?");
    	sb.append(listParams);
    	
    	String feedURL = sb.toString();
    	
    	IvleSpecialDayGetAndroidSaxFeedParser newParser = new IvleSpecialDayGetAndroidSaxFeedParser(feedURL);
    	
    	return newParser.parse();
    }
    
    /*AcadYear: Academic Year. Eg. 2010/2011
    Semester: Academic Semester. Eg. 1, 2, 3 or 4*/
    
    public List<IvleAcadSemesterInfoData> getAcadSemesterInfo(String acad_year, String semester){
    	ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
    	
    	listParams.add(new BasicNameValuePair(getString(R.string.apikeyfield), getString(R.string.apikey)));
    	listParams.add(new BasicNameValuePair(getString(R.string.authfield), IVLE_Token));
    	listParams.add(new BasicNameValuePair(getString(R.string.acadyear), acad_year));
    	listParams.add(new BasicNameValuePair(getString(R.string.semester), semester));
    	
    	StringBuilder sb = new StringBuilder(getString(R.string.my_organizer_get_acad_semester_info_uri));
    	
    	sb.append("?");
    	sb.append(listParams);
    	
    	String feedURL = sb.toString();
    	
    	IvleAcadSemesterInfoGetAndroidSaxFeedParser newParser = new IvleAcadSemesterInfoGetAndroidSaxFeedParser(feedURL);
    	
    	return newParser.parse();
    }
}