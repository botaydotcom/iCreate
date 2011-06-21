package com.android.apptime.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.apptime.Category;


public class CategoryDBAdapter {

	private static final String DATABASE_TABLE_CATEGORY = "Categories_Table";
    private static final String CATEGORYDB_KEY_ID = "categorydb_category_id";
    private static final String CATEGORYDB_TITLE = "categorydb_title";
    
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
    
    public CategoryDBAdapter(Context ctx) {
        this.context = ctx;
    }
    public void close() {
        mDb.close();
      }
	
    public CategoryDBAdapter open() throws SQLiteException {
    	this.mDbHelper = new DatabaseHelper(this.context);
    	this.mDb = this.mDbHelper.getWritableDatabase();
        return this;

      }
  //Insert a new category into the category table
    public long createCategory(Category _category) {
      // Create a new row of values to insert.
      ContentValues newTaskValues = new ContentValues();
      // Assign values for each row.
      newTaskValues.put(CATEGORYDB_TITLE, _category.GetTitle());
      
      // Insert the row.
      return mDb.insert(DATABASE_TABLE_CATEGORY, null, newTaskValues);
    }
    
    public int updateCategory(Category _category)
    {
      ContentValues newTaskValues = new ContentValues();
      
      newTaskValues.put(CATEGORYDB_TITLE, _category.GetTitle());
      
      
      return this.mDb.update(DATABASE_TABLE_CATEGORY, newTaskValues, CATEGORYDB_KEY_ID + "=" + _category.GetId(), null);    	
    }
    
    public int removeCategory(Category _category)
    {
    	// TODO: REMOVE ALL THE TASK RELATIONS ALSO
    	
    	return this.mDb.delete(DATABASE_TABLE_CATEGORY, _category.GetId()+ "=" + CATEGORYDB_KEY_ID, null);
    }
    
    
    
}
