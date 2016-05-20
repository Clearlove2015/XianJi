package com.schytd.discount.ui;

import java.util.ArrayList;
import java.util.List;

import com.schytd.discount.bean.ConsumHistory;
import com.schytd.discount.bean.ConsumHistoryItem;
import com.schytd.discount.bean.ConsumptionTimesAndAmount;
import com.schytd.discount.business.UserBusiness;
import com.schytd.discount.business.impl.UserBusinessImpl;
import com.schytd.discount.tools.DateTools;
import com.schytd.discount.ui.progress.ProgressLayout;
import com.schytd.xianji.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityHistoryCustoms extends FragmentActivity implements
		SwipeRefreshLayout.OnRefreshListener, OnClickListener {
	private List<ConsumHistoryItem> mData_history;
	private ListView mListView;
	private int mTotalPage;
	private int mCurrentPage = 1;
	// 用户消费总次数
	private TextView mTextView_times;
	// 用户消费总金额
	private TextView mTextView_total;
	private UserBusiness mUserBussiness;
	private ConsumHistory mConsumHistroy;
	// 异步任务
	private GetDataTask mTask;
	// swiplayout
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private ProgressLayout mProgress;
	// 消费历史的总页数
	private SharedPreferences sp_page;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_customs);
		init();
	}

	private class TimeAndAmoutTask extends
			AsyncTask<Void, Void, ConsumptionTimesAndAmount> {
		@Override
		protected ConsumptionTimesAndAmount doInBackground(Void... arg0) {
			ConsumptionTimesAndAmount timesAndAmount = null;
			try {
				timesAndAmount = mUserBussiness.getConsumInfo();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return timesAndAmount;
		}

		protected void onPostExecute(ConsumptionTimesAndAmount result) {
			if (result != null) {
				mTextView_times.setText(result.getTimes());
				mTextView_total.setText(result.getTotal());
			}

		};
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

	public void init() {
		sp_page = getSharedPreferences("data_page", Context.MODE_PRIVATE);
		mTextView_times = (TextView) this.findViewById(R.id.cus_times);
		mTextView_total = (TextView) this.findViewById(R.id.cus_total);
		mUserBussiness = new UserBusinessImpl(this);
		mData_history = new ArrayList<ConsumHistoryItem>();
		// 获取数据
		mListView = (ListView) findViewById(R.id.fragment_history_constoms_listview);
		mListView.setAdapter(mAdapter);
		new TimeAndAmoutTask().execute();
		mSwipeRefreshLayout = (SwipeRefreshLayout) this
				.findViewById(R.id.list_swipe_container);
		mSwipeRefreshLayout.setEnabled(false);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mProgress = (ProgressLayout) this.findViewById(R.id.history_progress);
		mProgress.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mListView.setOnScrollListener(new SwpipeListViewOnScrollListener(mSwipeRefreshLayout));
		// 加载数据
		mTask = new GetDataTask();
		mTask.execute(mCurrentPage);
	}
	private class GetDataTask extends AsyncTask<Integer, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgress.setRefreshing(true);
		}

		@Override
		protected void onPostExecute(Void result) {
			mAdapter.notifyDataSetChanged();
			mProgress.setRefreshing(false);
			mSwipeRefreshLayout.setEnabled(true);
			mSwipeRefreshLayout.setRefreshing(false);
		}

		@Override
		protected Void doInBackground(Integer... params) {
			try {
				mConsumHistroy = mUserBussiness.getConsumHistory("10",
						params[0].toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			ArrayList<ConsumHistoryItem> data = null;
			if (mConsumHistroy != null) {
				data = mConsumHistroy.getResultList();
			}
			if (data != null) {
				for (ConsumHistoryItem consumHistoryItem : data) {
					mData_history.add(consumHistoryItem);
				}
				mTotalPage = sp_page.getInt("consum_total_page", 10);
			}
			return null;
		}
	};

	private static class ViewHolder {
		TextView tv_name;
		TextView tv_date;
		TextView tv_total;
	}

	private ViewHolder mHolder;
	private BaseAdapter mAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				mHolder = new ViewHolder();
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(
						R.layout.fragment_customs_listview_item, null);
				mHolder.tv_name = (TextView) convertView
						.findViewById(R.id.listview_name);
				mHolder.tv_date = (TextView) convertView
						.findViewById(R.id.listview_date);
				mHolder.tv_total = (TextView) convertView
						.findViewById(R.id.listview_total);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			ConsumHistoryItem item = mData_history.get(position);
			if (item != null) {
				mHolder.tv_name.setText(item.getBussinessName());
				String date = DateTools.getDate(Long.parseLong(item
						.getConsumptionTime()));
				mHolder.tv_date.setText(date);
				mHolder.tv_total.setText(String.valueOf(item.getAmount()));
			}
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return mData_history.get(position);
		}

		@Override
		public int getCount() {
			return mData_history.size();
		}
	};

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		mTask.cancel(true);
		// 关闭异步任务
		super.onDestroy();
	}

	@Override
	public void onRefresh() {
		// 下拉刷新
		if (mCurrentPage + 1 <= mTotalPage) {
			mCurrentPage++;
			new GetDataTask().execute(mCurrentPage);
		} else {
			mSwipeRefreshLayout.setRefreshing(false);
		}
	}

	public class SwpipeListViewOnScrollListener implements
			AbsListView.OnScrollListener {
		private SwipeRefreshLayout mSwipeView;
		private AbsListView.OnScrollListener mOnScrollListener;

		public SwpipeListViewOnScrollListener(SwipeRefreshLayout swipeView) {
			mSwipeView = swipeView;
		}

		public SwpipeListViewOnScrollListener(SwipeRefreshLayout swipeView,
				OnScrollListener onScrollListener) {
			mSwipeView = swipeView;
			mOnScrollListener = onScrollListener;
		}

		@Override
		public void onScrollStateChanged(AbsListView absListView, int i) {
			switch (i) {
			// 当不滚动时
			case OnScrollListener.SCROLL_STATE_IDLE:
				// 判断滚动到底部
				if (absListView.getLastVisiblePosition() == (absListView
						.getCount() - 1)) {
					if (mCurrentPage + 1 <= mTotalPage) {
						mCurrentPage++;
						new GetDataTask().execute(mCurrentPage);
					} else {
						// 没有数据 不加载
						mSwipeView.setEnabled(false);
						mSwipeView.setRefreshing(false);
					}
					break;
				}
			}
		}

		@Override
		public void onScroll(AbsListView absListView, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			View firstView = absListView.getChildAt(firstVisibleItem);

			// 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部,
			// 也需要刷新
			if (firstVisibleItem == 0
					&& (firstView == null || firstView.getTop() == 0)) {
				mSwipeView.setEnabled(true);
			} else {
				mSwipeView.setEnabled(false);
			}
			if (null != mOnScrollListener) {
				mOnScrollListener.onScroll(absListView, firstVisibleItem,
						visibleItemCount, totalItemCount);
			}
		}
	}
}
