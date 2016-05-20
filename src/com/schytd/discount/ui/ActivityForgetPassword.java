package com.schytd.discount.ui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.schytd.discount.business.UserBusiness;
import com.schytd.discount.business.impl.UserBusinessImpl;
import com.schytd.discount.tools.StrTools;
import com.schytd.xianji.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityForgetPassword extends BaseActivity {
	// 电话号码
	private EditText mEditText_phoneNum;
	// 新密码
	private EditText mEditText_newPassword;
	private EditText mEditText_Code;
	//
	private TextView mTextView_getCode, mTextView_alter;
	private CodeThread mCodeThread;
	private UserBusiness mUserBussiness;
	// 倒计时
	private TimeCount mTimeCount;
	// 线程池
	private ExecutorService pool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_psd_layout);
		init();
	}

	private void init() {
		mUserBussiness = new UserBusinessImpl(this);
		mTimeCount = new TimeCount(60000, 1000);
		mEditText_phoneNum = (EditText) this.findViewById(R.id.alter_phone_num);
		mTextView_getCode = (TextView) this.findViewById(R.id.alter_btn_getCode);
		mTextView_alter = (TextView) this.findViewById(R.id.btn_alter);
		mEditText_Code = (EditText) this.findViewById(R.id.alter_sms_code);
		pool = Executors.newSingleThreadExecutor();
		mTextView_getCode.setOnClickListener(mClickListener);
		mTextView_alter.setOnClickListener(mClickListener);
		findViewById(R.id.forget_back).setOnClickListener(mClickListener);
	}

	private String phoneNum = null;
	private String caCode = null;
	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.alter_btn_getCode:
				phoneNum = mEditText_phoneNum.getText().toString();
				// 电话号码必须11位
				if (StrTools.isNull(phoneNum)) {
					Toast.makeText(getApplicationContext(), "电话号码不能为空！", Toast.LENGTH_SHORT).show();
				} else if (!StrTools.isPhoneNum(phoneNum)) {
					Toast.makeText(getApplicationContext(), "电话号码长度非法！", Toast.LENGTH_SHORT).show();
				} else {
					mCodeThread = new CodeThread(phoneNum);
					pool.execute(mCodeThread);
				}
				break;

			case R.id.btn_alter:
				phoneNum = mEditText_phoneNum.getText().toString();
				caCode = mEditText_Code.getText().toString();
				// 电话号码不能为空 新密码不能为空 密码长度大于6 电话号码必须为11位
				if (StrTools.isNull(phoneNum)) {
					Toast.makeText(getApplicationContext(), "电话号码不能为空！", Toast.LENGTH_SHORT).show();
					return;
				} else if (!StrTools.isPhoneNum(phoneNum)) {
					Toast.makeText(getApplicationContext(), "电话号码长度非法！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (StrTools.isNull(caCode)) {
					Toast.makeText(getApplicationContext(), "验证码不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				// 准备修改 号码、验证码、新密码
				// new AlterTask().execute(phoneNum, caCode, newPassword);
				break;
			case R.id.forget_back:
				finish();
				break;
			}

		}
	};

	// 异步任务 换密码
	private class AlterTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			// 禁用按钮
			mTextView_alter.setEnabled(false);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			boolean result = false;
			try {
				result = mUserBussiness.forgetUserPassword(params[0], params[1], params[2]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		protected void onPostExecute(Boolean result) {
			// 解开按钮
			mTextView_alter.setEnabled(true);
			if (result) {
				Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
			}
		};
	};

	// 获取验证码
	private class CodeThread implements Runnable {
		String params = null;

		public CodeThread(String params) {
			this.params = params;
		}

		@Override
		public void run() {
			Boolean result = false;

			try {

//				result = mUserBussiness.getCaptchaCode(params);
				
				if (true) {
					// 60秒后解锁
					runOnUiThread(new Runnable() {
						public void run() {
							mTimeCount.start();
						}
					});
				}
				Log.d("+++++", "CodeThread");
				if (!result) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), "获取验证码失败！", Toast.LENGTH_SHORT).show();
						}
					});
				}
			} catch (Exception e) {

			}

		}
	}

	// 验证码倒计时
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			mTextView_getCode.setText("重新获取");
			mTextView_getCode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			mTextView_getCode.setClickable(false);
			mTextView_getCode.setText(millisUntilFinished / 1000 + "秒");
		}
	}
}
