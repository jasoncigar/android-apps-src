package com.org.jk.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class RSSReaderActivity extends Activity {

	RSSArrayAdapter feedList;
	ProgressDialog loadingDialog;
	Button bt;
	String feedURL;
	EditText feedURLField;
	ListView itemList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		feedURLField = (EditText) findViewById(R.id.feedURLfield);
		bt = (Button) findViewById(R.id.fetchFeeds);
		bt.setOnClickListener(clicked);
		itemList = (ListView) findViewById(R.id.feedItems);

	}

	private OnClickListener clicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if ((Button) v == bt) {
				feedURL = feedURLField.getText().toString();
				new FetchFeeds().execute(feedURL);
			}

		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		finish();
	}

	class FetchFeeds extends AsyncTask<String, String, String> {

		String[] titles;
		String[] link;
		String[] description;
		boolean isError = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = ProgressDialog.show(RSSReaderActivity.this,
					"Please wait", "Loading data ...", true);
			loadingDialog.show();
		}

		@Override
		protected String doInBackground(String... feedURLs) {
			RSSHandler handler = new RSSHandler();

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = null;
			try {
				parser = factory.newSAXParser();
				parser.parse(new URL(feedURLs[0]).openStream(), handler);
				List<RSSFeedItems> items = new ArrayList<RSSFeedItems>();

				items = handler.getItems();

				Iterator<RSSFeedItems> itrate = items.iterator();
				Log.e("RSS", items.size() + "");
				titles = new String[items.size()];
				link = new String[items.size()];
				description = new String[items.size()];

				int i = 0;
				while (itrate.hasNext()) {
					RSSFeedItems temp = itrate.next();
					titles[i] = temp.getTitle();
					link[i] = temp.getLink();
					description[i] = temp.getDescription();
					i++;
				}
			} catch (ParserConfigurationException e) {
				loadingDialog.dismiss();
				isError = true;
				publishProgress("ParserConfiguration Exception");
			} catch (SAXException e) {
				loadingDialog.dismiss();
				publishProgress("SAXException");
				isError = true;
			} catch (MalformedURLException e) {
				loadingDialog.dismiss();
				publishProgress("Invalid URL");
				isError = true;
			} catch (IOException e) {
				loadingDialog.dismiss();
				publishProgress("IOException");
				isError = true;
			}
			if (isError) {
				return "error";
			} else {
				return "complete";
			}
		}

		@Override
		protected void onProgressUpdate(String... errorMsg) {
			Toast.makeText(RSSReaderActivity.this, errorMsg[0],
					Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals("complete")) {
				loadingDialog.dismiss();
				feedList = new RSSArrayAdapter(RSSReaderActivity.this, titles,
						description, link);
				itemList.setAdapter(feedList);
				itemList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String openLink = feedList.getLink(arg2);
						Intent browserIntent = new Intent(Intent.ACTION_VIEW,
								Uri.parse(openLink));
						startActivity(browserIntent);
					}
				});
			}
		}
	}
}