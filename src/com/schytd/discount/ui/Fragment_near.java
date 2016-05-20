package com.schytd.discount.ui;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.schytd.discount.bean.SellerInfo;
import com.schytd.discount.bean.SellerInfoItem;
import com.schytd.discount.business.SellerBusiness;
import com.schytd.discount.business.impl.SellerBusinessImp;
import com.schytd.discount.tools.StrTools;
import com.schytd.discount.ui.iui.ISellerView_FragmentNear;
import com.schytd.xianji.R;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_near extends BaseFragment implements ISellerView_FragmentNear, OnClickListener {
	private LinearLayout view;// pop 起点
	// 筛选条
	private LinearLayout mChoice_all, mChoice_field, mChoice_distance, mChoice_conditon;
	private int idx;
	// 分类数组
	private String classfiy[][];
	// 选择条颜色变化
	private TextView mTextView_all, mTextView_field, mTextView_distance, mTextView_condition;
	private ImageView mImageView_all, mImageView_field, mImageView_distance, mImageView_conditon;
	private Resources res;
	// 分类 selector
	private ImageView _food_icon, _fun_icon;
	private TextView _food_txt, _fun_txt;
	private View _food_line, _fun_line;
	private LinearLayout _food_bg, _fun_bg;
	private LinearLayout _food_lnfo, _fun_lnfo;
	// 全部
	private LinearLayout mLinear_all;
	// 条件人数
	private TextView mTextView_4, mTextView_6, mTextView_8, mTextView_10, mTextView_15;
	// 条件美食
	private SparseArray<TextView> mMap_food;
	// 条件娱乐
	private SparseArray<TextView> mMap_fun;
	private LinearLayout mCondition_food, mCondition_fun;
	// 当前为美食
	private boolean isfood = true;
	// 数据层
	private PullToRefreshListView mListView;
	private ArrayList<SellerInfoItem> mData;
	private LayoutInflater mInflater;
	// 图片路径
	private String[] mImgArray;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageLoadingListener animateFirstListener;
	private DisplayImageOptions options;
	private SellerInfo sellerInfo = null;
	private SellerBusiness mSellerBussiness;
	// 异步任务
	private GetDataTask mTask;
	// 筛选列表条件
	private double mLat, mLng;// 定位点经纬度
	// 类别 condition_one 行业分类 condition_two 大分类
	public String condition_one = "0", condition_two = "100";
	private String district = "0";// 城市
	private String mSearchTxt = "";// 搜索条件
	private int mCurrentPageNum = 1;// 当前页面
	private int distance = 5000;// 距离
	// 得到商家距离信息
	private ArrayList<String> mDataDistance;

	// 构造方法
	public Fragment_near(double mLat, double mLng) {
		this.mLat = mLat;
		this.mLng = mLng;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_near_layout, container, false);
		res = getActivity().getResources();
		init(view);
		return view;
	}

	private void init(View v) {
		mInflater = getActivity().getLayoutInflater();
		mSellerBussiness = new SellerBusinessImp(getActivity());
		mMap_food = new SparseArray<TextView>();
		mMap_fun = new SparseArray<TextView>();
		view = (LinearLayout) v.findViewById(R.id.near_choice_bar);
		// 筛选条
		mTextView_all = (TextView) v.findViewById(R.id.near_choice_all_txt);
		mTextView_field = (TextView) v.findViewById(R.id.near_choice_field_txt);
		mTextView_distance = (TextView) v.findViewById(R.id.near_choice_distance_txt);
		mTextView_condition = (TextView) v.findViewById(R.id.near_choice_condition_txt);
		mImageView_all = (ImageView) v.findViewById(R.id.near_choice_all_icon);
		mImageView_field = (ImageView) v.findViewById(R.id.near_choice_field_icon);
		mImageView_distance = (ImageView) v.findViewById(R.id.near_choice_distance_icon);
		mImageView_conditon = (ImageView) v.findViewById(R.id.near_choice_condition_icon);
		classfiy = new String[][] { null, this.getResources().getStringArray(R.array.array_subclassfiy_1),
				this.getResources().getStringArray(R.array.array_subclassfiy_2),
				this.getResources().getStringArray(R.array.array_subclassfiy_3),
				this.getResources().getStringArray(R.array.array_subclassfiy_4),
				this.getResources().getStringArray(R.array.array_subclassfiy_5),
				this.getResources().getStringArray(R.array.array_subclassfiy_6), };
		mChoice_all = (LinearLayout) v.findViewById(R.id.near_choice_all);
		mChoice_field = (LinearLayout) v.findViewById(R.id.near_choice_field);
		mChoice_distance = (LinearLayout) v.findViewById(R.id.near_choice_distance);
		mChoice_conditon = (LinearLayout) v.findViewById(R.id.near_choice_condtion);
		mChoice_all.setOnClickListener(this);
		mChoice_field.setOnClickListener(this);
		mChoice_distance.setOnClickListener(this);
		mChoice_conditon.setOnClickListener(this);
		initScreenWidth();
		mImageView_all.setBackground(res.getDrawable(R.drawable.near_default));
		mImageView_field.setBackground(res.getDrawable(R.drawable.near_default));
		mImageView_distance.setBackground(res.getDrawable(R.drawable.near_default));
		mImageView_conditon.setBackground(res.getDrawable(R.drawable.near_default));
		// 列表数据
		mListView = (PullToRefreshListView) v.findViewById(R.id.near_listview);
		mData = new ArrayList<SellerInfoItem>();
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mData.add(new SellerInfoItem());
		mListView.setAdapter(mAdapter);
		animateFirstListener = new AnimateFirstDisplayListener();
		mTask = new GetDataTask();
		mTask.execute(mLat + "", mLng + "", distance + "", mSearchTxt, 10 + "", mCurrentPageNum + "", condition_one,
				condition_two, district);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				startActivity(new Intent(getActivity(), ActivitySellerInfo.class));
			}
		});
	}

	// 点击事件
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.near_choice_all:
			mTextView_all.setTextColor(res.getColor(R.color.near_choiced_font_color));
			mImageView_all.setBackground(res.getDrawable(R.drawable.near_choice));
			showPopupWindow(view);
			break;
		case R.id.near_choice_field:
			mTextView_field.setTextColor(res.getColor(R.color.near_choiced_font_color));
			mImageView_field.setBackground(res.getDrawable(R.drawable.near_choice));
			idx = 1;
			showPopupWindow(view, 1);
			break;
		case R.id.near_choice_distance:
			mTextView_distance.setTextColor(res.getColor(R.color.near_choiced_font_color));
			mImageView_distance.setBackground(res.getDrawable(R.drawable.near_choice));
			idx = 2;
			showPopupWindow(view, 2);
			break;
		case R.id.near_choice_condtion:
			mTextView_condition.setTextColor(res.getColor(R.color.near_choiced_font_color));
			mImageView_conditon.setBackground(res.getDrawable(R.drawable.near_choice));
			showPopupWindow(view, 0.0);
			break;
		case R.id.pop_bg:
		case R.id.near_condition_bg:
			if (popupWindow != null && popupWindow.isShowing()) {
				popupWindow.dismiss();
			}
			break;
		// 全部分类中tab
		case R.id.near_food_bg:
			_food_bg.setBackgroundColor(res.getColor(R.color.white));
			_food_icon.setBackgroundResource(R.drawable.food_choice);
			_food_txt.setTextColor(res.getColor(R.color.near_tab_food_selected_color));
			_food_line.setVisibility(View.GONE);
			_fun_bg.setBackgroundColor(res.getColor(R.color.near_tab_bg));
			_fun_icon.setBackgroundResource(R.drawable.fun_default);
			_fun_txt.setTextColor(res.getColor(R.color.near_tab_food_default_color));
			_fun_line.setVisibility(View.VISIBLE);
			_food_lnfo.setVisibility(View.VISIBLE);
			_fun_lnfo.setVisibility(View.GONE);
			break;
		case R.id.near_fun_bg:
			_food_bg.setBackgroundColor(res.getColor(R.color.near_tab_bg));
			_food_icon.setBackgroundResource(R.drawable.food_defalult);
			_food_txt.setTextColor(res.getColor(R.color.near_tab_food_default_color));
			_food_line.setVisibility(View.VISIBLE);
			_fun_bg.setBackgroundColor(res.getColor(R.color.white));
			_fun_icon.setBackgroundResource(R.drawable.fun_choiced);
			_fun_txt.setTextColor(res.getColor(R.color.red));
			_fun_line.setVisibility(View.GONE);
			_food_lnfo.setVisibility(View.GONE);
			_fun_lnfo.setVisibility(View.VISIBLE);
			break;
		// 人数选择
		case R.id.condition_num_4:
			initNumButton();
			mTextView_4.setBackgroundResource(R.drawable.near_condition_selected_shape);
			mTextView_4.setTextColor(res.getColor(R.color.white));
			break;
		case R.id.condition_num_6:
			initNumButton();
			mTextView_6.setBackgroundResource(R.drawable.near_condition_selected_shape);
			mTextView_6.setTextColor(res.getColor(R.color.white));
			break;
		case R.id.condition_num_8:
			initNumButton();
			mTextView_8.setBackgroundResource(R.drawable.near_condition_selected_shape);
			mTextView_8.setTextColor(res.getColor(R.color.white));
			break;
		case R.id.condition_num_10:
			initNumButton();
			mTextView_10.setBackgroundResource(R.drawable.near_condition_selected_shape);
			mTextView_10.setTextColor(res.getColor(R.color.white));
			break;
		case R.id.condition_num_15:
			initNumButton();
			mTextView_15.setBackgroundResource(R.drawable.near_condition_selected_shape);
			mTextView_15.setTextColor(res.getColor(R.color.white));
			break;
		// food 条件
		case R.id.condition_food_wifi:
		case R.id.condition_food_kongtiao:
		case R.id.condition_food_majiang:
		case R.id.condition_food_sofa:
		case R.id.condition_food_stop:
		case R.id.condition_food_tv:
		case R.id.condition_food_wc:
			TextView mText_food = mMap_food.get(id);
			if (mText_food == null) {
				break;
			}
			if (mText_food.isSelected()) {
				mText_food.setSelected(false);
				mText_food.setBackgroundResource(R.drawable.near_condition_shape);
				mText_food.setTextColor(res.getColor(R.color.near_default_font_color));
			} else {
				mText_food.setSelected(true);
				mText_food.setBackgroundResource(R.drawable.near_condition_selected_shape);
				mText_food.setTextColor(res.getColor(R.color.white));
			}
			break;
		// fun 条件
		case R.id.condition_fun_01:
		case R.id.condition_fun_02:
		case R.id.condition_fun_03:
		case R.id.condition_fun_04:
		case R.id.condition_fun_05:
		case R.id.condition_fun_06:
		case R.id.condition_fun_07:
		case R.id.condition_fun_08:
			TextView mText_fun = mMap_fun.get(id);
			if (mText_fun == null) {
				break;
			}
			if (mText_fun.isSelected()) {
				mText_fun.setSelected(false);
				mText_fun.setBackgroundResource(R.drawable.near_condition_shape);
				mText_fun.setTextColor(res.getColor(R.color.near_default_font_color));
			} else {
				mText_fun.setSelected(true);
				mText_fun.setBackgroundResource(R.drawable.near_condition_selected_shape);
				mText_fun.setTextColor(res.getColor(R.color.white));
			}
			break;
		// 更新标题
		case R.id.class_chinese_food:
			popupWindow.dismiss();
			mTextView_all.setText("中餐");
			isfood = true;
			break;
		case R.id.class_hot_food:
			popupWindow.dismiss();
			mTextView_all.setText("火锅");
			isfood = true;
			break;
		case R.id.class_sea_food:
			popupWindow.dismiss();
			mTextView_all.setText("海鲜");
			isfood = true;
			break;
		case R.id.class_west_food:
			popupWindow.dismiss();
			mTextView_all.setText("西餐");
			isfood = true;
			break;
		case R.id.class_ganguo:
			popupWindow.dismiss();
			mTextView_all.setText("干锅");
			isfood = true;
			break;
		case R.id.class_liaoli:
			popupWindow.dismiss();
			mTextView_all.setText("料理");
			isfood = true;
			break;
		case R.id.class_dapaidang:
			popupWindow.dismiss();
			mTextView_all.setText("大排档");
			isfood = true;
			break;
		case R.id.class_special:
			popupWindow.dismiss();
			mTextView_all.setText("特色餐饮");
			isfood = true;
			break;
		case R.id.class_ktv:
			popupWindow.dismiss();
			isfood = false;
			mTextView_all.setText("KTV");
			break;
		case R.id.class_tea:
			popupWindow.dismiss();
			isfood = false;
			mTextView_all.setText("茶楼");
			break;
		case R.id.near_all:
			popupWindow.dismiss();
			if (_food_lnfo.getVisibility() == View.VISIBLE) {
				mTextView_all.setText("美食");
				isfood = true;
			}
			if (_fun_lnfo.getVisibility() == View.VISIBLE) {
				mTextView_all.setText("娱乐");
				isfood = false;
			}
			break;
		// 确定条件选择
		case R.id.condition_choice_ok:
			popupWindow.dismiss();
			break;
		}
	}

	// 初始化 人数按钮
	private void initNumButton() {
		mTextView_4.setBackgroundResource(R.drawable.near_condition_shape);
		mTextView_6.setBackgroundResource(R.drawable.near_condition_shape);
		mTextView_8.setBackgroundResource(R.drawable.near_condition_shape);
		mTextView_10.setBackgroundResource(R.drawable.near_condition_shape);
		mTextView_15.setBackgroundResource(R.drawable.near_condition_shape);
		mTextView_4.setTextColor(res.getColor(R.color.near_default_font_color));
		mTextView_6.setTextColor(res.getColor(R.color.near_default_font_color));
		mTextView_8.setTextColor(res.getColor(R.color.near_default_font_color));
		mTextView_10.setTextColor(res.getColor(R.color.near_default_font_color));
		mTextView_15.setTextColor(res.getColor(R.color.near_default_font_color));
	}

	private PopupWindow popupWindow;
	private int screenWidth;
	private int screenHeight;
	private ListView mlistView1, mlistView2;
	private LinearLayout mLinearLayout_con;
	// 筛选框的适配器
	private NearListAdapter adapter;
	private NearSubAdapter subAdapter;

	private void initScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		screenHeight = dm.heightPixels;
		screenWidth = dm.widthPixels;
	}

	// pop条件
	public void showPopupWindow(View anchor, double classfiy) {
		popupWindow = new PopupWindow(getActivity());
		View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_fragment_near_condition_layout,
				null);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				mTextView_condition.setTextColor(res.getColor(R.color.near_default_font_color));
				mImageView_conditon.setBackground(res.getDrawable(R.drawable.near_default));
			}
		});
		popupWindow.setWidth(screenWidth);
		// popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popupWindow.setHeight(screenHeight);
		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new PaintDrawable(Color.argb(120, 0, 0, 0)));
		popupWindow.showAsDropDown(anchor);
		contentView.findViewById(R.id.near_condition_bg).setOnClickListener(this);
		// 人数按钮
		mTextView_4 = (TextView) contentView.findViewById(R.id.condition_num_4);
		mTextView_6 = (TextView) contentView.findViewById(R.id.condition_num_6);
		mTextView_8 = (TextView) contentView.findViewById(R.id.condition_num_8);
		mTextView_10 = (TextView) contentView.findViewById(R.id.condition_num_10);
		mTextView_15 = (TextView) contentView.findViewById(R.id.condition_num_15);
		mTextView_4.setOnClickListener(this);
		mTextView_6.setOnClickListener(this);
		mTextView_8.setOnClickListener(this);
		mTextView_10.setOnClickListener(this);
		mTextView_15.setOnClickListener(this);
		// 具体条件
		mCondition_food = (LinearLayout) contentView.findViewById(R.id.food_condition);
		mCondition_fun = (LinearLayout) contentView.findViewById(R.id.fun_condition);
		if (isfood) {
			mCondition_food.setVisibility(View.VISIBLE);
			mCondition_fun.setVisibility(View.GONE);
		} else {
			mCondition_fun.setVisibility(View.VISIBLE);
			mCondition_food.setVisibility(View.GONE);

		}
		if (mCondition_food.getVisibility() == View.VISIBLE) {
			mMap_food.put(R.id.condition_food_wifi, (TextView) contentView.findViewById(R.id.condition_food_wifi));
			mMap_food.put(R.id.condition_food_kongtiao,
					(TextView) contentView.findViewById(R.id.condition_food_kongtiao));
			mMap_food.put(R.id.condition_food_stop, (TextView) contentView.findViewById(R.id.condition_food_stop));
			mMap_food.put(R.id.condition_food_majiang,
					(TextView) contentView.findViewById(R.id.condition_food_majiang));
			mMap_food.put(R.id.condition_food_tv, (TextView) contentView.findViewById(R.id.condition_food_tv));
			mMap_food.put(R.id.condition_food_sofa, (TextView) contentView.findViewById(R.id.condition_food_sofa));
			mMap_food.put(R.id.condition_food_wc, (TextView) contentView.findViewById(R.id.condition_food_wc));
			// 事件
			addEvents(mMap_food);
		}
		if (mCondition_fun.getVisibility() == View.VISIBLE) {
			mMap_fun.put(R.id.condition_fun_01, (TextView) contentView.findViewById(R.id.condition_fun_01));
			mMap_fun.put(R.id.condition_fun_02, (TextView) contentView.findViewById(R.id.condition_fun_02));
			mMap_fun.put(R.id.condition_fun_03, (TextView) contentView.findViewById(R.id.condition_fun_03));
			mMap_fun.put(R.id.condition_fun_04, (TextView) contentView.findViewById(R.id.condition_fun_04));
			mMap_fun.put(R.id.condition_fun_05, (TextView) contentView.findViewById(R.id.condition_fun_05));
			mMap_fun.put(R.id.condition_fun_06, (TextView) contentView.findViewById(R.id.condition_fun_06));
			mMap_fun.put(R.id.condition_fun_07, (TextView) contentView.findViewById(R.id.condition_fun_07));
			mMap_fun.put(R.id.condition_fun_08, (TextView) contentView.findViewById(R.id.condition_fun_08));
			addEvents(mMap_fun);
		}
		// 完成
		contentView.findViewById(R.id.condition_choice_ok).setOnClickListener(this);
	}

	// 绑定事件
	public void addEvents(SparseArray<TextView> d) {
		for (int i = 0; i < d.size(); i++) {
			d.valueAt(i).setOnClickListener(this);
			d.valueAt(i).setSelected(false);
		}
	}

	// pop 分类
	public void showPopupWindow(View anchor) {
		popupWindow = new PopupWindow(getActivity());
		View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_fragment_near_all_layout, null);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				mTextView_all.setTextColor(res.getColor(R.color.near_default_font_color));
				mImageView_all.setBackground(res.getDrawable(R.drawable.near_default));
			}
		});
		popupWindow.setWidth(screenWidth);
		// popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popupWindow.setHeight(screenHeight);
		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new PaintDrawable(Color.argb(120, 0, 0, 0)));
		popupWindow.showAsDropDown(anchor);
		contentView.findViewById(R.id.pop_bg).setOnClickListener(this);
		mLinear_all = (LinearLayout) contentView.findViewById(R.id.near_all);
		_food_icon = (ImageView) contentView.findViewById(R.id.near_food_icon);
		_food_txt = (TextView) contentView.findViewById(R.id.near_food_txt);
		_food_line = contentView.findViewById(R.id.near_food_line);
		_fun_icon = (ImageView) contentView.findViewById(R.id.near_fun_icon);
		_fun_txt = (TextView) contentView.findViewById(R.id.near_fun_txt);
		_fun_line = contentView.findViewById(R.id.near_fun_line);
		_food_bg = (LinearLayout) contentView.findViewById(R.id.near_food_bg);
		_fun_bg = (LinearLayout) contentView.findViewById(R.id.near_fun_bg);
		_fun_lnfo = (LinearLayout) contentView.findViewById(R.id.near_fun_info);
		_food_lnfo = (LinearLayout) contentView.findViewById(R.id.near_food_info);
		_food_bg.setOnClickListener(this);
		_fun_bg.setOnClickListener(this);
		mLinear_all.setOnClickListener(this);
		// 初始化
		if (isfood) {
			_food_bg.setBackgroundColor(res.getColor(R.color.white));
			_food_icon.setBackgroundResource(R.drawable.food_choice);
			_food_txt.setTextColor(res.getColor(R.color.near_tab_food_selected_color));
			_fun_bg.setBackgroundColor(res.getColor(R.color.near_tab_bg));
			_fun_icon.setBackgroundResource(R.drawable.fun_default);
			_fun_txt.setTextColor(res.getColor(R.color.near_tab_food_default_color));
			_food_line.setVisibility(View.GONE);
		} else {
			_food_bg.setBackgroundColor(res.getColor(R.color.near_tab_bg));
			_food_icon.setBackgroundResource(R.drawable.food_defalult);
			_food_txt.setTextColor(res.getColor(R.color.near_tab_food_default_color));
			_food_line.setVisibility(View.VISIBLE);
			_fun_bg.setBackgroundColor(res.getColor(R.color.white));
			_fun_icon.setBackgroundResource(R.drawable.fun_choiced);
			_fun_txt.setTextColor(res.getColor(R.color.red));
			_fun_line.setVisibility(View.GONE);
			_food_lnfo.setVisibility(View.GONE);
			_fun_lnfo.setVisibility(View.VISIBLE);
		}

		// 为分类添加事件
		contentView.findViewById(R.id.class_chinese_food).setOnClickListener(this);
		contentView.findViewById(R.id.class_hot_food).setOnClickListener(this);
		contentView.findViewById(R.id.class_sea_food).setOnClickListener(this);
		contentView.findViewById(R.id.class_west_food).setOnClickListener(this);
		contentView.findViewById(R.id.class_ganguo).setOnClickListener(this);
		contentView.findViewById(R.id.class_liaoli).setOnClickListener(this);
		contentView.findViewById(R.id.class_dapaidang).setOnClickListener(this);
		contentView.findViewById(R.id.class_special).setOnClickListener(this);
		contentView.findViewById(R.id.class_ktv).setOnClickListener(this);
		contentView.findViewById(R.id.class_tea).setOnClickListener(this);
	}

	// pop 区域 距离
	public void showPopupWindow(View anchor, final int flag) {
		popupWindow = new PopupWindow(getActivity());
		View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.shaixuan_popupwindow, null);
		mlistView1 = (ListView) contentView.findViewById(R.id.lv1);
		mlistView2 = (ListView) contentView.findViewById(R.id.lv2);
		mLinearLayout_con = (LinearLayout) contentView.findViewById(R.id.lv_layout);
		LinearLayout pop_bg = (LinearLayout) contentView.findViewById(R.id.pop_bg);
		switch (flag) {
		case 1:
			adapter = new NearListAdapter(getActivity(), initArrayData(R.array.array_classfiy), idx);
			break;
		case 2:
			adapter = new NearListAdapter(getActivity(), initArrayData(R.array.array_distance), idx);
			break;
		case 3:
			adapter = new NearListAdapter(getActivity(), initArrayData(R.array.array_shaixuan), idx);
			break;
		}
		mlistView1.setAdapter(adapter);
		mlistView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (parent.getAdapter() instanceof NearListAdapter) {
					// 设置选中项
					adapter.setSelectItem(position);
					adapter.notifyDataSetChanged();
					mlistView2.setVisibility(View.INVISIBLE);
					if (mlistView2.getVisibility() == View.INVISIBLE) {
						mlistView2.setVisibility(View.VISIBLE);
						LayoutParams params = mlistView2.getLayoutParams();
						params.height = mlistView1.getHeight();
						mlistView2.setLayoutParams(params);
						switch (idx) {
						case 1:
							mLinearLayout_con.getLayoutParams().width = 0;
							if (classfiy[position] != null) {
								subAdapter = new NearSubAdapter(getActivity(), classfiy[position]);
							} else {
								subAdapter = null;
							}
							break;
						case 2:
							mLinearLayout_con.getLayoutParams().width = 0;
							if (classfiy[position] != null) {
								subAdapter = new NearSubAdapter(getActivity(), classfiy[position]);
							} else {
								subAdapter = null;
							}

							break;
						case 3:
							mLinearLayout_con.getLayoutParams().width = 0;
							if (classfiy[position] != null) {
								subAdapter = new NearSubAdapter(getActivity(), classfiy[position]);
							} else {
								subAdapter = null;
							}

							break;
						}
						if (subAdapter != null && flag == 1) {

							// 存在下级
							mlistView2.setAdapter(subAdapter);
							subAdapter.notifyDataSetChanged();
							mlistView2.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
									String name = (String) parent.getAdapter().getItem(position);
									setHeadText(idx, name);
									popupWindow.dismiss();
									subAdapter = null;
								}
							});
						} else {
							// 当没有下级时直接将信息设置textview中
							String name = (String) parent.getAdapter().getItem(position);
							setHeadText(idx, name);
							popupWindow.dismiss();

						}
					}

				}
			}
		});

		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				if (flag == 1) {
					mTextView_field.setTextColor(res.getColor(R.color.near_default_font_color));
					mImageView_field.setBackground(res.getDrawable(R.drawable.near_default));
				} else if (flag == 2) {
					mTextView_distance.setTextColor(res.getColor(R.color.near_default_font_color));
					mImageView_distance.setBackground(res.getDrawable(R.drawable.near_default));
				}

			}
		});
		popupWindow.setWidth(screenWidth);
		// popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popupWindow.setHeight(screenHeight);
		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new PaintDrawable(Color.argb(120, 0, 0, 0)));
		popupWindow.showAsDropDown(anchor);
		pop_bg.setOnClickListener(this);
	}

	private List<String> initArrayData(int id) {
		List<String> list = new ArrayList<String>();
		String[] array = this.getResources().getStringArray(id);
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}

	// 筛选标题
	private void setHeadText(int idx, String text) {
		switch (idx) {
		case 1:
			mTextView_field.setText(text);
			break;
		case 2:
			mTextView_distance.setText(text);
			break;
		}
	}

	// 加载商家信息
	private class GetDataTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPostExecute(Boolean result) {
			mAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				if (StrTools.isNull(params[0]) || StrTools.isNull(params[1]) || StrTools.isNull(params[2])
						|| StrTools.isNull(params[4]) || StrTools.isNull(params[5])) {
					// 为空的操作。。。。。。
					Toast.makeText(getActivity(), "存在数据为空", Toast.LENGTH_SHORT).show();
				} else {
					sellerInfo = mSellerBussiness.getSellerInfo(params[0], params[1], params[2], params[3], params[4],
							params[5], params[6], params[7], params[8]);
				}
				mData.clear();
				if (sellerInfo != null) {
					if (sellerInfo.getResultList().size() > 0) {
						// mTotalPageNum = sellerInfo.getTotalPage();
						mData = sellerInfo.getResultList();
						// if (isDiscount) {
						// SortTools.discountSort(mLat, mLng, mData);// 折扣排序
						// } else {
						// mData = SortTools.distanceSort(mLat, mLng, mData);//
						// 距离排序
						// }
						mImgArray = getPath(mData);// 图片地址
						getDistance();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	// 获得图片的连接地址
	public String[] getPath(ArrayList<SellerInfoItem> mdata) {
		String[] imgUri = new String[mdata.size()];
		if (mdata != null && mdata.size() > 0) {
			for (int i = 0; i < mdata.size(); i++) {
				SellerInfoItem item = mdata.get(i);
				imgUri[i] = item.getLogoPic();
			}
		}
		return imgUri;
	}

	private static class ViewHolder {
		public TextView tv_Name;
		public TextView tv_Distance;
		public TextView tv_Detail;
		public ImageView iv_icon;
	}

	private ViewHolder mHolder;
	private BaseAdapter mAdapter = new BaseAdapter() {
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.seller_listview_item_layout, null);
				mHolder = new ViewHolder();
				mHolder.tv_Name = (TextView) convertView.findViewById(R.id.seller_name_txt);
				mHolder.tv_Detail = (TextView) convertView.findViewById(R.id.seller_specail_txt);
				mHolder.tv_Distance = (TextView) convertView.findViewById(R.id.seller_distance_txt);
				mHolder.iv_icon = (ImageView) convertView.findViewById(R.id.seller_img_iv);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			// if (mData.size() > 0 && mData.size() >= position + 1) {
			// // 店招
			// SellerInfoItem infoItem = (SellerInfoItem) getItem(position);
			// // 图片
			// if (mImgArray != null) {
			// imageLoader.displayImage(mImgArray[position], mHolder.iv_icon,
			// options, animateFirstListener);
			// }
			// mHolder.tv_Name.setText((infoItem.getBusinessName()));
			// if (infoItem.getBusinessDesc().length() > 10) {
			// mHolder.tv_Detail.setText((infoItem.getBusinessDesc()).substring(0,
			// 8) + "...");
			// } else {
			// mHolder.tv_Detail.setText(infoItem.getBusinessDesc());
			// }
			// if (mDataDistance.size() > 0 && mDataDistance.size() >= position
			// + 1) {
			// mHolder.tv_Distance.setText("<" + mDataDistance.get(position) +
			// "m");
			// }
			// }
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

	// 计算商家的距离
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

	// 附近商家
	@Override
	public void setNearSellerInfoData(ArrayList<SellerInfoItem> mData) {
		// TODO Auto-generated method stub

	}
}
