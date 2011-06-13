package com.android.apptime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class PopupForm extends Activity {
	public static int nextViewId = 1;
	private String TAG = "calendarview";
	private final int CONTEXT_MENU_ADD = 0;
	private final int CONTEXT_MENU_MODIFY = 1;
	private final int CONTEXT_MENU_REMOVE = 2;

	private final int START_TIME_DIALOG_ID = 0;
	private final int END_TIME_DIALOG_ID = 1;
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
	private LinearLayout layout = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup);
		layout = (LinearLayout) findViewById(R.id.linearpopup);
		Bundle extras = getIntent().getExtras();
		int offX = extras.getInt("offX");
		int offY = extras.getInt("offY");
		RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = offX;
		params.topMargin = offY;
		layout.setLayoutParams(params);
		startTime = new TimeSlot();
		startTime.setTime(0, 0);
		endTime = new TimeSlot();
		endTime.setTime(0, 0);
		mEtTitle = (EditText) findViewById(R.id.etTitle);
		mTvStartTime = (TextView) findViewById(R.id.tvDisplayStartTime);
		mTvEndTime = (TextView) findViewById(R.id.tvDisplayEndTime);
		Button changeStartTime = (Button) findViewById(R.id.btChangeStartTime);
		Button changeEndTime = (Button) findViewById(R.id.btChangeEndTime);
		changeStartTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// prepare the alert box
				AlertDialog.Builder alertbox = new AlertDialog.Builder(
						getApplicationContext());

				// set the message to display
				alertbox.setMessage("This is the alertbox!");

				// add a neutral button to the alert box and assign a click
				// listener
				alertbox.setNeutralButton("Ok",
						new DialogInterface.OnClickListener() {

							// click listener on the alert box
							public void onClick(DialogInterface arg0, int arg1) {
								// the button was clicked
								Toast.makeText(getApplicationContext(),
										"OK button clicked", Toast.LENGTH_LONG)
										.show();
							}
						});

				// show it
				alertbox.show();
				// showDialog(START_TIME_DIALOG_ID);
			}
		});

		changeEndTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(END_TIME_DIALOG_ID);
			}
		});

		mAutoTvLocation = (AutoCompleteTextView) findViewById(R.id.autoTvLocation);
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
	}

	private void removeItem() {

	}

	private void modifyItem() {
		// TODO Auto-generated method stub

	}

	private void addItem() {
		// TODO Auto-generated method stub

	}

	private void updateDisplay() {
		mTvStartTime.setText(startTime.getAPPMTimeFormatWithHourPadding());
		mTvEndTime.setText(endTime.getAPPMTimeFormatWithHourPadding());
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private TimePickerDialog.OnTimeSetListener mStartTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			startTime.setTime(hourOfDay, minute);
			updateDisplay();
		}
	};
	private TimePickerDialog.OnTimeSetListener mEndTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			endTime.setTime(hourOfDay, minute);
			updateDisplay();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		Log.d(TAG, "on create dialog");
		switch (id) {
		case START_TIME_DIALOG_ID:
			return new TimePickerDialog(this, mStartTimeSetListener,
					startTime.getHour(), startTime.getMinute(), false);
		case END_TIME_DIALOG_ID:
			return new TimePickerDialog(this, mEndTimeSetListener,
					endTime.getHour(), endTime.getMinute(), false);
		}
		return null;
	}
}