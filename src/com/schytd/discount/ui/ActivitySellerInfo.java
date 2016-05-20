package com.schytd.discount.ui;

import com.schytd.discount.ui.View.SystemBarTintManager;
import com.schytd.discount.ui.View.TabPageIndicator;
import com.schytd.xianji.R;
import com.umeng.socialize.bean.SHARE_MEDIA;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class ActivitySellerInfo extends ActivityShare implements OnClickListener {
	private static final String[] CONTENT = new String[] { "今天\n11-30", "周一\n12-01", "周二\n12-02", "周三\n12-03",
			"周四\n11-25", "周五\n11-21", "周六\n11-22" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.white);// 通知栏所需颜色
		}
		setContentView(R.layout.activity_sller_detail_info_layout);

		FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

		ViewPager pager = (ViewPager) findViewById(R.id.my_pager);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.tabpageindicator);
		indicator.setViewPager(pager);
		init();
	}

	private void init() {
		this.findViewById(R.id.share).setOnClickListener(this);
		this.findViewById(R.id.seller_info_back).setOnClickListener(this);

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

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length];
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share:
			// 是否只有已登录用户才能打开分享选择页
			mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
					SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN);
			mController.openShare(ActivitySellerInfo.this, false);
			break;
		case R.id.seller_info_back:
			finish();
			break;
		default:
			break;
		}

	}
}
