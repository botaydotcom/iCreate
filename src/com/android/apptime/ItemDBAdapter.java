package com.android.apptime;



import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


public class ItemDBAdapter {

 
  private static final String DATABASE_TABLE_ITEM = "Items_Table";
  private static final String ITEMDB_KEY_ID = "itemdb_item_id";
  private static final String ITEMDB_KEY_TASK = "itemdb_taskid";
  private static final String ITEMDB_KEY_EVENT = "itemdb_eventid";

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

  public ItemDBAdapter(Context ctx) {
      this.context = ctx;
  }

    
  public void close() {
    mDb.close();
  }
  
  public ItemDBAdapter open() throws SQLiteException {
	this.mDbHelper = new DatabaseHelper(this.context);
	this.mDb = this.mDbHelper.getWritableDatabase();
    return this;

  }  
  
  
  //Insert a new task into the Item table
  public long insertTask(TaskItem _task) {
    // Create a new row of values to insert.
    ContentValues newTaskValues = new ContentValues();
    // Assign values for each row.
    newTaskValues.put(ITEMDB_KEY_TASK, _task.GetId());
    newTaskValues.put(ITEMDB_KEY_EVENT, 0);
    
    TaskDBAdapter taskDBAdapter = new TaskDBAdapter(this.context);
    taskDBAdapter.open();
    taskDBAdapter.createTask(_task);
    taskDBAdapter.close();
    
    // Insert the row.
    return mDb.insert(DATABASE_TABLE_ITEM, null, newTaskValues);
  }

  //Insert a new event into the Item table
  public long insertEvent(EventItem _event) {
    // Create a new row of values to insert.
    ContentValues newTaskValues = new ContentValues();
    // Assign values for each row.
    newTaskValues.put(ITEMDB_KEY_EVENT, _event.GetId());
    newTaskValues.put(ITEMDB_KEY_TASK, 0);
    // Insert the row.
    
    
    return mDb.insert(DATABASE_TABLE_ITEM, null, newTaskValues);
  }

  public int createItem(Item _item)  {
	  if (_item.GetItemType()=="Event")
		  insertEvent((EventItem) _item);
	  else
		  insertTask((TaskItem) _item);
	  
	  return 0;
  }
  
   
  
  // Remove a task based on its index
  public boolean removeItem(long _rowIndex) {
    return mDb.delete(DATABASE_TABLE_ITEM, ITEMDB_KEY_ID + "=" + _rowIndex, null) > 0;
  }

  // Update a task
  public boolean updateTask(long _rowIndex, String _task) {
    
	return true;  
  }
  
  // Update an event
  public boolean updateEvent(long _rowIndex, String _event) {
	  
	  return true;
  }
  
  
  public Cursor getAllItems() {
	  return this.mDb.query(DATABASE_TABLE_ITEM, new String[] {ITEMDB_KEY_ID, ITEMDB_KEY_TASK, ITEMDB_KEY_EVENT}, null, null, null, null, null);
  }
  
  
  public Cursor getItemById(long rowId) throws SQLException {

      Cursor mCursor =

      this.mDb.query(true, DATABASE_TABLE_ITEM, new String[] { ITEMDB_KEY_ID, ITEMDB_KEY_TASK, ITEMDB_KEY_EVENT}, ITEMDB_KEY_ID + "=" + rowId, null, null, null, null, null);
      if (mCursor != null) {
          mCursor.moveToFirst();
      }
      return mCursor;
  }
 
  
 
}
