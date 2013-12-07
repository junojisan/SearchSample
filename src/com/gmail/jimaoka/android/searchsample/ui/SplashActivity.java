package com.gmail.jimaoka.android.searchsample.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.gmail.jimaoka.android.searchsample.R;
import com.gmail.jimaoka.android.searchsample.constant.Const;
import com.gmail.jimaoka.android.sfviewbuilder.vo.DescribeGlobalVo;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.sfnative.SalesforceActivity;

/**
 * Splash Activity
 * 
 * @author junji imaoka
 *
 */
public class SplashActivity extends SalesforceActivity {

	private RestClient client;
	private ArrayList<DescribeGlobalVo> describeVos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		getActionBar().hide();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onResume(RestClient client) {
		this.client = client;
		sendGetDescribeGlobalRequest();
	}
	
	/**
	 * DescribeGlobal を取得する RestRequest を送信します
	 * @param
	 * @return
	 */
	private void sendGetDescribeGlobalRequest() {
		describeVos = new ArrayList<DescribeGlobalVo>();
		RestRequest restRequest = RestRequest.getRequestForDescribeGlobal(getString(R.string.api_version));
		client.sendAsync(restRequest, new AsyncRequestCallback() {
			@Override
			public void onSuccess(RestRequest request, RestResponse response) {
				try {
					JSONArray records = response.asJSONObject().getJSONArray("sobjects");
					for(int i = 0; i < records.length(); i++){
						JSONObject jObj = (JSONObject) records.get(i);
						if(jObj.getBoolean("layoutable") && !jObj.getBoolean("customSetting")){
							describeVos.add(new DescribeGlobalVo(jObj));
						}
					}
					
					Intent intent = new Intent(SplashActivity.this, DescribeGlobalActivity.class);
					intent.putExtra(Const.ARG_DESCRIBE_GLOBAL_VOS, describeVos);
					startActivity(intent);
					finish();
					
				} catch (Exception e) {
					displayMessage(e.getMessage());
				}
				
			}
			
			@Override
			public void onError(Exception exception) {
				displayMessage(exception.getMessage());
			}
		});
	}
	
	/**
	 * メッセージを Toast で表示します
	 * @param message
	 * @return
	 */
	private void displayMessage(String message){
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
}
