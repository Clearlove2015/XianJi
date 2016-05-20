package com.schytd.discount.ui;

import com.schytd.xianji.R;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class ActivityRankHistory extends FragmentActivity {
	private ViewPager mViewPager;
	// 自动滑动条
	private ImageView mCursor;
	// 图片宽度
	private int imgwidhth = 0;
	private int currPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank_history);
		init();

	}

	private void init() {
		mViewPager = (ViewPager) this
				.findViewById(R.id.activity_rank_viewpager);
		mViewPager.setAdapter(mFragmentPagerAdapter);
		// 初始化游标
		initCursor();
		mViewPager.addOnPageChangeListener(new MyPageChangeListener());
	}

	// 自动滑动条
	private void initCursor() {
		mCursor = (ImageView) this.findViewById(R.id.viewpager_rank_tab);
		// 得到imageview的参数设置器
		LayoutParams layoutParams = mCursor.getLayoutParams();

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 获取屏幕的宽度
		int screenWidth = dm.widthPixels;
		imgwidhth = screenWidth / 2;
		// 为屏幕的一半 屏幕宽度除以 viewpager的页数
		layoutParams.width = imgwidhth;
		// 设置具体大小
		mCursor.setLayoutParams(layoutParams);
		// 计算偏移量
		// offset = (screenWidth / mTitle.length - imgwidhth) / 2;
		// 设置图片的位置
		Matrix matrix = new Matrix();
		matrix.postTranslate(0, 0);

		mCursor.setImageMatrix(matrix);

	}

	// 点击事件
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.get_rank:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.use_rank:
			mViewPager.setCurrentItem(1);
			break;
		default:
			break;
		}
	}

	// 页面改变监听器  
	private class MyPageChangeListener implements OnPageChangeListener {
		int one = imgwidhth;

		@Override
		public void onPageScrollStateChanged(int position) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			Animation animation = null;
			switch (position) {
			case 0:
				if (currPosition == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				}
				break;
			case 1:
				if (currPosition == 0) {
					animation = new TranslateAnimation(0, one, 0, 0);
				}
				break;

			}
			currPosition = position;
			animation.setFillAfter(true);//  True:图片停在动画结束位置  
			animation.setDuration(50);
			mCursor.startAnimation(animation);
		}
	}

	// viewpager适配器
	private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(
			getSupportFragmentManager()) {

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new FragmentRankGet(getApplicationContext());
				break;

			case 1:
				fragment = new FragmentRankUse(getApplicationContext());
				break;
			}
			return fragment;
		}
	};

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
}
