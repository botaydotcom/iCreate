package com.android.apptime.datahandler;

public class MapData{
	private String title;
	private String horizontal;
	private String vertical;
	private String longitude;
	private String latitude;
	private String link;

	public String title(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title.trim();
	}
	
	public String getHorizontal(){
		return horizontal;
	}
	
	public void setHorizontal(String _horizontal){
		this.horizontal = _horizontal.trim();
	}
	
	public String getVertical(){
		return vertical;
	}
	
	public void setVertical(String _vertical){
		this.vertical = _vertical.trim();
	}
	
	public String getLongitude(){
		return longitude;
	}
	
	public void setLongitude(String _longitude){
		this.longitude = _longitude.trim();
	}
	
	public String getLatitude(){
		return latitude;
	}
	
	public void setLatitude(String _latitude){
		this.latitude = _latitude.trim();
	}
	
	public MapData copy(){
		MapData copy = new MapData();
		
		copy.title = title;
		copy.horizontal = horizontal;
		copy.vertical = vertical;
		copy.longitude = longitude;
		copy.latitude = latitude;
		copy.link = link;
		
		return copy;
	}
	
	
}