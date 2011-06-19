package com.android.apptime;

import java.util.*;  // to use Time type
import android.graphics.Color; // to use Color type
public abstract class Item {
	public String _id;
	public String _title;
	public String _description;
	public String _location;
	public String _priority; // LOW-MED-HIGH?
	public String _itemType; // TaskItem or EventItem?
	public String _category;
	public List<String> _alertType;
	public String _startTime;
	public String _endTime;
	public String _alertTime; // alert time means sth like : alert 15 min before/ 1hr before / 2hr before
	public String _deadline;	
	public String _repeat;
	public String _completed;
	public Integer _color;
	
	// Constructor
	Item (String id, String title, String description, String location, String category, List<String> alerttype, String priority, 
	String itemtype, String starttime, String endtime, String deadline, String alerttime, String repeat, String completed, Integer color)
	{
		_id = id;
		_title = title;
		_description = description;
		_location = location;
		_category = category;
		_alertType = alerttype;
		_priority = priority;
		_itemType = itemtype;
		_startTime = starttime;
		_endTime = endtime;
		_deadline = deadline;
		_alertTime = alerttime;
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
		return _startTime;
	}
	public String   GetEndTime()
	{
		return _endTime;
	}
	public String   GetDeadline()
	{
		return _deadline;
	}
	public String GetAlertTime()
	{
		return _alertTime;
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
}



