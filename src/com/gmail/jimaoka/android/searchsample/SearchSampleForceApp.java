package com.gmail.jimaoka.android.searchsample;


import android.app.Application;

import com.gmail.jimaoka.android.searchsample.ui.SplashActivity;
import com.salesforce.androidsdk.app.SalesforceSDKManager;

/**
 * Application class for our application.
 */
public class SearchSampleForceApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		SalesforceSDKManager.initNative(getApplicationContext(), new KeyImpl(), SplashActivity.class);
	}
}
