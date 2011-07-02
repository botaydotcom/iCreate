package com.android.apptime.datahandler;

public class IvleEventData implements Comparable<IvleEventData>{
	private String date;
	private String description;
	private String event_type;
	private String id;
	private String location;
	private String title;

	public String getDate(){
		return date;
	}
	
	public void setDate(String date){
		this.date = date.trim();
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description.trim();
	}
	
	public String getEventType(){
		return event_type;
	}
	
	public void setEventType(String event_type){
		this.event_type = event_type.trim();
	}
	
	public String getID(){
		return id;
	}
	
	public void setID(String ID){
		this.id = id.trim();
	}
	
	public String getLocation(){
		return location;
	}
	
	public void setLocation(String location){
		this.location = location.trim();
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title.trim();
	}
	
	public IvleEventData copy(){
		IvleEventData copy = new IvleEventData();
		
		copy.date = date;
		copy.description = description;
		copy.event_type = event_type;
		copy.id = id;
		copy.location = location;
		copy.title = title;
		
		return copy;
	}

	public int compareTo(IvleEventData another) {
		if (another == null) return 1;
		// sort descending, most recent first
		return another.date.compareTo(date);
	}
}
