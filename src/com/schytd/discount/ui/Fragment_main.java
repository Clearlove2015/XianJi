package com.schytd.discount.ui;

import java.util.ArrayList;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.schytd.discount.bean.SellerInfo;
import com.schytd.discount.bean.SellerInfoItem;
import com.schytd.discount.business.SellerBusiness;
import com.schytd.discount.business.impl.SellerBusinessImp;
import com.schytd.discount.presenter.SellerPresenter;
import com.schytd.discount.tools.SortTools;
import com.schytd.discount.tools.StrTools;
import com.schytd.discount.ui.View.ImageCycleView;
import com.schytd.discount.ui.View.ImageCycleView.ImageCycleViewListener;
import com.schytd.discount.ui.iui.ISellerView_FragmentMain;
import com.schytd.xianji.R;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Fragment_main extends BaseFragment implements OnClickListener, ISellerView_FragmentMain {
	private ListView mListView;
	private LinearLayout mLinearBar;
	private ImageCycleView mCucleView;
	private ArrayList<SellerInfoItem> mData;// 优惠咨询
	private SellerInfo mSellerInfo = null;
	private SellerBusiness mSellerBussiness;
	// 总页数
	private int mTotalPageNum = 0;
	// 初始页面Os
	private int mCurrentPageNum = 1;
	private DisplayImageOptions options;
	private String[] imageUriArray;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	// 坐标
	private double mLat, mLng;
	// seller prenter
	private SellerPresenter sellerPresenter;
	// 搜索
	private LinearLayout mLinearLayout_to_search;
	private Intent intent_list;

	// 构造方法
	public Fragment_main(double mLat, double mLng) {
		this.mLat = mLat;
		this.mLng = mLng;
		sellerPresenter = new SellerPresenter(this, getActivity());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_layout, container, false);
		init(view);
		return view;
	}

	private void init(View v) {
		intent_list = new Intent(getActivity(), ActivityDiscountInfo.class);
		// 加载数据
		mLinearLayout_to_search = (LinearLayout) v.findViewById(R.id.goto_search);
		mLinearLayout_to_search.setOnClickListener(this);
		sellerPresenter.getDiscountInfo(null);
		mSellerBussiness = new SellerBusinessImp(getActivity());
		mData = new ArrayList<SellerInfoItem>();
		v.findViewById(R.id.test1).setOnClickListener(this);
		v.findViewById(R.id.test2).setOnClickListener(this);
		v.findViewById(R.id.test3).setOnClickListener(this);
		v.findViewById(R.id.index_code).setOnClickListener(this);
		mCucleView = (ImageCycleView) v.findViewById(R.id.index_img);
		mLinearBar = (LinearLayout) v.findViewById(R.id.index_bar);
		mLinearBar.setFocusable(true);
		mLinearBar.setFocusableInTouchMode(true);
		mLinearBar.requestFocus();
		mListView = (ListView) v.findViewById(R.id.main_listview);
		mListView.setAdapter(mAdapter);
		// new GetDataTask().execute(mLat + "", mLng + "", 10000 + "", 1000 +
		// "", mCurrentPageNum + "");
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		ArrayList<String> path = new ArrayList<String>();
		// *************
		path.add("1");
		path.add("1");
		path.add("1");
		path.add("1");
		path.add("1");
		// ***********
		// 图片
		mCucleView.setImageResources(path, mAdCycleViewListener, 1);
		// listview点击
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long duritaion) {
				startActivity(new Intent(getActivity(), ActivitySellerInfo.class));

			}
		});
	}

	static class ViewHolder {
		TextView mTextView_name;
		TextView mTextView_special;
		TextView mTextView_discount;
		TextView mTextView_distance;
		ImageView iv_icon;
	}

	ViewHolder mHolder;
	private BaseAdapter mAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				LayoutInflater inflater = getActivity().getLayoutInflater();
				convertView = inflater.inflate(R.layout.seller_listview_item_layout, null);
				mHolder = new ViewHolder();
				mHolder.mTextView_name = (TextView) convertView.findViewById(R.id.fragment_classfiy_shopname);
				mHolder.mTextView_discount = (TextView) convertView.findViewById(R.id.fragment_classfiy_discount);
				mHolder.iv_icon = (ImageView) convertView.findViewById(R.id.seller_img_iv);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			SellerInfoItem infoItem = mData.get(position);
			if (imageUriArray != null) {
				imageLoader.displayImage(imageUriArray[position], mHolder.iv_icon, options, animateFirstListener);
			}
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mData.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}
	};
	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
		@Override
		public void onImageClick(int position, View imageView) {
			// 点击轮播图片
			Intent i = new Intent(getActivity(), ActivityMessageDetail.class);
			// i.putExtra("id", mImageData.get(position).getId());
			i.putExtra("type", "manpage");
			startActivity(i);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.test1:
			intent_list.putExtra("title", "美食狂欢");
			startActivity(intent_list);
			break;
		case R.id.test2:
			intent_list.putExtra("title", "麦霸开唱");
			startActivity(intent_list);
			break;
		case R.id.test3:
			intent_list.putExtra("title", "招牌推荐");
			startActivity(intent_list);
			break;
		case R.id.goto_search:
			startActivity(new Intent(getActivity(), ActivitySearch.class));
			break;
		case R.id.index_code:
			// 调用登录
			Intent intent_login = new Intent(getActivity(), ActivityLogin.class);
			startActivity(intent_login);

			break;
		default:
			break;
		}

	}

	// 加载商家信息
	private class GetDataTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPostExecute(Boolean result) {
			setImg();
			mAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Boolean doInBackground(String... params) {
			if (StrTools.isNull(params[0]) || StrTools.isNull(params[1]) || StrTools.isNull(params[2])
					|| StrTools.isNull(params[4])) {
			} else {
				try {
					mSellerInfo = mSellerBussiness.getDiscountInfo(params[0], params[1], params[2], params[3],
							params[4]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mData.clear();
				if (mSellerInfo != null) {
					if (mSellerInfo.getResultList().size() > 0) {
						mTotalPageNum = mSellerInfo.getTotalPage();
						mData = mSellerInfo.getResultList();
						imageUriArray = getPath(mData);
						// 对其进行距离排序
						mData = SortTools.distanceSort(mLat, mLng, mData);
						getDistance();
					}
				}
			}
			return null;
		}
	}

	// 得到商家距离信息
	private ArrayList<String> mDataDistance;

	private void getDistance() {
		mDataDistance = new ArrayList<String>();
		if (mData.size() > 0) {
			for (SellerInfoItem sellerInfoItem : mData) {
				LatLng l1 = new LatLng(mLat, mLng);
				LatLng l2 = new LatLng(Double.parseDouble(sellerInfoItem.getLat()),
						Double.parseDouble(sellerInfoItem.getLng()));
				String distance = DistanceUtil.getDistance(l1, l2) + "";
				mDataDistance.add(distance.subSequence(0, distance.indexOf(".")) + "");
			}
		}
	}

	// 设置图标路径
	@Override
	public void setImageList(String[] imgurl) {
		// TODO Auto-generated method stub

	}

	// 设置优惠咨询 数据源
	@Override
	public void setDiscountInfoData(ArrayList<SellerInfoItem> mData) {
		// TODO Auto-generated method stub
		this.mData = mData;
		// 更新适配器
		mAdapter.notifyDataSetChanged();
	}
}
