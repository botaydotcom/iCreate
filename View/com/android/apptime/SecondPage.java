//package com.android.apptime;
//
//import java.util.ArrayList;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//public class SecondPage extends Activity {
//    /** Called when the activity is first created. */
//	private ListView mLvApplication;
//	private ArrayList<TimeSlot>  mApplicationList;
//	private DataHandler mDataHandler;
//	private DataUpdateReceiver mDataUpdateReceiver;
//	private TextView mTvTitle;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main);
//		Intent intent = getIntent();
//		Bundle extras = intent.getExtras();
//		int catId = extras.getInt("Category");
//		String title = extras.getString("CategoryTitle");
//		mTvTitle = (TextView) findViewById(R.id.tvTitle);
//		mTvTitle.setText(title);
//		Log.d("app aggregator", ""+catId);
//		mLvApplication = (ListView) findViewById(R.id.lvListApp);
//		try{
//			mDataHandler = new DataHandler(getApplicationContext());
//			mApplicationList = mDataHandler.getApplicationList(catId);
//			Log.d("app aggregator", "number of application in list" + mApplicationList.size());
//			ArrayAdapter<TimeSlot> aa = new CalendarDayAdapter(this, R.layout.app_list_item, mApplicationList);
//			mLvApplication.setAdapter(aa);
//			
//		}
//		catch (Exception e)
//		{
//			Log.d("app aggregator", "got exception "+ e.getMessage());
//		}
//		
//		mDataUpdateReceiver = new DataUpdateReceiver();
//		
//		mLvApplication.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				String url = getString(R.string.applicationUrlFormat);
//				url = String.format(url, mApplicationList.get((int)arg3).getUrl());
//				Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//				startActivity(intent);				
//			}			
//		});	
//	}
//    @Override
//    public void onResume(){
//    	Log.d("second page", getSharedPreferences("AppAggregator", 0).getString("JString", ""));
//    	registerReceiver(mDataUpdateReceiver, new IntentFilter(DataHandler.NEW_DATA_RECEIVED));
//    	super.onResume();
//    }
//    @Override
//    public void onPause(){
//    	unregisterReceiver(mDataUpdateReceiver);
//    	super.onPause();
//    }
//    
//    private class DataUpdateReceiver extends BroadcastReceiver{
//		@Override
//		public void onReceive(Context contextArg, Intent arg1) {
//			final Context context = contextArg;
//	    	AlertDialog.Builder builder = new AlertDialog.Builder(SecondPage.this);
//			builder.setMessage("The contents have been updated and needs to be restarted. Restart now?")
//			       .setCancelable(false)
//			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//			           public void onClick(DialogInterface dialog, int id) {
//			        	   Intent i = new Intent(context, SecondPage.class);
//			        	   SecondPage.this.finish();
//			        	   context.startActivity(i);			        	   
//			           }
//			       })
//			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
//			           public void onClick(DialogInterface dialog, int id) {
//			                dialog.cancel();
//			           }
//			       }).create();
//			
//		}
//		
//    }
//}