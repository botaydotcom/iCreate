package com.android.apptime.view;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.android.apptime.Item;
import com.android.apptime.R;

public class CalendarMonthView extends Activity {
	private static int nextViewId = 1;
	private String TAG = "calendarview";
	private final int CONTEXT_MENU_ADD = 0;
	private final int CONTEXT_MENU_MODIFY = 1;
	private final int CONTEXT_MENU_REMOVE = 2;

	public static final int POPUP_TIME_SLOT = 0;
	public static final int POPUP_ITEM = 1;

	private final int POPUP_FORM = 0;
	private DayInMonthView[] daysInMonth = new DayInMonthView[Constant.NUM_DAY_IN_WEEK
			* Constant.NUM_ROW_IN_MONTH_CALENDAR];
	private TextView title = null;
	private TextView[] weeksColumnTitle = new TextView[Constant.NUM_DAY_IN_WEEK];
	private ArrayList<WeekItemView> itemsInWeek = null;

	private RelativeLayout containerLayout = null;
	private ZoomableViewGroup titleLayout = null;
	private ZoomableViewGroup contentLayout = null;
	private int width;
	private int rowHeight = 80;
	private int titleHeight = 50;
	private PopupWindow popupWindow = null;
	private EditText mEtTitle = null;
	private TextView mTvStartTime = null;
	private TextView mTvEndTime = null;
	private AutoCompleteTextView mAutoTvLocation = null;
	private Button mAddButton = null;
	private Button mModifyButton = null;
	private Button mRemoveButton = null;
	private Button mDetailButton = null;
	private View itemBeingSelected = null;
	private TimeSlot startTime = null;
	private TimeSlot endTime = null;
	private float marginWidth = 70;
	private int height = 1000;
	private int columnWidth = 50;
	private int screenWidth = 0, screenHeight = 0;
	private Handler mHandler = null;

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendarmonthview);
		containerLayout = (RelativeLayout) findViewById(R.id.container);

		// width = contentLayout.getWidth();
		// Log.d(TAG, "" + width);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		mHandler = new Handler();
		try {
			displayAllTimeSlot();
			// addZoomButtons(containerLayout);
			// addItemViewToTimeSlot(null, 0, 5, 35, 10, 30, 15, 35);
			// addItemViewToTimeSlot(null, 1, 6, 30, 7, 30, 15, 35);
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		itemsInWeek = new ArrayList<WeekItemView>();
		addZoomButtons();
	}

	@Override
	public void onResume(){
		super.onResume();
		updateView();
	}
	
	private void updateView() {
//		
//		for (int i = contentLayout.getChildCount() - 1; i >= 0; i--) {
//			if (contentLayout.getChildAt(i).getClass()
//					.equals(DayInMonthView.class)) {
//				contentLayout.removeViewAt(i);
//			}
//		}
//		for (int t = 0; t < numDisplayedDay; t++) {
//			Date thisDate = daysInWeekSlot[t].getDate();
//			listItem = mDBinterface.RetrieveItemFromDatabase(
//					getApplicationContext(), thisDate);
//			listEvent = listItem.get(0);
//			listTask = listItem.get(1);
//			if (listEvent.size() != 0) {
//				ArrayList<ArrayList<Item>> layeredListEvent = sortIntoLayer(listEvent);
//				int numberLayer = layeredListEvent.size();
//				int widthEachLayer = (daySlotWidth) / numberLayer;
//				for (int i = 0; i < layeredListEvent.size(); i++) {
//					ArrayList<Item> thisLayeredList = layeredListEvent.get(i);
//					for (int j = 0; j < thisLayeredList.size(); j++) {
//						Item item = thisLayeredList.get(j);
//						Date startTime = item.GetStartTime();
//						Date endTime = item.GetEndTime();
//
//						addItemViewToTimeSlot(item, t, startTime.getHours(),
//								startTime.getMinutes(), endTime.getHours(),
//								endTime.getMinutes(), i * widthEachLayer,
//								widthEachLayer);
//					}
//				}
//			}
//		}
	}

	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			showZoomButtons();
			Log.d(TAG, "dispatch touch event");
		}
		return super.dispatchTouchEvent(ev);
	}

	/*
	 * Adding zoom controls This part adds the zoom buttons to the view
	 */

	private ZoomControls mZoomButtonsController = null;

	private void showZoomButtons() {
		if (!mZoomButtonsController.isShown()) {
			makeZoomButtonAutoDismiss();
			mZoomButtonsController.bringToFront();
		}
	}

	private void addZoomButtons() {
		if (mZoomButtonsController == null) {
			mZoomButtonsController = (ZoomControls) findViewById(R.id.zoomcontrolbutton);
			try {
				nextViewId++;
				showZoomButtons();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
			mZoomButtonsController
					.setOnZoomInClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							contentLayout
									.zoom(ZoomableViewGroup.DEFAULT_ZOOM_IN_RATE);
						}
					});
			mZoomButtonsController
					.setOnZoomOutClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							contentLayout
									.zoom(ZoomableViewGroup.DEFAULT_ZOOM_OUT_RATE);
						}
					});
		}
	}

	private void makeZoomButtonAutoDismiss() {
		mZoomButtonsController.show();
		Runnable hideZoom = new Runnable() {
			@Override
			public void run() {
				mZoomButtonsController.hide();
			}
		};
		mHandler.removeCallbacks(hideZoom);
		new Handler().postDelayed(hideZoom, 3000);
	}

	/*
	 * 
	 * @see android.app.Activity#onResume()
	 */

	private void displayAllTimeSlot() {
		Resources myResources = getResources();
		rowHeight = (int) myResources.getDimension(R.dimen.timeslot_height);
		titleHeight = (int) myResources.getDimension(R.dimen.title_height);
		columnWidth = (int) (screenWidth / 7);

		Log.d(TAG, "add view to month calendar" + " " + screenWidth + " "
				+ columnWidth + " " + rowHeight);
		// containerLayout.setBackgroundColor(Color.BLUE);
		containerLayout.addView(new View(getApplicationContext()));
		// Adding title part
		// This part is used to generate the title for the week calendar

		// adding the big title layout
		int exTitleHeight = RelativeLayout.MeasureSpec.makeMeasureSpec(
				titleHeight, RelativeLayout.MeasureSpec.EXACTLY);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, titleHeight);
		titleLayout = new ZoomableViewGroup(getApplicationContext());
		// titleLayout.measure(LayoutParams.FILL_PARENT, 200);
		titleLayout.setLayoutParams(params);
		titleLayout.setId(nextViewId++);
		// titleLayout.setBackgroundColor(Color.RED);
		containerLayout.addView(titleLayout);

		titleLayout.addView(new View(getApplicationContext()));
		for (int i = 0; i < Constant.NUM_DAY_IN_WEEK; i++) {
			weeksColumnTitle[i] = new TextView(getApplicationContext());
			weeksColumnTitle[i]
					.setText(Constant.DAY_IN_WEEK[i].substring(0, 3));
			int exWidth = View.MeasureSpec.makeMeasureSpec((int) columnWidth,
					View.MeasureSpec.EXACTLY);
			int exHeight = View.MeasureSpec.makeMeasureSpec(titleHeight,
					View.MeasureSpec.EXACTLY);
			weeksColumnTitle[i].measure(exWidth, exHeight);
			weeksColumnTitle[i].layout((int) i * columnWidth, 0, (int) (i + 1)
					* columnWidth, titleHeight);
			weeksColumnTitle[i].setId(titleLayout.getChildCount());
			titleLayout.addView(weeksColumnTitle[i]);
			Log.d(TAG,
					"add day " + i + " title " + " at "
							+ weeksColumnTitle[i].getLeft() + " "
							+ weeksColumnTitle[i].getTop() + " " + " "
							+ weeksColumnTitle[i].getMeasuredWidth() + " "
							+ weeksColumnTitle[i].getMeasuredHeight());
		}

		// Adding content part
		// This part is used to generate the content for the week calendar

		// adding the big content layout
		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		params.addRule(RelativeLayout.BELOW, titleLayout.getId());
		contentLayout = new ZoomableViewGroup(getApplicationContext());
		contentLayout.setLayoutParams(params);
		contentLayout.setId(nextViewId++);
		containerLayout.addView(contentLayout);
		contentLayout.addView(new View(getApplicationContext()));
		contentLayout.setClickable(true);
		contentLayout.setFocusable(true);
		contentLayout.setScrollContainer(true);
		contentLayout.setOnZoomListener(mOnZoomListener);
		contentLayout.setOnScrollListener(mOnScrollListener);

		Date startDate = findStartDateForMonthContainDate(new Date());
		Log.d(TAG, startDate.getDay() + " " + startDate.getDate() + " "
				+ startDate.getMonth() + " " + startDate.getYear());
		// adding time slot in content view
		int numDay = 0;
		for (int i = 0; i < Constant.NUM_ROW_IN_MONTH_CALENDAR; i++) {
			for (int j = 0; j < Constant.NUM_DAY_IN_WEEK; j++) {
				Log.d(TAG, i+" "+j+" "+numDay);
				daysInMonth[numDay] = new DayInMonthView(getApplicationContext());
				Log.d(TAG, ""+(startDate.getTime()
						+ numDay * Constant.NUM_MILI_SEC_IN_DAY));
				Date time = new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate()); 
				time.setTime(startDate.getTime()+ Constant.NUM_MILI_SEC_IN_DAY*numDay);
				Log.d(TAG, ""+time.toString());
				daysInMonth[numDay].setTime(time);
				daysInMonth[numDay].setText(""+time.getDate());
				
				int exWidth = View.MeasureSpec.makeMeasureSpec(columnWidth,
						View.MeasureSpec.EXACTLY);
				int exHeight = View.MeasureSpec.makeMeasureSpec(rowHeight,
						View.MeasureSpec.EXACTLY);
				daysInMonth[numDay].measure(exWidth, exHeight);

				daysInMonth[numDay].layout(j * columnWidth, i * rowHeight, j
						* columnWidth + daysInMonth[numDay].getMeasuredWidth(), i
						* rowHeight + daysInMonth[numDay].getMeasuredHeight());
				Log.d(TAG, "adding day: "+numDay+" which is "+" "
						+(j * columnWidth)+" "
						+(i * rowHeight)+" "
						+(j	* columnWidth + daysInMonth[numDay].getMeasuredWidth())+" "
						+(i	* rowHeight + daysInMonth[numDay].getMeasuredHeight()));
				daysInMonth[numDay].setId(contentLayout.getChildCount());
				contentLayout.addView(daysInMonth[numDay]);
				numDay++;
			}
		}

		Log.d("calendarview",
				"content layout size screen " + contentLayout.getWidth() + " "
						+ contentLayout.getHeight());
		contentLayout.viewWidth = contentLayout.getWidth();
		contentLayout.viewHeight = rowHeight * Constant.NUM_ROW_IN_MONTH_CALENDAR;
		Log.d("calendarview", "content layout size view "
				+ contentLayout.viewWidth + " " + contentLayout.viewHeight);

	}

	private Date findStartDateForMonthContainDate(Date date) {
		Date startDateInMonth = new Date(date.getYear(), date.getMonth(), 1);
		Log.d(TAG, "find start date for month contains: "+ date.toString());
		int dayInWeek = startDateInMonth.getDay();
		Log.d(TAG, "start date: "+ startDateInMonth.toString());
		int numDayFromStart = dayInWeek - 0;
		long timeNow = startDateInMonth.getTime();
		long timeFromStart = numDayFromStart * Constant.NUM_MILI_SEC_IN_DAY;
		startDateInMonth.setTime(timeNow - timeFromStart);
		Log.d(TAG, "start date move to beginning of week: "+ startDateInMonth.toString());
		return startDateInMonth;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Toast.makeText(getApplicationContext(), "on touch event",
					Toast.LENGTH_SHORT);
			Log.d(TAG, "on touch event");
			return true;
		}
		return false;
	}

	private void showPopUpWindow() {
		if (itemBeingSelected.getClass().equals(TimeSlotView.class)) {
			TimeSlotView timeSlotView = (TimeSlotView) itemBeingSelected;
			Intent createPopup = new Intent(getApplicationContext(),
					PopupForm.class);
			createPopup.putExtra("popupType", POPUP_TIME_SLOT);
			createPopup.putExtra("startTime", timeSlotView.getTime());
			createPopup.putExtra("offX", itemBeingSelected.getLeft());
			createPopup.putExtra("offY", itemBeingSelected.getBottom());
			startActivityForResult(createPopup, POPUP_FORM);
		} else {
			WeekItemView itemView = (WeekItemView) itemBeingSelected;
			Intent createPopup = new Intent(getApplicationContext(),
					PopupForm.class);
			createPopup
					.putExtra("startTime", itemView.getItem().GetStartTime());
			createPopup.putExtra("endTime", itemView.getItem().GetEndTime());
			createPopup.putExtra("endTime", itemView.getItem().GetEndTime());
			createPopup.putExtra("popupType", POPUP_ITEM);
			createPopup.putExtra("offX", itemBeingSelected.getLeft());
			createPopup.putExtra("offY", itemBeingSelected.getBottom());
			startActivityForResult(createPopup, POPUP_FORM);
		}
	}

	public WeekItemView addItemViewToTimeSlot(Item item, int day, int fromHour,
			int fromMin, int toHour, int toMin, int leftMargin, int width) {
		WeekItemView newItem = new WeekItemView(getApplicationContext());
		/*
		 * need to read from database
		 */
		// newItem.setItem(object);
		newItem.setText("place holder text");
		contentLayout.addView(newItem);
		adjustPositionWeekItemView(newItem, day, fromHour, fromMin, toHour,
				toMin, leftMargin, width);
		return newItem;
	}

	private void adjustPositionWeekItemView(WeekItemView newItem, int day,
			int fromHour, int fromMin, int toHour, int toMin, int leftMargin,
			int width) {
		int height = getHeightForItemInTimeSlot(fromHour, fromMin, toHour,
				toMin);
		int exWidth = View.MeasureSpec.makeMeasureSpec(width,
				View.MeasureSpec.EXACTLY);
		int exHeight = View.MeasureSpec.makeMeasureSpec(height,
				View.MeasureSpec.EXACTLY);
		newItem.measure(exWidth, exHeight);
		int left = daysInMonth[day].getLeft() + leftMargin;
		int top = daysInMonth[fromHour].getTop()
				+ getMarginCorrespondingToPeriod(fromMin);
		int right = left + newItem.getMeasuredWidth();
		int bottom = top + newItem.getMeasuredHeight();
		newItem.layout(left, top, right, bottom);
		newItem.setId(contentLayout.getChildCount());
	}

	private int getHeightForItemInTimeSlot(int fromHour, int fromMin,
			int toHour, int toMin) {
		int result = rowHeight * (toHour - fromHour)
				- getMarginCorrespondingToPeriod(fromMin)
				+ getMarginCorrespondingToPeriod(toMin);
		return result;
	}

	private int getMarginCorrespondingToPeriod(int numMin) {
		return (int) (1.0 * rowHeight * numMin / 60);
	}

	private void removeItem(int itemId) {
		// TODO Auto-generated method stub

	}

	/*
	 * Zooming and scrolling part Since the content view is a viewgroup that
	 * already supported zooming and scrolling, this part only helps that view
	 * to adjust the controls that are added. It helps by re-adjust the subviews
	 * so that they are not misplaced due to round-off error.
	 */
	private ZoomableViewGroup.OnScrollListener mOnScrollListener = new ZoomableViewGroup.OnScrollListener() {

		@Override
		public void onScrollBy(int dx, int dy) {
			titleLayout.scrollBy(dx, 0);
		}
	};

	private ZoomableViewGroup.OnZoomListener mOnZoomListener = new ZoomableViewGroup.OnZoomListener() {

		@Override
		public void onZoomWithScale(float zoomScale) {
			// zoom out the width of the title row
			int startId = weeksColumnTitle[0].getId();
			for (int i = 0; i < Constant.NUM_DAY_IN_WEEK; i++) {
				int id = startId + i;
				View view = titleLayout.getChildAt(id);
				if (view == null)
					continue;
				int cw = (int) (view.getWidth() * zoomScale);
				int ch = (int) view.getHeight();
				int cl = (int) (view.getLeft() * zoomScale);
				int ct = (int) (view.getTop());

				Log.d(TAG, "on zoom, child " + id + " " + cl + " " + ct + " "
						+ cw + " " + ch);
				int ecw = View.MeasureSpec.makeMeasureSpec(cw,
						View.MeasureSpec.EXACTLY);
				int ech = View.MeasureSpec.makeMeasureSpec(ch,
						View.MeasureSpec.EXACTLY);
				view.measure(ecw, ech);
				view.layout(cl, ct, cl + cw, ct + ch);
				view.invalidate(cl, ct, cl + cw, ct + ch);
			}

			// adjusting the day slot so that they are not misplaced due to
			// round off error

			// first we adjust the title, then align the day slot according
			// to the title and the slot of the beginning day of the month
			for (int i = 1; i < Constant.NUM_DAY_IN_WEEK; i++) {
				int id = startId + i;
				View view = titleLayout.getChildAt(id);
				View prevView = titleLayout.getChildAt(id - 1);
				int prevBorder = prevView.getRight();
				view.layout(prevBorder, view.getTop(),
						prevBorder + view.getWidth(), view.getBottom());
			}

			// now we adjust the day-slots in the month
			int startDateId = daysInMonth[0].getId();
			int totalNumber = Constant.NUM_ROW_IN_MONTH_CALENDAR
					* Constant.NUM_DAY_IN_WEEK;
			for (int i = 1; i < totalNumber; i++) {
				int id = startDateId + i;
				DayInMonthView view = (DayInMonthView) contentLayout
						.getChildAt(id);
				int titleId = startId + view.getTime().getDay();
				View titleView = titleLayout.getChildAt(titleId);
				View prevView = null;
				if (i >= Constant.NUM_DAY_IN_WEEK) {
					prevView = contentLayout.getChildAt(id
							- Constant.NUM_DAY_IN_WEEK);
					view.layout(titleView.getLeft(), prevView.getBottom(),
							titleView.getRight(),
							prevView.getBottom() + view.getHeight());
				} else {
					view.layout(titleView.getLeft(), 0, titleView.getRight(),
							0 + view.getHeight());
				}
			}
			contentLayout.invalidate();
		}
	};

}
