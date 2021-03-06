package com.schytd.discount.ui;

import java.io.Serializable;
import java.util.List;

import com.schytd.discount.bean.IndexImage;
import com.schytd.xianji.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FragmentFirst extends Fragment {
	public static int REQUEST_MAIN = 501;
	private int iconId, type;
	// 图片
	private LinearLayout mTeachBg;
	private LinearLayout mLinearLayout_btn;
	// 首页轮播图path
	private List<IndexImage> mdata;

	public FragmentFirst(int iconId, int type) {
		this.iconId = iconId;
		this.type = type;
	}

	public FragmentFirst(int iconId, int type, List<IndexImage> mdata) {
		this.iconId = iconId;
		this.type = type;
		this.mdata = mdata;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_index_layout, container, false);
		init(view);
		return view;
	}

	private void init(View v) {
		mTeachBg = (LinearLayout) v.findViewById(R.id.teaching_bg);
		mTeachBg.setBackgroundResource(iconId);
		if (type == 2) {
			mLinearLayout_btn = (LinearLayout) v.findViewById(R.id.first_main_start);
			mLinearLayout_btn.setVisibility(View.VISIBLE);
			mLinearLayout_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(), MainActivity.class);
					i.putExtra("path", (Serializable) mdata);
					startActivityForResult(i, REQUEST_MAIN);
				}
			});
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_MAIN) {
			getActivity().finish();
		}
	}
}
