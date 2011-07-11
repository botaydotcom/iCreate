package com.android.apptime.view;

import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.apptime.DatabaseInterface;
import com.android.apptime.Item;
import com.android.apptime.R;

public class PopupForm extends Activity {
	private static final int TASK = 0;
	private static final int EVENT = 1;
	private static final int LEFTMARGIN = 30;
	private static final int TOPMARGIN = 100;
	public static int nextViewId = 1;
	private String TAG = "popup";
	private final int CONTEXT_MENU_ADD = 0;
	private final int CONTEXT_MENU_MODIFY = 1;
	private final int CONTEXT_MENU_REMOVE = 2;

	private int itemId = -1;

	private final int START_TIME_DIALOG_ID = 0;
	private final int END_TIME_DIALOG_ID = 1;
	protected static final int START_DATE_DIALOG_ID = 2;
	protected static final int END_DATE_DIALOG_ID = 3;

	private TimeSlotView[] dayTimeSlot = new TimeSlotView[Constant.NUM_HOUR];
	private RelativeLayout listTimeSlotLayout = null;
	private int timeSlotHeight = 80;
	private PopupWindow popupWindow = null;
	private EditText mEtTitle = null;
	private Button mBtChangeStartTime = null, mBtChangeEndTime = null,
			mBtChangeStartDate = null, mBtChangeEndDate = null;
	private AutoCompleteTextView mAutoTvLocation = null;
	private Button mAddButton = null;
	private Button mModifyButton = null;
	private Button mRemoveButton = null;
	private Button mDetailButton = null;
	private View itemBeingSelected = null;
	private Date startTime = null, endTime = null;
	private LinearLayout mLayoutContainer = null;
	private RadioGroup mTypeGroup = null;
	private String location = "";
	private Resources myResource = null;
	private int offX, offY, width, height;

