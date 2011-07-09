package com.android.apptime.datahandler;

public class IvleTimetableData implements Comparable<IvleTimetableData>{
	private String acad_year; // 
	private String class_no; // description 
	private String day_code; // 
	private String day_text;
	private String end_time; // end time 
	private String lesson_type; // title
	private String module_code; // title
	private String semester; 
	private String start_time; // start time
	private String venue; // location
	private String week_code;
	private String week_text;

	public String getAcadYear(){
		return acad_year;
	}
	
	public void setAcadYear(String acad_year){
		this.acad_year = acad_year.trim();
	}
	
	public String getClassNo(){
		return class_no;
	}
	
	public void setClassNo(String class_no){
		this.class_no = class_no.trim();
	}
	
	public String getDayCode(){
		return day_code;
	}
	
	public void setDayCode(String day_code){
		this.day_code = day_code.trim();
	}
	
	public String getDayText(){
		return day_text;
	}
	
	public void setDayText(String day_text){
		this.day_text = day_text.trim();
	}
	
	public String getEndTime(){
		return end_time;
	}
	
	public void setEndTime(String end_time){
		this.end_time = end_time.trim();
	}
	
	public String getLessonType(){
		return lesson_type;
	}
	
	public void setLessonType(String lesson_type){
		this.lesson_type = lesson_type.trim();
	}
	
	public String getModuleCode(){
		return module_code;
	}
	
	public void setModuleCode(String module_code){
		this.module_code = module_code.trim();
	}
	
	public String getSemster(){
		return semester;
	}
	
	public void setSemester(String semester){
		this.semester = semester.trim();
	}
	
	public String getStartTime(){
		return start_time;
	}
	
	public void setStartTime(String start_time){
		this.start_time = start_time.trim();
	}
	
	public String getVenue(){
		return venue;
	}
	
	public void setVenue(String venue){
		this.venue = venue.trim();
	}
	
	public String getWeekCode(){
		return week_code;
	}
	
	public void setWeekCode(String week_code){
		this.week_code = week_code.trim();
	}
	
	public String getWeekText(){
		return week_text;
	}
	
	public void setWeekText(String week_text){
		this.week_text = week_text.trim();
	}
	
	public IvleTimetableData copy(){
		IvleTimetableData copy = new IvleTimetableData();
		
		copy.acad_year = acad_year;
		copy.class_no = class_no;
		copy.day_code = day_code;
		copy.day_text = day_text;
		copy.end_time = end_time;
		copy.lesson_type = lesson_type;
		copy.module_code = module_code;
		copy.semester = semester;
		copy.start_time = start_time;
		copy.venue = venue;
		copy.week_code = week_code;
		copy.week_text = week_text;
		
		return copy;
	}
	
	public int compareTo(IvleTimetableData another) {
		if (another == null) return 1;
		// sort descending, most recent first
		return another.start_time.compareTo(start_time);
	}
}