package com.android.apptime.view;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;

import com.android.apptime.R;

public class MapView extends Activity{
	private FrameLayout container = null;
	private InteractiveImageView imageView = null;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
//		container = (FrameLayout) findViewById(R.id.container);
		imageView = (InteractiveImageView) findViewById(R.id.imageView);
		imageView.setHandler(new Handler());
//		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);
//		imageView.setLayoutParams(params);
		imageView.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.kentridgemap));
//		container.addView(imageView);
	}
	@Override
	public void onResume(){
		super.onResume();
		imageView.zoomIn(2.0f);
	}
}
