package com.android.apptime.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

/**
 * A viewgroup that can be zoomed in/out when users pinch.
 * 
 * @author Tran Cong Hoang
 * 
 */

public class ZoomableViewGroup extends ViewGroup {

	/**
	 * Call back when the view is zooming
	 * 
	 * @author Tran Cong Hoang
	 * 
	 */
	public interface OnZoomListener {
		public void onZoomWithScale(float zoomScale);
	}

	private OnZoomListener mOnZoomListener = null;

	public void setOnZoomListener(OnZoomListener onZoomListener) {
		mOnZoomListener = onZoomListener;
	}

	/**
	 * Call back when the view is scrolling
	 * 
	 * @author Tran Cong Hoang
	 * 
	 */

	public interface OnTouchListener {
		public void onTouchAt(int dx, int dy);
	}

	private OnTouchListener mOnTouchListener = null;

	
	public interface OnScrollListener {
		public void onScrollBy(int dx, int dy);
	}

	private OnScrollListener mOnScrollListener = null;

	public void setOnScrollListener(OnScrollListener onScrollListener) {
		mOnScrollListener = onScrollListener;
	}

	private static final String TAG = "touch";
	protected static final float DEFAULT_ZOOM_IN_RATE = 1.2f;
	protected static final float DEFAULT_ZOOM_OUT_RATE = 0.8f;
	private float scale = 1f;
	private float maxScale = 3f, minScale = 1f;

	ViewInfo thisViewInfo = null;

	private class ViewInfo {
		int width = 0, height = 0, top = 0, left = 0;
	}

	public float getMaxScale() {
		return maxScale;
	}

	public float getMinScale() {
		return minScale;
	}

	public void setMaxScale(float scale) {
		maxScale = scale;
	}

	public void setMinScale(float scale) {
		minScale = scale;
	}

	private ArrayList<ViewInfo> childrenInfoList = null;
	private float deltaScale = 1f;
	private boolean mIsBeingDragged = false;
	private PointF lastPoint;
	private int mTouchSlop = 5;
	private int mScrollX, mScrollY, mMaxScrollX, mMaxScrollY;
	public int screenWidth, screenHeight, viewWidth, viewHeight;

	public ZoomableViewGroup(Context context) {
		super(context);
		Log.d(TAG, "constructor 1 ");
		init(context);
	}

