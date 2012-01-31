package com.jk.org.battery;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class BatteryService extends Service {

	AppWidgetManager appWidgetManager;
	int[] appWidgetIds;
	int widgetId;
	String batteryLevel;
	int intBatteryLevel;
	boolean isCharging;

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.e("Widget Battery", "In Start Service");
		appWidgetManager = AppWidgetManager.getInstance(this
				.getApplicationContext());
		appWidgetIds = intent
				.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

		widgetId = appWidgetIds[0];
		registerReceiver(this.batteryUpdate, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		stopSelf();
	}

	BroadcastReceiver batteryUpdate = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e("Widget Battery", "In Broadcast recieve");
			if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
				intBatteryLevel = intent.getIntExtra("level", 0);
				batteryLevel = String.valueOf(intBatteryLevel);
				updateWidget();
			}

		}
	};

	public void updateWidget() {
		Log.e("Widget Battery", "In onUpdate");
		RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.widget_layout);

		if (intBatteryLevel > 90) {
			remoteViews.setViewVisibility(R.id.batteryColor5, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor4, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor3, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor2, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor1, View.VISIBLE);
		} else if (intBatteryLevel < 90 && intBatteryLevel > 80) {
			remoteViews.setViewVisibility(R.id.batteryColor5, View.INVISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor4, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor3, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor2, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor1, View.VISIBLE);
		} else if (intBatteryLevel < 80 && intBatteryLevel > 60) {
			remoteViews.setViewVisibility(R.id.batteryColor5, View.INVISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor4, View.INVISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor3, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor2, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor1, View.VISIBLE);
		} else if (intBatteryLevel < 60 && intBatteryLevel > 40) {
			remoteViews.setViewVisibility(R.id.batteryColor5, View.INVISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor4, View.INVISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor3, View.INVISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor2, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor1, View.VISIBLE);
		} else if (intBatteryLevel < 40 && intBatteryLevel > 10) {
			remoteViews.setViewVisibility(R.id.batteryColor5, View.INVISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor4, View.INVISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor3, View.INVISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor2, View.INVISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor1, View.VISIBLE);
		} else if (intBatteryLevel < 10) {
			remoteViews.setViewVisibility(R.id.batteryColor5, View.INVISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor4, View.INVISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor3, View.INVISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor2, View.INVISIBLE);
			remoteViews.setViewVisibility(R.id.batteryColor1, View.INVISIBLE);
		}

		remoteViews.setTextViewText(R.id.batteryLevel, batteryLevel + "%");
		appWidgetManager.updateAppWidget(widgetId, remoteViews);
	}
}
