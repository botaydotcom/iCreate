package com.android.apptime.database;


import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.apptime.TaskItem;



public class TaskDBAdapter {

	private static final String DATABASE_TABLE_TASK = "Tasks_Table";
    private static final String TASKDB_KEY_ID = "taskdb_task_id";
    private static final String TASKDB_TITLE = "taskdb_task_title";
    private static final String TASKDB_DESCRIPTION = "taskdb_description";
    private static final String TASKDB_LOCATION = "taskdb_location";
    private static final String TASKDB_PRIORITY = "taskdb_priority";
    private static final String TASKDB_REPEAT = "taskdb_repeat";
    private static final String TASKDB_CATEGORY = "taskdb_category";
    private static final String TASKDB_COMPLETED = "taskdb_completed";
    private static final String TASKDB_COLOR = "taskdb_color";
    private static final String TASKDB_DEADLINE = "taskdb_deadline";
    private static final String TASKDB_ALERTTIME = "taskdb_alert_time";
    private static final String TASKDB_TYPE = "taskdb_type";
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
    
    public TaskDBAdapter(Context ctx) {
        this.context = ctx;
    }
	
    public void close() {
        mDb.close();
      }
	
    public TaskDBAdapter open() throws SQLiteException {
    	this.mDbHelper = new DatabaseHelper(this.context);
    	this.mDb = this.mDbHelper.getWritableDatabase();
        return this;

      }  
    //Insert a new task into the TASK table
    public long createTask(TaskItem _task) {
      // Create a new row of values to insert.
    	String t0 = "INSERT INTO " + DATABASE_TABLE_TASK + " (" + TASKDB_TITLE + ", " + TASKDB_DESCRIPTION + ", " + TASKDB_LOCATION + ", " + TASKDB_PRIORITY
        + ", " + TASKDB_REPEAT + ", " + TASKDB_CATEGORY + ", " + TASKDB_COMPLETED + ", " + TASKDB_COLOR
        + ", " + TASKDB_DEADLINE + ", " +  TASKDB_ALERTTIME + ", " +   TASKDB_TYPE +    ")" + " VALUES ('" +
        _task.GetTitle() + "','" +
        _task.GetDescription() + "','" +
        _task.GetLocation() + "','" +
        _task.GetPriority() + "','" +
        _task.GetRepeat() + "','" +
        _task.GetCategory() + "','" +
        _task.GetCompleted() + "','" +
        _task.GetColor() + "','" + 
       
        _task.GetDeadline() + "','" +  
        _task.GetAlertTime() + "','" + 
        _task.GetItemType() + "','" + "');";
        //1', '2', '3', '4', '5', '6' , '7', '8', '9');";
        mDb.execSQL(t0);
        // Insert the row.
        String query = "SELECT last_insert_rowid();";
        Cursor cursor = mDb.rawQuery(query, null);
        int id = 0;     
        if (cursor.moveToFirst())
        {
            do
            {           
                id = cursor.getInt(0);                  
            } while(cursor.moveToNext());           
        }

        
        return id;
    }
    
    public int updateTask(TaskItem _task)
    {
      ContentValues newTaskValues = new ContentValues();
      
      newTaskValues.put(TASKDB_DESCRIPTION, _task.GetDescription());
      newTaskValues.put(TASKDB_LOCATION, _task.GetLocation());
      newTaskValues.put(TASKDB_PRIORITY, _task.GetPriority());
      newTaskValues.put(TASKDB_REPEAT, _task.GetRepeat());
      newTaskValues.put(TASKDB_CATEGORY, _task.GetCategory());
      newTaskValues.put(TASKDB_COMPLETED, _task.GetCompleted());
      newTaskValues.put(TASKDB_COLOR, _task.GetColor());
      
      
      newTaskValues.put(TASKDB_DEADLINE, String.valueOf(_task.GetDeadline().getTime()));
      newTaskValues.put(TASKDB_ALERTTIME, String.valueOf(_task.GetAlertTime().getTime()));
      newTaskValues.put(TASKDB_TYPE, _task.GetItemType());
      return this.mDb.update(DATABASE_TABLE_TASK, newTaskValues, TASKDB_KEY_ID + "=" + _task.GetId(), null);    	
    }
    
    public int removeTask(TaskItem _task)
    {
    	// TODO: REMOVE ALL THE TASK RELATIONS ALSO
    	
    	return this.mDb.delete(DATABASE_TABLE_TASK, _task.GetId()+ "=" + TASKDB_KEY_ID, null);
    }
	
	
 // get task by task id
    public Cursor getTaskById(long rowId) throws SQLException {

        Cursor mCursor =

        this.mDb.query(true, DATABASE_TABLE_TASK, new String[] { TASKDB_KEY_ID,TASKDB_TITLE, TASKDB_DESCRIPTION, TASKDB_LOCATION, TASKDB_PRIORITY, TASKDB_REPEAT, TASKDB_CATEGORY, TASKDB_COMPLETED, TASKDB_COLOR, TASKDB_DEADLINE, TASKDB_ALERTTIME, TASKDB_TYPE  }, TASKDB_KEY_ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    // get task by task deadline date
    public Cursor getTaskByDate(Date datetime) throws SQLException {

    	int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
    	long t = datetime.getTime();
    	long thedayafter = MILLIS_IN_DAY + t;
        Cursor mCursor =

        this.mDb.query(true, DATABASE_TABLE_TASK, new String[] {  TASKDB_KEY_ID,TASKDB_TITLE, TASKDB_DESCRIPTION, TASKDB_LOCATION, TASKDB_PRIORITY, TASKDB_REPEAT, TASKDB_CATEGORY, TASKDB_COMPLETED, TASKDB_COLOR, TASKDB_DEADLINE, TASKDB_ALERTTIME, TASKDB_TYPE    }, TASKDB_DEADLINE + " >= " + t + " AND " + TASKDB_DEADLINE + " < " + thedayafter , null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor getTaskByDateRange(Date fromdate, Date todate) throws SQLException {

    	int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
    	long fromtime = fromdate.getTime();
    	long totime = todate.getTime();
        Cursor mCursor =

        this.mDb.query(true, DATABASE_TABLE_TASK, new String[] {  TASKDB_KEY_ID,TASKDB_TITLE, TASKDB_DESCRIPTION, TASKDB_LOCATION, TASKDB_PRIORITY, TASKDB_REPEAT, TASKDB_CATEGORY, TASKDB_COMPLETED, TASKDB_COLOR, TASKDB_DEADLINE, TASKDB_ALERTTIME, TASKDB_TYPE    }, TASKDB_DEADLINE + " >= " + fromtime + " AND " + TASKDB_DEADLINE + " < " + totime , null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
}
