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

public class EventDBAdapter {

	private static final String DATABASE_TABLE_EVENT = "Events_Table";
	private static final String EVENTDB_KEY_ID = "eventdb_event_id";
	private static final String EVENTDB_TITLE = "eventdb_event_title";
	private static final String EVENTDB_DESCRIPTION = "eventdb_description";
	private static final String EVENTDB_LOCATION = "eventdb_location";
	private static final String EVENTDB_PRIORITY = "eventdb_priority";
	private static final String EVENTDB_REPEAT = "eventdb_repeat";
	private static final String EVENTDB_CATEGORY = "eventdb_category";
	private static final String EVENTDB_COMPLETED = "eventdb_completed";
	private static final String EVENTDB_COLOR = "eventdb_color";
	private static final String EVENTDB_STARTTIME = "eventdb_start_time";
	private static final String EVENTDB_ENDTIME = "eventdb_end_time";
	private static final String EVENTDB_ALERTTIME = "eventdb_alert_time";
	private static final String EVENTDB_TYPE = "eventdb_type";
	// private SQLiteDatabase db;
	private final Context context;

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, MainDBAdapter.DATABASE_NAME, null,
					MainDBAdapter.DATABASE_VERSION);
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

	// Insert a new event into the Event table
	public long createEvent(Item _event) {
		// Create a new row of values to insert.
		ContentValues newEventValues = new ContentValues();
		// Assign values for each row.

		String t0 = "INSERT INTO " + DATABASE_TABLE_EVENT + " ("
				+ EVENTDB_TITLE + ", " + EVENTDB_DESCRIPTION + ", "
				+ EVENTDB_LOCATION + ", " + EVENTDB_PRIORITY + ", "
				+ EVENTDB_REPEAT + ", " + EVENTDB_CATEGORY + ", "
				+ EVENTDB_COMPLETED + ", " + EVENTDB_COLOR + ", "
				+ EVENTDB_STARTTIME + ", " + EVENTDB_ENDTIME + ", "
				+ EVENTDB_ALERTTIME + ", " + EVENTDB_TYPE + ")" + " VALUES ('"
				+ _event.GetTitle() + "','" + _event.GetDescription() + "','"
				+ _event.GetLocation() + "','" + _event.GetPriority() + "','"
				+ _event.GetRepeat() + "','" + _event.GetCategory() + "','"
				+ _event.GetCompleted() + "','" + _event.GetColor() + "','"
				+ _event.GetStartTime().getTime() + "','" + _event.GetEndTime().getTime() + "','"
				+ _event.GetAlertTime().getTime() + "','" + _event.GetItemType() + "');";
		// 1', '2', '3', '4', '5', '6' , '7', '8', '9');";
		mDb.execSQL(t0);
		// Insert the row.
		String query = "SELECT last_insert_rowid();";
		Cursor cursor = mDb.rawQuery(query, null);
		int id = 0;
		if (cursor.moveToFirst()) {
			do {
				id = cursor.getInt(0);
			} while (cursor.moveToNext());
		}

		return id;
	}

	// Update a new event in the Event table
	public long updateEvent(Item _event) {
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

		newEventValues.put(EVENTDB_STARTTIME,
				String.valueOf(_event.GetStartTime().getTime()));
		newEventValues.put(EVENTDB_ENDTIME,
				String.valueOf(_event.GetEndTime().getTime()));
		newEventValues.put(EVENTDB_ALERTTIME,
				String.valueOf(_event.GetEndTime().getTime()));
		newEventValues.put(EVENTDB_TYPE, _event.GetItemType());
		// Insert the row.
		long t = mDb.update(DATABASE_TABLE_EVENT, newEventValues,
				EVENTDB_KEY_ID + "=" + _event.GetId(), null);
		return t;
	}

	public int removeEvent(Item _event) {
		// TODO: REMOVE ALL THE EVENT RELATIONS ALSO

		return this.mDb.delete(DATABASE_TABLE_EVENT, _event.GetId() + "="
				+ EVENTDB_KEY_ID, null);
	}

	// get item by item id
	public Cursor getEventById(long rowId) throws SQLException {

		Cursor mCursor =

		this.mDb.query(true, DATABASE_TABLE_EVENT, new String[] {
				EVENTDB_KEY_ID, EVENTDB_TITLE, EVENTDB_DESCRIPTION,
				EVENTDB_LOCATION, EVENTDB_PRIORITY, EVENTDB_REPEAT,
				EVENTDB_CATEGORY, EVENTDB_COMPLETED, EVENTDB_COLOR,
				EVENTDB_STARTTIME, EVENTDB_ENDTIME, EVENTDB_ALERTTIME },
				EVENTDB_KEY_ID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// get item by item id
	public Cursor getEventByDate(Date datetime) throws SQLException {

		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		long t = datetime.getTime();
		long thedayafter = MILLIS_IN_DAY + t;
		Cursor mCursor =

		this.mDb.query(true, DATABASE_TABLE_EVENT, new String[] {
				EVENTDB_KEY_ID, EVENTDB_TITLE, EVENTDB_DESCRIPTION,
				EVENTDB_LOCATION, EVENTDB_PRIORITY, EVENTDB_REPEAT,
				EVENTDB_CATEGORY, EVENTDB_COMPLETED, EVENTDB_COLOR,
				EVENTDB_STARTTIME, EVENTDB_ENDTIME, EVENTDB_ALERTTIME, EVENTDB_TYPE },
				EVENTDB_STARTTIME + " >= " + t + " AND " + EVENTDB_STARTTIME
						+ " < " + thedayafter, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

}
