package com.android.apptime;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;



public class TaskDBAdapter {

	private static final String DATABASE_TABLE_TASK = "Tasks_Table";
    private static final String TASKDB_KEY_ID = "taskdb_task_id";
    private static final String TASKDB_DESCRIPTION = "taskdb_description";
    private static final String TASKDB_LOCATION = "taskdb_location";
    private static final String TASKDB_PRIORITY = "taskdb_priority";
    private static final String TASKDB_REPEAT = "taskdb_repeat";
    private static final String TASKDB_CATEGORY = "taskdb_category";
    private static final String TASKDB_COMPLETED = "taskdb_completed";
    private static final String TASKDB_COLOR = "taskdb_color";
    private static final String TASKDB_DEADLINE = "taskdb_deadline";
    
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
      ContentValues newTaskValues = new ContentValues();
      // Assign values for each row.
      newTaskValues.put(TASKDB_DESCRIPTION, _task.GetDescription());
      newTaskValues.put(TASKDB_LOCATION, _task.GetLocation());
      newTaskValues.put(TASKDB_PRIORITY, _task.GetPriority());
      newTaskValues.put(TASKDB_REPEAT, _task.GetRepeat());
      newTaskValues.put(TASKDB_CATEGORY, _task.GetCategory());
      newTaskValues.put(TASKDB_COMPLETED, _task.GetCompleted());
      newTaskValues.put(TASKDB_COLOR, _task.GetColor());
      newTaskValues.put(TASKDB_DEADLINE, _task.GetDeadline());
      // Insert the row.
      return mDb.insert(DATABASE_TABLE_TASK, null, newTaskValues);
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
      newTaskValues.put(TASKDB_DEADLINE, _task.GetDeadline());
      
      return this.mDb.update(DATABASE_TABLE_TASK, newTaskValues, TASKDB_KEY_ID + "=" + _task.GetId(), null);    	
    }
    
    public int removeTask(TaskItem _task)
    {
    	// TODO: REMOVE ALL THE TASK RELATIONS ALSO
    	
    	return this.mDb.delete(DATABASE_TABLE_TASK, _task.GetId()+ "=" + TASKDB_KEY_ID, null);
    }
	
	
}
