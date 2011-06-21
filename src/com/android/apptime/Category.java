package com.android.apptime;

import java.util.List;

public class Category {
	private List<Integer> _eventList;
	private List<Integer> _taskList;
	private String _title;
	private Integer _id;
	// TODO: Waiting for API request from GUI Category View
	
	public Integer GetId()
	{
		return _id;
	}

	
	public void SetTitle(String newTitle)
	{
		_title = newTitle;
		// TODO: Update/notify database change here
	}
	public String GetTitle()
	{
		return _title;
	}
}
