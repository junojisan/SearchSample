package com.gmail.jimaoka.android.searchsample.ui;

import com.gmail.jimaoka.android.searchsample.R;
import com.gmail.jimaoka.android.searchsample.R.layout;
import com.gmail.jimaoka.android.searchsample.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DescribeGlobalActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.describe_global_activity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.describe_global, menu);
		return true;
	}

}
