package com.android.apptime;

import android.content.Context;

import com.android.apptime.database.ItemDBAdapter;

public class DatabaseInterface {
	// working with database
	private ItemDBAdapter itemdb;
	
	public DatabaseInterface(Context context)
	{
		itemdb = new ItemDBAdapter(context);
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
	
}
