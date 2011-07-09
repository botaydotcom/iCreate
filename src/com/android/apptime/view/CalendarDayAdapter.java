package com.android.apptime.view;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class CalendarDayAdapter extends ArrayAdapter<TimeSlot> {
	int mViewResourceId;
	Context context;
	public CalendarDayAdapter(Context context, int viewResourceId,
			List<TimeSlot> applicationItem) {
		super(context, viewResourceId, applicationItem);
		this.context=context;
		mViewResourceId = viewResourceId;
	}
	@Override
	public View getView (int position, View contentView, ViewGroup parent)
	{
		LinearLayout itemView = null;
		TimeSlot anItem = getItem(position);
		if (contentView == null){
			itemView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(inflater);
			li.inflate(mViewResourceId, itemView, true);
		} else
		{
			itemView = (LinearLayout)contentView;
		}	
		return itemView;
		
	}

}
