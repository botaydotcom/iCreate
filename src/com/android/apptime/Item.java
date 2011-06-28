package com.android.apptime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.android.apptime.database.ItemDBAdapter;

public class Item {
	private String _id;
	private String _title;
	private String _description;
	private String _location;
	private String _priority; // LOW-MED-HIGH?
	private String _itemType; // TaskItem or EventItem?
	private String _category;
	private List<String> _alertType;
	private String _startTime;
	private String _endTime;
	private String _alertTime; // alert time means sth like : alert 15 min before/ 1hr before / 2hr before
	private String _deadline;	
	private String _repeat;
	private String _completed;
	private Integer _color;
	
	// Constructor
	Item (String title, String description, String location, String category, List<String> alerttype, String priority, 
	String itemtype, String starttime, String endtime, String deadline, String alerttime, String repeat, String completed, Integer color)
	{
		
		_title = title;
		_description = description;
		_location = location;
		_category = category;
		_alertType = alerttype;
		_priority = priority;
		_itemType = itemtype;
		Date startdate = new Date();
		Date enddate = new Date();
		Date deadlinedate = new Date();
		Date alertdate = new Date();
		DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
			if (starttime!=null)
			{
				startdate = dfm.parse(starttime);
			}
			if (endtime!=null)
			{
				enddate = dfm.parse(endtime);
			}
			if (deadline!=null)
			{
				deadlinedate = dfm.parse(deadline);
			}
			if (alerttime!=null)
			{
				alertdate = dfm.parse(alerttime);
			}
			int yu= 0;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (starttime!=null)
		{
			_startTime = String.valueOf(startdate.getTime());
		}
		if (endtime!=null)
		{
			_endTime = String.valueOf(enddate.getTime());
		}
		if (deadline!=null)
		{
			_deadline = String.valueOf(deadlinedate.getTime());
		}
		if (alerttime!=null)
		{
			_alertTime = String.valueOf(alertdate.getTime());
		}
		
		_repeat = repeat;
		_completed = completed;
		_color = color;		
	}
	
	
	
	// Getters
	public String  GetId()
	{
		return _id;		
	}
	public String  GetTitle()
	{
		return _title;
	}
	public String  GetDescription()
	{
		return _description;		
	}
	public String  GetLocation()
	{
		return _location;
	}
	public String GetCategory()
	{
		return _category;
	}
	public List<String>  GetAlertType()
	{
		return _alertType;
	}
	public String  GetPriority()
	{
		return _priority;
	}
	public String    GetStartTime()
	{
		Date startdate = new Date(Long.parseLong(_startTime));	
		return startdate.toString();
	}
	public String   GetEndTime()
	{
		Date enddate = new Date(Long.parseLong(_endTime));
		return enddate.toString();
	}
	public String   GetDeadline()
	{
		Date deadlinedate = new Date(Long.parseLong(_deadline));
		return deadlinedate.toString();
	}
	public String GetAlertTime()
	{
		Date alertdate = new Date(Long.parseLong(_alertTime));
		return alertdate.toString();
	}
	public String GetRepeat()
	{
		return _repeat;
	}
	public String GetCompleted()
	{
		return _completed;
	}
	public Integer  GetColor()
	{
		return _color;
	}
	public String GetItemType()
	{
		return _itemType;
	}
	
	// Setters
	public void  SetId(String newId)
	{
		_id = newId;
	}
	public void  SetTitle(String newTitle)
	{
		_title = newTitle;
	}
	public void  SetDescription(String newDescription)
	{
		_description = newDescription;
	}
	public void  SetLocation(String newLocation)
	{
		_location = newLocation;
	}
	public void  SetCategory(String newCategory)
	{
		_category = newCategory;
	}
	public void  SetAlertType(List<String> newAlertType)
	{
		_alertType = newAlertType;
	}
	public void  SetPriority(String newPriority)
	{
		_priority = newPriority;
	}
	public void  SetStartTime(String newStartTime)
	{
		_startTime = newStartTime;
	}
	public void  SetEndTime(String newEndTime)
	{
		_endTime = newEndTime;
	}
	public void  SetDeadline(String newDeadline)
	{
		_deadline = newDeadline;
	}
	public void  SetAlertTime(String newAlertTime)
	{
		_alertTime = newAlertTime;
	}
	public void  SetRepeat(String newRepeat)
	{
		_repeat = newRepeat;
	}
	public void  SetCompleted(String newCompleted)
	{
		_completed = newCompleted;
	}
	public void  SetColor(Integer newColor)
	{
		_color = newColor;
	}
	public void  SetItemType(String itemType)
	{
		_itemType = itemType;
	}
	
	
	
	public boolean GetItemInOneDay(Date thedate)
	{
		
		
		return true;
	}
}



