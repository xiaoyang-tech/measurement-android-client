package cn.xymind.healthdetection.synthesis.androidsdksamples;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


public class XProgressDialog extends Dialog {

	private TextView messageTv;

	private String message = new String();

	public XProgressDialog(Context context) {
		super(context, R.style.progressDialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_progress_layout);

		View progress = findViewById(R.id.progress);
		AnimationDrawable drawable = (AnimationDrawable) progress
				.getBackground();
		drawable.start();

		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.gravity = Gravity.CENTER;
		getWindow().setAttributes(params);


		messageTv = (TextView) findViewById(R.id.tv_message);
		messageTv.setText(message);
	}

	public void setMessage(String msg) {
		this.message = msg;
		if (messageTv != null) {
			messageTv.setText(msg);
		}
	}

	public void setTipMessage(String msg) {
		this.message = msg;
		if (messageTv != null) {
			messageTv.setText(msg);
			messageTv.setTextColor(Color.RED);
			messageTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,20.0f);
		}
	}
}
