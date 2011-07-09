package com.android.apptime.view;

import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.android.apptime.Item;
import com.android.apptime.R;

public class DayInMonthView extends TextView {
	private static final String TAG = "dayinweekview";
	private Context context;
	private Paint marginPaint = null;
	private Paint borderPaint = null;
	private Paint textPaint = null;
	private float margin;
	private int bgColor;
	private int w, h;
	private String content = "";
	private Item item = null;
	private Date date = null;
	
	public DayInMonthView(Context context, AttributeSet ats, int ds) {
		super(context, ats, ds);
		this.context = context;
		init();
	}

	public DayInMonthView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public DayInMonthView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {
		// Get a reference to our resource table.
		Resources myResources = getResources();
		// Create the paint brushes we will use in the onDraw method.
		borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setColor(myResources.getColor(R.color.timeslot_margin));
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(2);
		marginPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		marginPaint.setColor(myResources.getColor(R.color.timeslot_margin));
		marginPaint.setStyle(Style.STROKE);
		marginPaint.setStrokeWidth(5);
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(30);
		bgColor = myResources.getColor(R.color.timeslot_background);
		// Get the paper background color and the margin width.
		date = new Date();
		margin = myResources.getDimension(R.dimen.timeslot_margin);
	}

	public void setTime(Date date){
		this.date = date;
	}
	
	public Date getTime(){
		return this.date;
	}
	

	@Override
	public void onDraw(Canvas canvas) {
		Log.d(TAG, "on draw");
		canvas.drawColor(bgColor);
		// Draw border:
		canvas.drawRect(new RectF(0, 0, getMeasuredWidth(),
				getMeasuredHeight()), borderPaint);

		canvas.drawText(this.getText().toString(), 20, 35, textPaint);

//		canvas.drawLine(0, 40, getMeasuredWidth(), 40, marginPaint);
//
//		canvas.drawText(this.getText().toString(), 20,
//				40 + linePaint.getTextSize() + 10, linePaint);
	}

	/**
	 * set the background color for this view
	 * 
	 * @param color
	 */

	public void setBGColor(int color) {
		this.bgColor = color;
	}

}