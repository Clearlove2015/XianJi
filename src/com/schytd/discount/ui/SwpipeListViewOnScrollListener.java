package com.schytd.discount.ui;

import java.util.List;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class SwpipeListViewOnScrollListener implements
		AbsListView.OnScrollListener {
	private SwipeRefreshLayout mSwipeView;
	private AbsListView.OnScrollListener mOnScrollListener;
	private onLoadMoreListener mOnLoadMoreListener;

	// 回调
	private interface onLoadMoreListener {
		public void onLoadMore(List<Object> mdata);
	};

	public SwpipeListViewOnScrollListener(
			onLoadMoreListener mOnLoadMoreListener,
			SwipeRefreshLayout swipeView,
			AsyncTask<Object, Object, Object> task, int paramCount,
			String... params) {
		mSwipeView = swipeView;
		this.mOnLoadMoreListener = mOnLoadMoreListener;
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
			if (absListView.getLastVisiblePosition() == (absListView.getCount() - 1)) {
				// 加载数据
				// if (params[0] + 1 <= totalPage) {
				// currentPage++;
				// mOnLoadMoreListener.onLoadMore(new
				// GetPayListTask().execute(flag, pageSize,
				// currentPage + ""));
				// } else {
				// // 没有数据 不加载
				// mSwipeView.setEnabled(false);
				// mSwipeView.setRefreshing(false);
				// }
			}
			break;
		}
	}

	@Override
	public void onScroll(AbsListView absListView, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// View firstView = absListView.getChildAt(firstVisibleItem);

		// 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部,
		// 也需要刷新
		// if (firstVisibleItem == 0
		// && (firstView == null || firstView.getTop() == 0)) {
		// mSwipeView.setEnabled(true);
		// } else {
		// mSwipeView.setEnabled(false);
		// }
		// if (null != mOnScrollListener) {
		// mOnScrollListener.onScroll(absListView, firstVisibleItem,
		// visibleItemCount, totalItemCount);
		// }
	}
}