	public ZoomableViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d(TAG, "constructor 2 ");
		init(context);
	}

	public ZoomableViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.d(TAG, "constructor 3 ");
		init(context);
	}

	private void init(Context context) {
		Log.d(TAG, "init");
		this.setBackgroundDrawable(new BitmapDrawable());
		lastPoint = new PointF();
	}

	@Override
	protected int computeVerticalScrollRange() {
		return viewHeight;
	}

	@Override
	protected int computeHorizontalScrollRange() {
		return viewWidth;
	}

	@Override
	protected int computeVerticalScrollExtent() {
		return screenHeight;
	}

	@Override
	protected int computeHorizontalScrollExtent() {
		return screenWidth;
	}

	@Override
	protected int computeVerticalScrollOffset() {
		return mScrollY;
	}

	@Override
	protected int computeHorizontalScrollOffset() {
		return mScrollX;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.d("scroll", "on touch intercept" + ev.getAction() + " being drag"
				+ mIsBeingDragged);
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
			Log.d("scroll", "on touch intercept - action move - drag");
			lastPoint.set(ev.getX(), ev.getY());
			return true;
		}

		if (!canScroll()) {
			mIsBeingDragged = false;
			Log.d("scroll",
					"on touch intercept - cannot scroll, so return false");
			lastPoint.set(ev.getX(), ev.getY());
			return false;
		}
		final float x = ev.getX();
		final float y = ev.getY();
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			Log.d("scroll", "on touch intercept - action move");

			final int diff = (int) Math.hypot(x - lastPoint.x, y - lastPoint.y);
			if (diff > mTouchSlop) {
				mIsBeingDragged = true;
				Log.d("scroll", "being dragged");
			}
			break;

		// case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			/* Release the drag */
			mIsBeingDragged = false;
			Log.d("scroll", "on touch intercept - not drag");
			break;
		}

		/*
		 * The only time we want to intercept motion events is if we are in the
		 * drag mode.
		 */
		lastPoint.set(ev.getX(), ev.getY());
		return mIsBeingDragged;
	}

	private boolean canScroll() {
		Log.d("scroll", "extent - range" + computeHorizontalScrollExtent()
				+ " " + computeHorizontalScrollRange() + " "
				+ computeVerticalScrollExtent() + " "
				+ computeVerticalScrollRange() + "offset: "
				+ computeHorizontalScrollOffset() + " "
				+ computeVerticalScrollOffset());
		return (computeHorizontalScrollExtent() < computeHorizontalScrollRange() || computeVerticalScrollExtent() < computeVerticalScrollRange());
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.d("scroll", "on touch event " + ev.getAction());
		if (ev.getAction() == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0) {
			// Don't handle edge touches immediately -- they may actually belong
			// to one of our
			// descendants.
			Log.d("scroll", "dispatch click to children");
			return false;
		}

		if (!canScroll()) {
			return false;
		}

		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.d("scroll", "on touch event - action down in scrollview");
			lastPoint.set(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			Log.d("scroll", "on touch event - action move in scrollview");
			// Scroll to follow the motion event
			int deltaX = (int) (x - lastPoint.x);
			int deltaY = (int) (y - lastPoint.y);
			lastPoint.set(x, y);
			Log.d("scroll", "delta X, deltaY" + deltaX + " " + deltaY);
			if (deltaX > 0) {
				if (mScrollX > 0) {
					deltaX = Math.min(deltaX, mScrollX);
				} else
					deltaX = 0;
			} else if (deltaX < 0) {
				if (mScrollX < computeHorizontalScrollRange()
						- computeHorizontalScrollExtent()) {
					deltaX = Math
							.max(deltaX,
									mScrollX
											- (computeHorizontalScrollRange() - computeHorizontalScrollExtent()));
				} else
					deltaX = 0;
			}
			if (deltaY > 0) {
				if (mScrollY > 0) {
					deltaY = Math.min(deltaY, mScrollY);
				} else
					deltaY = 0;
			} else if (deltaY < 0) {
				if (mScrollY < computeVerticalScrollRange()
						- computeVerticalScrollExtent()) {
					deltaY = Math
							.max(deltaY,
									mScrollY
											- (computeVerticalScrollRange() - computeVerticalScrollExtent()));
				} else
					deltaY = 0;
			}
			Log.d("scroll", "delta X, deltaY" + deltaX + " " + deltaY);
			mScrollX -= deltaX;
			mScrollY -= deltaY;
			Log.d("scroll", "scrollBy " + deltaX + " " + deltaY);
			scrollTo(mScrollX, mScrollY);
			// dispatch event to observer
			if (mOnScrollListener != null)
				mOnScrollListener.onScrollBy(-deltaX, -deltaY);
			// this.invalidate(0, 0, getMeasuredWidth(), getMeasuredHeight());
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
		default:
			mIsBeingDragged = false;
			lastPoint.set(ev.getX(), ev.getY());
			return false;
		}
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		try {
			final int width = MeasureSpec.getSize(widthMeasureSpec);
			final int height = MeasureSpec.getSize(heightMeasureSpec);
			setMeasuredDimension(width, height);
			Log.d(TAG, "the size of this view is " + width + " "
					+ height);
			// viewWidth = MeasureSpec.getSize(widthMeasureSpec);
			// viewHeight = MeasureSpec.getSize(heightMeasureSpec);
			screenWidth = MeasureSpec.getSize(widthMeasureSpec);
			screenHeight = MeasureSpec.getSize(heightMeasureSpec);
			Log.d(TAG, "on measure " + viewWidth + " " + viewHeight);
		} catch (Exception e) {
			Log.e("calendarview", e.getMessage());
		}
	}

	// @Override
	// public void removeView(View view) {
	// int id = view.getId();
	// if (childrenInfoList.size() < id || childrenInfoList.get(id) == null)
	// return;
	// childrenInfoList.set(id, null);
	// Log.d(TAG, "remove view id " + id);
	// super.removeView(view);
	// }
	//
	// @Override
	// public void removeViewAt(int id) {
	// if (childrenInfoList.size() < id || childrenInfoList.get(id) == null)
	// return;
	// childrenInfoList.set(id, null);
	// Log.d(TAG, "remove view id " + id);
	// super.removeViewAt(id);
	// }
	//
	// @Override
	// public void addView(View child, int id) {
	// super.addView(child, id);
	// Log.d(TAG, "id " + id);
	// if (id == NO_ID)
	// id = getChildCount();
	// Log.d(TAG, "add view id " + id);
	// }
	//
	// @Override
	// public void addView(View child) {
	// super.addView(child);
	// int id = child.getId();
	// Log.d(TAG, "id " + id);
	// if (id == NO_ID)
	// id = getChildCount();
	// Log.d(TAG, "add view id " + id);
	// }
	//
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				final int childLeft = child.getLeft();
				final int childTop = child.getTop();
				final int childWidth = child.getMeasuredWidth();
				final int childHeight = child.getMeasuredHeight();
				viewWidth = viewWidth > child.getRight() ? viewWidth : child
						.getRight();
				viewHeight = viewHeight > child.getBottom() ? viewHeight
						: child.getBottom();
				Log.d(TAG, "child left top child width child height "
						+ childLeft + " " + childTop + " " + childWidth + " "
						+ childHeight);
				Log.d(TAG, "view width, view height " + viewWidth + " "
						+ viewHeight + " screen width, screen height "
						+ screenWidth + " " + screenHeight);
			}
		}

	}

	public void zoom(float deltaScale) {
		Log.d(TAG, "zoom with delta scale = " + deltaScale);
		float tempScale = deltaScale * this.scale;
		if (tempScale < minScale)
			tempScale = minScale;
		if (tempScale > maxScale)
			tempScale = maxScale;
		deltaScale = tempScale / this.scale;
		this.deltaScale = deltaScale;
		Log.d(TAG, "zoom to scale " + tempScale + " delta scale " + deltaScale);

		this.scale = tempScale;
		// int maxRight = 0, maxBottom = 0;
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			int id = view.getId();
			int cw = (int) (view.getWidth() * deltaScale);
			int ch = (int) (view.getHeight() * deltaScale);
			int cl = (int) (view.getLeft() * deltaScale);
			int ct = (int) (view.getTop() * deltaScale);
			if (id < 0)
				continue;
			Log.d(TAG, "on zoom, child " + id + " " + cl + " " + ct + " " + cw
					+ " " + ch);
			int ecw = View.MeasureSpec.makeMeasureSpec(cw,
					View.MeasureSpec.EXACTLY);
			int ech = View.MeasureSpec.makeMeasureSpec(ch,
					View.MeasureSpec.EXACTLY);
			view.measure(ecw, ech);
			view.layout(cl, ct, cl + cw, ct + ch);
			view.invalidate(cl, ct, cl + cw, ct + ch);
			// if (cl+cw>maxRight) maxRight = cl+cw;
			// if (ct+ch>maxBottom) maxBottom = ct+ch;
		}
		viewWidth = (int) (viewWidth * deltaScale);
		viewHeight = (int) (viewHeight * deltaScale);
		mScrollX = (int) (mScrollX * deltaScale);
		mScrollY = (int) (mScrollY * deltaScale);
		scrollTo(mScrollX, mScrollY);
		if (mOnZoomListener != null)
			mOnZoomListener.onZoomWithScale(deltaScale);
	}

	public void zoomToScale(float scale) {
		zoom(scale / this.scale);
	}

	// @Override
	// protected void onDraw(Canvas canvas) {
	// canvas.save(Canvas.MATRIX_SAVE_FLAG);
	// canvas.scale(scale, scale);
	// canvas.restore();
	// super.onDraw(canvas);
	// }

}