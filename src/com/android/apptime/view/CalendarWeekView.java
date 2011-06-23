package com.android.apptime.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;
import android.widget.ZoomButtonsController.OnZoomListener;

import com.android.apptime.Item;
import com.android.apptime.R;

public class CalendarWeekView extends Activity {
	private static int nextViewId = 1;
	private String TAG = "calendarview";
	private final int CONTEXT_MENU_ADD = 0;
	private final int CONTEXT_MENU_MODIFY = 1;
	private final int CONTEXT_MENU_REMOVE = 2;

	public static final int POPUP_TIME_SLOT = 0;
	public static final int POPUP_ITEM = 1;

	private final int POPUP_FORM = 0;
	private TimeSlotView[] dayTimeSlot = new TimeSlotView[Constant.NUM_HOUR];
	private DayInWeekView[] daysInWeekSlot = new DayInWeekView[Constant.NUM_DAY_IN_WEEK];

	private TextView title = null;
	private TextView[] daysInWeekTitle = new TextView[Constant.NUM_DAY_IN_WEEK];
	private RelativeLayout containerLayout = null;
	private ZoomableViewGroup titleLayout = null;
	private ZoomableViewGroup contentLayout = null;
	private int width;
	private int timeSlotHeight = 80;
	private int titleHeight = 50;
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
	private float marginWidth = 70;
	private int height = 1000;
	private int daySlotWidth = 50;
	private int screenWidth = 0, screenHeight = 0;

	private ZoomableViewGroup.OnScrollListener mOnScrollListener = new ZoomableViewGroup.OnScrollListener() {

		@Override
		public void onScrollBy(int dx, int dy) {
			titleLayout.scrollBy(dx, 0);
		}
	};

	private ZoomableViewGroup.OnZoomListener mOnZoomListener = new ZoomableViewGroup.OnZoomListener() {

		@Override
		public void onZoomWithScale(float zoomScale) {
			// zoom out the width of the title row
			int startId = title.getId();
			for (int i = 0; i <= Constant.NUM_DAY_IN_WEEK; i++) {
				int id = startId + i;
				View view = titleLayout.getChildAt(id);
				if (view == null)
					continue;
				int cw = (int) (view.getWidth() * zoomScale);
				int ch = (int) view.getHeight();
				int cl = (int) (view.getLeft() * zoomScale);
				int ct = (int) (view.getTop());

				if (view == title)
					Log.d(TAG, "this is title");
				Log.d(TAG, "on zoom, child " + id + " " + cl + " " + ct + " "
						+ cw + " " + ch);
				int ecw = View.MeasureSpec.makeMeasureSpec(cw,
						View.MeasureSpec.EXACTLY);
				int ech = View.MeasureSpec.makeMeasureSpec(ch,
						View.MeasureSpec.EXACTLY);
				view.measure(ecw, ech);
				view.layout(cl, ct, cl + cw, ct + ch);
				view.invalidate(cl, ct, cl + cw, ct + ch);
			}

			// adjusting the day slot so that they are not misplaced due to
			// round off error
			startId = dayTimeSlot[0].getId();

			for (int i = 1; i < Constant.NUM_HOUR; i++) {
				int id = startId + i;
				View view = contentLayout.getChildAt(id);
				View prevView = contentLayout.getChildAt(id - 1);
				int prevBot = prevView.getBottom();
				int thisTop = prevBot;
				int thisWidth = view.getWidth();
				int thisHeight = view.getHeight();
				view.layout(0, thisTop, thisWidth, thisTop + thisHeight);
			}

			int viewHeight = dayTimeSlot[Constant.NUM_HOUR - 1].getBottom();
			// adjust all the day slot height
			startId = daysInWeekSlot[0].getId();
			for (int i = 0; i < Constant.NUM_DAY_IN_WEEK; i++) {
				int id = startId + i;
				View view = contentLayout.getChildAt(id);
				int exWidth = View.MeasureSpec.makeMeasureSpec(view.getWidth(),
						View.MeasureSpec.EXACTLY);
				int exHeight = View.MeasureSpec.makeMeasureSpec(viewHeight,
						View.MeasureSpec.EXACTLY);
				view.measure(exWidth, exHeight);
			}
			contentLayout.invalidate();
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendarweekview);
		containerLayout = (RelativeLayout) findViewById(R.id.container);

		// width = contentLayout.getWidth();
		// Log.d(TAG, "" + width);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		try {
			displayAllTimeSlot();
			// addZoomButtons(containerLayout);
			// addItemViewToTimeSlot(null, 0, 5, 35, 10, 30, 15, 35);
			// addItemViewToTimeSlot(null, 1, 6, 30, 7, 30, 15, 35);
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
	}

