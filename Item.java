package com.dummies.android.helloandroid;

import java.util.*;  // to use Time type
import android.graphics.Color; // to use Color type
public abstract class Item {
	public String _id;
	public String _title;
	public String _description;
	public String _location;
	public String _priority; // LOW-MED-HIGH?
	public String _itemType; // TaskItem or EventItem?
	public List<String> _category;
	public List<String> _alertType;
	public Date _startTime;
	public Date _endTime;
	public Date _alertTime; // alert time means sth like : alert 15 min before/ 1hr before / 2hr before
	public Date _deadline;	
	public Boolean _repeat;
	public Boolean _completed;
	public Color _color;
	
	// Constructor
	Item (String id, String title, String description, String location, List<String> category, List<String> alerttype, String priority, 
	String itemtype, Date starttime, Date endtime, Date deadline, Date alerttime, Boolean repeat, Boolean completed, Color color)
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
	public List<String>  GetCategory()
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
	public Date    GetStartTime()
	{
		return _startTime;
	}
	public Date    GetEndTime()
	{
		return _endTime;
	}
	public Date    GetDeadline()
	{
		return _deadline;
	}
	public Date GetAlertTime()
	{
		return _alertTime;
	}
	public Boolean GetRepeat()
	{
		return _repeat;
	}
	public Boolean GetCompleted()
	{
		return _completed;
	}
	public Color   GetColor()
	{
		return _color;
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
	public void  SetCategory(List<String> newCategory)
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
	public void  SetStartTime(Date newStartTime)
	{
		_startTime = newStartTime;
	}
	public void  SetEndTime(Date newEndTime)
	{
		_endTime = newEndTime;
	}
	public void  SetDeadline(Date newDeadline)
	{
		_deadline = newDeadline;
	}
	public void  SetAlertTime(Date newAlertTime)
	{
		_alertTime = newAlertTime;
	}
	public void  SetRepeat(Boolean newRepeat)
	{
		_repeat = newRepeat;
	}
	public void  SetCompleted(Boolean newCompleted)
	{
		_completed = newCompleted;
	}
	public void  SetColor(Color newColor)
	{
		_color = newColor;
	}
}



