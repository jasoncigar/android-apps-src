package com.jk.org.battery;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BatteryWidget extends AppWidgetProvider {

	Intent intent;

	@Override
	public void onEnabled(Context context) {
		Log.e("Widget Battery", "In enabled");
	}


	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.e("Widget Battery", "In onUpdate");
		intent = new Intent(context.getApplicationContext(),
				BatteryService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
		context.startService(intent);
	}

}
