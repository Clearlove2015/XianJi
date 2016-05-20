package com.schytd.discount.ui;

import java.util.ArrayList;
import java.util.Collections;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.schytd.discount.bean.SellerInfoItem;
import com.schytd.discount.business.SellerBusiness;
import com.schytd.discount.business.impl.SellerBusinessImp;
import com.schytd.discount.tools.StrTools;
import com.schytd.discount.ui.View.MyListView;
import com.schytd.discount.ui.View.SystemBarTintManager;
import com.schytd.xianji.R;

import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityReadHistory extends ImageActivity {
	private MyListView mListView;
	private ArrayList<SellerInfoItem> mData;
	private String[] imageUriArray;
	private SellerBusiness mSellerBussiness;
	private ArrayList<String> mTime;
	private ProgressBar mProgressBar;
	private Double mLat, mLng;
	private DisplayImageOptions options;
	// 没有数据是
	private LinearLayout mLinearNoData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.bar_bg_color);// 通知栏所需颜色
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_history_layout);
		init();
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

	private void init() {
		mLinearNoData = (LinearLayout) this.findViewById(R.id.read_history_no_data);
		options = setImg();// 设置图片加载
		mData = new ArrayList<SellerInfoItem>();
		mTime = new ArrayList<String>();
		mLat = getIntent().getDoubleExtra("lat", 0);
		mLng = getIntent().getDoubleExtra("lng", 0);
		mListView = (MyListView) this.findViewById(R.id.read_his_listview);
		applyScrollListener(mListView);
		mSellerBussiness = new SellerBusinessImp(this);
		mListView.setAdapter(mAdapter);
		mProgressBar = (ProgressBar) this.findViewById(R.id.read_progress_bar);
		new ReadHisTask().execute();
	}

	private void applyScrollListener(ListView listView) {
		listView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
	}

	static class ViewHolder {
		TextView mTextView_name;
		TextView mTextView_special;
		TextView mTextView_discount;
		TextView mTextView_distance;
		ImageView iv_icon;
	}

	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	ViewHolder mHolder;
	public BaseAdapter mAdapter = new BaseAdapter() {
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = getLayoutInflater();
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.fragment_classfiy_listview_item, null);
				mHolder = new ViewHolder();
				mHolder.mTextView_name = (TextView) convertView.findViewById(R.id.fragment_classfiy_shopname);
				mHolder.mTextView_discount = (TextView) convertView.findViewById(R.id.fragment_classfiy_discount);
				mHolder.mTextView_special = (TextView) convertView.findViewById(R.id.fragment_classfiy_special);
				mHolder.mTextView_distance = (TextView) convertView.findViewById(R.id.fragment_classfiy_distance);
				mHolder.iv_icon = (ImageView) convertView.findViewById(R.id.fragment_classfiy_listview_img);
				Typeface face = Typeface.createFromAsset(getAssets(), "fonts/discount_font.TTF");
				mHolder.mTextView_discount.setTypeface(face);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			// 填充数据
			// 店招
			SellerInfoItem infoItem = (SellerInfoItem) getItem(position);
			if (imageUriArray != null) {
				imageLoader.displayImage(imageUriArray[position], mHolder.iv_icon, options, animateFirstListener);
			}
			mHolder.mTextView_name.setTextSize(13f);
			mHolder.mTextView_name.setText((infoItem.getBusinessName()));
			if (infoItem.getBusinessDesc().length() > 10) {
				mHolder.mTextView_special.setText((infoItem.getBusinessDesc()).substring(0, 8) + "...");
			} else {
				mHolder.mTextView_special.setText(infoItem.getBusinessDesc());
			}

			mHolder.mTextView_discount.setText(infoItem.getDiscount() + "折");
			// 时间转换
			if (mTime != null && mTime.size() > 0) {
				String timestr = mTime.get(position);
				if (!StrTools.isNull(timestr)) {
					long readtime = Long.parseLong(timestr);
					long nowtime = System.currentTimeMillis();
					long read = (nowtime - readtime) / 60000;
					int hour = (int) (read / 60);
					if (hour >= 24) {
						mHolder.mTextView_distance.setText(hour / 24 + "天前");
					} else if (hour > 0 && hour < 24) {
						mHolder.mTextView_distance.setText(hour + "小时" + read % 60 + "分钟前");
					} else {
						if (read >= 1) {
							mHolder.mTextView_distance.setText(read + "分钟前");
						} else {
							mHolder.mTextView_distance.setText("刚刚");
						}
					}
				}
			}
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// // 传递数据 id 坐标 折扣
					String id = null;
					id = mData.get(position).getId();
					String discount = mData.get(position).getDiscount();
					String lat = mData.get(position).getLat();
					String lng = mData.get(position).getLng();
					// Intent i = new Intent(ActivityReadHistory.this,
					// ActivitySellerDetail.class);
					// i.putExtra("id", id);
					// i.putExtra("discount", discount);
					// // 商家坐标
					// i.putExtra("lat", lat);
					// i.putExtra("lng", lng);
					// // 定位点坐标
					// i.putExtra("nowlat", mLat);
					// i.putExtra("nowlng", mLng);
					// // 商家名
					// i.putExtra("title",
					// mData.get(position).getBusinessName());
					// startActivity(i);
				}
			});
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public int getCount() {
			return mData.size();
		}
	};

	// 获得浏览历史
	private class ReadHisTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mProgressBar.setVisibility(View.GONE);
			setImg();
			mAdapter.notifyDataSetChanged();
			if (mData.size() < 1) {
				mLinearNoData.setVisibility(View.VISIBLE);
			} else {
				mLinearNoData.setVisibility(View.GONE);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				mData = mSellerBussiness.getReadHistory();
				mTime = mSellerBussiness.getReadTime();
				Collections.reverse(mTime);
				// imageUriArray = getPath(mData);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.read_his_back:
			finish();
			break;
		}
	}
}
