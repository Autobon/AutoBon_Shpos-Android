package cn.com.incardata.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.com.incardata.autobon_shops.R;

public class CaptureDialog extends Dialog{
	
	private TextView mTvMessage;
	private CharSequence mMsg = "";

	public CaptureDialog(Context context, CharSequence message, boolean cancel, View.OnClickListener sureListener, View.OnClickListener cancelListener) {
		super(context, R.style.AlertDialogTheme);
		mMsg = message;
		init(cancel, sureListener, cancelListener);
	}

	public void setMsg(CharSequence message) {
		this.mMsg = message;
		mTvMessage.setText(mMsg);
	}

	private void init(boolean cancel, View.OnClickListener sureListener, View.OnClickListener cancelListener) {
		setContentView(R.layout.dialog_capture);
		mTvMessage = (TextView) findViewById(R.id.capture_message);
		Button btnOk = (Button) findViewById(R.id.btn_ok_capture);
		Button btnCancel = (Button) findViewById(R.id.btn_cancel_capture);
		if (cancel) {
			btnCancel.setVisibility(View.VISIBLE);
			btnCancel.setOnClickListener(cancelListener);
		} else {
			btnCancel.setVisibility(View.GONE);
		}
		btnOk.setOnClickListener(sureListener);
		mTvMessage.setText(mMsg);
	}

}
