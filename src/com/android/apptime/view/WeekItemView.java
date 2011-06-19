package com.android.apptime.view;

import com.android.apptime.Item;
import com.android.apptime.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.TextView;

public class WeekItemView extends TextView {
	private Context context;
	private Paint marginPaint = null;
	private Paint borderPaint = null;
	private Paint linePaint = null;
	private Paint textPaint = null;
	private float margin;
	private int bgColor;
	private int w, h;
	private String time = "1pm-3pm";
	private String content = "";
	private Item item = null;

	public WeekItemView(Context context, AttributeSet ats, int ds) {
		super(context, ats, ds);
		this.context = context;
		init();
	}

	public WeekItemView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public WeekItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {
		// Get a reference to our resource table.
		Resources myResources = getResources();
		// Create the paint brushes we will use in the onDraw method.
		borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setColor(Color.BLACK);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(10);
		marginPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		marginPaint.setColor(Color.RED);
		marginPaint.setStyle(Style.STROKE);
		marginPaint.setStrokeWidth(5);
		linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		linePaint.setColor(myResources.getColor(R.color.timeslot_lines));
		linePaint.setStyle(Style.STROKE);
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(30);
		bgColor = Color.TRANSPARENT;
		// Get the paper background color and the margin width.

		margin = myResources.getDimension(R.dimen.timeslot_margin);
	}

	

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.argb(100, 0, 0, 0));
		// Draw border:
		canvas.drawRoundRect(new RectF(0, 0, getMeasuredWidth(),
				getMeasuredHeight()), 3, 3, borderPaint);
	}

	protected void createContextMenuListener(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// super.onCreateContextMenu(menu, v, menuInfo);
		//
		// menu.setHeaderTitle("Selected To Do Item");
		// menu.add(0, REMOVE_TODO, Menu.NONE, R.string.remove);
	}

	/**
	 * set the item for this view
	 * 
	 * @param object
	 *            - the item
	 */

	public void setItem(Item object) {
		item = object;
	}

	/**
	 * return the item that this view is representing
	 * 
	 * @return
	 */

	public Item getItem() {
		return item;
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