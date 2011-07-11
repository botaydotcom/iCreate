package com.android.apptime.view;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ZoomButtonsController;

public class InteractiveImageView extends ImageView {

	private Handler mHandler = null;
	private RectF rect = null;

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
		return width;
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
		return height;
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
	}

	private static final float ZOOM_SPEED = 1.0f;
	private static final String TAG = "Touch";
	// These matrices will be used to move and zoom image
	private Matrix originalMatrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private Bitmap image;
	private File imageFile;

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	private float scale = 1.0f;
	private float maxScale = 3.0f;
	private float minScale = 1.0f;
	private int savedwidth, savedheight, width, height, vWidth, vHeight;

	// Remember some things for zooming
	// below modification by naveen. remember initial scale for x and y

	private PointF start = new PointF();
	private PointF mid = new PointF();
	private Context context;
	private float oldScale = 1f, netScale = 1f;
	private float oldDist = 1f;
	private float deltaX = 0, deltaY = 0;

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
				// if (computeHorizontalScrollExtent() >=
				// computeHorizontalScrollRange())
				// deltaX = 0;
				// if (computeVerticalScrollExtent() >=
				// computeVerticalScrollRange())
				// deltaY = 0;
				// Log.d(TAG, "drag " + deltaX + " " + deltaY);
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
		Matrix m = getImageMatrix();
		rect = new RectF(0, 0, image.getWidth(), image.getHeight());
		// Log.d("Rect", rect.toString());
		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();
		float deltaX = 0, deltaY = 0;

		if (height < vHeight) {
			deltaY = (vHeight - height) / 2 - rect.top;
		} else if (rect.top > 0) {
			deltaY = -rect.top;
		} else if (rect.bottom < vHeight) {
			deltaY = getHeight() - rect.bottom;
		}

		if (width < vWidth) {
			deltaX = (vWidth - width) / 2 - rect.left;
		} else if (rect.left > 0) {
			deltaX = -rect.left;
		} else if (rect.right < vWidth) {
			deltaX = vWidth - rect.right;
		}

		m.postTranslate(deltaX, deltaY);
		savedMatrix.set(m);
	}

	private Runnable updateUI = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// Log.d(TAG,
			// "update view: scale = " + scale + " "
			// + savedMatrix.toShortString());
			Matrix matrix = getImageMatrix();
			matrix.set(savedMatrix);
			setCenter();
			// Log.d(TAG, "after checking " + getImageMatrix().toShortString());
			invalidate();
		}
	};

	private void updateView() {
		mHandler.removeCallbacks(updateUI);
		mHandler.post(updateUI);

	}

	public void setImage2() {
		super.setImageBitmap(image);
		center();
	}

	protected boolean fullSizeLoaded = false;

	public void setImage(Bitmap newBitmap, boolean isFullSize) {
		// //Log.d(TAG, "loaded");
		if (!isFullSize) {
			// if the full sized image is available - return;
			if (fullSizeLoaded)
				return;
			image = newBitmap;
			rect = new RectF(0, 0, image.getWidth(), image.getHeight());
			super.setImageBitmap(newBitmap);
			center();
		} else {
			fullSizeLoaded = true;
			if (image == null) {
				image = newBitmap;
				rect = new RectF(0, 0, image.getWidth(), image.getHeight());
				super.setImageBitmap(newBitmap);
				center();
			} else {
				// Log.d("BACKGROUND", "try to set the full image ");
				// Log.d("Touch", "current matrix:"
				// +getImageMatrix().toString());
				rect = new RectF(0, 0, image.getWidth(), image.getHeight());
				Matrix matrix = getImageMatrix();
				matrix.mapRect(rect);
				// Log.d("Touch", "current rectanlge :" + rect.toString());
				image = newBitmap;

				this.setImageBitmap(image);
				matrix = new Matrix();
				matrix.postScale((float) (rect.width() / image.getWidth()),
						(float) (rect.height() / image.getHeight()), 0, 0);
				matrix.postTranslate(rect.left, rect.top);
				width = (int) rect.width();
				height = (int) rect.height();
				// Log.d("Touch", "set to:" +matrix);
				this.setImageMatrix(matrix);
				savedMatrix.set(matrix);
				// Log.d("Touch", "now it is: "+getImageMatrix().toString());
				rect = new RectF(0, 0, image.getWidth(), image.getHeight());
				matrix.mapRect(rect);
				// Log.d("Touch", "which will create the rect:" +
				// rect.toString());
				updateView();
			}
		}
	}

	public boolean setImage(int pageNo) {
		// image = Viewer.getImgFromFile(file);
		// picture.setImageBitmap(myBitmap);
		// this.imageFile = file;
		this.imageFile = new File(
				"/mnt/sdcard/Images/high_quality_150dpi_part/1_Page_010.jpg");
		if (imageFile.exists()) {
			image = BitmapFactory
					.decodeFile("/mnt/sdcard/Images/high_quality_150dpi_part/1_Page_010.jpg");
			super.setImageBitmap(image);
			center();
			return true;
		} else
			return false;
	}

	public File getFile() {
		return imageFile;
	}

	@Override
	public void onSizeChanged(int displayWidth, int displayHeight, int s, int d) {
		super.onSizeChanged(displayWidth, displayHeight, s, d);
		vWidth = displayWidth;
		vHeight = displayHeight;
		center();
	}

	private void center() {
		width = image.getWidth();
		height = image.getHeight();
		float sc = 1f;
		if (vWidth != 0 && vHeight != 0) {
			if (1.0 * vWidth / width < 1.0 * vHeight / height) {
				sc = 1f * vWidth / width;
				height = (int) (height * sc);
				width = vWidth;
			} else {
				sc = 1f * vHeight / height;
				width = (int) (width * sc);
				height = vHeight;
			}
			if (1 / sc > 1) {
				maxScale = 3.0f;
				minScale = 1f;
			} else {
				maxScale = 3.0f;
				minScale = 1f;
			}
			// ////Log.d(TAG, "translate the view, to scale: "+sc);
			originalMatrix.postScale(sc, sc);
			originalMatrix.postTranslate((vWidth - width) / 2f,
					(vHeight - height) / 2f);
			savedMatrix = originalMatrix;
			savedwidth = width;
			savedheight = height;

		}
		// Log.d(TAG, "size of view:" + vWidth + " " + vHeight
		// + " size of image: " + width + " " + height);
		updateView();
	}

}
