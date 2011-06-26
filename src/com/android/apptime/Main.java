package com.android.apptime;

import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import com.android.apptime.database.EventDBAdapter;
import com.android.apptime.database.ItemDBAdapter;
import com.android.apptime.database.MainDBAdapter;
import com.android.apptime.view.CalendarView;
import com.android.apptime.view.MapView;
import com.android.apptime.view.OrganizerView;
public class Main extends TabActivity {
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
        MainDBAdapter newdb = new MainDBAdapter(this);
        newdb.open();
        ItemDBAdapter idb1 = new ItemDBAdapter(this);
        EventDBAdapter idb = new EventDBAdapter(this);
        idb.open();
        idb1.open();
        
        
        DatabaseInterface db = new DatabaseInterface(this);
        List<String> alerttype = new ArrayList<String>();
        alerttype.add("alert1"); alerttype.add("alert2");
        Item item = new Item("title test", "description test", "location test", "category test", 
        		alerttype, "priority test", "Event", "starttime test", "endtime test", 
        		"deadline test", "alerttime test", "repeat test", "completed test", 1);
        idb.createEvent(item);
        db.AddItemToDatabase(this, item);
        
        newdb.close();
    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.d("calendarview", "WTF?");
    }
    
    
}