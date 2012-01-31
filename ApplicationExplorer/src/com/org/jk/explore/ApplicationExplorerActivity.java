package com.org.jk.explore;

import java.util.List;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TabHost;

public class ApplicationExplorerActivity extends TabActivity {

	TabHost mTabHost;
	ListView installedAppsView;
	ListView runningAppsView;
	ListView runningServiceView;
	String[] processName;
	String[] appName;
	Drawable[] appIcons;
	ProgressDialog loadingDialog;

	PackageManager pm;
	ActivityManager am;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		pm = getPackageManager();
		am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		new LoadApps().execute("");
	}

	class LoadApps extends AsyncTask<String, String, String> {

		ApplicationAdapter installedAppsAdapter;
		ApplicationAdapter runningAppsAdapter;
		ApplicationAdapter servicesAdapter;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog = ProgressDialog.show(
					ApplicationExplorerActivity.this, "Please wait",
					"Loading data ...", true);
			loadingDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			List<ApplicationInfo> packages = pm
					.getInstalledApplications(PackageManager.GET_META_DATA);

			processName = new String[packages.size()];
			appName = new String[packages.size()];
			appIcons = new Drawable[packages.size()];

			if (packages != null) {
				for (int i = 0; i < packages.size(); i++) {
					try {
						appName[i] = pm.getApplicationLabel(
								pm.getApplicationInfo(
										packages.get(i).processName,
										PackageManager.GET_META_DATA))
								.toString();
					} catch (NameNotFoundException e) {

					}
					processName[i] = packages.get(i).processName;
					appIcons[i] = pm.getApplicationIcon(packages.get(i));
				}
			}
			publishProgress("All");

			processName = null;
			appName = null;
			appIcons = null;

			List<ActivityManager.RunningAppProcessInfo> runningAppList = am
					.getRunningAppProcesses();

			processName = new String[runningAppList.size()];
			appName = new String[runningAppList.size()];
			appIcons = new Drawable[runningAppList.size()];

			if (runningAppList != null) {
				for (int i = 0; i < runningAppList.size(); i++) {
					try {
						appName[i] = pm.getApplicationLabel(
								pm.getApplicationInfo(
										runningAppList.get(i).processName,
										PackageManager.GET_META_DATA))
								.toString();
						appIcons[i] = pm.getApplicationIcon(runningAppList
								.get(i).processName);
					} catch (NameNotFoundException e) {

					}
					processName[i] = runningAppList.get(i).processName;

				}
			}
			publishProgress("Running");

			processName = null;
			appName = null;
			appIcons = null;

			List<ActivityManager.RunningServiceInfo> runningServiceList = am
					.getRunningServices(1024);

			processName = new String[runningServiceList.size()];
			appIcons = new Drawable[runningServiceList.size()];

			if (runningServiceList != null) {
				for (int i = 0; i < runningServiceList.size(); i++) {

					processName[i] = runningServiceList.get(i).service
							.getClassName();
					try {
						appIcons[i] = pm.getApplicationIcon(runningServiceList
								.get(i).service.getClassName());
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
			publishProgress("Services");

			return "completed";
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			if (progress[0].equals("All")) {

				installedAppsView = (ListView) findViewById(R.id.listview1);
				installedAppsAdapter = new ApplicationAdapter(
						ApplicationExplorerActivity.this, 0, processName,
						appIcons, appName);
				installedAppsView.setAdapter(installedAppsAdapter);

			} else if (progress[0].equals("Running")) {
				runningAppsView = (ListView) findViewById(R.id.listview2);
				runningAppsAdapter = new ApplicationAdapter(
						ApplicationExplorerActivity.this, 0, processName,
						appIcons, appName);
				runningAppsView.setAdapter(runningAppsAdapter);
				runningAppsView.setVisibility(View.INVISIBLE);

			} else if (progress[0].equals("Services")) {
				runningServiceView = (ListView) findViewById(R.id.listview3);
				servicesAdapter = new ApplicationAdapter(
						ApplicationExplorerActivity.this, 0, processName,
						appIcons, appName);
				runningServiceView.setAdapter(servicesAdapter);
				runningServiceView.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			loadingDialog.dismiss();
			mTabHost = getTabHost();

			mTabHost.addTab(mTabHost.newTabSpec("All").setIndicator("All")
					.setContent(R.id.listview1));
			mTabHost.addTab(mTabHost.newTabSpec("Running")
					.setIndicator("Running").setContent(R.id.listview2));
			mTabHost.addTab(mTabHost.newTabSpec("Services")
					.setIndicator("Services").setContent(R.id.listview3));
			runningAppsView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					int pid = android.os.Process
							.getUidForName(runningAppsAdapter.processName[arg2]);
					Log.e("APPMAN", "kill "
							+ runningAppsAdapter.processName[arg2] + " " + pid);
					android.os.Process.killProcess(pid);

				}
			});
			mTabHost.setCurrentTab(0);

		}
	}
}