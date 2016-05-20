package com.schytd.discount.ui;

import com.schytd.discount.bean.ConstantData;
import com.schytd.xianji.R;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityAboutUs extends BaseActivity implements OnClickListener {
	private TextView mText_Version;
	private ImageView mChangeNetwork;

	private void init() {
		mText_Version = (TextView) findViewById(R.id.software_version);
		mText_Version.setText(ConstantData.SOFT_VERSION);
		mChangeNetwork = (ImageView) findViewById(R.id.change_network);
		mChangeNetwork.setOnLongClickListener(longlistener);
	}

	private OnLongClickListener longlistener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			switch (v.getId()) {
			case R.id.change_network:
				if (ConstantData.URI.equals("http://code.schytd.com:8080/discountserver/api")) {
					ConstantData.URI = ConstantData.URI_LOCAL;
					Toast.makeText(ActivityAboutUs.this, ConstantData.URI, Toast.LENGTH_SHORT).show();
				} else {
					ConstantData.URI = ConstantData.URI_REMOTE;
					Toast.makeText(ActivityAboutUs.this, ConstantData.URI, Toast.LENGTH_SHORT).show();
				}
				break;

			default:
				break;
			}
			return false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		init();
	}

	// 重写返回键事件
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}

	}

}
