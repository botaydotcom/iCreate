package com.android.apptime.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.apptime.DatabaseInterface;
import com.android.apptime.Item;
import com.android.apptime.R;

public class CalendarDayView extends Activity {

	private DatabaseInterface mDBinterface = null;
	public static int nextViewId = 1;
	private String TAG = "calendarview";
	private final int CONTEXT_MENU_ADD = 0;
	private final int CONTEXT_MENU_MODIFY = 1;
	private final int CONTEXT_MENU_REMOVE = 2;

	public static final int POPUP_TIME_SLOT = 0;
	public static final int POPUP_ITEM = 1;
	private static final int RIGHT_MARGIN = 50;

	private final int POPUP_FORM = 0;
	private TimeSlotView[] dayTimeSlot = new TimeSlotView[Constant.NUM_HOUR];
	private RelativeLayout listTimeSlotLayout = null;
	private int width;
	private int widthDisplayField;
	private int timeSlotHeight = 80;
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
	private Date thisDate = null;

	private ScrollView mScrollView = null;

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
		setContentView(R.layout.calendardayview);
		listTimeSlotLayout = (RelativeLayout) findViewById(R.id.timeslotlistlayout);
		width = listTimeSlotLayout.getWidth();
		setDBInterface();

		thisDate = new Date();
		thisDate = new Date(thisDate.getYear(), thisDate.getMonth(),
				thisDate.getDate());
		Log.d(TAG, "" + width);

		try {
			displayAllTimeSlot();
			// addItemViewToTimeSlot(null, 5, 35, 10, 30, 50, 200);
			// addItemViewToTimeSlot(null, 6, 30, 7, 30, 250, 200);
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		updateView();
	}

	private void setDBInterface() {
		this.mDBinterface = DatabaseInterface
				.getDatabaseInterface(getApplicationContext());
		mDBinterface.SetObserver(observer);
	}

