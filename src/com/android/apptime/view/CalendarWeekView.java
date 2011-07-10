package com.android.apptime.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.android.apptime.DatabaseInterface;
import com.android.apptime.Item;
import com.android.apptime.R;

public class CalendarWeekView extends Activity {
	private static int nextViewId = 1;
	private String TAG = "calendarview";
	private final int CONTEXT_MENU_ADD = 0;
	private final int CONTEXT_MENU_MODIFY = 1;
	private final int CONTEXT_MENU_REMOVE = 2;

	public static final int POPUP_TIME_SLOT = 0;
	public static final int POPUP_ITEM = 1;
	private static final int RIGHT_MARGIN = 50;

	private final int POPUP_FORM = 0;
	private TimeSlotView[] dayTimeSlot = new TimeSlotView[Constant.NUM_HOUR];
	private DayInWeekView[] daysInWeekSlot = new DayInWeekView[Constant.NUM_DAY_IN_WEEK];
	private TextView title = null;
	private TextView[] daysInWeekTitle = new TextView[Constant.NUM_DAY_IN_WEEK];
	private ArrayList<WeekItemView> itemsInWeek = null;

	private RelativeLayout containerLayout = null;
	private ZoomableViewGroup titleLayout = null;
	private ZoomableViewGroup contentLayout = null;
	private int width;
	private int timeSlotHeight = 80;
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
	private int daySlotWidth = 50;
	private int screenWidth = 0, screenHeight = 0;
	private Handler mHandler = null;

	private Date thisWeek = null;

	private ArrayList<ArrayList<Item>> listItem = null;
	private ArrayList<Item> listTask = null, listEvent = null;

