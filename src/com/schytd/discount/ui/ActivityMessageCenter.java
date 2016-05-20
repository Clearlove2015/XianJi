package com.schytd.discount.ui;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.schytd.discount.bean.PushItem;
import com.schytd.discount.business.PushBusiness;
import com.schytd.discount.business.impl.PushBusinessImp;
import com.schytd.discount.ui.View.swipelistview.SwipeMenu;
import com.schytd.discount.ui.View.swipelistview.SwipeMenuCreator;
import com.schytd.discount.ui.View.swipelistview.SwipeMenuItem;
import com.schytd.discount.ui.View.swipelistview.SwipeMenuListView;
import com.schytd.discount.ui.View.swipelistview.SwipeMenuListView.OnMenuItemClickListener;
import com.schytd.discount.ui.View.swipelistview.SwipeMenuListView.OnSwipeListener;
import com.schytd.xianji.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityMessageCenter extends BaseActivity
		implements SwipeRefreshLayout.OnRefreshListener, OnClickListener {

	private List<PushItem> mData;
	private ProgressBar loading;
	private static MessageListTask mTask;
	private PushBusiness mPushBussiness;
	private int flag = 0;
	// 图片相关
	private DisplayImageOptions options;
	private String[] imageUriArray;
	private ImageLoadingListener animateFirstListener;
	// = new AnimateFirstDisplayListener();
	// 没有消息的时候
	private LinearLayout mLinearLayoutNoData;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_layout);
		init();
	}

	private void init() {
		mLinearLayoutNoData = (LinearLayout) this.findViewById(R.id.message_no_data);
		mPushBussiness = new PushBusinessImp(this);
		mTask = new MessageListTask();
		mData = new ArrayList<PushItem>();
		// mTask.execute("0");
	}

	private class MessageListTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			String type = params[0];
			try {
				if (type.equals("0")) {
					mData = mPushBussiness.getPushData();
				} else {
					mData = mPushBussiness.getPushDataByType(type);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			loading.setVisibility(ProgressBar.GONE);
			if (mData == null || mData.size() < 1) {
				mData = new ArrayList<PushItem>();
				mLinearLayoutNoData.setVisibility(View.VISIBLE);// 没有数据是显示
			} else {
				getImgPath();
			}
			mAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute() {
			// setImg();
			mData.clear();
			loading.setVisibility(ProgressBar.VISIBLE);
		}

	}

	// 得到图片的路径
	private void getImgPath() {
		for (int i = 0; i < mData.size(); i++) {
			PushItem item = mData.get(i);
			imageUriArray[i] = item.getImgs();
		}
	}

	BaseAdapter mAdapter = new BaseAdapter() {
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.item_list_app, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (imageUriArray != null) {
				// imageLoader.displayImage(imageUriArray[position],
				// holder.iv_icon, options, animateFirstListener);
			}
			holder.tv_name.setText(mData.get(position).getTitle());
			return convertView;
		}

		class ViewHolder {
			ImageView iv_icon;
			TextView tv_name;

			public ViewHolder(View view) {
				iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				tv_name = (TextView) view.findViewById(R.id.tv_name);
				view.setTag(this);
			}
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	};

	@Override
	public void onRefresh() {
		mTask = new MessageListTask();
		if (flag == 0) {
			mTask.execute("0");
		} else if (flag == 1) {
			mTask.execute("1");
		} else {
			mTask.execute("2");
		}
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.message_back:
			finish();
			break;
		default:
			break;
		}

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

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}

}