	private void displayAllTimeSlot() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		widthDisplayField = metrics.widthPixels
				- (int) getResources().getDimension(R.dimen.timeslot_margin);
		mScrollView = (ScrollView) findViewById(R.id.tabcontentscrollview);
		for (int i = 0; i < Constant.NUM_HOUR; i++) {
			dayTimeSlot[i] = new TimeSlotView(getApplicationContext());
			dayTimeSlot[i].getTime().setHours(i);
			dayTimeSlot[i].getTime().setMinutes(0);
			dayTimeSlot[i].setText(TimeFormat
					.getAMPMHourFormatWithoutHourPadding(dayTimeSlot[i]
							.getTime()));
			RelativeLayout.LayoutParams layoutParams = null;
			layoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT, timeSlotHeight);
			if (i > 0)
				layoutParams.addRule(RelativeLayout.BELOW,
						dayTimeSlot[i - 1].getId());
			dayTimeSlot[i].setLayoutParams(layoutParams);
			dayTimeSlot[i].setId(nextViewId++);
			listTimeSlotLayout.addView(dayTimeSlot[i]);
			final TimeSlotView thisView = dayTimeSlot[i];
			dayTimeSlot[i].setClickable(true);
			dayTimeSlot[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					itemBeingSelected = thisView;
					mScrollView.scrollTo(0, thisView.getTop());
					showPopUpWindow();
				}
			});
		}
		dayTimeSlot[0].requestFocus();
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

	public void scrollTo(Date startTime) {

	}

	private void updateView() {
		for (int i = listTimeSlotLayout.getChildCount() - 1; i >= 0; i--) {
			if (listTimeSlotLayout.getChildAt(i).getClass()
					.equals(DayItemView.class)) {
				listTimeSlotLayout.removeViewAt(i);
			}
		}
		listItem = mDBinterface.RetrieveItemFromDatabase(
				getApplicationContext(), thisDate);
		listEvent = listItem.get(0);
		listTask = listItem.get(1);
		if (listEvent.size() != 0) {
			ArrayList<ArrayList<Item>> layeredListEvent = sortIntoLayer(listEvent);
			int numberLayer = layeredListEvent.size();
			int widthEachLayer = (widthDisplayField - RIGHT_MARGIN)
					/ numberLayer;
			for (int i = 0; i < layeredListEvent.size(); i++) {
				ArrayList<Item> thisLayeredList = layeredListEvent.get(i);
				for (int j = 0; j < thisLayeredList.size(); j++) {
					Item item = thisLayeredList.get(j);
					Date startTime = item.GetStartTime();
					Date endTime = item.GetEndTime();

					addEventViewToTimeSlot(item, startTime.getHours(),
							startTime.getMinutes(), endTime.getHours(),
							endTime.getMinutes(), (int) getResources()
									.getDimension(R.dimen.timeslot_margin)
									+ i
									* widthEachLayer, widthEachLayer);
				}
			}
		}

		if (listTask.size() != 0) {
			for (int i = 0; i < listTask.size(); i++) {
				Item item = listTask.get(i);
				Date deadLine = item.GetDeadline();
				addTaskViewToTimeSlot(item, deadLine);
			}
		}
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

	private void showPopUpWindow() {
		if (itemBeingSelected.getClass().equals(TimeSlotView.class)) {
			TimeSlotView timeSlotView = (TimeSlotView) itemBeingSelected;
			Intent createPopup = new Intent(getApplicationContext(),
					PopupForm.class);
			createPopup.putExtra("popupType", POPUP_TIME_SLOT);
			createPopup.putExtra("startTime", timeSlotView.getTime());
			Log.d(TAG, timeSlotView.getTime().toString());
			createPopup.putExtra("endTime", new Date(timeSlotView.getTime()
					.getTime() + Constant.NUM_MILI_SEC_IN_HOUR));
			createPopup.putExtra("offX", itemBeingSelected.getLeft());
			createPopup.putExtra("offY", itemBeingSelected.getBottom());
			startActivityForResult(createPopup, POPUP_FORM);
		} else {
			DayItemView itemView = (DayItemView) itemBeingSelected;
			Intent createPopup = new Intent(getApplicationContext(),
					PopupForm.class);
			createPopup
					.putExtra("startTime", itemView.getItem().GetStartTime());
			createPopup.putExtra("endTime", itemView.getItem().GetEndTime());
			createPopup.putExtra("popupType", POPUP_ITEM);
			createPopup.putExtra("itemId", itemView.getItem().GetId());
			createPopup.putExtra("offX", itemBeingSelected.getLeft());
			createPopup.putExtra("offY", itemBeingSelected.getBottom());
			startActivityForResult(createPopup, POPUP_FORM);
		}
	}

	private void addTaskViewToTimeSlot(Item item, Date deadLine) {
		DayTaskView newItem = new DayTaskView(getApplicationContext());
		newItem.setText("Place holder text");
		// need to read from database
		// newItem.setItem(object);
		int toHour = deadLine.getHours();
		int toMin = deadLine.getMinutes();
		if (toHour >= 1) {
			// display a line with title above (style 1)
			RelativeLayout.LayoutParams layoutParams = null;
			int height = (int) getResources().getDimension(
					R.dimen.taskview_height);
			layoutParams = new RelativeLayout.LayoutParams(widthDisplayField,
					height);
			if (toMin == 0) {
				layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM,
						dayTimeSlot[toHour].getId());
				layoutParams.setMargins(
						(int) getResources().getDimension(
								R.dimen.timeslot_margin), 0, 0, 0);
			} else {
				layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM,
						dayTimeSlot[toHour + 1].getId());
				layoutParams.setMargins(
						(int) getResources().getDimension(
								R.dimen.timeslot_margin), 0, 0,
						getMarginCorrespondingToPeriod(60 - toMin));
			}
			newItem.setLayoutParams(layoutParams);
			newItem.setId(nextViewId++);
			listTimeSlotLayout.addView(newItem);
		} else {
			// display a line with title below (style 2)
			newItem.setStyle(DayTaskView.STYLE_BELOW);
			RelativeLayout.LayoutParams layoutParams = null;
			int height = (int) getResources().getDimension(
					R.dimen.taskview_height);
			layoutParams = new RelativeLayout.LayoutParams(widthDisplayField,
					height);
			if (toMin == 0) {
				layoutParams.addRule(RelativeLayout.ALIGN_TOP,
						dayTimeSlot[toHour].getId());
				layoutParams.setMargins(
						(int) getResources().getDimension(R.dimen.timeslot_margin),
						getMarginCorrespondingToPeriod(toMin), 0, 0);
			}
			newItem.setLayoutParams(layoutParams);
			newItem.setId(nextViewId++);
			listTimeSlotLayout.addView(newItem);
		}

	}

	public DayItemView addEventViewToTimeSlot(Item item, int fromHour,
			int fromMin, int toHour, int toMin, int leftMargin, int width) {
		DayItemView newItem = new DayItemView(getApplicationContext());
		newItem.setText(item.GetTitle());
		newItem.setItem(item);
		// need to read from database
		// newItem.setItem(object);
		RelativeLayout.LayoutParams layoutParams = null;
		int height = getHeightForItemInTimeSlot(fromHour, fromMin, toHour,
				toMin);
		layoutParams = new RelativeLayout.LayoutParams(width, height);
		layoutParams.addRule(RelativeLayout.ALIGN_TOP,
				dayTimeSlot[fromHour].getId());
		layoutParams.setMargins(leftMargin,
				getMarginCorrespondingToPeriod(fromMin), 0, 0);
		newItem.setLayoutParams(layoutParams);
		newItem.setId(nextViewId++);
		listTimeSlotLayout.addView(newItem);
		return newItem;
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (v.getClass().equals(TimeSlotView.class)) {
			menu.setHeaderTitle("Selected Time Slot Menu:");
			menu.add(Menu.NONE, CONTEXT_MENU_ADD, Menu.NONE,
					getString(R.string.modifycontextmenu));
			menu.add(Menu.NONE, CONTEXT_MENU_MODIFY, Menu.NONE,
					getString(R.string.modifycontextmenu));
			menu.add(Menu.NONE, CONTEXT_MENU_REMOVE, Menu.NONE,
					getString(R.string.removecontextmenu));
		}
		if (v.getClass().equals(DayItemView.class)) {
			menu.setHeaderTitle("Selected Item Menu:");
			menu.add(Menu.NONE, CONTEXT_MENU_MODIFY, Menu.NONE,
					getString(R.string.modifycontextmenu));
			menu.add(Menu.NONE, CONTEXT_MENU_REMOVE, Menu.NONE,
					getString(R.string.removecontextmenu));
		} else
			return;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		switch (item.getItemId()) {
		case CONTEXT_MENU_ADD:
			return true;
		case CONTEXT_MENU_MODIFY:
			return true;
		case CONTEXT_MENU_REMOVE:
			return true;
		}
		return false;
	}

	// will not get called

}