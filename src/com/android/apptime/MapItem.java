package com.android.apptime;

public class MapItem {
	
	private String _title;
	private Integer _id;
	private Integer _horizontal;
	private Integer _vertical;
	private double _longitude;
	private double _latitude;
	private String _link;
	
	public String GetTitle()
	{
		return _title;
	}
	public Integer GetId()
	{
		return _id;
	}
	
	public void SetTitle(String newtitle)
	{
		_title = newtitle;
	}
	
	public Integer GetHorizontal()
	{
		return _horizontal;
	}
	
	public Integer GetVertical()
	{
		return _vertical;
	}
	
	public double GetLongitude()
	{
		return _longitude;
	}
	
	public double GetLatitude()
	{
		return _latitude;
	}
	
	public String GetLink()
	{
		return _link;
	}
	
}
