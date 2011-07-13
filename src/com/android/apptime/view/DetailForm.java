package com.android.apptime.view;

import java.util.Date;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
		
		/*EditCompleted = ;
		RgType2 = ;
		RadioEventCompleted = ;
		RadioEventNotCompleted = ;
		
		RelativeEditPart = ;
		EditFormDesc = ;
		EditDescTbox = ;
		
		EditPriority = ;
		BtChangePriority = ;
		
		EditAlertTime = ;
		SnAlertTime = ;
		EditLinearLayout = ;
		BtEditUpdate = ;*/
		/*
		mLayoutContainer = (LinearLayout) findViewById(R.id.linearpopup);
		mEtTitle = (EditText) findViewById(R.id.etTitle);
		mTypeGroup = (RadioGroup) findViewById(R.id.rgType);
		
		mBtChangeStartTime = (Button) findViewById(R.id.btChangeStartTime);
		mBtChangeEndTime = (Button) findViewById(R.id.btChangeEndTime);
		mBtChangeStartDate = (Button) findViewById(R.id.btChangeStartDate);
		mBtChangeEndDate = (Button) findViewById(R.id.btChangeEndDate);
		mAddButton = (Button) findViewById(R.id.btAdd);
		mModifyButton = (Button) findViewById(R.id.btModify);
		mRemoveButton = (Button) findViewById(R.id.btRemove);
		mDetailButton = (Button) findViewById(R.id.btDetail);
		*/
		
		
	}
}
