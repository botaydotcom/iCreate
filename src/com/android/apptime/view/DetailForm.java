package com.android.apptime.view;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.apptime.DatabaseInterface;
import com.android.apptime.Item;
import com.android.apptime.R;

public class DetailForm extends Activity{

	private ScrollView EditDetails = null;
	private LinearLayout MLinearLayout = null;
	private TextView EditFormTitle = null;
	
	private RelativeLayout RelativePopUpPart = null;
	private TextView TvTitle3 = null;
	private EditText EtTitle3 = null;
	
	private TextView TvType3 = null;
	private RadioGroup RgType3 = null;
	private RadioButton RadioEvent3 = null;
	private RadioButton RadioTask3 = null;
	
	private TextView TvStartTime3 = null;
	private Button BtChangeStartTime3 = null;
	private Button BtChangeStartDate3 = null;
	
	private TextView TvEndTime3 = null;
	private Button BtChangeEndTime3 = null;
	private Button BtChangeEndDate3 = null;
	
	private TextView TvLocation = null;
	private AutoCompleteTextView AutoTvLocation = null;
	
	private TextView EditCompleted = null;
	private RadioGroup RgType2 = null;
	private RadioButton RadioEventCompleted = null;
	private RadioButton RadioEventNotCompleted = null;
	
	private RelativeLayout RelativeEditPart = null;
	private TextView EditFormDesc = null;
	private EditText EditDescTbox = null;
	
	private TextView EditPriority = null;
	private Spinner BtChangePriority = null;
	
	private TextView EditAlertTime = null;
	private Spinner SnAlertTime = null;
	
	private LinearLayout EditLinearLayout = null;
	private Button BtEditUpdate = null;
	
	private Resources myResource = null;
	private Bundle mExtras;
	private int mType;
	
	private Date startTime = null,
				endTime = null;
	private String TAG = "EDIT_DETAILS";
	
	private static final int LEFTMARGIN = 30;
	private static final int TOPMARGIN = 100;
	
	private final int START_TIME_DIALOG_ID = 0;
	private final int END_TIME_DIALOG_ID = 1;
	protected static final int START_DATE_DIALOG_ID = 2;
	protected static final int END_DATE_DIALOG_ID = 3;
	
	private String location = "";
	
	private static final int TASK = 0;
	private static final int EVENT = 1;
	
