package com.android.apptime.view;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ViewSwitcher;

/**
 * A viewgroup that can be zoomed in/out when users pinch.
 * 
 * @author Tran Cong Hoang
 * 
 */

	
public class ZoomableViewGroup extends ViewGroup {
	private static final String TAG = "zoomable view group";
	private Activity activity;
	private int screenHeight, screenWidth;
	private int viewHeight, viewWidth;
	private float scale = 1f;
	private float maxScale = 3f, minScale = 1f;
	
	ViewInfo thisViewInfo = null;
	
	private class ViewInfo {
		int width=0, height=0, top=0, left=0;
	}
	
	public float getMaxScale(){
		return maxScale;
	}
	public float getMinScale(){
		return minScale;
	}
	public void setMaxScale(float scale){
		maxScale = scale;
	}
	public void setMinScale(float scale){
		minScale = scale;
	}
	
	private ArrayList<ViewInfo> childrenInfoList  = null;

	public void getDisplayMetrics() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		screenHeight = displaymetrics.heightPixels;
		screenWidth = displaymetrics.widthPixels;
	}
	
	public ZoomableViewGroup(Activity context) {
		super(context);
		init(context);
	}

	public ZoomableViewGroup(Activity context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ZoomableViewGroup(Activity context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}
	private void init(Activity context) {
		activity = context;
		childrenInfoList = new ArrayList<ZoomableViewGroup.ViewInfo>();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	}

	@Override 
	public void removeView (View view){
		int id = view.getId();
		if (childrenInfoList.size()<id || childrenInfoList.get(id)==null) return;
		childrenInfoList.set(id, null);
		super.removeView(view);
	}
	
	@Override 
	public void removeViewAt (int id){
		if (childrenInfoList.size()<id || childrenInfoList.get(id)==null) return;
		childrenInfoList.set(id, null);
		super.removeViewAt(id);
	}
	
	@Override
	public void addView (View child, int id){
		super.addView(child, id);
		setInfoForChild(child, id);
	}
	
	
	
	@Override
	public void addView (View child){
		super.addView(child);
		int id = child.getId();
		if (id==NO_ID) id = getChildCount()-1;
		setInfoForChild(child, id);
	}
	
	private void setInfoForChild(View child, int id){
		int width = child.getMeasuredWidth();
		int height = child.getMeasuredHeight();
		int left = child.getLeft();
		int top = child.getTop();
		if (childrenInfoList.size()<=id){
			for (int i = childrenInfoList.size(); i<id; i++)
				childrenInfoList.add(new ViewInfo());
		}
		if (childrenInfoList.get(id)!=null){
			childrenInfoList.set(id, new ViewInfo());
		}
		childrenInfoList.get(id).width = width;
		childrenInfoList.get(id).height = height;
		childrenInfoList.get(id).left = left;
		childrenInfoList.get(id).top = top;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (thisViewInfo == null){
			thisViewInfo = new ViewInfo();
			thisViewInfo.left = l;
			thisViewInfo.top = t;
			thisViewInfo.width = r-l;
			thisViewInfo.height = b-r;
		}
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				int childOriginWidth = childrenInfoList.get(i).width;
				int childOriginHeight = childrenInfoList.get(i).height;
				int childOriginTop = childrenInfoList.get(i).top;
				int childOriginLeft = childrenInfoList.get(i).left;
				int newChildWidth = (int)(childOriginWidth * scale);
				int newChildHeight = (int)(childOriginHeight * scale);
				int newChildTop = (int)(childOriginTop * scale);
				int newChildLeft = (int)(childOriginLeft * scale);
				
				child.layout(newChildLeft, newChildTop, newChildLeft+newChildWidth, newChildTop+newChildHeight);
			}
		}
	}
	
	public void zoom(float deltaScale){
		zoomToScale(scale*deltaScale);
	}
	
	public void zoomToScale(float scale){
		if (scale<minScale || scale>maxScale) return;
		this.scale = scale;
		int l = (int)(thisViewInfo.left*scale);
		int t = (int)(thisViewInfo.top*scale);
		int w = (int)(thisViewInfo.width*scale);
		int h = (int)(thisViewInfo.height*scale);
		this.layout(l, t, l+w, t+h);
		this.invalidate();
	}

}