	private DatabaseInterface.DbSetChange observer = new DatabaseInterface.DbSetChange() {

		@Override
		public void Update() {
			updateView();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendarweekview);
		containerLayout = (RelativeLayout) findViewById(R.id.container);

		// width = contentLayout.getWidth();
		// Log.d(TAG, "" + width);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		mHandler = new Handler();
		thisWeek = new Date();
		thisWeek = new Date(thisWeek.getYear(), thisWeek.getMonth(),
				thisWeek.getDate());
		int dayInWeek = thisWeek.getDay();
		Log.d(TAG, "start date: " + thisWeek.getDay());
		int numDayFromStart = dayInWeek - 0;
		long timeNow = thisWeek.getTime();
		long timeFromStartOfTheWeek = numDayFromStart
				* Constant.NUM_MILI_SEC_IN_DAY;
		thisWeek.setTime(timeNow - timeFromStartOfTheWeek);
		Log.d(TAG,
				"start date move to beginning of week: " + thisWeek.toString());

		setDBInterface();
		try {
			displayAllTimeSlot();
			// addZoomButtons(containerLayout);
			//addItemViewToTimeSlot(null, 0, 5, 35, 10, 30, 15, 35);
			// addItemViewToTimeSlot(null, 1, 6, 30, 7, 30, 15, 35);
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		itemsInWeek = new ArrayList<WeekItemView>();
		addZoomButtons();
	}

	private void setDBInterface() {
		this.mDBinterface = DatabaseInterface
				.getDatabaseInterface(getApplicationContext());
		mDBinterface.SetObserver(observer);
	}

	@Override
	public void onResume() {
		super.onResume();
		updateView();
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
	private DatabaseInterface mDBinterface;
	private int widthDisplayField;

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
		marginWidth = myResources.getDimension(R.dimen.timeslot_margin);
		timeSlotHeight = (int) myResources
				.getDimension(R.dimen.timeslot_height);
		titleHeight = (int) myResources.getDimension(R.dimen.title_height);
		daySlotWidth = (int) ((screenWidth - marginWidth) / 7);

		Log.d(TAG, "add view to week calendar" + " " + screenWidth + " "
				+ marginWidth + " " + daySlotWidth);
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
		// adding the title row
		title = new TextView(getApplicationContext());
		title.setText("Time");
		int exWidth = View.MeasureSpec.makeMeasureSpec((int) marginWidth,
				View.MeasureSpec.EXACTLY);
		int exHeight = View.MeasureSpec.makeMeasureSpec(titleHeight,
				View.MeasureSpec.EXACTLY);
		title.measure(exWidth, exHeight);
		title.layout(0, 0, (int) marginWidth, titleHeight);
		title.setId(titleLayout.getChildCount());
		// title.setBackgroundColor(Color.GREEN);
		titleLayout.addView(title);
		Log.d(TAG,
				"add title " + " at " + title.getLeft() + " " + title.getTop()
						+ " " + " " + title.getMeasuredWidth() + " "
						+ title.getMeasuredHeight());

		for (int i = 0; i < Constant.NUM_DAY_IN_WEEK; i++) {
			daysInWeekTitle[i] = new TextView(getApplicationContext());
			daysInWeekTitle[i].setText(Constant.DAY_IN_WEEK[i].substring(0, 3));
			exWidth = View.MeasureSpec.makeMeasureSpec((int) daySlotWidth,
					View.MeasureSpec.EXACTLY);
			exHeight = View.MeasureSpec.makeMeasureSpec(titleHeight,
					View.MeasureSpec.EXACTLY);
			daysInWeekTitle[i].measure(exWidth, exHeight);
			daysInWeekTitle[i].layout((int) marginWidth + i * daySlotWidth, 0,
					(int) marginWidth + (i + 1) * daySlotWidth, titleHeight);
			daysInWeekTitle[i].setId(titleLayout.getChildCount());
			titleLayout.addView(daysInWeekTitle[i]);
			Log.d(TAG,
					"add day " + i + " title " + " at "
							+ daysInWeekTitle[i].getLeft() + " "
							+ daysInWeekTitle[i].getTop() + " " + " "
							+ daysInWeekTitle[i].getMeasuredWidth() + " "
							+ daysInWeekTitle[i].getMeasuredHeight());

		}

		// Adding content part
		// This part is used to generate the content for the week calendar

		// adding the big content layout
		int contentHeight = View.MeasureSpec.makeMeasureSpec(1000,
				View.MeasureSpec.EXACTLY);
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

		// adding time slot in content view
		for (int i = 0; i < Constant.NUM_HOUR; i++) {
			dayTimeSlot[i] = new TimeSlotView(getApplicationContext());
			dayTimeSlot[i].getTime().setHours(i);
			dayTimeSlot[i].getTime().setMinutes(0);
			dayTimeSlot[i].setText(TimeFormat
					.getAPPMHourFormatWithoutHourPadding(dayTimeSlot[i]
							.getTime()));
			exWidth = View.MeasureSpec.makeMeasureSpec(screenWidth,
					View.MeasureSpec.EXACTLY);
			exHeight = View.MeasureSpec.makeMeasureSpec(timeSlotHeight,
					View.MeasureSpec.EXACTLY);
			dayTimeSlot[i].measure(exWidth, exHeight);

			dayTimeSlot[i]
					.layout(0, i * timeSlotHeight,
							dayTimeSlot[i].getMeasuredWidth(), (i + 1)
									* timeSlotHeight);

			dayTimeSlot[i].setId(contentLayout.getChildCount());
			contentLayout.addView(dayTimeSlot[i]);
		}

		// adding day slot in content layout (showing the lines)
		for (int i = 0; i < Constant.NUM_DAY_IN_WEEK; i++) {
			Log.d(TAG, "adding day in week slot");
			daysInWeekSlot[i] = new DayInWeekView(getApplicationContext());
			daysInWeekSlot[i].setDate(new Date(thisWeek.getTime() + i
					* Constant.NUM_MILI_SEC_IN_DAY));
			exWidth = View.MeasureSpec.makeMeasureSpec(daySlotWidth,
					View.MeasureSpec.EXACTLY);
			exHeight = View.MeasureSpec.makeMeasureSpec(Constant.NUM_HOUR
					* timeSlotHeight, View.MeasureSpec.EXACTLY);
			daysInWeekSlot[i].measure(exWidth, exHeight);
			daysInWeekSlot[i].layout((int) marginWidth + i * daySlotWidth, 0,
					(int) marginWidth + (i + 1) * daySlotWidth,
					Constant.NUM_HOUR * timeSlotHeight);

			daysInWeekSlot[i].setId(contentLayout.getChildCount());

			contentLayout.addView(daysInWeekSlot[i]);
			daysInWeekSlot[i].setClickable(true);

			final View thisView = daysInWeekSlot[i];
			daysInWeekSlot[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					itemBeingSelected = thisView;
					Log.d(TAG, "day slot selected");
				}
			});
		}
		Log.d("calendarview",
				"content layout size screen " + contentLayout.getWidth() + " "
						+ contentLayout.getHeight());
		contentLayout.viewWidth = contentLayout.getWidth();
		contentLayout.viewHeight = timeSlotHeight * Constant.NUM_HOUR;
		Log.d("calendarview", "content layout size view "
				+ contentLayout.viewWidth + " " + contentLayout.viewHeight);

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

