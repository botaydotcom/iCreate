package com.android.apptime.database;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.apptime.EventItem;
import com.android.apptime.Item;
import com.android.apptime.TaskItem;


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
  public long insertEvent(Item _event) {
    // Create a new row of values to insert.
    ContentValues newTaskValues = new ContentValues();
    // Assign values for each row.
    newTaskValues.put(ITEMDB_KEY_EVENT, _event.GetId());
    newTaskValues.put(ITEMDB_KEY_TASK, 0);
    // Insert the row.
    
    EventDBAdapter eventDBAdapter = new EventDBAdapter(this.context);
    eventDBAdapter.open();
    eventDBAdapter.createEvent(_event);
    eventDBAdapter.close();
    
    return mDb.insert(DATABASE_TABLE_ITEM, null, newTaskValues);
  }

  public int createItem(Item _item)  {
	  if (_item.GetItemType()=="Event")
		  insertEvent( _item);
	  else
		  insertTask((TaskItem) _item);
	  
	  return 0;
  }
  
   
  
  // Remove a task based on its index
  public boolean removeItem(long _rowIndex) {
    return mDb.delete(DATABASE_TABLE_ITEM, ITEMDB_KEY_ID + "=" + _rowIndex, null) > 0;
  }

  // Update a task
  public boolean updateTask(long _rowIndex, TaskItem _task) {
    TaskDBAdapter taskDBAdapter = new TaskDBAdapter(this.context);
    taskDBAdapter.open();
    taskDBAdapter.updateTask(_task);
    taskDBAdapter.close();
	return true;  
  }
  
  // Update an event
  public boolean updateEvent(long _rowIndex, EventItem _event) {
	  EventDBAdapter eventDBAdapter = new EventDBAdapter(this.context);
	  eventDBAdapter.open();
	  eventDBAdapter.updateEvent(_event);
	  eventDBAdapter.close();	  
	  
	  Cursor myItem = this.getItemById(_rowIndex);
	  
	  ContentValues newEventValues = new ContentValues();
	  //newTaskValues.put(ITEMDB_KEY_ID, myItem.getInt(0));
	  newEventValues.put(ITEMDB_KEY_TASK, 0);
	  newEventValues.put(ITEMDB_KEY_EVENT, _event.GetId());
	  return (mDb.update(DATABASE_TABLE_ITEM, newEventValues, ITEMDB_KEY_ID + "=" + myItem.getInt(0), null) >0);
	  //return mDb.update(DATABASE_TABLE_EVENT, newEventValues, EVENTDB_KEY_ID + "=" + _event.GetId(), null);
  }
  
  public boolean updateItem(long _rowIndex, Item _item) {
	  
	  Cursor myItem = this.getItemById(_rowIndex);
	  long _taskIndex = (long) myItem.getLong(1);
	  long _eventIndex = (long) myItem.getLong(2);
	  if (_item.GetItemType()=="Event")
	  {
		  return updateEvent(_eventIndex, (EventItem) _item);	  
	  }
	  else
		  return updateTask(_taskIndex, (TaskItem) _item);
	  
	 
  }
  
  public boolean updateItem(Item _item)
  {
	  if (_item.GetItemType()=="Task")
	  {
		  Cursor myCursor = getItemByTaskId(Long.valueOf(_item.GetId()));
		  updateItem(myCursor.getLong(0), _item);
	  }
	  else
	  {
		  Cursor myCursor = getItemByEventId(Long.valueOf(_item.GetId()));
		  updateItem(myCursor.getLong(0), _item);
	  }
	  
	  
	  return true;
  }
  
  public Cursor getAllItems() {
	  return this.mDb.query(DATABASE_TABLE_ITEM, new String[] {ITEMDB_KEY_ID, ITEMDB_KEY_TASK, ITEMDB_KEY_EVENT}, null, null, null, null, null);
  }
  
  // get item by item id
  public Cursor getItemById(long rowId) throws SQLException {

      Cursor mCursor =

      this.mDb.query(true, DATABASE_TABLE_ITEM, new String[] { ITEMDB_KEY_ID, ITEMDB_KEY_TASK, ITEMDB_KEY_EVENT}, ITEMDB_KEY_ID + "=" + rowId, null, null, null, null, null);
      if (mCursor != null) {
          mCursor.moveToFirst();
      }
      return mCursor;
  }
 
 public Cursor getItemByTaskId(long taskId) throws SQLException {
	 
	 Cursor mCursor  =
		 this.mDb.query(true, DATABASE_TABLE_ITEM,new String[] { ITEMDB_KEY_ID, ITEMDB_KEY_TASK, ITEMDB_KEY_EVENT}, ITEMDB_KEY_TASK + "=" + taskId, null, null, null, null, null);
	 if (mCursor != null) {
         mCursor.moveToFirst();
     }
     return mCursor;
 }
  
 public Cursor getItemByEventId(long eventId) throws SQLException {
	 Cursor mCursor  =
		 this.mDb.query(true, DATABASE_TABLE_ITEM,new String[] { ITEMDB_KEY_ID, ITEMDB_KEY_TASK, ITEMDB_KEY_EVENT}, ITEMDB_KEY_EVENT + "=" + eventId, null, null, null, null, null);
	 if (mCursor != null) {
         mCursor.moveToFirst();
     }
     return mCursor;
 }
 
}
