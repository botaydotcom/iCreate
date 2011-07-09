package com.android.apptime;

import java.util.Date;
import java.util.List;

import android.graphics.Color;

// REMARK: TaskItem *MUST* has deadline.
public class TaskItem extends Item{
	
	public TaskItem (String title, String description, String location, String category, List<String> alerttype, String priority, 
			String itemtype, Date starttime, Date endtime, Date deadline, Date alerttime, String repeat, String completed, Integer color)
	{
		super(title, description,location, category,alerttype, priority, 
				itemtype, starttime, endtime, deadline, alerttime, repeat, completed, color);
	}
}
