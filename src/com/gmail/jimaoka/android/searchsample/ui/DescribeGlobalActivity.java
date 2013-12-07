package com.gmail.jimaoka.android.searchsample.ui;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.gmail.jimaoka.android.searchsample.R;
import com.gmail.jimaoka.android.searchsample.constant.Const;
import com.gmail.jimaoka.android.searchsample.ui.adapter.DescribeGlobalListAdapter;
import com.gmail.jimaoka.android.sfviewbuilder.ui.SearchActivity;
import com.gmail.jimaoka.android.sfviewbuilder.vo.CompactLayoutVo;
import com.gmail.jimaoka.android.sfviewbuilder.vo.DescribeGlobalVo;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestRequest.RestMethod;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.sfnative.SalesforceActivity;

/**
 * DescribeGlobalの取得結果を一覧表示するActivity
 * @author junji imaoka
 *
 */
public class DescribeGlobalActivity extends SalesforceActivity {
	
	private RestClient client;
	private List<DescribeGlobalVo> describeGlobalDtos;
	private DescribeGlobalListAdapter adapter;
	private ProgressDialogFragment progressDialogfragment;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.describe_global_activity);
		Intent intent = getIntent();
		describeGlobalDtos = (List<DescribeGlobalVo>) intent.getSerializableExtra(Const.ARG_DESCRIBE_GLOBAL_VOS);
		
		setAdapters();
		setListners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.describe_global, menu);
		return true;
	}

	@Override
	public void onResume(RestClient client) {
		this.client = client;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bundle args = new Bundle();
		AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
		
		if(resultCode == RESULT_OK){
			switch (requestCode) {
			case Const.REQUEST_CODE_DESCRIBE_GLOBAL_ACTIVITY:
				Bundle extra = data.getExtras();
				@SuppressWarnings("unchecked")
				HashMap<String, Object> selectedItemMap = 
					(HashMap<String, Object>) extra.getSerializable(SearchActivity.ARG_SELECTED_ITEM);
				
				args.putString(Const.ARG_TITLE, getString(R.string.title_dialog_info));
				args.putString(Const.ARG_MESSAGE, "選択されたID:\n" + selectedItemMap.get("Id"));
				break;
			default:
				break;
			}
		}else if(resultCode == RESULT_CANCELED){
			args.putString(Const.ARG_TITLE, getString(R.string.title_dialog_warn));
			args.putString(Const.ARG_MESSAGE, "選択はキャンセルされました。");
		}
		
		alertDialogFragment.setArguments(args);
		alertDialogFragment.show(getFragmentManager(), "alert_dialog_fragment");
	}

	/**
	 * Adapter を設定します
	 */
	private void setAdapters() {
		adapter = new DescribeGlobalListAdapter(this, R.layout.describe_global_list_item, describeGlobalDtos);
		
		ListView lvDescribeGlobal = (ListView)findViewById(R.id.lv_describe_global);
		lvDescribeGlobal.setAdapter(adapter);
	}

	/**
	 * Listner を設定します
	 */
	private void setListners() {
		ListView lvDescribeGlobal = (ListView)findViewById(R.id.lv_describe_global);
		lvDescribeGlobal.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onSobjectItemClick(parent, view, position, id);
			}
		});
	}
	
	/**
	 * Sobject onItemClick 時の処理です
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	protected void onSobjectItemClick(AdapterView<?> parent, View view,	int position, long id) {
		Bundle args = new Bundle();
		
		args.putString(Const.ARG_MESSAGE, getString(R.string.msg_info_loading));
		progressDialogfragment = new ProgressDialogFragment();
		progressDialogfragment.setArguments(args);
		progressDialogfragment.show(getFragmentManager(), "progress_dialog_fragment");
		
		DescribeGlobalVo vo = describeGlobalDtos.get(position);
		
		sendGetCompactLayoutRequest(vo.getName(), vo.getLabel());
	}
	
	
	
	/**
	 * CompactLayout を取得する RestRequest を送信します 
	 * @param name
	 * @param label
	 */
	private void sendGetCompactLayoutRequest(final String name, final String label) {
		String apiVersion = getString(R.string.api_version);
		String path = "/services/data/" + apiVersion + "/sobjects/" + name + "/describe/compactLayouts/primary";
		
		RestRequest restRequest = new RestRequest(RestMethod.GET, path, null);
		client.sendAsync(restRequest, new AsyncRequestCallback() {
			
			@Override
			public void onSuccess(RestRequest request, RestResponse response) {
				progressDialogfragment.dismiss();
				
				try {
					JSONObject root = response.asJSONObject();
					if(root.has("errorCode")){
						Bundle args = new Bundle();
						args.putString(Const.ARG_ERROR_CODE, root.getString("errorCode"));
						args.putString(Const.ARG_MESSAGE, root.getString("message"));
						AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
						alertDialogFragment.setArguments(args);
						alertDialogFragment.show(getFragmentManager(), "alert_dialog_fragment");
					}else{
						CompactLayoutVo compactLayoutVo = new CompactLayoutVo(root);
						Intent intent = new Intent(DescribeGlobalActivity.this, SearchActivity.class);
						intent.putExtra(SearchActivity.ARG_COMPACT_LAYOUT_VO, compactLayoutVo);
						intent.putExtra(SearchActivity.ARG_NAME, name);
						intent.putExtra(SearchActivity.ARG_LABEL, label);
						startActivityForResult(intent, Const.REQUEST_CODE_DESCRIBE_GLOBAL_ACTIVITY);
					}
				} catch (Exception e) {
					Log.e(DescribeGlobalActivity.class.toString(), e.getMessage());
					displayMessage(e.getMessage());
				}
				
			}
			
			@Override
			public void onError(Exception exception) {
				progressDialogfragment.dismiss();
				Log.e(DescribeGlobalActivity.class.toString(), exception.getMessage());
				displayMessage(exception.getMessage());
			}
		});
	}
	
	/**
	 * メッセージを Toast で表示します
	 * @param message
	 */
	private void displayMessage(String message){
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

}
