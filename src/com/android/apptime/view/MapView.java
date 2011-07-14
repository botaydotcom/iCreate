package com.android.apptime.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.android.apptime.R;
import com.android.apptime.datahandler.MapData;
import com.android.apptime.datahandler.MapGetAndroidSaxFeedParser;

public class MapView extends Activity {
	String TAG = "map view";
	private FrameLayout container = null;
	private InteractiveImageView imageView = null;
	private ZoomControls mZoomControls = null;
	private List<MapData> listLocation;
	private List<String> listLocationTitle;
	private AutoCompleteTextView autoTextViewLocation = null;
	private TextView mTitle = null;
	private TextView mDescription = null;
	private TextView mLink = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);

		mTitle = (TextView) findViewById(R.id.title);
		mDescription = (TextView) findViewById(R.id.description);
		mLink = (TextView) findViewById(R.id.bookingLink);
		getMapDataFromFile();
		
		imageView = (InteractiveImageView) findViewById(R.id.imageView);
		imageView.setHandler(new Handler());
		mZoomControls = (ZoomControls) findViewById(R.id.zoomButton);
		mZoomControls.setOnZoomInClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageView.zoomIn(1.5f);
			}
		});

		mZoomControls.setOnZoomOutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageView.zoomOut(1.5f);
			}
		});

		imageView.setImage(BitmapFactory.decodeResource(getResources(),
				R.drawable.kentridgemap));

		autoTextViewLocation = (AutoCompleteTextView) findViewById(R.id.autoTvLocation);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(),
				android.R.layout.simple_dropdown_item_1line, listLocationTitle);
		autoTextViewLocation.setAdapter(adapter);
		autoTextViewLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				autoTextViewLocation.showDropDown();
			}
		});
		autoTextViewLocation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				moveToLocation(arg3);				
			}
			
		});
	}

	protected void moveToLocation(long arg3) {
		MapData location = listLocation.get((int) arg3);
		mTitle.setText(location.getTitle());
		mLink .setText(location.getLink());
		Toast.makeText(getApplicationContext(), location.getHorizontal()+" "+location.getVertical(), Toast.LENGTH_LONG).show();
		imageView.scrollToPosition(Integer.parseInt(location.getHorizontal()), Integer.parseInt(location.getVertical()));
	
	}

	private void getMapDataFromFile() {
		MapGetAndroidSaxFeedParser mapParser = new MapGetAndroidSaxFeedParser(
				getApplicationContext().getResources().openRawResource(
						R.raw.buildinglist));
		listLocation = mapParser.parse();
		listLocationTitle = new ArrayList<String>();
		for (int i = 0; i <listLocation.size(); i++)
			listLocationTitle.add(listLocation.get(i).getTitle());
		Log.d(TAG, "finish parsing");
	}

	@Override
	public void onResume() {
		super.onResume();
		// imageView.zoomIn(2.0f);
	}
}

