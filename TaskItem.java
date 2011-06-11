package com.dummies.android.helloandroid;

import java.util.Date;
import java.util.List;

import android.graphics.Color;

// REMARK: TaskItem *MUST* has deadline.
public class TaskItem extends Item{
	
	TaskItem (String id, String title, String description, String location, List<String> category, List<String> alerttype, String priority, 
	String itemtype, Date starttime, Date endtime, Date deadline, Date alerttime, Boolean repeat, Boolean completed, Color color)
	{
		super(id, title, description,location, category,alerttype, priority, 
				itemtype, starttime, endtime, deadline, alerttime, repeat, completed, color);
	}
}