	private Item modifiedItem = null;
	private Bundle mExtras;
	private int mType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popup);

		loadResources();
		setupLayout();
		try {
			setupTimeEditButtonHandler();
			setupAutoTextViewLocation();
			setupButtonHandler();

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}

		/*
		 * auto complete textview - Location
		 */

		switch (mType) {
		case (CalendarDayView.POPUP_TIME_SLOT):
			mModifyButton.setVisibility(View.GONE);
			mRemoveButton.setVisibility(View.GONE);
			break;
		case (CalendarDayView.POPUP_ITEM):
			mAddButton.setVisibility(View.GONE);
			itemId = mExtras.getInt("itemId");
			break;
		}
	}

	private void setupButtonHandler() {
		/*
		 * Controller buttons
		 */

		mAddButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addItem();
				PopupForm.this.finish();
			}
		});
		mModifyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				modifyItem();
				PopupForm.this.finish();
			}
		});
		mRemoveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeItem();
				PopupForm.this.finish();
			}
		});
		mDetailButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				modifyDetails();
			}
		});
	}

	private void setupAutoTextViewLocation() {
		mAutoTvLocation = (AutoCompleteTextView) findViewById(R.id.autoTvLocation);
		// Prepares Atv
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, getResources()
						.getStringArray(R.array.locationArray));
		mAutoTvLocation.setAdapter(adapter);
		mAutoTvLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mAutoTvLocation.showDropDown();
			}
		});
		mAutoTvLocation
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						location = getResources().getStringArray(
								R.array.locationArray)[(int) arg3];
					}
				});
	}

	private void setupTimeEditButtonHandler() {
		updateDisplay();
		mBtChangeStartTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(START_TIME_DIALOG_ID);
			}
		});

		mBtChangeEndTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(END_TIME_DIALOG_ID);
			}
		});
		mBtChangeStartDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(START_DATE_DIALOG_ID);
			}
		});

		mBtChangeEndDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(END_DATE_DIALOG_ID);
			}
		});
	}

	private void setupLayout() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = LEFTMARGIN;// offX;
		params.topMargin = TOPMARGIN;// offY;
		mLayoutContainer.setLayoutParams(params);

	}

	private void loadResources() {

		myResource = getResources();
		mExtras = getIntent().getExtras();
		mType = mExtras.getInt("popupType");
		startTime = (Date) mExtras.get("startTime");
		endTime = (Date) mExtras.get("endTime");
		Log.d(TAG, "time " + startTime.toString() + " " + endTime.toString());

		mLayoutContainer = (LinearLayout) findViewById(R.id.linearpopup);
		mEtTitle = (EditText) findViewById(R.id.etTitle);
		mBtChangeStartTime = (Button) findViewById(R.id.btChangeStartTime);
		mBtChangeEndTime = (Button) findViewById(R.id.btChangeEndTime);
		mBtChangeStartDate = (Button) findViewById(R.id.btChangeStartDate);
		mBtChangeEndDate = (Button) findViewById(R.id.btChangeEndDate);
		mTypeGroup = (RadioGroup) findViewById(R.id.rgType);
		mAddButton = (Button) findViewById(R.id.btAdd);
		mModifyButton = (Button) findViewById(R.id.btModify);
		mRemoveButton = (Button) findViewById(R.id.btRemove);
		mDetailButton = (Button) findViewById(R.id.btDetail);
	}

	private void modifyDetails() {
		String title = mEtTitle.getText().toString();
		Intent data = new Intent();
		if (title.equals("")) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.strAnnounceNotEnoughDetail),
					Toast.LENGTH_SHORT).show();
			return;
		}
		int type = mTypeGroup.getCheckedRadioButtonId();
		if (type == R.id.radio_event)
			type = EVENT;
		else
			type = TASK;
		location = mAutoTvLocation.getText().toString();
		data.putExtra("detailed", false);
		data.putExtra("title", title);
		data.putExtra("type", type);
		data.putExtra("startTime", startTime);
		data.putExtra("endTime", endTime);
		data.putExtra("location", location);
		Intent detailFormActivity = new Intent(getApplicationContext(),
				getClass());
		startActivityForResult(detailFormActivity, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case (RESULT_CANCELED):
			this.setResult(RESULT_CANCELED);
			finish();
			break;
		case (RESULT_OK):
			data.putExtra("detailed", "true");
			this.setResult(RESULT_OK, data);
			finish();
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// updateDisplay();
	}

	private void removeItem() {
		Intent data = new Intent();
		data.putExtra("removed", true);
		this.setResult(RESULT_OK, data);
		finish();
	}

	private void modifyItem() {
		String title = mEtTitle.getText().toString();
		Intent data = new Intent();
		if (title.equals("")) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.strAnnounceNotEnoughDetail),
					Toast.LENGTH_SHORT).show();
			return;
		}
		int type = mTypeGroup.getCheckedRadioButtonId();
		if (type == R.id.radio_event)
			type = EVENT;
		else
			type = TASK;
		location = mAutoTvLocation.getText().toString();

		DatabaseInterface database = DatabaseInterface
				.getDatabaseInterface(getApplicationContext());

		modifiedItem = database.RetrieveItemFromDatabase(
				getApplicationContext(), itemId);
		modifiedItem.SetTitle(title);
		modifiedItem.SetLocation(location);
		if (type == EVENT) {
			modifiedItem.SetStartTime(startTime);
			modifiedItem.SetEndTime(endTime);
		} else {
			modifiedItem.SetDeadline(endTime);
		}
		database.UpdateItemInDatabase(getApplicationContext(), modifiedItem);
		this.finish();
	}

	private void addItem() {
		String title = mEtTitle.getText().toString();
		Intent data = new Intent();
		if (title.equals("")) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.strAnnounceNotEnoughDetail),
					Toast.LENGTH_SHORT).show();
			return;
		}
		int type = mTypeGroup.getCheckedRadioButtonId();
		if (type == R.id.radio_event)
			type = EVENT;
		else
			type = TASK;
		location = mAutoTvLocation.getText().toString();
		DatabaseInterface database = DatabaseInterface
				.getDatabaseInterface(getApplicationContext());
		Item newItem = null;
		if (type == EVENT) {
			newItem = new Item(title, "Event", startTime, endTime, location);
		} else {
			newItem = new Item(title, "Task", endTime, location);
		}
		newItem = database.AddItemToDatabase(getApplicationContext(), newItem);
		if (newItem == null) {
			Log.d(TAG, "got problem in inserting to database");
		} else {
			Log.d(TAG, "item inserted to database");
		}
		Log.d(TAG, "result set");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		width = mLayoutContainer.getWidth();
		height = mLayoutContainer.getHeight();
		Log.d(TAG, "on touch" + event.getX() + " " + event.getY() + " " + offX
				+ " " + offY + " " + width + " " + height);
		float x = event.getX();
		float y = event.getY();
		if ((x < offX || x > offX + width || y < offY || x > offY + height)) {
			setResult(RESULT_CANCELED);
			PopupForm.this.finish();
			return true;
		}
		return false;
	}

	private void updateDisplay() {
		mBtChangeStartTime.setText(TimeFormat
				.getAMPMTimeFormatWithHourPadding(startTime));
		mBtChangeStartDate.setText(DateFormat.format("MMM/dd/yyyy", startTime)
				.toString());
		mBtChangeEndTime.setText(TimeFormat
				.getAMPMTimeFormatWithHourPadding(endTime));
		mBtChangeEndDate.setText(DateFormat.format("MMM/dd/yyyy", endTime)
				.toString());
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private TimePickerDialog.OnTimeSetListener mStartTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			startTime.setHours(hourOfDay);
			startTime.setMinutes(minute);
			updateDisplay();
		}
	};
	private TimePickerDialog.OnTimeSetListener mEndTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			endTime.setHours(hourOfDay);
			endTime.setMinutes(minute);
			updateDisplay();
		}
	};
	private DatePickerDialog.OnDateSetListener mStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			startTime.setYear(year-1900);
			startTime.setMonth(monthOfYear);
			startTime.setDate(dayOfMonth);
			updateDisplay();
		}
	};
	private DatePickerDialog.OnDateSetListener mEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			endTime.setYear(year-1900);
			endTime.setMonth(monthOfYear);
			endTime.setDate(dayOfMonth);
			updateDisplay();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		Log.d(TAG, "on create dialog");
		Log.d(TAG, startTime.getYear() + " "+startTime.getMonth()+" "+startTime.getDate());
		switch (id) {
		case START_TIME_DIALOG_ID:
			return new TimePickerDialog(this, mStartTimeSetListener,
					startTime.getHours(), startTime.getMinutes(), false);
		case END_TIME_DIALOG_ID:
			return new TimePickerDialog(this, mEndTimeSetListener,
					endTime.getHours(), endTime.getMinutes(), false);
		case START_DATE_DIALOG_ID:
			return new DatePickerDialog(this, mStartDateSetListener, 1900+startTime.getYear(), startTime.getMonth(),
					startTime.getDate());
		case END_DATE_DIALOG_ID:
			return new DatePickerDialog(this, mEndDateSetListener,
					1900+endTime.getYear(), endTime.getMonth(), endTime.getDate());
		}
		return null;
	}
}