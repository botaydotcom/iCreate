package com.android.apptime.database;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.apptime.Item;
import com.android.apptime.MapItem;
import com.android.apptime.Category;
//import com.android.apptime.database.EventDBAdapter.DatabaseHelper;


public class MapDBAdapter {
	
	private static final String DATABASE_TABLE_MAP = "Map_Table";
    private static final String MAPDB_KEY_ID = "mapdb_loc_id";
    private static final String MAPDB_TITLE = "map_title";
    private static final String MAPDB_HORIZONTAL = "mapdb_horizontal";
    private static final String MAPDB_VERTICAL = "map_vertical";
    private static final String MAPDB_LONGITUDE = "mapdb_longitude";
    private static final String MAPDB_LATITUDE = "map_latitude";
    private static final String MAPDB_LINK = "map_link";
    
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
    
    public MapDBAdapter(Context ctx) {
        this.context = ctx;
    }
	
    public void close() {
        mDb.close();
      }
	
    public MapDBAdapter open() throws SQLiteException {
    	this.mDbHelper = new DatabaseHelper(this.context);
    	this.mDb = this.mDbHelper.getWritableDatabase();
        return this;

      }  
    
    
  //Insert a new event into the Event table
    public long createMap(MapItem _map) {
      // Create a new row of values to insert.
      ContentValues newMapValues = new ContentValues();
      // Assign values for each row.
      
      
    		
      String t0 = "INSERT INTO " + DATABASE_TABLE_MAP + " (" + MAPDB_TITLE + ", " + MAPDB_HORIZONTAL + ", " + MAPDB_VERTICAL + ", " + MAPDB_LONGITUDE
      + ", " + MAPDB_LATITUDE + ", " + MAPDB_LINK + ")" + " VALUES ('" +
      _map.GetTitle() + "','" +
      _map.GetHorizontal() + "','" +
      _map.GetVertical() + "','" +
      _map.GetLongitude() + "','" +
      _map.GetLatitude() + "','" +
      _map.GetLink() +"');";
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
	
    //Update a new event in the Event table
//    public long updateEvent(Item _event) {
//      // Create a new row of values to insert.
//      ContentValues newEventValues = new ContentValues();
//      // Assign values for each row.
//      newEventValues.put(EVENTDB_DESCRIPTION, _event.GetDescription());
//      newEventValues.put(EVENTDB_LOCATION, _event.GetLocation());
//      newEventValues.put(EVENTDB_PRIORITY, _event.GetPriority());
//      newEventValues.put(EVENTDB_REPEAT, _event.GetRepeat());
//      newEventValues.put(EVENTDB_CATEGORY, _event.GetCategory());
//      newEventValues.put(EVENTDB_COMPLETED, _event.GetCompleted());
//      newEventValues.put(EVENTDB_COLOR, _event.GetColor());
//      newEventValues.put(EVENTDB_STARTTIME, _event.GetStartTime());
//      newEventValues.put(EVENTDB_ENDTIME, _event.GetEndTime());
//      newEventValues.put(EVENTDB_ALERTTIME, _event.GetEndTime());
//      newEventValues.put(EVENTDB_TYPE, _event.GetItemType());
//      // Insert the row.
//      long t = mDb.update(DATABASE_TABLE_EVENT, newEventValues, EVENTDB_KEY_ID + "=" + _event.GetId(), null);
//      return t;
//    }
//	
//    
//    public int removeEvent(Item _event)
//    {
//    	// TODO: REMOVE ALL THE EVENT RELATIONS ALSO
//    	
//    	return this.mDb.delete(DATABASE_TABLE_EVENT, _event.GetId()+ "=" + EVENTDB_KEY_ID, null);
//    }
    
    // get item by item id
    public Cursor getEventById(long rowId) throws SQLException {

        Cursor mCursor =

        this.mDb.query(true, DATABASE_TABLE_MAP, new String[] { MAPDB_KEY_ID, MAPDB_TITLE, MAPDB_HORIZONTAL, MAPDB_VERTICAL, MAPDB_LONGITUDE, MAPDB_LATITUDE, MAPDB_LINK,}, MAPDB_KEY_ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    // get item by item id
//    public Cursor getEventByDate(Date datetime) throws SQLException {
//
//    	int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
//    	long t = datetime.getTime();
//    	long thedayafter = MILLIS_IN_DAY + t;
//        Cursor mCursor =
//
//        this.mDb.query(true, DATABASE_TABLE_MAP, new String[] { MAPDB_KEY_ID, MAPDB_TITLE, MAPDB_HORIZONTAL, MAPDB_VERTICAL, MAPDB_LONGITUDE, MAPDB_LATITUDE, MAPDB_LINK   }, EVENTDB_STARTTIME + " >= " + t + " AND " + EVENTDB_STARTTIME + " < " + thedayafter , null, null, null, null, null);
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
//        return mCursor;
//    }

}
