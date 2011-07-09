package com.android.apptime;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.android.apptime.database.ItemDBAdapter;
import com.android.apptime.database.MainDBAdapter;

public class DatabaseInterface {
	// working with database
	private static DatabaseInterface theInstance = null;
	private ItemDBAdapter itemdb = null;;
	private Context context = null;
	private MainDBAdapter newdb;

	private DatabaseInterface() {
	};

	public DbSetChange dbsetchange;

	public interface DbSetChange {
		public void Update();
	}

	public static DatabaseInterface getDatabaseInterface(Context context) {
		if (theInstance == null) {
			theInstance = new DatabaseInterface();
		}
		theInstance.itemdb = new ItemDBAdapter(context);
		theInstance.context = context;
		return theInstance;
	}

	public void OpenDatabase(Context newcontext) {
		newdb = new MainDBAdapter(newcontext);
		newdb.open();
	}

	public void CloseDatabase() {
		newdb.close();
	}

	public Item AddItemToDatabase(Context context, Item _item) {
		//try {
			itemdb.open();
			long id = itemdb.createItem(_item);
			itemdb.close();
			_item.SetId(String.valueOf(id));
			//dbsetchange.Update();
			return _item;
		//} catch (Exception e) {
		//	Log.e("DB", e.getMessage());
		//	return null;
		//}
	}

	public void RemoveItemFromDatabase(Context context, Item _item) {

		itemdb.open();
		itemdb.removeItem(Integer.parseInt(_item.GetId()));
		itemdb.close();
		dbsetchange.Update();
	}

	public void UpdateItemInDatabase(Context context, Item _item) {
		itemdb.open();
		itemdb.updateItem(_item);
		itemdb.close();
		dbsetchange.Update();
	}

	public Item RetrieveItemFromDatabase(Context context, long id) {
		itemdb.open();
		Cursor mycs = itemdb.getItemById(id);
		String t = mycs.getString(12);
		if (mycs.getString(12).equalsIgnoreCase("Event")) {
			// return event
			Cursor eventcursor = mycs;
			String eid = eventcursor.getString(0);
			String etitle = eventcursor.getString(1);
			String edescription = eventcursor.getString(2);
			String elocation = eventcursor.getString(3);
			String epriority = eventcursor.getString(4);
			String erepeat = eventcursor.getString(5);
			String ecategory = eventcursor.getString(6);
			String ecompleted = eventcursor.getString(7);
			Integer ecolor = Integer.parseInt(eventcursor.getString(8));
			Long starttime = Long.parseLong(eventcursor.getString(9));
			Date estarttime = new Date(starttime);
			Long endtime = Long.parseLong(eventcursor.getString(10));
			Date eendtime = new Date(endtime);
			Long alerttime = Long.parseLong(eventcursor.getString(11));
			Date ealerttime = new Date(alerttime);
			String etype = eventcursor.getString(12);
			Item newitem = new Item(etitle, edescription, elocation, ecategory,
					null, epriority, etype, estarttime, eendtime, null,
					ealerttime, erepeat, ecompleted, ecolor);
			newitem.SetId(eid);
			return newitem;
		} else {
			Cursor eventcursor = mycs;
			String eid = eventcursor.getString(0);
			String etitle = eventcursor.getString(1);
			String edescription = eventcursor.getString(2);
			String elocation = eventcursor.getString(3);
			String epriority = eventcursor.getString(4);
			String erepeat = eventcursor.getString(5);
			String ecategory = eventcursor.getString(6);
			String ecompleted = eventcursor.getString(7);
			Integer ecolor = Integer.parseInt(eventcursor.getString(8));

			Long edeadline = Long.parseLong(eventcursor.getString(9));
			Date deadline = new Date(edeadline);
			Long alerttime = Long.parseLong(eventcursor.getString(10));
			Date ealerttime = new Date(alerttime);
			String etype = eventcursor.getString(11);
			Item newitem = new Item(etitle, edescription, elocation, ecategory,
					null, epriority, etype, null, null, deadline, ealerttime,
					erepeat, ecompleted, ecolor);
			newitem.SetId(eid);
			return newitem;
		}

	}

	// retrieve item by ITEM id
	public ArrayList<ArrayList<Item>> RetrieveItemFromDatabase(Context context,
			Date datetime) {
		ArrayList<ArrayList<Item>> myitem = new ArrayList<ArrayList<Item>>();
		itemdb.open();
		ArrayList<Cursor> mycs = itemdb.getItemByDate(datetime);
		itemdb.close();
		ArrayList<Item> myevent = new ArrayList<Item>();
		ArrayList<Item> mytask = new ArrayList<Item>();
		Cursor eventcursor = mycs.get(0);
		if (eventcursor.moveToFirst()) {
			do {
				String eid = eventcursor.getString(0);
				String etitle = eventcursor.getString(1);
				String edescription = eventcursor.getString(2);
				String elocation = eventcursor.getString(3);
				String epriority = eventcursor.getString(4);
				String erepeat = eventcursor.getString(5);
				String ecategory = eventcursor.getString(6);
				String ecompleted = eventcursor.getString(7);
				Integer ecolor = Integer.parseInt(eventcursor.getString(8));
				Long starttime = Long.parseLong(eventcursor.getString(9));
				Date estarttime = new Date(starttime);
				Long endtime = Long.parseLong(eventcursor.getString(10));
				Date eendtime = new Date(endtime);
				Long alerttime = Long.parseLong(eventcursor.getString(11));
				Date ealerttime = new Date(alerttime);
				String etype = eventcursor.getString(12);
				Item newitem = new Item(etitle, edescription, elocation,
						ecategory, null, epriority, etype, estarttime,
						eendtime, null, ealerttime, erepeat, ecompleted, ecolor);
				newitem.SetId(eid);
				myevent.add(newitem);

			} while (eventcursor.moveToNext());
		}
		myitem.add(myevent);
		eventcursor = mycs.get(1);
		if (eventcursor.moveToFirst()) {
			do {
				String eid = eventcursor.getString(0);
				String etitle = eventcursor.getString(1);
				String edescription = eventcursor.getString(2);
				String elocation = eventcursor.getString(3);
				String epriority = eventcursor.getString(4);
				String erepeat = eventcursor.getString(5);
				String ecategory = eventcursor.getString(6);
				String ecompleted = eventcursor.getString(7);
				Integer ecolor = Integer.parseInt(eventcursor.getString(8));

				Long edeadline = Long.parseLong(eventcursor.getString(9));
				Date deadline = new Date(edeadline);
				Long alerttime = Long.parseLong(eventcursor.getString(10));
				Date ealerttime = new Date(alerttime);
				String etype = eventcursor.getString(11);
				Item newitem = new Item(etitle, edescription, elocation,
						ecategory, null, epriority, etype, null, null,
						deadline, ealerttime, erepeat, ecompleted, ecolor);
				newitem.SetId(eid);
				mytask.add(newitem);

			} while (eventcursor.moveToNext());
		}
		myitem.add(mytask);

		return myitem;

	}

	public void SetObserver(DbSetChange dbsetchange) {
		this.dbsetchange = dbsetchange;

	}

}
