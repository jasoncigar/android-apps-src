package com.org.jk.viewswitch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ViewSwitcher;

public class ViewSwitcherActivity extends Activity implements OnClickListener {
	ViewSwitcher vs;
	Button bt1;
	Button bt2;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		vs = (ViewSwitcher) findViewById(R.id.switcher);
		bt1 = (Button) findViewById(R.id.swtichtosecond);
		bt2 = (Button) findViewById(R.id.swtichtofirst);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.swtichtosecond) {
			vs.showNext();
		} else if (v.getId() == R.id.swtichtofirst) {
			vs.showPrevious();
		}
	}
}