	/**
	 * Zoom handling part This part adds the zoom buttons to the view
	 */

	private ZoomButtonsController mZoomButtonsController = null;

	private void addZoomButtons() {
		if (mZoomButtonsController == null) {
			mZoomButtonsController = new ZoomButtonsController(containerLayout);

			Log.d(TAG, "" + containerLayout.getWindowToken());
			mZoomButtonsController.setAutoDismissed(true);
			mZoomButtonsController.setVisible(true);

			Log.d(TAG, "" + mZoomButtonsController.getContainer() + " "
					+ mZoomButtonsController.isVisible() + " "
					+ mZoomButtonsController.isAutoDismissed());
			nextViewId++;
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.FILL_PARENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			//mZoomButtonsController.getContainer().setLayoutParams(params);
			FrameLayout layout = (FrameLayout) findViewById(R.id.zoomcontrol);
			layout.addView(mZoomButtonsController.getContainer());
			layout.bringToFront();
			mZoomButtonsController.setOnZoomListener(new OnZoomListener() {

				@Override
				public void onZoom(boolean zoomIn) {
					if (zoomIn) {
						contentLayout
								.zoom(ZoomableViewGroup.DEFAULT_ZOOM_IN_RATE);
					} else {
						contentLayout
								.zoom(ZoomableViewGroup.DEFAULT_ZOOM_OUT_RATE);
					}
				}

				@Override
				public void onVisibilityChanged(boolean visible) {
					// TODO Auto-generated method stub

				}
			});
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		addZoomButtons();
	}

	private void displayAllTimeSlot() {
		Resources myResources = getResources();
		marginWidth = myResources.getDimension(R.dimen.timeslot_margin);
		timeSlotHeight = (int) myResources
				.getDimension(R.dimen.timeslot_height);
		titleHeight = (int) myResources.getDimension(R.dimen.title_height);
		daySlotWidth = (int) ((screenWidth - marginWidth) / 7);

		Log.d(TAG, "add view to week calendar" + " " + screenWidth + " "
				+ marginWidth + " " + daySlotWidth);
		// containerLayout.setBackgroundColor(Color.BLUE);
		containerLayout.addView(new View(getApplicationContext()));
		// Adding title part
		// This part is used to generate the title for the week calendar

		// adding the big title layout
		int exTitleHeight = RelativeLayout.MeasureSpec.makeMeasureSpec(
				titleHeight, RelativeLayout.MeasureSpec.EXACTLY);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, titleHeight);
		titleLayout = new ZoomableViewGroup(getApplicationContext());
		// titleLayout.measure(LayoutParams.FILL_PARENT, 200);
		titleLayout.setLayoutParams(params);
		titleLayout.setId(nextViewId++);
		// titleLayout.setBackgroundColor(Color.RED);
		containerLayout.addView(titleLayout);

		titleLayout.addView(new View(getApplicationContext()));
		// adding the title row
		title = new TextView(getApplicationContext());
		title.setText("Time");
		int exWidth = View.MeasureSpec.makeMeasureSpec((int) marginWidth,
				View.MeasureSpec.EXACTLY);
		int exHeight = View.MeasureSpec.makeMeasureSpec(titleHeight,
				View.MeasureSpec.EXACTLY);
		title.measure(exWidth, exHeight);
		title.layout(0, 0, (int) marginWidth, titleHeight);
		title.setId(titleLayout.getChildCount());
		// title.setBackgroundColor(Color.GREEN);
		titleLayout.addView(title);
		Log.d(TAG,
				"add title " + " at " + title.getLeft() + " " + title.getTop()
						+ " " + " " + title.getMeasuredWidth() + " "
						+ title.getMeasuredHeight());

		for (int i = 0; i < Constant.NUM_DAY_IN_WEEK; i++) {
			daysInWeekTitle[i] = new TextView(getApplicationContext());
			daysInWeekTitle[i].setText(Constant.DAY_IN_WEEK[i].substring(0, 3));
			exWidth = View.MeasureSpec.makeMeasureSpec((int) daySlotWidth,
					View.MeasureSpec.EXACTLY);
			exHeight = View.MeasureSpec.makeMeasureSpec(titleHeight,
					View.MeasureSpec.EXACTLY);
			daysInWeekTitle[i].measure(exWidth, exHeight);
			daysInWeekTitle[i].layout((int) marginWidth + i * daySlotWidth, 0,
					(int) marginWidth + (i + 1) * daySlotWidth, titleHeight);
			daysInWeekTitle[i].setId(titleLayout.getChildCount());
			titleLayout.addView(daysInWeekTitle[i]);
			Log.d(TAG,
					"add day " + i + " title " + " at "
							+ daysInWeekTitle[i].getLeft() + " "
							+ daysInWeekTitle[i].getTop() + " " + " "
							+ daysInWeekTitle[i].getMeasuredWidth() + " "
							+ daysInWeekTitle[i].getMeasuredHeight());

		}

		// Adding content part
		// This part is used to generate the content for the week calendar

		// adding the big content layout
		int contentHeight = View.MeasureSpec.makeMeasureSpec(1000,
				View.MeasureSpec.EXACTLY);
		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		params.addRule(RelativeLayout.BELOW, titleLayout.getId());
		contentLayout = new ZoomableViewGroup(getApplicationContext());
		contentLayout.setLayoutParams(params);
		contentLayout.setId(nextViewId++);
		containerLayout.addView(contentLayout);
		contentLayout.addView(new View(getApplicationContext()));
		contentLayout.setClickable(true);
		contentLayout.setFocusable(true);
		contentLayout.setScrollContainer(true);
		contentLayout.setOnZoomListener(mOnZoomListener);
		contentLayout.setOnScrollListener(mOnScrollListener);

		// adding time slot in content view
		for (int i = 0; i < Constant.NUM_HOUR; i++) {
			dayTimeSlot[i] = new TimeSlotView(getApplicationContext());
			dayTimeSlot[i].getTime().setHours(i);
			dayTimeSlot[i].getTime().setMinutes(0);
			dayTimeSlot[i].setText(TimeFormat
					.getAPPMHourFormatWithoutHourPadding(dayTimeSlot[i]
							.getTime()));
			exWidth = View.MeasureSpec.makeMeasureSpec(screenWidth,
					View.MeasureSpec.EXACTLY);
			exHeight = View.MeasureSpec.makeMeasureSpec(timeSlotHeight,
					View.MeasureSpec.EXACTLY);
			dayTimeSlot[i].measure(exWidth, exHeight);

			dayTimeSlot[i]
					.layout(0, i * timeSlotHeight,
							dayTimeSlot[i].getMeasuredWidth(), (i + 1)
									* timeSlotHeight);

			dayTimeSlot[i].setId(contentLayout.getChildCount());
			contentLayout.addView(dayTimeSlot[i]);
		}

		// adding day slot in content layout (showing the lines)
		for (int i = 0; i < Constant.NUM_DAY_IN_WEEK; i++) {
			Log.d(TAG, "adding day in week slot");
			daysInWeekSlot[i] = new DayInWeekView(getApplicationContext());

			exWidth = View.MeasureSpec.makeMeasureSpec(daySlotWidth,
					View.MeasureSpec.EXACTLY);
			exHeight = View.MeasureSpec.makeMeasureSpec(Constant.NUM_HOUR
					* timeSlotHeight, View.MeasureSpec.EXACTLY);
			daysInWeekSlot[i].measure(exWidth, exHeight);
			daysInWeekSlot[i].layout((int) marginWidth + i * daySlotWidth, 0,
					(int) marginWidth + (i + 1) * daySlotWidth,
					Constant.NUM_HOUR * timeSlotHeight);

			daysInWeekSlot[i].setId(contentLayout.getChildCount());

			contentLayout.addView(daysInWeekSlot[i]);
			daysInWeekSlot[i].setClickable(true);

			final View thisView = daysInWeekSlot[i];
			// daysInWeekSlot[i].setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// itemBeingSelected = thisView;
			// Log.d(TAG, "day slot selected");
			// }
			// });
		}
		Log.d("calendarview",
				"content layout size screen " + contentLayout.getWidth() + " "
						+ contentLayout.getHeight());
		contentLayout.viewWidth = contentLayout.getWidth();
		contentLayout.viewHeight = timeSlotHeight * Constant.NUM_HOUR;
		Log.d("calendarview", "content layout size view "
				+ contentLayout.viewWidth + " " + contentLayout.viewHeight);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Toast.makeText(getApplicationContext(), "on touch event",
					Toast.LENGTH_SHORT);
			Log.d(TAG, "on touch event");
			return true;
		}
		return false;
	}

	private void updateView() {

	}

	private void showPopUpWindow() {
		if (itemBeingSelected.getClass().equals(TimeSlotView.class)) {
			TimeSlotView timeSlotView = (TimeSlotView) itemBeingSelected;
			Intent createPopup = new Intent(getApplicationContext(),
					PopupForm.class);
			createPopup.putExtra("popupType", POPUP_TIME_SLOT);
			createPopup.putExtra("startTime", timeSlotView.getTime());
			createPopup.putExtra("offX", itemBeingSelected.getLeft());
			createPopup.putExtra("offY", itemBeingSelected.getBottom());
			startActivityForResult(createPopup, POPUP_FORM);
		} else {
			WeekItemView itemView = (WeekItemView) itemBeingSelected;
			Intent createPopup = new Intent(getApplicationContext(),
					PopupForm.class);
			createPopup
					.putExtra("startTime", itemView.getItem().GetStartTime());
			createPopup.putExtra("endTime", itemView.getItem().GetEndTime());
			createPopup.putExtra("endTime", itemView.getItem().GetEndTime());
			createPopup.putExtra("popupType", POPUP_ITEM);
			createPopup.putExtra("offX", itemBeingSelected.getLeft());
			createPopup.putExtra("offY", itemBeingSelected.getBottom());
			startActivityForResult(createPopup, POPUP_FORM);
		}
	}

	public WeekItemView addItemViewToTimeSlot(Item item, int day, int fromHour,
			int fromMin, int toHour, int toMin, int leftMargin, int width) {
		WeekItemView newItem = new WeekItemView(getApplicationContext());
		newItem.setText("Place holder text");
		// need to read from database
		// newItem.setItem(object);
		RelativeLayout.LayoutParams layoutParams = null;
		int height = getHeightForItemInTimeSlot(fromHour, fromMin, toHour,
				toMin);
		layoutParams = new RelativeLayout.LayoutParams(width, height);
		layoutParams.addRule(RelativeLayout.ALIGN_TOP,
				dayTimeSlot[fromHour].getId());

		layoutParams.addRule(RelativeLayout.ALIGN_LEFT,
				daysInWeekSlot[day].getLeft());

		// layoutParams.addRule(RelativeLayout.ALIGN_LEFT,
		// daysInWeekSlot[day].getId());
		layoutParams.setMargins(leftMargin,
				getMarginCorrespondingToPeriod(fromMin), 0, 0);
		newItem.setLayoutParams(layoutParams);
		newItem.setId(nextViewId++);
		contentLayout.addView(newItem);
		return newItem;
	}

	private int getHeightForItemInTimeSlot(int fromHour, int fromMin,
			int toHour, int toMin) {
		int result = timeSlotHeight * (toHour - fromHour)
				- getMarginCorrespondingToPeriod(fromMin)
				+ getMarginCorrespondingToPeriod(toMin);
		return result;
	}

	private int getMarginCorrespondingToPeriod(int numMin) {
		return (int) (1.0 * timeSlotHeight * numMin / 60);
	}

	private void removeItem(int itemId) {
		// TODO Auto-generated method stub

	}

	/**
	 * Zoom and scrolling part
	 */

	// public static final float DEFAULT_ZOOM_IN_RATE = 1.2f;
	// public static final float DEFAULT_ZOOM_OUT_RATE = 0.8f;
	// private float scale = 1f;
	// private float maxScale = 3f, minScale = 1f;
	//
	// public float getMaxScale() {
	// return maxScale;
	// }
	//
	// public float getMinScale() {
	// return minScale;
	// }
	//
	// public void setMaxScale(float scale) {
	// maxScale = scale;
	// }
	//
	// public void setMinScale(float scale) {
	// minScale = scale;
	// }
	//
	// private ArrayList<ViewInfo> childrenInfoList = null;
	// private float deltaScale = 1f;
	// private boolean mIsBeingDragged = false;
	// private PointF lastPoint;
	// private int mTouchSlop = 5;
	// private int mScrollX, mScrollY, mMaxScrollX, mMaxScrollY;
	// private int screenWidth, screenHeight, viewWidth, viewHeight;
	//
	// public ZoomableViewGroup(Context context) {
	// super(context);
	// Log.d(TAG, "constructor 1 ");
	// init(context);
	// }
	//
	// public ZoomableViewGroup(Context context, AttributeSet attrs) {
	// super(context, attrs);
	// Log.d(TAG, "constructor 2 ");
	// init(context);
	// }
	//
	// public ZoomableViewGroup(Context context, AttributeSet attrs, int
	// defStyle) {
	// super(context, attrs, defStyle);
	// Log.d(TAG, "constructor 3 ");
	// init(context);
	// }
	//
	// private void init(Context context) {
	// Log.d(TAG, "init");
	// this.setBackgroundDrawable(new BitmapDrawable());
	// lastPoint = new PointF();
	// }
	//
	// @Override
	// protected int computeVerticalScrollRange() {
	// return viewHeight;
	// }
	//
	// @Override
	// protected int computeHorizontalScrollRange() {
	// return viewWidth;
	// }
	//
	// @Override
	// protected int computeVerticalScrollExtent() {
	// return screenHeight;
	// }
	//
	// @Override
	// protected int computeHorizontalScrollExtent() {
	// return screenWidth;
	// }
	//
	// @Override
	// protected int computeVerticalScrollOffset() {
	// return mScrollY;
	// }
	//
	// @Override
	// protected int computeHorizontalScrollOffset() {
	// return mScrollX;
	// }
	//
	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev) {
	// Log.d("scroll", "on touch intercept" + ev.getAction() + " being drag"
	// + mIsBeingDragged);
	// final int action = ev.getAction();
	// if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
	// Log.d("scroll", "on touch intercept - action move - drag");
	// lastPoint.set(ev.getX(), ev.getY());
	// return true;
	// }
	//
	// if (!canScroll()) {
	// mIsBeingDragged = false;
	// Log.d("scroll",
	// "on touch intercept - cannot scroll, so return false");
	// lastPoint.set(ev.getX(), ev.getY());
	// return false;
	// }
	// final float x = ev.getX();
	// final float y = ev.getY();
	// switch (action) {
	// case MotionEvent.ACTION_MOVE:
	// Log.d("scroll", "on touch intercept - action move");
	//
	// final int diff = (int) Math.hypot(x-lastPoint.x, y - lastPoint.y);
	// if (diff > mTouchSlop) {
	// mIsBeingDragged = true;
	// Log.d("scroll", "being dragged");
	// }
	// break;
	//
	// // case MotionEvent.ACTION_CANCEL:
	// case MotionEvent.ACTION_UP:
	// /* Release the drag */
	// mIsBeingDragged = false;
	// Log.d("scroll", "on touch intercept - not drag");
	// break;
	// }
	//
	// /*
	// * The only time we want to intercept motion events is if we are in the
	// * drag mode.
	// */
	// lastPoint.set(ev.getX(), ev.getY());
	// return mIsBeingDragged;
	// }
	//
	// private boolean canScroll() {
	// Log.d("scroll", "extent - range" + computeHorizontalScrollExtent()
	// + " " + computeHorizontalScrollRange() + " "
	// + computeVerticalScrollExtent() + " "
	// + computeVerticalScrollRange() + "offset: "
	// + computeHorizontalScrollOffset() + " "
	// + computeVerticalScrollOffset());
	// return (computeHorizontalScrollExtent() < computeHorizontalScrollRange()
	// || computeVerticalScrollExtent() < computeVerticalScrollRange());
	// }
	//
	// @Override
	// public boolean onTouchEvent(MotionEvent ev) {
	// Log.d("scroll", "on touch event " + ev.getAction());
	// if (ev.getAction() == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0)
	// {
	// // Don't handle edge touches immediately -- they may actually belong
	// // to one of our
	// // descendants.
	// Log.d("scroll", "dispatch click to children");
	// return false;
	// }
	//
	// if (!canScroll()) {
	// return false;
	// }
	//
	// final int action = ev.getAction();
	// final float x = ev.getX();
	// final float y = ev.getY();
	//
	// switch (action) {
	// case MotionEvent.ACTION_DOWN:
	// Log.d("scroll", "on touch event - action down in scrollview");
	// lastPoint.set(x, y);
	// break;
	// case MotionEvent.ACTION_MOVE:
	// Log.d("scroll", "on touch event - action move in scrollview");
	// // Scroll to follow the motion event
	// int deltaX = (int) (x- lastPoint.x);
	// int deltaY = (int) (y- lastPoint.y);
	// lastPoint.set(x, y);
	// Log.d("scroll", "delta X, deltaY" + deltaX + " " + deltaY);
	// if (deltaX > 0) {
	// if (mScrollX > 0) {
	// deltaX = Math.min(deltaX, mScrollX);
	// } else
	// deltaX = 0;
	// } else if (deltaX < 0) {
	// if (mScrollX < computeHorizontalScrollRange()
	// - computeHorizontalScrollExtent()) {
	// deltaX = Math.max(deltaX, mScrollX - (computeHorizontalScrollRange()
	// - computeHorizontalScrollExtent()));
	// } else
	// deltaX = 0;
	// }
	// if (deltaY > 0) {
	// if (mScrollY > 0) {
	// deltaY = Math.min(deltaY, mScrollY);
	// } else
	// deltaY = 0;
	// } else if (deltaY < 0) {
	// if (mScrollY < computeVerticalScrollRange()
	// - computeVerticalScrollExtent()) {
	// deltaY = Math.max(deltaY, mScrollY - (computeVerticalScrollRange()
	// - computeVerticalScrollExtent()));
	// } else
	// deltaY = 0;
	// }
	// Log.d("scroll", "delta X, deltaY" + deltaX + " " + deltaY);
	// mScrollX -= deltaX;
	// mScrollY -= deltaY;
	// Log.d("scroll", "scrollBy " + deltaX + " " + deltaY);
	// scrollTo(mScrollX, mScrollY);
	// break;
	// case MotionEvent.ACTION_CANCEL:
	// case MotionEvent.ACTION_UP:
	// default:
	// mIsBeingDragged = false;
	// lastPoint.set(ev.getX(), ev.getY());
	// return false;
	// }
	// return true;
	// }
	//
	// /**
	// * Zoom handling part This part adds the zoom buttons to the view
	// */
	//
	// private ZoomButtonsController mZoomButtonsController = null;
	//
	// @Override
	// protected void onAttachedToWindow() {
	// super.onAttachedToWindow();
	// this.setVerticalScrollBarEnabled(true);
	// this.setHorizontalScrollBarEnabled(true);
	// Log.d(TAG, " " + getVerticalScrollbarWidth() + " "
	// + getHorizontalScrollbarHeight());
	// if (mZoomButtonsController == null) {
	// mZoomButtonsController = new ZoomButtonsController(this);
	//
	// Log.d(TAG, "" + this.getWindowToken());
	// mZoomButtonsController.setAutoDismissed(false);
	// mZoomButtonsController.setVisible(true);
	// Log.d(TAG, "" + mZoomButtonsController.getContainer() + " "
	// + mZoomButtonsController.isVisible() + " "
	// + mZoomButtonsController.isAutoDismissed());
	// mZoomButtonsController.setOnZoomListener(new OnZoomListener() {
	//
	// @Override
	// public void onZoom(boolean zoomIn) {
	// if (zoomIn) {
	// ZoomableViewGroup.this
	// .zoom(ZoomableViewGroup.DEFAULT_ZOOM_IN_RATE);
	// } else {
	// ZoomableViewGroup.this
	// .zoom(ZoomableViewGroup.DEFAULT_ZOOM_OUT_RATE);
	// }
	// }
	//
	// @Override
	// public void onVisibilityChanged(boolean visible) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// }
	// }
	//
	// @Override
	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// viewWidth = MeasureSpec.getSize(widthMeasureSpec);
	// viewHeight = MeasureSpec.getSize(heightMeasureSpec);
	// screenWidth = viewWidth;
	// screenHeight = viewHeight;
	// Log.d(TAG, "on measure " + viewWidth + " " + viewHeight);
	// }
	//
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
	// @Override
	// protected void onLayout(boolean changed, int l, int t, int r, int b) {
	// final int count = getChildCount();
	// for (int i = 0; i < count; i++) {
	// final View child = getChildAt(i);
	// if (child.getVisibility() != View.GONE) {
	// final int childLeft = child.getLeft();
	// final int childTop = child.getTop();
	// final int childWidth = child.getMeasuredWidth();
	// final int childHeight = child.getMeasuredHeight();
	// child.layout(childLeft, childTop, childLeft + childWidth,
	// childTop + childHeight);
	// viewWidth = viewWidth > child.getRight() ? viewWidth : child
	// .getRight();
	// viewHeight = viewHeight > child.getBottom() ? viewHeight
	// : child.getBottom();
	// Log.d(TAG, "view width, view height " + viewWidth + " "
	// + viewHeight + " screen width, screen height "
	// + screenWidth + " " + screenHeight);
	// }
	// }
	// }
	//
	// public void zoom(float deltaScale) {
	// Log.d(TAG, "zoom with delta scale = " + deltaScale);
	// float tempScale = deltaScale * this.scale;
	// if (tempScale < minScale || tempScale > maxScale)
	// return;
	// this.deltaScale = deltaScale;
	// Log.d(TAG, "zoom to scale " + tempScale);
	//
	// this.scale = tempScale;
	// // int maxRight = 0, maxBottom = 0;
	// for (int i = 0; i < getChildCount(); i++) {
	// View view = getChildAt(i);
	// int id = view.getId();
	// int cw = (int) (view.getWidth() * deltaScale);
	// int ch = (int) (view.getHeight() * deltaScale);
	// int cl = (int) (view.getLeft() * deltaScale);
	// int ct = (int) (view.getTop() * deltaScale);
	// if (id < 0)
	// continue;
	// Log.d(TAG, "on zoom, child " + id + " " + cl + " " + ct + " " + cw
	// + " " + ch);
	// view.measure(cw, ch);
	// view.layout(cl, ct, cl + cw, ct + ch);
	// view.invalidate(cl, ct, cl + cw, ct + ch);
	// // if (cl+cw>maxRight) maxRight = cl+cw;
	// // if (ct+ch>maxBottom) maxBottom = ct+ch;
	// }
	// viewWidth = (int) (viewWidth * deltaScale);
	// viewHeight = (int) (viewHeight * deltaScale);
	// mScrollX = (int) (mScrollX * deltaScale);
	// mScrollY = (int) (mScrollY * deltaScale);
	// scrollTo(mScrollX, mScrollY);
	// }
	//
	// public void zoomToScale(float scale) {
	// zoom(scale / this.scale);
	// }
	//
	// @Override
	// protected void onDraw(Canvas canvas) {
	// canvas.save(Canvas.MATRIX_SAVE_FLAG);
	// canvas.scale(scale, scale);
	// canvas.restore();
	// super.onDraw(canvas);
	// }

}
