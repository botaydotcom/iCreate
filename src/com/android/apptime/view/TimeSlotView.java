package com.android.apptime.view;

import java.util.Date;

import com.android.apptime.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimeSlotView extends TextView {

  private Paint marginPaint;
  private Paint linePaint;
  private int paperColor;
  private float margin;
  private Date time;
  
  public TimeSlotView (Context context, AttributeSet ats, int ds) {
    super(context, ats, ds);
    init();
  }

  public TimeSlotView (Context context) {
    super(context);
    init();
  }

  public TimeSlotView (Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }
  
  public void setTime(Date time){
	  this.time = time;
  }
  public Date getTime(){
	  return time;
  }
  
  private void init() {
	time = new Date();
	time.setHours(0);
	time.setMinutes(0);
    // Get a reference to our resource table.
    Resources myResources = getResources();

    // Create the paint brushes we will use in the onDraw method.
    marginPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    marginPaint.setColor(myResources.getColor(R.color.timeslot_margin));
    linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    linePaint.setColor(myResources.getColor(R.color.timeslot_lines));

    // Get the paper background color and the margin width.
    paperColor = myResources.getColor(R.color.timeslot_background);
    margin = myResources.getDimension(R.dimen.timeslot_margin);
  }

  @Override
  public void onDraw(Canvas canvas) {
    // Color as paper
    canvas.drawColor(paperColor);

    // Draw ruled lines
    canvas.drawLine(0, 0, getMeasuredWidth(), 0, linePaint);
    canvas.drawLine(0, getMeasuredHeight(), 
                       getMeasuredWidth(), getMeasuredHeight(), 
                       linePaint);

    // Draw margin
    canvas.drawLine(margin, 0, margin, getMeasuredHeight(), marginPaint);

    // Move the text across from the margin
    canvas.save();
    canvas.translate(5, 0);

    // Use the TextView to render the text.
    super.onDraw(canvas);
    canvas.restore();
  }
}