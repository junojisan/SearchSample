package com.gmail.jimaoka.android.searchsample.ui;


import com.gmail.jimaoka.android.searchsample.R;
import com.gmail.jimaoka.android.searchsample.constant.Const;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * アラート用 DialogFragment
 * @author junji imaoka
 *
 */
public class AlertDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = getArguments();
		String title = args.getString(Const.ARG_TITLE, getString(R.string.title_dialog_error));
		String message = args.getString(Const.ARG_ERROR_CODE, "");
		
		if(TextUtils.isEmpty(message)){
			message = args.getString(Const.ARG_MESSAGE, "");
		}else{
			message += "\n" + args.getString(Const.ARG_MESSAGE, "");
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(R.string.lbl_yes, null);
		
		return builder.create();
	}
}
