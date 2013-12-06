package com.gmail.jimaoka.android.searchsample.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;

import com.gmail.jimaoka.android.searchsample.R;

public class ProgressDialogFragment extends DialogFragment {
	public static final String ARG_TITLE = "arg_title";
	public static final String ARG_MESSAGE = "arg_message";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = getArguments();
		String title = args.getString(ARG_TITLE, "");
		String message = args.getString(ARG_MESSAGE, getString(R.string.msg_info_loading));
		
		ProgressDialog dialog = new ProgressDialog(getActivity());
		if(!TextUtils.isEmpty(title)){
			dialog.setTitle(title);
		}
		dialog.setMessage(message);
		
		return dialog;
	}
}
