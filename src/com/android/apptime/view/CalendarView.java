package com.android.apptime.view;

import com.android.apptime.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

public class CalendarView extends TabActivity {
	public static int nextViewId = 0;
	private String TAG = "calendarview";
	private RelativeLayout listTimeSlotLayout = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendarview);
		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		intent = new Intent().setClass(this, CalendarDayView.class);

		spec = tabHost.newTabSpec("calendarday").setIndicator("Day")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, CalendarWeekView.class);
		spec = tabHost.newTabSpec("calendarweek").setIndicator("Week")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, CalendarMonthView.class);
		spec = tabHost.newTabSpec("calendarmonth").setIndicator("Month")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, CalendarAgendaView.class);
		spec = tabHost.newTabSpec("calendaragenda").setIndicator("Agenda")
				.setContent(intent);
		tabHost.addTab(spec);
		tabHost.setCurrentTab(0);
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.d("calendarview", "WTF? hic hic");
    }
}