package com.schytd.discount.ui;

import com.schytd.xianji.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class Fragment_my extends Fragment implements OnClickListener {
	Intent intent_login;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_my_layout, container, false);
		init(view);
		return view;
	}

	private void init(View v) {
		intent_login = new Intent(getActivity(), ActivityLogin.class);
		v.findViewById(R.id.my_code).setOnClickListener(this);
		v.findViewById(R.id.test_10).setOnClickListener(this);
		v.findViewById(R.id.test_11).setOnClickListener(this);
		v.findViewById(R.id.test_12).setOnClickListener(this);
		v.findViewById(R.id.test_13).setOnClickListener(this);
		v.findViewById(R.id.test_14).setOnClickListener(this);
		v.findViewById(R.id.test_15).setOnClickListener(this);
		v.findViewById(R.id.test_16).setOnClickListener(this);
		v.findViewById(R.id.test_20).setOnClickListener(this);
		v.findViewById(R.id.test_21).setOnClickListener(this);
		v.findViewById(R.id.test_30).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_code:
			startActivity(intent_login);
			break;
		case R.id.test_10:
			startActivity(intent_login);
			break;
		case R.id.test_11:
			startActivity(intent_login);

			break;
		case R.id.test_12:
			startActivity(intent_login);

			break;
		case R.id.test_13:
			startActivity(intent_login);

			break;
		case R.id.test_14:
			startActivity(intent_login);

			break;
		case R.id.test_15:
			startActivity(intent_login);

			break;
		case R.id.test_16:
			// 浏览历史
			startActivity(new Intent(getActivity(), ActivityReadHistory.class));
			break;
		case R.id.test_20:
			// 设置
			startActivity(new Intent(getActivity(), ActivitySetting.class));
			break;
		case R.id.test_21:
			// 关于我们
			startActivity(new Intent(getActivity(), ActivityAboutUs.class));
			break;
		case R.id.test_30:
			// 关于我们
			startActivity(new Intent(getActivity(), ActivityMessageCenter.class));
			break;
		default:
			break;
		}

	}

}