	private ArrayList<ArrayList<Item>> sortIntoLayer(ArrayList<Item> listEvent) {
		ArrayList<ArrayList<Item>> result = new ArrayList<ArrayList<Item>>();
		List<Item> listToSort = listEvent;
		Collections.sort(listToSort, new Comparator<Item>() {
			@Override
			public int compare(Item arg0, Item arg1) {
				if (arg0.GetStartTime().compareTo(arg1.GetStartTime()) == 0)
					return arg0.GetEndTime().compareTo(arg1.GetEndTime());
				else
					return arg0.GetStartTime().compareTo(arg1.GetStartTime());
			}

		});
		boolean[] checked = new boolean[listToSort.size()];
		for (int i = 0; i < listToSort.size(); i++)
			checked[i] = false;
		int numList = 0;
		for (int i = 0; i < listToSort.size(); i++) {
			Item thisItem = listToSort.get(i);
			long minDistance = Long.MAX_VALUE;
			int bestList = -1;
			for (int j = 0; j < numList; j++) {
				ArrayList<Item> layeredList = result.get(j);
				Item lastItem = layeredList.get(layeredList.size() - 1);
				if (lastItem.GetEndTime().compareTo(thisItem.GetStartTime()) < 0) {
					long distance = thisItem.GetStartTime().getTime()
							- lastItem.GetEndTime().getTime();
					if (distance < minDistance) {
						minDistance = distance;
						bestList = j;
					}
				}
			}
			if (bestList == -1) {
				result.add(new ArrayList<Item>());
				result.get(numList).add(thisItem);
				numList++;
			} else {
				result.get(bestList).add(thisItem);
			}
		}
		return result;
	}

	private void updateView() {
		for (int i = contentLayout.getChildCount() - 1; i >= 0; i--) {
			if (contentLayout.getChildAt(i).getClass()
					.equals(WeekItemView.class)) {
				contentLayout.removeViewAt(i);
			}
		}
		for (int t = 0; t < Constant.NUM_DAY_IN_WEEK; t++) {
			Date thisDate = daysInWeekSlot[t].getDate();
			listItem = mDBinterface.RetrieveItemFromDatabase(
					getApplicationContext(), thisDate);
			listEvent = listItem.get(0);
			listTask = listItem.get(1);
			if (listEvent.size() != 0) {
				ArrayList<ArrayList<Item>> layeredListEvent = sortIntoLayer(listEvent);
				int numberLayer = layeredListEvent.size();
				int widthEachLayer = (daySlotWidth) / numberLayer;
				for (int i = 0; i < layeredListEvent.size(); i++) {
					ArrayList<Item> thisLayeredList = layeredListEvent.get(i);
					for (int j = 0; j < thisLayeredList.size(); j++) {
						Item item = thisLayeredList.get(j);
						Date startTime = item.GetStartTime();
						Date endTime = item.GetEndTime();

						addItemViewToTimeSlot(item, t, startTime.getHours(),
								startTime.getMinutes(), endTime.getHours(),
								endTime.getMinutes(), i * widthEachLayer,
								widthEachLayer);
					}
				}
			}
		}
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
		int left = daysInWeekSlot[day].getLeft() + leftMargin;
		int top = dayTimeSlot[fromHour].getTop()
				+ getMarginCorrespondingToPeriod(fromMin);
		int right = left + newItem.getMeasuredWidth();
		int bottom = top + newItem.getMeasuredHeight();
		newItem.layout(left, top, right, bottom);
		newItem.setId(contentLayout.getChildCount());
	}

	private int getHeightForItemInTimeSlot(int fromHour, int fromMin,
			int toHour, int toMin) {
		int result = timeSlotHeight * (toHour - fromHour)
				- getMarginCorrespondingToPeriod(fromMin)
				+ getMarginCorrespondingToPeriod(toMin);
		return result;
	}

	private int getMarginCorrespondingToPeriod(int numMin) {
		return (int) (1.0 * timeSlotHeight * numMin / 60);
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
			int startId = title.getId();
			for (int i = 0; i <= Constant.NUM_DAY_IN_WEEK; i++) {
				int id = startId + i;
				View view = titleLayout.getChildAt(id);
				if (view == null)
					continue;
				int cw = (int) (view.getWidth() * zoomScale);
				int ch = (int) view.getHeight();
				int cl = (int) (view.getLeft() * zoomScale);
				int ct = (int) (view.getTop());

				if (view == title)
					Log.d(TAG, "this is title");
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
			startId = dayTimeSlot[0].getId();

			for (int i = 1; i < Constant.NUM_HOUR; i++) {
				int id = startId + i;
				View view = contentLayout.getChildAt(id);
				View prevView = contentLayout.getChildAt(id - 1);
				int prevBot = prevView.getBottom();
				int thisTop = prevBot;
				int thisWidth = view.getWidth();
				int thisHeight = view.getHeight();
				view.layout(0, thisTop, thisWidth, thisTop + thisHeight);
			}

			int viewHeight = dayTimeSlot[Constant.NUM_HOUR - 1].getBottom();
			// adjust all the day slot height
			startId = daysInWeekSlot[0].getId();
			for (int i = 0; i < Constant.NUM_DAY_IN_WEEK; i++) {
				int id = startId + i;
				View view = contentLayout.getChildAt(id);
				int exWidth = View.MeasureSpec.makeMeasureSpec(view.getWidth(),
						View.MeasureSpec.EXACTLY);
				int exHeight = View.MeasureSpec.makeMeasureSpec(viewHeight,
						View.MeasureSpec.EXACTLY);
				view.measure(exWidth, exHeight);
			}
			contentLayout.invalidate();
		}

	};

}
