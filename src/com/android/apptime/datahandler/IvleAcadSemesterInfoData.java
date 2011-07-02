package com.android.apptime.datahandler;

public class IvleAcadSemesterInfoData implements Comparable<IvleAcadSemesterInfoData>{
	private String acad_year;
	private String even_odd_week;
	private String lecture_start_date;
	private String semester;
	private String semester_end_date;
	private String semester_start_date;
	private String tutorial_start_date;
	private String type_name;
	private String week_type_end_date;
	private String week_type_start_date;

	public String getAcadYear(){
		return acad_year;
	}
	
	public void setAcadYear(String acad_year){
		this.acad_year = acad_year.trim();
	}
	
	public String getEvenOddWeek(){
		return even_odd_week;
	}
	
	public void setEvenOddWeek(String even_odd_week){
		this.even_odd_week = even_odd_week.trim();
	}
	
	public String getLectureStartDate(){
		return lecture_start_date;
	}
	
	public void setLectureStartDate(String lecture_start_date){
		this.lecture_start_date = lecture_start_date.trim();
	}
	
	public String getSemester(){
		return semester;
	}
	
	public void setSemester(String semester){
		this.semester = semester.trim();
	}
	
	public String getSemesterEndDate(){
		return semester_end_date;
	}
	
	public void setSemesterEndDate(String semester_end_date){
		this.semester_end_date = semester_end_date.trim();
	}
	public String getSemesterStartDate(){
		return semester_start_date;
	}
	
	public void setSemesterStartDate(String semester_start_date){
		this.semester_start_date = semester_start_date.trim();
	}
	public String getTutorialStartDate(){
		return tutorial_start_date;
	}
	
	public void setTutorialStartDate(String tutorial_start_date){
		this.tutorial_start_date = tutorial_start_date.trim();
	}
	public String getTypeName(){
		return type_name;
	}
	
	public void setTypeName(String type_name){
		this.type_name = type_name.trim();
	}
	public String getWeekTypeEndDate(){
		return week_type_end_date;
	}
	
	public void setWeekTypeEndDate(String week_type_end_date){
		this.week_type_end_date = week_type_end_date.trim();
	}
	public String getWeekTypeStartDate(){
		return week_type_start_date;
	}
	
	public void setWeekTypeStartDate(String week_type_start_date){
		this.week_type_start_date = week_type_start_date.trim();
	}
	

	public IvleAcadSemesterInfoData copy(){
		IvleAcadSemesterInfoData copy = new IvleAcadSemesterInfoData();
		
		copy.acad_year = acad_year;
		copy.even_odd_week = even_odd_week;
		copy.lecture_start_date = lecture_start_date;
		copy.semester = semester;
		copy.semester_end_date = semester_end_date;
		copy.semester_start_date = semester_start_date;
		copy.tutorial_start_date = tutorial_start_date;
		copy.type_name = type_name;
		copy.week_type_end_date = week_type_end_date;
		copy.week_type_start_date = week_type_start_date;
		
		return copy;
	}
	
	public int compareTo(IvleAcadSemesterInfoData another) {
		if (another == null) return 1;
		// sort descending, most recent first
		return another.acad_year.compareTo(acad_year);
	}
}
