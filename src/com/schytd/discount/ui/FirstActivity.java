package com.schytd.discount.ui;

import java.util.ArrayList;
import java.util.List;

import com.schytd.discount.bean.IndexImage;
import com.schytd.discount.ui.View.DepthPageTransformer;
import com.schytd.xianji.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class FirstActivity extends FragmentActivity {
	private ViewPager mViewPager_index;
	private int[] icons = { R.drawable.teaching_icon_1, R.drawable.teaching_icon_2, R.drawable.teaching_icon_3,
			R.drawable.teaching_icon_4 };
	private List<IndexImage> mData;
	private ImageView[] views;
	// 原点
	private ViewGroup mViewGroup_point;
	FragmentFirst fragment_1, fragment_2, fragment_3, fragment_4;
	private List<FragmentFirst> mListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_layout);
		init();
	}

	private void init() {
		mViewGroup_point = (ViewGroup) this.findViewById(R.id.index_view_point);
		mListFragment = new ArrayList<FragmentFirst>();
		mData = (List<IndexImage>) getIntent().getSerializableExtra("path");
		mViewPager_index = (ViewPager) this.findViewById(R.id.first_viewpager);
		addPointView();
		mViewPager_index.setPageTransformer(true, new DepthPageTransformer());
		mViewPager_index.addOnPageChangeListener(new MyPageChangeListener());
		fragment_1 = new FragmentFirst(icons[0], 1);
		fragment_2 = new FragmentFirst(icons[1], 1);
		fragment_3 = new FragmentFirst(icons[2], 1);
		fragment_4 = new FragmentFirst(icons[3], 2, mData);
		mListFragment.add(fragment_1);
		mListFragment.add(fragment_2);
		mListFragment.add(fragment_3);
		mListFragment.add(fragment_4);
		mViewPager_index.setAdapter(new MyAdapter(getSupportFragmentManager()));
	}

	private class MyAdapter extends FragmentStatePagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}

		@Override
		public int getCount() {
			return mListFragment.size();
		}

		@Override
		public Fragment getItem(int position) {
			return mListFragment.get(position);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	};

	private void addPointView() {
		views = new ImageView[4];
		mViewGroup_point.removeAllViews();
		for (int i = 0; i < 4; i++) {
			ImageView v = new ImageView(this);
			views[i] = v;
			if (i == 0) {
				views[i].setBackgroundResource(R.drawable.seller_detail_view_shape2);
			} else {
				views[i].setBackgroundResource(R.drawable.seller_detail_view_shape1);
			}
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 15;
			params.height = 15;
			params.width = 15;
			v.setScaleType(ScaleType.CENTER_CROP);
			v.setLayoutParams(params);
			mViewGroup_point.addView(views[i]);
		}
	}

	// view原点切换
	private void changeView(int position) {
		for (int i = 0; i < views.length; i++) {
			if (position == 3) {
				mViewGroup_point.setVisibility(View.GONE);
				return;
			} else {
				mViewGroup_point.setVisibility(View.VISIBLE);
			}
			if (i == position) {
				views[i].setBackgroundResource(R.drawable.seller_detail_view_shape2);
			} else {
				views[i].setBackgroundResource(R.drawable.seller_detail_view_shape1);
			}
		}
	}

	// viewpager适配器
	// 页面改变监听器  
	private class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int position) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			changeView(position);
		}
	}
}
