package com.android.apptime.datahandler;

public class IvleSpecialDayData implements Comparable<IvleSpecialDayData>{
	private String description;
	private String end_date;
	private String start_date;
	private String type;

	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description.trim();
	}
	
	public String getEndDate(){
		return end_date;
	}
	
	public void setEndDate(String end_date){
		this.end_date = end_date.trim();
	}
	
	public String getStartDate(){
		return start_date;
	}
	
	public void setStartDate(String start_date){
		this.start_date = start_date.trim();
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		this.type = type.trim();
	}

	public IvleSpecialDayData copy(){
		IvleSpecialDayData copy = new IvleSpecialDayData();
		
		copy.description = description;
		copy.end_date = end_date;
		copy.start_date = start_date;
		copy.type = type;
		
		return copy;
	}
	
	public int compareTo(IvleSpecialDayData another) {
		if (another == null) return 1;
		// sort descending, most recent first
		return another.start_date.compareTo(start_date);
	}
}
