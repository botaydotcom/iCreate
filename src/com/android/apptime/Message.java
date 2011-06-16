package com.android.apptime;

/*import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;*/


public class Message implements Comparable<Message>{
	//static SimpleDateFormat FORMATTER = 
	//	new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	
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
		this.date = description.trim();
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
	
	public Message copy(){
		Message copy = new Message();
		
		copy.date = date;
		copy.description = description;
		copy.event_type = event_type;
		copy.id = id;
		copy.location = location;
		copy.title = title;
		
		return copy;
	}
	
	/*@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Title: ");
		sb.append(title);
		sb.append('\n');
		sb.append("Date: ");
		sb.append(this.getDate());
		sb.append('\n');
		sb.append("Link: ");
		sb.append(link);
		sb.append('\n');
		sb.append("Description: ");
		sb.append(description);
		
		return sb.toString();
	}*/

	/*@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}*/
	
	/*@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}*/

	public int compareTo(Message another) {
		if (another == null) return 1;
		// sort descending, most recent first
		return another.date.compareTo(date);
	}
}