	private int itemId = -1;
		
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
			//mModifyButton.setVisibility(View.GONE);
			//mRemoveButton.setVisibility(View.GONE);
			break;
		case (CalendarDayView.POPUP_ITEM):
			BtEditUpdate.setVisibility(View.GONE);
			itemId = mExtras.getInt("itemId");
			break;
		}
	}
	
	private void loadResources(){
		myResource = getResources();
		mExtras = getIntent().getExtras();
		mType = mExtras.getInt("popupType");
		
		startTime = (Date) mExtras.get("startTime");
		endTime = (Date) mExtras.get("endTime");
		
		Log.d(TAG, "time " + startTime.toString() + " " + endTime.toString());

		EditDetails = (ScrollView)findViewById(R.id.editdetails);
		MLinearLayout = (LinearLayout)findViewById(R.id.mLinearLayout);
		 EditFormTitle = (TextView)findViewById(R.id.editFormTitle);
		
		RelativePopUpPart = (RelativeLayout)findViewById(R.id.RelativePopUpPart);
		TvTitle3 = (TextView)findViewById(R.id.tvTitle3);
		EtTitle3 = (EditText)findViewById(R.id.etTitle3);
		
		TvType3 = (TextView)findViewById(R.id.tvType3);
		RgType3 = (RadioGroup)findViewById(R.id.rgType3);
		RadioEvent3 = (RadioButton)findViewById(R.id.radio_event3);
		RadioTask3 = (RadioButton)findViewById(R.id.radio_task3);
		
		TvStartTime3 = (TextView)findViewById(R.id.tvStartTime3);
		BtChangeStartTime3 = (Button)findViewById(R.id.btChangeStartTime3);
		BtChangeStartDate3 = (Button)findViewById(R.id.btChangeStartDate3);
		
		TvEndTime3 = (TextView)findViewById(R.id.tvEndTime3);
		BtChangeEndTime3 = (Button)findViewById(R.id.btChangeEndTime3);
		BtChangeEndDate3 = (Button)findViewById(R.id.btChangeEndDate3);
		
		TvLocation = (TextView)findViewById(R.id.tvLocation);
		AutoTvLocation = (AutoCompleteTextView)findViewById(R.id.autoTvLocation);
		
		EditCompleted = (TextView)findViewById(R.id.editCompleted);
		RgType2 = (RadioGroup)findViewById(R.id.rgType2);
		RadioEventCompleted = (RadioButton)findViewById(R.id.radio_event_completed);
		RadioEventNotCompleted = (RadioButton)findViewById(R.id.radio_event_notcompleted);
		
		RelativeEditPart = (RelativeLayout)findViewById(R.id.RelativeEditPart);
		EditFormDesc = (TextView)findViewById(R.id.editFormDesc);
		EditDescTbox = (EditText)findViewById(R.id.editDescTbox);
		
		EditPriority = (TextView)findViewById(R.id.editPriority);
		BtChangePriority = (Spinner)findViewById(R.id.btChangePriority);
		
		EditAlertTime = (TextView)findViewById(R.id.editAlertTime);
		SnAlertTime = (Spinner)findViewById(R.id.snAlertTime);
		EditLinearLayout = (LinearLayout)findViewById(R.id.editLinearLayout);
		BtEditUpdate = (Button)findViewById(R.id.btEditUpdate);
	}
	
	private void setupLayout(){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = LEFTMARGIN;
		params.topMargin = TOPMARGIN;
		MLinearLayout.setLayoutParams(params);
	}
	
	private void setupTimeEditButtonHandler() {
		updateDisplay();
		
		BtChangeStartTime3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(START_TIME_DIALOG_ID);
			}
		});

		BtChangeEndTime3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(END_TIME_DIALOG_ID);
			}
		});
		
		BtChangeStartDate3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(START_DATE_DIALOG_ID);
			}
		});

		BtChangeEndDate3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(END_DATE_DIALOG_ID);
			}
		});
	}
	
	private void updateDisplay() {
		BtChangeStartTime3.setText(TimeFormat
				.getAMPMTimeFormatWithHourPadding(startTime));
		BtChangeStartDate3.setText(DateFormat.format("MMM/dd/yyyy", startTime)
				.toString());
		BtChangeEndTime3.setText(TimeFormat
				.getAMPMTimeFormatWithHourPadding(endTime));
		BtChangeEndDate3.setText(DateFormat.format("MMM/dd/yyyy", endTime)
				.toString());
	}
	
	private void setupAutoTextViewLocation() {
		AutoTvLocation = (AutoCompleteTextView) findViewById(R.id.autoTvLocation);
		// Prepares Atv
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line,
				getResources().getStringArray(R.array.locationArray));
		AutoTvLocation.setAdapter(adapter);
		AutoTvLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AutoTvLocation.showDropDown();
			}
			
		});
		AutoTvLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					location = getResources().getStringArray(R.array.locationArray)[(int) arg3];
			}
		});
	}
	
	private void setupButtonHandler() {
		/*
		 * Controller buttons
		 */

		BtEditUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addItem();
				DetailForm.this.finish();
			}
		});
	}
	
	private void addItem() {
		String title = EtTitle3.getText().toString();
		String desc = EditDescTbox.getText().toString();
		String prio = BtChangePriority.toString();
		String alertTime = SnAlertTime.toString();
		
		Intent data = new Intent();
		
		if (title.equals("")) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.strAnnounceNotEnoughDetail),
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		int type = RgType3.getCheckedRadioButtonId();
		
		if (type == R.id.radio_event)
			type = EVENT;
		else
			type = TASK;
		
		int completed = RgType2.getCheckedRadioButtonId();
		String completedParam;
		
		if (completed == R.id.radio_event_completed)
			completedParam = "YES";
		else
			completedParam = "NO";
		
		location = AutoTvLocation.getText().toString();
		DatabaseInterface database = DatabaseInterface.getDatabaseInterface(getApplicationContext());
		Item newItem = null;
		
		/* Double-check requirements */
		// title, type, from time, to time, location, completed, desc, prio, alert
		if (type == EVENT) {
			newItem = new Item(title, desc, location, null, null, prio, "Event", startTime, endTime, null, alertTime, null, completedParam, 0);
		} else {
			//newItem = new Item(title, desc, location, null, null, prio, "Task", null, null, , alertTime, null, completedParam);
		}
		
		newItem = database.AddItemToDatabase(getApplicationContext(), newItem);
		
		if (newItem == null) {
			Log.d(TAG, "got problem in inserting to database");
		} else {
			Log.d(TAG, "item inserted to database");
		}
		Log.d(TAG, "result set");
	}
}