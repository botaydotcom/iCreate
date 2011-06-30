package com.android.apptime.view;

import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.apptime.DatabaseInterface;
import com.android.apptime.R;

public class PopupForm extends Activity {
	private static final int TASK = 0;
	private static final int EVENT = 1;
	private static final int LEFTMARGIN = 30;
	private static final int TOPMARGIN = 100;
	public static int nextViewId = 1;
	private String TAG = "calendarview";
	private final int CONTEXT_MENU_ADD = 0;
	private final int CONTEXT_MENU_MODIFY = 1;
	private final int CONTEXT_MENU_REMOVE = 2;

	private int itemId = -1;

	private final int START_TIME_DIALOG_ID = 0;
	private final int END_TIME_DIALOG_ID = 1;
	private TimeSlotView[] dayTimeSlot = new TimeSlotView[Constant.NUM_HOUR];
	private RelativeLayout listTimeSlotLayout = null;
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
	private Date startTime = null, endTime = null;
	private LinearLayout layout = null;
	private RadioGroup mTypeGroup = null;
	private String location = "";
	private Resources myResource = null;
	private int offX, offY, width, height;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popup);
		layout = (LinearLayout) findViewById(R.id.linearpopup);
		myResource = getResources();
		offX = (int)myResource.getDimension(R.dimen.left_margin_popup);
		offY = (int)myResource.getDimension(R.dimen.top_margin_popup);
		Bundle extras = getIntent().getExtras();
		int type = extras.getInt("popupType");
		startTime = (Date) extras.get("startTime");
		endTime = (Date) extras.get("endTime");
		
		offX = extras.getInt("offX");
		offY = extras.getInt("offY");
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = LEFTMARGIN;//offX;
		params.topMargin = TOPMARGIN;//offY;
		layout.setLayoutParams(params);
		mEtTitle = (EditText) findViewById(R.id.etTitle);
		mTvStartTime = (TextView) findViewById(R.id.tvDisplayStartTime);
		mTvEndTime = (TextView) findViewById(R.id.tvDisplayEndTime);
		Button changeStartTime = (Button) findViewById(R.id.btChangeStartTime);
		Button changeEndTime = (Button) findViewById(R.id.btChangeEndTime);
		mTypeGroup = (RadioGroup) findViewById(R.id.rgType);

		changeStartTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(START_TIME_DIALOG_ID);
			}
		});

		changeEndTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(END_TIME_DIALOG_ID);
			}
		});

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

		mAddButton = (Button) findViewById(R.id.btAdd);
		mModifyButton = (Button) findViewById(R.id.btModify);
		mRemoveButton = (Button) findViewById(R.id.btRemove);
		mDetailButton = (Button) findViewById(R.id.btDetail);
		mAddButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addItem();
			}
		});
		mModifyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				modifyItem();
			}
		});
		mRemoveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeItem();

			}
		});
		mDetailButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				modifyDetails();

			}
		});
		switch (type) {
		case (CalendarDayView.POPUP_TIME_SLOT):
			mModifyButton.setVisibility(View.GONE);
			mRemoveButton.setVisibility(View.GONE);
			break;
		case (CalendarDayView.POPUP_ITEM):
			mAddButton.setVisibility(View.GONE);
			itemId = extras.getInt("itemId");
			break;
		}
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
		data.putExtra("detailed", false);
		data.putExtra("title", title);
		data.putExtra("type", type);
		data.putExtra("startTime", startTime);
		data.putExtra("endTime", endTime);
		data.putExtra("location", location);
		this.setResult(RESULT_OK, data);
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
		DatabaseInterface database = new DatabaseInterface(getApplicationContext());
		//database.AddItemToDatabase(getApplicationContext(), _item)
		
		data.putExtra("newItem", true);
		data.putExtra("title", title);
		data.putExtra("type", type);
		data.putExtra("startTime", startTime);
		data.putExtra("endTime", endTime);
		data.putExtra("location", location);
		Log.d(TAG, "result set");
		this.setResult(RESULT_OK, data);
		this.finish();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		width = layout.getWidth();
		height = layout.getHeight();
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
		mTvStartTime.setText(TimeFormat
				.getAPPMTimeFormatWithHourPadding(startTime));
		mTvEndTime
				.setText(TimeFormat.getAPPMTimeFormatWithHourPadding(endTime));
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

	@Override
	protected Dialog onCreateDialog(int id) {
		Log.d(TAG, "on create dialog");
		switch (id) {
		case START_TIME_DIALOG_ID:
			return new TimePickerDialog(this, mStartTimeSetListener,
					startTime.getHours(), startTime.getMinutes(), false);
		case END_TIME_DIALOG_ID:
			return new TimePickerDialog(this, mEndTimeSetListener,
					endTime.getHours(), endTime.getMinutes(), false);
		}
		return null;
	}
}