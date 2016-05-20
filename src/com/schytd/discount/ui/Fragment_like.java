package com.schytd.discount.ui;

import com.schytd.xianji.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class Fragment_like extends Fragment implements OnClickListener {
	private TextView mTextView_shop, mTextView_room;
	private View mView_bar;
	private Animation translateAnimation;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_like_layout, container, false);
		init(view);
		return view;
	}

	private void init(View v) {
		mTextView_shop = (TextView) v.findViewById(R.id.like_shop);
		mTextView_room = (TextView) v.findViewById(R.id.like_room);
		mTextView_room.setOnClickListener(this);
		mTextView_shop.setOnClickListener(this);
		mView_bar = v.findViewById(R.id.like_bar);// tab 条
		mTextView_shop.setClickable(false);
	}

	// 点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.like_shop:
			initAnimation(2);
			mView_bar.startAnimation(translateAnimation);// 设置动画效果
			translateAnimation.startNow();
			mTextView_room.setClickable(true);
			mTextView_shop.setClickable(false);
			break;
		case R.id.like_room:
			initAnimation(1);
			mView_bar.startAnimation(translateAnimation);// 设置动画效果
			translateAnimation.startNow();
			mTextView_room.setClickable(false);
			mTextView_shop.setClickable(true);
			break;
		}
	}

	// bar 动画
	private int initScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		return screenWidth;
	}

	private void initAnimation(int a) {
		LayoutParams layoutParams = mView_bar.getLayoutParams();
		int view_width = layoutParams.width;
		int d = initScreenWidth() / 2;
		int margin = (d - view_width * 2) / 4;
		if (a == 1) {
			translateAnimation = new TranslateAnimation(0, 4 * margin + view_width, 0, 0);
		} else {
			translateAnimation = new TranslateAnimation(4 * margin + view_width, 0, 0, 0);
		}
		translateAnimation.setFillAfter(true);// 停在结束位置
		translateAnimation.setDuration(500); // 设置动画持续时间
	}
}
