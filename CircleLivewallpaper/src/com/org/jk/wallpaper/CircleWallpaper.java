package com.org.jk.wallpaper;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class CircleWallpaper extends WallpaperService {

	private final Handler mHandler = new Handler();

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new CircleEngine();
	}

	class CircleEngine extends Engine {
		private final Paint mPaint = new Paint();
		private boolean mVisible;
		int radius = 40;
		boolean isCreated=false;
		Circle[] circleArray = new Circle[6];

		Random r = new Random();

		private final Runnable mDrawCircles = new Runnable() {
			public void run() {
				radius++;
				if (radius > 60) {
					radius = 40;
				}
				drawCircles();
			}
		};

		CircleEngine() {
			mPaint.setColor(Color.WHITE);
			mPaint.setAntiAlias(true);
			mPaint.setStrokeWidth(2);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStyle(Paint.Style.STROKE);

		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			setTouchEventsEnabled(true);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			mVisible = false;
			mHandler.removeCallbacks(mDrawCircles);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			mVisible = visible;
			if (visible) {
				drawCircles();
			} else {
				mHandler.removeCallbacks(mDrawCircles);
			}

		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			Log.e("Circles", "in onsurfacechanged");
			if (!isCreated) {
				for (int i = 0; i < 6; i++) {
					circleArray[i] = new Circle(width, height);
					circleArray[i].randomizeCenter();
					isCreated=true;
				}
			}
			drawCircles();

		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mVisible = false;
			mHandler.removeCallbacks(mDrawCircles);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
				float yStep, int xPixels, int yPixels) {
			drawCircles();
			Log.e("Circles", "in offsetChanged");
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			super.onTouchEvent(event);
			Log.e("Circles", "in onTouchEvent");

		}

		@Override
		public Bundle onCommand(String action, int x, int y, int z,
				Bundle extras, boolean resultRequested) {
			if ("android.wallpaper.tap".equals(action)) {
				Log.e("Circles", "in on tap");
			}
			return super.onCommand(action, x, y, z, extras, resultRequested);
		}

		void drawCircles() {
			final SurfaceHolder holder = getSurfaceHolder();

			Canvas c = null;

			try {
				c = holder.lockCanvas();
				c.save();
				c.drawColor(0xff000000);
				if (c != null) {
					for (int i = 0; i < 6; i++) {
						circleArray[i].updateValues();
						mPaint.setColor(circleArray[i].currentColor);
						mPaint.setAlpha(circleArray[i].currentAlpha);
						c.drawCircle(circleArray[i].centerX,
								circleArray[i].centerY,
								circleArray[i].currentRadius, mPaint);
					}
					c.restore();
				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}
			mHandler.removeCallbacks(mDrawCircles);
			if (mVisible) {
				mHandler.postDelayed(mDrawCircles, 3500 / 25);
			}
		}
	}
}
