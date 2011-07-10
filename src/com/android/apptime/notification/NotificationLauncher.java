package com.android.apptime.notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.View;

import com.android.apptime.DatabaseInterface;
import com.android.apptime.Item;


public class NotificationLauncher{ 
/*	public NotificationLauncher(Context con){
		context = con;
	}
	
	private Context context;
	private DatabaseInterface database = DatabaseInterface.getDatabaseInterface(this.context);

	public void InitDatabase(){
		database.OpenDatabase(this.getApplicationContext());
		List<String> alerttype = new ArrayList<String>(); alerttype.add("alert1");
		Date starttime= new Date(1221423535);
		Date endtime =   new Date(122142400);
		Date alerttime = new Date(122142340);
		Item item = new Item("title test",
				  "description test", "location test", "category test", alerttype,
				 "priority test", "Event", starttime,
				 endtime, null, alerttime, "repeat test",
				  "completed test", 1);
		//database.AddItemToDatabase(getApplicationContext(), item);
		//database.AddItemToDatabase(this.getApplicationContext(),item);
		
		database.RetrieveItemFromDatabase(getApplicationContext(), 5);
		database.CloseDatabase();	
	}
	
	public void getNextItem(){
		// get the next item to be sent in the reminder
	}
	
	public void SendItemReminder(View v){
        
    }*/
}
