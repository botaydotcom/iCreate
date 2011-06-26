package com.android.apptime.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.apptime.AlertType;
import com.android.apptime.Category;


public class AlertTypeDBAdapter {
	
	private static final String DATABASE_TABLE_ALERTTYPE = "Alerttype_Table";
    private static final String ALERTTYPEDB_KEY_ID = "alerttypedb_alerttype_id";
    private static final String ALERTTYPEDB_TITLE = "alerttypedb_title";
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
    
    public AlertTypeDBAdapter(Context ctx) {
        this.context = ctx;
    }
    public void close() {
        mDb.close();
      }
	
    public AlertTypeDBAdapter open() throws SQLiteException {
    	this.mDbHelper = new DatabaseHelper(this.context);
    	this.mDb = this.mDbHelper.getWritableDatabase();
        return this;

      }
  //Insert a new category into the category table
    public long createAlertType(AlertType _alerttype) {
      // Create a new row of values to insert.
      ContentValues newTaskValues = new ContentValues();
      // Assign values for each row.
      newTaskValues.put(ALERTTYPEDB_TITLE, _alerttype.GetTitle());
      
      // Insert the row.
      return mDb.insert(DATABASE_TABLE_ALERTTYPE, null, newTaskValues);
    }
    
    public int updateAlertType(AlertType _alerttype)
    {
      ContentValues newTaskValues = new ContentValues();
      
      newTaskValues.put(ALERTTYPEDB_TITLE, _alerttype.GetTitle());
      
      
      return this.mDb.update(DATABASE_TABLE_ALERTTYPE, newTaskValues, ALERTTYPEDB_KEY_ID + "=" + _alerttype.GetId(), null);    	
    }
    
    public int removeAlertType(AlertType _alerttype)
    {
    	// TODO: REMOVE ALL THE TASK RELATIONS ALSO
    	
    	return this.mDb.delete(DATABASE_TABLE_ALERTTYPE, _alerttype.GetId()+ "=" + ALERTTYPEDB_KEY_ID, null);
    }
    

}
