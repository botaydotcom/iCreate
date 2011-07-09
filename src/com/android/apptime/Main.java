package com.android.apptime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import com.android.apptime.database.EventDBAdapter;
import com.android.apptime.database.ItemDBAdapter;
import com.android.apptime.database.MainDBAdapter;
import com.android.apptime.datahandler.Ivle;
import com.android.apptime.datahandler.IvleTimetableData;
import com.android.apptime.view.CalendarView;
import com.android.apptime.view.LoginScreen;
import com.android.apptime.view.MapView;
import com.android.apptime.view.OrganizerView;

public class Main extends TabActivity {

    /** Called when the activity is first created. */
	/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec; 
        Intent intent;  

        intent = new Intent().setClass(this, CalendarView.class);

        spec = tabHost.newTabSpec("calendar").setIndicator("Calendar",
                          res.getDrawable(R.drawable.ic_tab_calendar))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, MapView.class);
        spec = tabHost.newTabSpec("map").setIndicator("Map",
                          res.getDrawable(R.drawable.ic_tab_map))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, OrganizerView.class);
        spec = tabHost.newTabSpec("organizer").setIndicator("Organizer",
                          res.getDrawable(R.drawable.ic_tab_organizer))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
        */
       // DatabaseInterface database = DatabaseInterface.getDatabaseInterface(getApplicationContext());
       // database.CreateDatabase(getApplicationContext());
        
        
        /*
        DatabaseInterface db = new DatabaseInterface(this);
        List<String> alerttype = new ArrayList<String>();
        alerttype.add("alert1"); alerttype.add("alert2");
        Item item = new Item("title test", "description test", "location test", "category test", 
        		alerttype, "priority test", "Event", "1991-07-11 12:08:12", "2007-02-26 20:15:00", 
        		null, "2014-08-20 20:21:22", "repeat test", "completed test", 1);
        String et = item.GetAlertTime();
        //String et1 = item.GetDeadline();
        idb.createEvent(item);
        
        item.SetTitle("title test 2");
        idb.updateEvent(item);
        db.AddItemToDatabase(this, item);
        Cursor mycs = idb.getEventById(1);
        String titletest = mycs.getString(8);
        */

		/*
        Date newdate = new Date();
        long ttt = newdate.getTime();
        */
        /*
        Date olddate = new Date("1991-07-11 12:08:12");
        boolean tttt = false;
        if (olddate.getTime() < newdate.getTime()) tttt=true;
        */
        
        /*
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
			Date a = dfm.parse("2007-02-26 20:15:00");
			int yu= 0;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        */

//        Date newdate = new Date();
//        long ttt = newdate.getTime();
//        /*
//        Date olddate = new Date("1991-07-11 12:08:12");
//        boolean tttt = false;
//        if (olddate.getTime() < newdate.getTime()) tttt=true;
//        */
//        
//        
//        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//			Date a = dfm.parse("2007-02-26 20:15:00");
//			int yu= 0;
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        
//        
//        newdb.close();
//        IvleTimetableData timetable = new IvleTimetableData();
//        

    
        
    //}

   // @Override

	private static final String TAG = "Main";
	public static final String SHARED_PREF_FILE = "shared_pref";
	public final int LOGIN_SCREEN = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, CalendarView.class);

		spec = tabHost
				.newTabSpec("calendar")
				.setIndicator("Calendar",
						res.getDrawable(R.drawable.ic_tab_calendar))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, MapView.class);
		spec = tabHost.newTabSpec("map")
				.setIndicator("Map", res.getDrawable(R.drawable.ic_tab_map))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, OrganizerView.class);
		spec = tabHost
				.newTabSpec("organizer")
				.setIndicator("Organizer",
						res.getDrawable(R.drawable.ic_tab_organizer))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
		
		
		
		DatabaseInterface database =
		DatabaseInterface.getDatabaseInterface(getApplicationContext());
		database.OpenDatabase(getApplicationContext());
		List<String> alerttype = new ArrayList<String>(); alerttype.add("alert1");
		Date starttime= new Date(1221423535);
		Date endtime =   new Date(122142400);
		Date alerttime = new Date(122142340);
		Item item = new Item("title test",
				  "description test", "location test", "category test", alerttype,
				 "priority test", "Event", starttime,
				 endtime, null, alerttime, "repeat test",
				  "completed test", 1);
		//database.AddItemToDatabase(getApplicationContext(), item);
		//database.AddItemToDatabase(this.getApplicationContext(),item);
		
		database.RetrieveItemFromDatabase(getApplicationContext(), 5);
		database.CloseDatabase();

		
		
		
		/*
		 * DatabaseInterface db = new DatabaseInterface(this); List<String>
		 * alerttype = new ArrayList<String>(); alerttype.add("alert1");
		 * alerttype.add("alert2"); Item item = new Item("title test",
		 * "description test", "location test", "category test", alerttype,
		 * "priority test", "Event", "1991-07-11 12:08:12",
		 * "2007-02-26 20:15:00", null, "2014-08-20 20:21:22", "repeat test",
		 * "completed test", 1); String et = item.GetAlertTime(); //String et1 =
		 * item.GetDeadline(); idb.createEvent(item);
		 * 
		 * item.SetTitle("title test 2"); idb.updateEvent(item);
		 * db.AddItemToDatabase(this, item); Cursor mycs = idb.getEventById(1);
		 * String titletest = mycs.getString(8);
		 */
		// Date newdate = new Date();
		// long ttt = newdate.getTime();
		// /*
		// Date olddate = new Date("1991-07-11 12:08:12");
		// boolean tttt = false;
		// if (olddate.getTime() < newdate.getTime()) tttt=true;
		// */
		//
		//
		// DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// try {
		// Date a = dfm.parse("2007-02-26 20:15:00");
		// int yu= 0;
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		//
		// newdb.close();
		// IvleTimetableData timetable = new IvleTimetableData();
		//

	}
	
	@Override
	public void onResume(){
		Log.d(TAG, "on resume");
		super.onResume();
		String IVLE_Token = "";
		SharedPreferences settings = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
		IVLE_Token = settings.getString("IVLE_Token", "");
		Log.d(TAG, "token is+ "+ IVLE_Token);
		if (IVLE_Token.equals("")) {
			Intent intent = new Intent(getApplicationContext(),
					LoginScreen.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivityForResult(intent, LOGIN_SCREEN);
		} else ;//logged in
	}

	@Override
//>>>>>>> fb593f78ce139013743cabaeccaa729e459a493c
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == LOGIN_SCREEN) {
			if (resultCode==RESULT_CANCELED){
//				Toast.makeText(getApplicationContext(), "")
//				finish();
			}
		}

	}

}