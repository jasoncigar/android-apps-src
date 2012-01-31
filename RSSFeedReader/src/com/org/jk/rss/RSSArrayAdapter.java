package com.org.jk.rss;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RSSArrayAdapter extends ArrayAdapter<String> {

	protected Activity context;
	protected String[] titles;
	protected String[] description;
	protected String[] link;
	protected String headerTitle;
	protected String headerDescription;
	protected String headerLink;

	public RSSArrayAdapter(Activity context, String[] titles,
			String[] description, String[] link) {
		super(context, R.layout.row_layout, titles);
		this.context = context;
		this.titles = titles;
		this.description = description;
		this.link = link;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflate = context.getLayoutInflater();
			convertView = inflate.inflate(R.layout.row_layout, null);
		}

		((TextView) convertView.findViewById(R.id.title))
				.setText(titles[position]);

		String tempDescription = Html.fromHtml(description[position])
				.toString().replaceAll("[^a-zA-Z\\s.',\"]", "");

		((TextView) convertView.findViewById(R.id.description))
				.setText(tempDescription.trim() + "\n");

		return convertView;
	}

	public String getLink(int position) {
		return link[position];
	}

}
