package com.schytd.discount.ui;

import com.schytd.discount.ui.View.SystemBarTintManager;
import com.schytd.xianji.R;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

public class BaseActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.bar_bg_color);// 通知栏所需颜色
		}
	}

	// 4.4修改状态栏
	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	//https 网络请求
	public abstract class MyResultCallback<T> extends ResultCallback<T> {

		@Override
		public void onBefore(Request request) {
			super.onBefore(request);
		}

		@Override
		public void onAfter() {
			super.onAfter();
		}
	}
}
