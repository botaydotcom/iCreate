package com.android.apptime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CalendarDayView extends Activity {
	public static int nextViewId = 1;
	private String TAG = "calendarview";
	private final int CONTEXT_MENU_ADD = 0;
	private final int CONTEXT_MENU_MODIFY = 1;
	private final int CONTEXT_MENU_REMOVE = 2;

	private final int POPUP_FORM= 0;
	private TimeSlotView[] dayTimeSlot = new TimeSlotView[Constant.NUM_HOUR];
	private RelativeLayout listTimeSlotLayout = null;
	private int width;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendardayview);
		listTimeSlotLayout = (RelativeLayout) findViewById(R.id.timeslotlistlayout);
		width = listTimeSlotLayout.getWidth();
		Log.d(TAG, "" + width);

		try {
			displayAllTimeSlot();
			//
			// TextView timeSlotView = new TextView(getApplicationContext());
			// timeSlotView.setText("1pm-3pm: \n Meeting with team @YIH");
			// timeSlotView.setBackgroundColor(Color.RED);
			//
			// RelativeLayout.LayoutParams layoutParams = null;
			// layoutParams = new RelativeLayout.LayoutParams(200, 160);
			// layoutParams.addRule(RelativeLayout.ALIGN_TOP, nextViewId - 20);
			// layoutParams.setMargins(50, 0, 0, 0);
			// timeSlotView.setLayoutParams(layoutParams);
			// timeSlotView.setId(nextViewId++);
			// listTimeSlotLayout.addView(timeSlotView);
			// timeSlotView.setFocusable(true);
			//
			// TextView timeSlotView1 = new TextView(getApplicationContext());
			// timeSlotView1.setText("1pm-2pm: \n Assignment due");
			// timeSlotView1.setBackgroundColor(Color.GREEN);
			// layoutParams = null;
			// layoutParams = new RelativeLayout.LayoutParams(250, 80);
			// layoutParams.addRule(RelativeLayout.ALIGN_TOP, nextViewId - 21);
			// layoutParams.setMargins(250, 0, 0, 0);
			// timeSlotView1.setLayoutParams(layoutParams);
			// timeSlotView1.setId(nextViewId++);
			// listTimeSlotLayout.addView(timeSlotView1);
			// timeSlotView1.setFocusable(true);

			addItemViewToTimeSlot(null, 5, 35, 10, 30, 50, 200);
			addItemViewToTimeSlot(null, 6, 30, 7, 30, 250, 200);
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
	}

	private void displayAllTimeSlot() {
		for (int i = 0; i < Constant.NUM_HOUR; i++) {
			dayTimeSlot[i] = new TimeSlotView(getApplicationContext());
			dayTimeSlot[i].getTime().setTime(i, 0);
			dayTimeSlot[i].setText(dayTimeSlot[i].getTime()
					.getAPPMHourFormatWithoutHourPadding());
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

	private void showPopUpWindow() {
		if (itemBeingSelected.getClass().equals(TimeSlotView.class)){
			TimeSlotView timeSlotView = (TimeSlotView) itemBeingSelected;
			Intent createPopup = new Intent(getApplicationContext(), PopupForm.class);
			createPopup.putExtra("startTime", timeSlotView.getTime().getHour());
			createPopup.putExtra("offX", itemBeingSelected.getLeft());
			createPopup.putExtra("offY", itemBeingSelected.getBottom());
			startActivityForResult(createPopup, POPUP_FORM);
		}
	}

	public void addItemViewToTimeSlot(Object object, int fromHour, int fromMin,
			int toHour, int toMin, int leftMargin, int width) {
		DayItemView newItem = new DayItemView(getApplicationContext());
		newItem.setText("Place holder text");
		newItem.setItem(object);
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

}