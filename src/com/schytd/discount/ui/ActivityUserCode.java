package com.schytd.discount.ui;

import java.util.Timer;
import java.util.TimerTask;

import com.schytd.xianji.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityUserCode extends BaseActivity implements OnClickListener {
	private ProgressBar mProgress_level;
	private TextView mTextView_now;
	private int start = 0, max = 500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_code_layout);
		init();
	}

	Timer mTimer;

	private void init() {
		mTextView_now = (TextView) this.findViewById(R.id.now_level_value);
		mProgress_level = (ProgressBar) this.findViewById(R.id.level_progress);
		this.findViewById(R.id.act_back).setOnClickListener(this);
		mTimer = new Timer();
		TimerTask mTimerTask = new TimerTask() {
			@Override
			public void run() {
				mProgress_level.setProgress(start);
				runOnUiThread(new Runnable() {
					public void run() {
						mTextView_now.setText("" + start);
					}
				});
				start += 10;
				if (start == max) {
					mTimer.cancel();
				}
			}
		};
		// 开始一个定时任务
		mTimer.schedule(mTimerTask, 50, 50);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mTimer.cancel();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_back:
			finish();
			break;

		default:
			break;
		}

	}

}
