package com.android.apptime;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

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
    }
}