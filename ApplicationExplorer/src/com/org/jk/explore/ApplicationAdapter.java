package com.org.jk.explore;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplicationAdapter extends ArrayAdapter<String> {

	Activity context;
	String[] processName;
	Drawable[] appIcons;
	String[] appName;

	public ApplicationAdapter(Activity context, int textViewResourceId,
			String[] processName, Drawable[] appIcons, String[] appName) {
		super(context, textViewResourceId, processName);
		this.processName = processName;
		this.context = context;
		this.appIcons = appIcons;
		this.appName = appName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflate = context.getLayoutInflater();
			convertView = inflate.inflate(R.layout.row_layout, null);
		}
		if (appName == null) {
			((TextView) convertView.findViewById(R.id.applicationName))
					.setText(processName[position]);
		} else if (appName[position] != null) {
			((TextView) convertView.findViewById(R.id.applicationName))
					.setText(appName[position]);
		} else {
			((TextView) convertView.findViewById(R.id.applicationName))
					.setText(processName[position]);
		}
		if (appIcons[position] != null) {
			((ImageView) convertView.findViewById(R.id.applicationIcon))
					.setImageDrawable(appIcons[position]);
		}
		return convertView;
	}

}
