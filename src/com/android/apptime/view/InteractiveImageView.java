package com.android.apptime.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class InteractiveImageView extends ImageView {

	private static final float ZOOM_SPEED = 1.0f;
	private static final String TAG = "Touch";
	// These matrices will be used to move and zoom image
	private Matrix originalMatrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private Bitmap image;

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	private float scale = 1.0f;
	private float maxScale = 1.5f;
	private float minScale = 0.3f;
	private int savedwidth, savedheight, viewPortWidth, viewPortHeight, vWidth,
			vHeight;

	private PointF start = new PointF();
	private PointF mid = new PointF();
	private Context context;
	private float oldScale = 1f, netScale = 1f;
	private float oldDist = 1f;
	private float deltaX = 0, deltaY = 0;
	private int centerX = 0, centerY = 0;

	private Handler mHandler = null;
	private RectF rect = null;
	private Rect viewPort = null;
	private Rect frame;
	private Paint paint;

	public InteractiveImageView(Context context) {

		super(context);
		init();
	}

	public InteractiveImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public InteractiveImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {
		mHandler = new Handler();
		rect = new RectF();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	// width of the view depending of you set in the layout
	@Override
	public int computeHorizontalScrollExtent() {
		// Log.d("compute", "horizontal scroll extent "+vWidth);
		return vWidth;
	}

	// width of the webpage depending of the zoom
	@Override
	public int computeHorizontalScrollRange() {
		// Log.d("compute", "horizontal scroll range "+width);
		return viewPortWidth;
	}

	// width of the view depending of you set in the layout
	@Override
	public int computeVerticalScrollExtent() {
		// Log.d("compute", "vertical scroll extent "+vHeight);
		return vHeight;
	}

	// width of the webpage depending of the zoom
	@Override
	public int computeVerticalScrollRange() {
		// Log.d("compute", "vertical scroll range"+height);
		return viewPortHeight;
	}

	// position of the left side of the horizontal scrollbar
	@Override
	public int computeHorizontalScrollOffset() {
		RectF rect = new RectF(0, 0, image.getWidth(), image.getHeight());
		getImageMatrix().mapRect(rect);
		// Log.d("compute", "rect "+rect.toString());
		// Log.d("compute", "horizontal scroll offset "+rect.left);
		return (int) -rect.left;
	}

	@Override
	public int computeVerticalScrollOffset() {
		RectF rect = new RectF(0, 0, image.getWidth(), image.getHeight());
		getImageMatrix().mapRect(rect);
		// Log.d("compute", "vertical scroll offset "+rect.top);
		return (int) -rect.top;
	}

	private int overScrollTop() {
		if (computeVerticalScrollOffset() > 0)
			return computeVerticalScrollOffset() - 0;
		else
			return 0;
	}

	private int overScrollBottom() {
		if (computeVerticalScrollOffset() + computeVerticalScrollRange() < computeVerticalScrollExtent())
			return computeVerticalScrollOffset() + computeVerticalScrollRange()
					- computeVerticalScrollExtent();
		else
			return 0;
	}

	private int overScrollLeft() {
		if (computeHorizontalScrollOffset() > 0)
			return computeHorizontalScrollOffset() - 0;
		else
			return 0;
	}

	private int overScrollRight() {
		if (computeHorizontalScrollOffset() + computeHorizontalScrollRange() < computeHorizontalScrollExtent())
			return computeHorizontalScrollOffset()
					+ computeHorizontalScrollRange()
					- computeHorizontalScrollExtent();
		else
			return 0;
	}

	public float getScale() {
		return netScale;
	}

	public void init(Context context) {
		super.setClickable(true);
		this.context = context;
		setScaleType(ScaleType.MATRIX);
		viewPort = new Rect();
		frame = new Rect();
	}

	public void zoomOut(float deltaScale) {
		Log.d(TAG, "comhere");
		float newNetScale = netScale / deltaScale;
		if (newNetScale <= minScale){
			deltaScale = netScale/minScale;
			newNetScale = minScale;
		}
		netScale = newNetScale;
		viewPortWidth = (int) (viewPortWidth * deltaScale);
		viewPortHeight = (int) (viewPortHeight * deltaScale);
		updateView();
	}

	public void zoomIn(float deltaScale) {
		Log.d(TAG, "comhere");
		float newNetScale = netScale * deltaScale;
		if (newNetScale >= maxScale) {
			deltaScale = maxScale/netScale;
			newNetScale = maxScale;
		}
		netScale = newNetScale;
		viewPortWidth = (int) (viewPortWidth / deltaScale);
		viewPortHeight = (int) (viewPortHeight / deltaScale);
		updateView();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Log.d("Touch",
		// "on touch - current matrix:"+getImageMatrix().toString());
		deltaX = 0;
		deltaY = 0;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mode = DRAG;
			start.set(event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_UP:
			// int xDiff = (int) Math.abs(event.getX() - start.x);
			// int yDiff = (int) Math.abs(event.getY() - start.y);
			// if (xDiff < 15 && yDiff < 15) {
			// performClick();
			// }
			mode = NONE;
			// //Log.d(TAG, "mode=NONE");
			break;

		case MotionEvent.ACTION_MOVE:
			// //Log.d(TAG, "action move");
			if (mode == DRAG) {
				deltaX = event.getX() - start.x;
				deltaY = event.getY() - start.y;
				centerX -= deltaX;
				centerY -= deltaY;
				Log.d(TAG, centerX + " " + centerY);
				savedMatrix.postTranslate(deltaX, deltaY);
				start.set(event.getX(), event.getY());
			}
			break;
		}
		updateView();

		return true; // indicate event was handled
	}

	protected void setCenter() {
		if (image == null) {
			return;
		}
		centerX = image.getWidth() / 2;
		centerY = image.getHeight() / 2;
	}

	private Runnable updateUI = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// Log.d(TAG,
			// "update view: scale = " + scale + " "
			// + savedMatrix.toShortString());
			// Matrix matrix = getImageMatrix();
			// matrix.set(savedMatrix);
			// setCenter();
			// Log.d(TAG, "after checking " + getImageMatrix().toShortString());
			invalidate();
		}
	};

	private void updateView() {
		// adjust the view
		if (centerX - viewPortWidth / 2 < 0)
			centerX = viewPortWidth / 2;
		if (centerX + viewPortWidth / 2 > image.getWidth())
			centerX = image.getWidth() - viewPortWidth / 2;
		if (centerY - viewPortHeight / 2 < 0)
			centerY = viewPortHeight / 2;
		if (centerY + viewPortHeight / 2 > image.getHeight())
			centerY = image.getHeight() - viewPortHeight / 2;
		Log.d(TAG, centerX + " " + centerY + " " + viewPortWidth + " "
				+ viewPortHeight + " " + frame.toString());
		mHandler.removeCallbacks(updateUI);
		mHandler.post(updateUI);

	}

	protected boolean fullSizeLoaded = false;

	public void setImage(Bitmap newBitmap) {
		// //Log.d(TAG, "loaded");
		if (image == null) {
			image = newBitmap;
			rect = new RectF(0, 0, image.getWidth(), image.getHeight());

			super.setImageBitmap(newBitmap);
			setCenter();
		}

	}

	@Override
	public void onSizeChanged(int displayWidth, int displayHeight, int s, int d) {
		super.onSizeChanged(displayWidth, displayHeight, s, d);
		vWidth = displayWidth;
		vHeight = displayHeight;
		center();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		vWidth = MeasureSpec.getSize(widthMeasureSpec);
		vHeight = MeasureSpec.getSize(widthMeasureSpec);
		frame = new Rect(0, 0, vWidth, vHeight);
	}

	private void center() {
		viewPortWidth = image.getWidth();
		viewPortHeight = image.getHeight();
		Log.d(TAG, "on center size of view:" + vWidth + " " + vHeight
				+ " size of image: " + viewPortWidth + " " + viewPortHeight);
		float sc = 1f;
		if (vWidth != 0 && vHeight != 0) {
			if (1.0 * vWidth / viewPortWidth < 1.0 * vHeight / viewPortHeight) {
				sc = 1f * vWidth / viewPortWidth;
				viewPortHeight = (int) (viewPortHeight * sc);
				viewPortWidth = vWidth;
			} else {
				sc = 1f * vHeight / viewPortHeight;
				viewPortWidth = (int) (viewPortWidth * sc);
				viewPortHeight = vHeight;
			}
			// if (1 / sc > 1) {
			// maxScale = 3.0f;
			// minScale = 1f;
			// } else {
			// maxScale = 3.0f;
			// minScale = 1f;
			// }
			Log.d(TAG, "translate the view, to scale: " + sc);
			originalMatrix.postScale(sc, sc);
			originalMatrix.postTranslate((vWidth - viewPortWidth) / 2f,
					(vHeight - viewPortHeight) / 2f);
			savedMatrix = originalMatrix;
			savedwidth = viewPortWidth;
			savedheight = viewPortHeight;

		}

		Log.d(TAG, "size of view:" + vWidth + " " + vHeight
				+ " size of image: " + viewPortWidth + " " + viewPortHeight);
		updateView();
	}

	public void setHandler(Handler handler) {
		mHandler = handler;

	}
	
	public void scrollToPosition(int x, int y){
		centerX = x;
		centerY = y;
		updateView();
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (image != null && frame != null) {
			int left = centerX - viewPortWidth / 2;
			int top = centerY - viewPortHeight / 2;
			canvas.drawBitmap(image, new Rect(left, top, left + viewPortWidth,
					top + viewPortHeight), frame, paint);
		}
	}

}
