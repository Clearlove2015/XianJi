package com.schytd.discount.ui;

import java.util.ArrayList;
import java.util.List;

import com.schytd.discount.bean.ScoreGetHistory;
import com.schytd.discount.bean.ScoreGetHistoryItem;
import com.schytd.discount.business.UserBusiness;
import com.schytd.discount.business.impl.UserBusinessImpl;
import com.schytd.discount.tools.DateTools;
import com.schytd.discount.ui.progress.ProgressLayout;
import com.schytd.xianji.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentRankGet extends Fragment implements
		SwipeRefreshLayout.OnRefreshListener {
	private List<ScoreGetHistoryItem> mData;
	private ListView mListView;
	private UserBusiness mUserBussiness;
	private ScoreGetHistory mScoreGetHistory;
	private int totalPage;
	private int mCurrentPageNum = 1;
	// 异步任务引用
	public static GetDataTask mTask;
	// 上下文
	private Context c;
	// swiplayout
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private ProgressLayout mProgress;
	// 积分获得的总页数
	private SharedPreferences sp_page;

	public FragmentRankGet(Context c) {
		this.c = c;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_rank_get_layout, container,
				false);
		init(v);
		return v;
	}

	private void init(View v) {
		sp_page = getActivity().getSharedPreferences("data_page",
				Context.MODE_PRIVATE);
		mUserBussiness = new UserBusinessImpl(getActivity());
		mData = new ArrayList<ScoreGetHistoryItem>();
		mListView = (ListView) v.findViewById(R.id.fragment_rank_listview);
		mListView.setAdapter(mAdapter);
		mSwipeRefreshLayout = (SwipeRefreshLayout) v
				.findViewById(R.id.rank_swipe_container);
		mSwipeRefreshLayout.setEnabled(false);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mProgress = (ProgressLayout) v.findViewById(R.id.rank_progress);
		mProgress.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mListView.setOnScrollListener(new SwpipeListViewOnScrollListener(mSwipeRefreshLayout));
		// 加载数据
		mTask = new GetDataTask();
		mTask.execute(mCurrentPageNum);
	}


	class GetDataTask extends AsyncTask<Integer, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgress.setRefreshing(true);
		}

		@Override
		protected void onPostExecute(Void result) {
			mProgress.setRefreshing(false);
			mSwipeRefreshLayout.setEnabled(true);
			mSwipeRefreshLayout.setRefreshing(false);
			ArrayList<ScoreGetHistoryItem> data = null;
			if (mScoreGetHistory != null) {
				data = mScoreGetHistory.getResultList();
			}
			if (data != null) {
				for (ScoreGetHistoryItem scoreGetHistoryItem : data) {
					mData.add(scoreGetHistoryItem);
				}
			}
			mAdapter.notifyDataSetChanged();
			totalPage = sp_page.getInt("score_total_page", 10);
		}

		@Override
		protected Void doInBackground(Integer... params) {
			try {
				mScoreGetHistory = mUserBussiness.getScoreGetHistory("10",
						params[0].toString());
				Log.d("total", mScoreGetHistory.getTotalCount() + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	};

	private BaseAdapter mAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = getActivity().getLayoutInflater();
				convertView = inflater.inflate(
						R.layout.fragment_rank_get_listview_item, null);
			}
			TextView textView_name = (TextView) convertView
					.findViewById(R.id.listview_get_name);
			TextView textView_date = (TextView) convertView
					.findViewById(R.id.listview_get_date);
			TextView textView_rank = (TextView) convertView
					.findViewById(R.id.listview_get_rankvalue);
			ScoreGetHistoryItem item = mData.get(position);
			if (item != null) {
				textView_name.setText(item.getBusinessName());
				String date = DateTools.getDate(Long.parseLong(item
						.getTheTime()));
				textView_date.setText(date);
				textView_rank.setText(item.getScore());
			}
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
			// TODO Auto-generated method stub
			return mData.size();
		}
	};

	@Override
	public void onRefresh() {
		// 下拉刷新
		if (mCurrentPageNum + 1 <= totalPage) {
			mCurrentPageNum++;
			new GetDataTask().execute(mCurrentPageNum);
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
					// 加载数据
					if (mCurrentPageNum + 1 <= totalPage) {
						mCurrentPageNum++;
						new GetDataTask().execute(mCurrentPageNum);
					} else {
						// 没有数据 不加载
						mSwipeRefreshLayout.setEnabled(false);
						mSwipeRefreshLayout.setRefreshing(false);
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
