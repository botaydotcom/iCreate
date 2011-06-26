package com.android.apptime.database;

import com.android.apptime.EventItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;



public class EventDBAdapter {
	
	private static final String DATABASE_TABLE_EVENT = "Events_Table";
    private static final String EVENTDB_KEY_ID = "eventdb_event_id";
    private static final String EVENTDB_DESCRIPTION = "eventdb_description";
    private static final String EVENTDB_LOCATION = "eventdb_location";
    private static final String EVENTDB_PRIORITY = "eventdb_priority";
    private static final String EVENTDB_REPEAT = "eventdb_repeat";
    private static final String EVENTDB_CATEGORY = "eventdb_category";
    private static final String EVENTDB_COMPLETED = "eventdb_completed";
    private static final String EVENTDB_COLOR = "eventdb_color";
    private static final String EVENTDB_STARTTIME = "eventdb_start_time";
    private static final String EVENTDB_ENDTIME = "eventdb_end_time";
    
    //private SQLiteDatabase db;
    private final Context context;
    
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, MainDBAdapter.DATABASE_NAME, null, MainDBAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
    
    public EventDBAdapter(Context ctx) {
        this.context = ctx;
    }
	
    public void close() {
        mDb.close();
      }
	
    public EventDBAdapter open() throws SQLiteException {
    	this.mDbHelper = new DatabaseHelper(this.context);
    	this.mDb = this.mDbHelper.getWritableDatabase();
        return this;

      }  
    
    
  //Insert a new event into the Event table
    public long createEvent(EventItem _event) {
      // Create a new row of values to insert.
      ContentValues newEventValues = new ContentValues();
      // Assign values for each row.
      newEventValues.put(EVENTDB_DESCRIPTION, _event.GetDescription());
      newEventValues.put(EVENTDB_LOCATION, _event.GetLocation());
      newEventValues.put(EVENTDB_PRIORITY, _event.GetPriority());
      newEventValues.put(EVENTDB_REPEAT, _event.GetRepeat());
      newEventValues.put(EVENTDB_CATEGORY, _event.GetCategory());
      newEventValues.put(EVENTDB_COMPLETED, _event.GetCompleted());
      newEventValues.put(EVENTDB_COLOR, _event.GetColor());
      newEventValues.put(EVENTDB_STARTTIME, _event.GetStartTime());
      newEventValues.put(EVENTDB_ENDTIME, _event.GetEndTime());
      
      
      // Insert the row.
      return mDb.insert(DATABASE_TABLE_EVENT, null, newEventValues);
    }
	
    //Update a new event in the Event table
    public long updateEvent(EventItem _event) {
      // Create a new row of values to insert.
      ContentValues newEventValues = new ContentValues();
      // Assign values for each row.
      newEventValues.put(EVENTDB_DESCRIPTION, _event.GetDescription());
      newEventValues.put(EVENTDB_LOCATION, _event.GetLocation());
      newEventValues.put(EVENTDB_PRIORITY, _event.GetPriority());
      newEventValues.put(EVENTDB_REPEAT, _event.GetRepeat());
      newEventValues.put(EVENTDB_CATEGORY, _event.GetCategory());
      newEventValues.put(EVENTDB_COMPLETED, _event.GetCompleted());
      newEventValues.put(EVENTDB_COLOR, _event.GetColor());
      newEventValues.put(EVENTDB_STARTTIME, _event.GetStartTime());
      newEventValues.put(EVENTDB_ENDTIME, _event.GetEndTime());
      
      
      // Insert the row.
      return mDb.update(DATABASE_TABLE_EVENT, newEventValues, EVENTDB_KEY_ID + "=" + _event.GetId(), null);
    }
	
    
    public int removeEvent(EventItem _event)
    {
    	// TODO: REMOVE ALL THE EVENT RELATIONS ALSO
    	
    	return this.mDb.delete(DATABASE_TABLE_EVENT, _event.GetId()+ "=" + EVENTDB_KEY_ID, null);
    }
    
    
}
