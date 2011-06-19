package com.android.apptime;

import java.util.Date;
import java.util.List;

import android.graphics.Color;

// REMARK: EventItem *MUST* has [startTime,endTime]
public class EventItem extends Item{
	EventItem (String id, String title, String description, String location, String category, List<String> alerttype, String priority, 
			String itemtype, String starttime, String endtime, String deadline, String alerttime, String repeat, String completed, Integer color)
			{
				super(id, title, description,location, category,alerttype, priority, 
						itemtype, starttime, endtime, deadline, alerttime, repeat, completed, color);
			}
}
