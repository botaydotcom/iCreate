//package com.android.apptime.view;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.apptime.Item;
//import com.android.apptime.R;
//import com.android.apptime.view.ZoomableViewGroup;
//
//public class CopyOfCalendarWeekView extends Activity {
//	public static int nextViewId = 1;
//	private String TAG = "calendarview";
//	private final int CONTEXT_MENU_ADD = 0;
//	private final int CONTEXT_MENU_MODIFY = 1;
//	private final int CONTEXT_MENU_REMOVE = 2;
//
//	public static final int POPUP_TIME_SLOT = 0;
//	public static final int POPUP_ITEM = 1;
//
//	private final int POPUP_FORM = 0;
//	private TimeSlotView[] dayTimeSlot = new TimeSlotView[Constant.NUM_HOUR];
//	private DayInWeekView[] daysInWeekSlot = new DayInWeekView[Constant.NUM_DAY_IN_WEEK];
//
//	private TextView[] daysInWeekTitle = new TextView[Constant.NUM_DAY_IN_WEEK];
//
//	private int width;
//	private int timeSlotHeight = 80;
//	private PopupWindow popupWindow = null;
//	private EditText mEtTitle = null;
//	private TextView mTvStartTime = null;
//	private TextView mTvEndTime = null;
//	private AutoCompleteTextView mAutoTvLocation = null;
//	private Button mAddButton = null;
//	private Button mModifyButton = null;
//	private Button mRemoveButton = null;
//	private Button mDetailButton = null;
//	private View itemBeingSelected = null;
//	private TimeSlot startTime = null;
//	private TimeSlot endTime = null;
//
//	private float marginWidth = 70;
//	private float titleHeight = 70;
//
//	private int height = 1000;
//	private int daySlotWidth = 50;
//	private int timeSlotWidth = 50;
//	private int screenWidth = 0, screenHeight = 0;
//	private FrameLayout titleLayout = null;
//	private FrameLayout contentLayout = null;
//	private ZoomableViewGroup titletitlecontainer = null;
//	private ZoomableViewGroup titleContainer = null;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.calendarweekview);
//
//		width = contentLayout.getWidth();
//		Log.d(TAG, "" + width);
//		DisplayMetrics metrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(metrics);
//		screenWidth = metrics.widthPixels;
//		screenHeight = metrics.heightPixels;
//
//		contentLayout = (FrameLayout) findViewById(R.id.content);
//		titleContainer = new ZoomableViewGroup(getApplicationContext());
//		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
//				LayoutParams.FILL_PARENT);
//		titleContainer.setLayoutParams(params);
//		titleContainer.addView(new View(getApplicationContext()));
//		titleContainer.bringToFront();
//		contentLayout.addView(titleContainer);
//
//		titleLayout = (FrameLayout) findViewById(R.id.content);
//		titleContainer = new ZoomableViewGroup(getApplicationContext());
//		params = new LayoutParams(LayoutParams.FILL_PARENT,
//				LayoutParams.FILL_PARENT);
//		titleContainer.setLayoutParams(params);
//		titleContainer.addView(new View(getApplicationContext()));
//		titleContainer.bringToFront();
//		titleLayout.addView(titleContainer);
//
//		try {
//			displayAllTimeSlot();
//			//
//			// TextView timeSlotView = new TextView(getApplicationContext());
//			// timeSlotView.setText("1pm-3pm: \n Meeting with team @YIH");
//			// timeSlotView.setBackgroundColor(Color.RED);
//			//
//			// RelativeLayout.LayoutParams layoutParams = null;
//			// layoutParams = new RelativeLayout.LayoutParams(200, 160);
//			// layoutParams.addRule(RelativeLayout.ALIGN_TOP, nextViewId - 20);
//			// layoutParams.setMargins(50, 0, 0, 0);
//			// timeSlotView.setLayoutParams(layoutParams);
//			// timeSlotView.setId(nextViewId++);
//			// listTimeSlotLayout.addView(timeSlotView);
//			// timeSlotView.setFocusable(true);
//			//
//			// TextView timeSlotView1 = new TextView(getApplicationContext());
//			// timeSlotView1.setText("1pm-2pm: \n Assignment due");
//			// timeSlotView1.setBackgroundColor(Color.GREEN);
//			// layoutParams = null;
//			// layoutParams = new RelativeLayout.LayoutParams(250, 80);
//			// layoutParams.addRule(RelativeLayout.ALIGN_TOP, nextViewId - 21);
//			// layoutParams.setMargins(250, 0, 0, 0);
//			// timeSlotView1.setLayoutParams(layoutParams);
//			// timeSlotView1.setId(nextViewId++);
//			// listTimeSlotLayout.addView(timeSlotView1);
//			// timeSlotView1.setFocusable(true);
//
//			addItemViewToTimeSlot(null, 0, 5, 35, 10, 30, 15, 35);
//			addItemViewToTimeSlot(null, 1, 6, 30, 7, 30, 15, 35);
//		} catch (Exception e) {
//			Log.d(TAG, e.getMessage());
//		}
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//	}
//
//	private void displayAllTimeSlot() {
//
//		Resources myResources = getResources();
//
//		marginWidth = myResources.getDimension(R.dimen.timeslot_margin);
//		titleHeight = myResources.getDimension(R.dimen.title);
//
//		// uppon starting, the width (beside the margin is
//		daySlotWidth = (int) ((screenWidth - marginWidth) / 7);
//
//		Log.d(TAG, "add view to week calendar" + " " + screenWidth + " "
//				+ marginWidth + " " + daySlotWidth);
//		TextView title = new TextView(getApplicationContext());
//		title.setText("Time");
//		title.setId(nextViewId++);
//		titleContainer.addView(title);
//		title.setWidth((int) marginWidth);
//		title.setHeight((int) titleHeight);
//		title.layout(0, 0, 0 + title.getMeasuredWidth(),
//				title.getMeasuredHeight());
//
//		contentLayout.addView(title);
//		for (int i = 0; i < Constant.NUM_DAY_IN_WEEK; i++) {
//			daysInWeekTitle[i] = new TextView(getApplicationContext());
//			daysInWeekTitle[i].setText(Constant.DAY_IN_WEEK[i].substring(0, 3));
//			daysInWeekTitle[i].setWidth((int) daySlotWidth);
//			daysInWeekTitle[i].setHeight((int) titleHeight);
//			daysInWeekTitle[i].layout(
//					title.getWidth() + daySlotWidth * i,
//					0,
//					title.getWidth() + daySlotWidth * i
//							+ daysInWeekTitle[i].getWidth(), (int) titleHeight);
//			daysInWeekTitle[i].setId(nextViewId++);
//			contentLayout.addView(daysInWeekTitle[i]);
//		}
//
//		for (int i = 0; i < Constant.NUM_HOUR; i++) {
//			dayTimeSlot[i] = new TimeSlotView(getApplicationContext());
//			dayTimeSlot[i].getTime().setHours(i);
//			dayTimeSlot[i].getTime().setMinutes(0);
//			dayTimeSlot[i].setText(TimeFormat
//					.getAPPMHourFormatWithoutHourPadding(dayTimeSlot[i]
//							.getTime()));
//			dayTimeSlot[i].setWidth(LayoutParams.FILL_PARENT);
//			dayTimeSlot[i].setHeight((int) timeSlotHeight);
//			dayTimeSlot[i].layout(0, (int) (i * timeSlotHeight),
//					dayTimeSlot[i].getWidth(), (int) (i * timeSlotHeight)
//							+ dayTimeSlot[i].getHeight());
//			dayTimeSlot[i].setId(nextViewId++);
//			contentLayout.addView(dayTimeSlot[i]);
//		}
//
//		for (int i = 0; i < Constant.NUM_DAY_IN_WEEK; i++) {
//			Log.d(TAG, "adding day in week slot");
//			daysInWeekSlot[i] = new DayInWeekView(getApplicationContext());
//			daysInWeekSlot[i].setWidth((int) daySlotWidth);
//			daysInWeekSlot[i].setHeight(LayoutParams.FILL_PARENT);
//			daysInWeekSlot[i].layout(
//					title.getWidth() + daySlotWidth * i,
//					0,
//					title.getWidth() + daySlotWidth * i
//							+ daysInWeekTitle[i].getWidth(),
//					daysInWeekSlot[i].getHeight());
//			daysInWeekSlot[i].setId(nextViewId++);
//			contentLayout.addView(daysInWeekSlot[i]);
//			daysInWeekSlot[i].setClickable(true);
//			final View thisView = daysInWeekSlot[i];
//			daysInWeekSlot[i].setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					itemBeingSelected = thisView;
//					Log.d(TAG, "day slot selected");
//				}
//			});
//		}
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			Toast.makeText(getApplicationContext(), "on touch event",
//					Toast.LENGTH_SHORT);
//			Log.d(TAG, "on touch event");
//			return true;
//		}
//		return false;
//	}
//
//	private void updateView() {
//
//	}
//
//	private void showPopUpWindow() {
//		if (itemBeingSelected.getClass().equals(TimeSlotView.class)) {
//			TimeSlotView timeSlotView = (TimeSlotView) itemBeingSelected;
//			Intent createPopup = new Intent(getApplicationContext(),
//					PopupForm.class);
//			createPopup.putExtra("popupType", POPUP_TIME_SLOT);
//			createPopup.putExtra("startTime", timeSlotView.getTime());
//			createPopup.putExtra("offX", itemBeingSelected.getLeft());
//			createPopup.putExtra("offY", itemBeingSelected.getBottom());
//			startActivityForResult(createPopup, POPUP_FORM);
//		} else {
//			WeekItemView itemView = (WeekItemView) itemBeingSelected;
//			Intent createPopup = new Intent(getApplicationContext(),
//					PopupForm.class);
//			createPopup
//					.putExtra("startTime", itemView.getItem().GetStartTime());
//			createPopup.putExtra("endTime", itemView.getItem().GetEndTime());
//			createPopup.putExtra("endTime", itemView.getItem().GetEndTime());
//			createPopup.putExtra("popupType", POPUP_ITEM);
//			createPopup.putExtra("offX", itemBeingSelected.getLeft());
//			createPopup.putExtra("offY", itemBeingSelected.getBottom());
//			startActivityForResult(createPopup, POPUP_FORM);
//		}
//	}
//
//	public WeekItemView addItemViewToTimeSlot(Item item, int day, int fromHour,
//			int fromMin, int toHour, int toMin, int leftMargin, int width) {
//		WeekItemView newItem = new WeekItemView(getApplicationContext());
//		newItem.setText("Place holder text");
//		// need to read from database
//		// newItem.setItem(object);
//		RelativeLayout.LayoutParams layoutParams = null;
//		int height = getHeightForItemInTimeSlot(fromHour, fromMin, toHour,
//				toMin);
//		layoutParams = new RelativeLayout.LayoutParams(width, height);
//		layoutParams.addRule(RelativeLayout.ALIGN_TOP,
//				dayTimeSlot[fromHour].getId());
//		layoutParams.addRule(RelativeLayout.ALIGN_LEFT,
//				daysInWeekSlot[day].getId());
//		layoutParams.setMargins(leftMargin,
//				getMarginCorrespondingToPeriod(fromMin), 0, 0);
//		newItem.setLayoutParams(layoutParams);
//		newItem.setId(nextViewId++);
//		contentLayout.addView(newItem);
//		return newItem;
//	}
//
//	private int getHeightForItemInTimeSlot(int fromHour, int fromMin,
//			int toHour, int toMin) {
//		int result = timeSlotHeight * (toHour - fromHour)
//				- getMarginCorrespondingToPeriod(fromMin)
//				+ getMarginCorrespondingToPeriod(toMin);
//		return result;
//	}
//
//	private int getMarginCorrespondingToPeriod(int numMin) {
//		return (int) (1.0 * timeSlotHeight * numMin / 60);
//	}
//
//	private void removeItem(int itemId) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
