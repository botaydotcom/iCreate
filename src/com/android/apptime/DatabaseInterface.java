package com.android.apptime;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;

import com.android.apptime.database.ItemDBAdapter;

public class DatabaseInterface {
	// working with database
	private static ItemDBAdapter itemdb = null;;
	
	public DatabaseInterface(Context context)
	{
		if (itemdb == null) itemdb = new ItemDBAdapter(context);
		
	}
	
	public void AddItemToDatabase(Context context, Item _item)
	{
		itemdb.open();
		itemdb.createItem(_item);
		itemdb.close();
		
	}
	
	public void RemoveItemFromDatabase(Context context, Item _item)
	{
		
		itemdb.open();
		itemdb.removeItem(Integer.parseInt(_item.GetId()));
		itemdb.close();
	}
	
	public void UpdateItemFmomDatabase(Context context, Item _item)
	{
		itemdb.open();
		itemdb.updateItem(_item);
		itemdb.close();
		
	}
	
	// retrieve item by ITEM id
	public ArrayList<ArrayList<Item>> RetrieveItemFromDatabase(Context context, Date datetime)
	{
		ArrayList<ArrayList<Item>> myitem = new ArrayList<ArrayList<Item>>(); 
		itemdb.open();
		ArrayList<Cursor> mycs = itemdb.getItemByDate(datetime);
		itemdb.close();
		ArrayList<Item> myevent = new ArrayList<Item>();
		ArrayList<Item> mytask = new ArrayList<Item>(); 
		Cursor eventcursor = mycs.get(0);
		if	(eventcursor.moveToFirst())
		{
			do
			{
				String eid = eventcursor.getString(0);
				String etitle = eventcursor.getString(1);
				String edescription = eventcursor.getString(2);
				String elocation = eventcursor.getString(3);
				String epriority = eventcursor.getString(4);
				String erepeat = eventcursor.getString(5);
				String ecategory = eventcursor.getString(6);
				String ecompleted = eventcursor.getString(7);
				Integer ecolor = Integer.parseInt(eventcursor.getString(8));
				String estarttime = eventcursor.getString(9);
				String eendtime = eventcursor.getString(10);
				String ealerttime = eventcursor.getString(11);
				String etype = eventcursor.getString(12);
				Item newitem = new Item (etitle, edescription, elocation, ecategory, null, epriority,
						etype, estarttime, eendtime,null,  ealerttime, erepeat, ecompleted, ecolor);
				newitem.SetId(eid);
				myevent.add(newitem);
				
				
			} while (eventcursor.moveToNext());
		}
		myitem.add(myevent);
		eventcursor = mycs.get(1);
		if	(eventcursor.moveToFirst())
		{
			do
			{
				String eid = eventcursor.getString(0);
				String etitle = eventcursor.getString(1);
				String edescription = eventcursor.getString(2);
				String elocation = eventcursor.getString(3);
				String epriority = eventcursor.getString(4);
				String erepeat = eventcursor.getString(5);
				String ecategory = eventcursor.getString(6);
				String ecompleted = eventcursor.getString(7);
				Integer ecolor = Integer.parseInt(eventcursor.getString(8));
				String estarttime = eventcursor.getString(9);
				String eendtime = eventcursor.getString(10);
				String ealerttime = eventcursor.getString(11);
				String etype = eventcursor.getString(12);
				Item newitem = new Item (etitle, edescription, elocation, ecategory, null, epriority,
						etype, estarttime, eendtime,null,  ealerttime, erepeat, ecompleted, ecolor);
				newitem.SetId(eid);
				mytask.add(newitem);
				
				
			} while (eventcursor.moveToNext());
		}
		myitem.add(mytask);
		
		return myitem;
		
	}
	
}
