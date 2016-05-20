package com.schytd.discount.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.schytd.discount.bean.ConstantData;
import com.schytd.discount.bean.IndexImage;
import com.schytd.discount.business.UpdateBusiness;
import com.schytd.discount.business.UserBusiness;
import com.schytd.discount.business.impl.UpdateBusinessImpl;
import com.schytd.discount.business.impl.UserBusinessImpl;
import com.schytd.discount.tools.StrTools;
import com.schytd.discount.ui.dialog.PromptDialog;
import com.schytd.discount.ui.pushservice.MyPushService;
import com.schytd.discount.ui.pushservice.PushDemoReceiver;
import com.schytd.xianji.R;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	private static final int REQUEST_TO_ACTMAP = 3;
	public static String tView = "";
	public static boolean USER_TO_LOGOUT = false;
	private static int REQUEST_PERSON_CENTER = 111;
	private static int REQUEST_LOGIN = 201;
	private static int REQUEST_SEARCH = 112;
	private long downloadId;
	private DownloadManager downloadManager;
	private DownloadReceiver receiver;
	private UpdateAsyncTask updateAsyncTask;
	private UserBusiness mUserBusiness;
	private UpdateBusiness mUpdateBussiness;
	Drawable drawable = null;
	// 定位点
	private static TextView mTextView_location;
	// 轮播图片
	private List<IndexImage> mImageData;
	// 图片fragment
	private List<FragmentSellerImg> mFragment;
	// 图片 url
	private ArrayList<String> path;
	SharedPreferences sp_city;
	private LinearLayout mLinearLayout_loading;
	// tab fragment
	private Fragment mMain_fg, mNear_fg, mLike_fg, mMy_fg;
	// tab 图标和文字
	private ImageView mImageView_main, mImageView_near, mImageView_like, mImageView_my;
	private TextView mTextView_main, mTextView_near, mTextView_like, mTextView_my;
	// 登录session
	private String sessionId = "测试";

	private void init() {
		getPosition();
		mImageView_main = (ImageView) this.findViewById(R.id.index_main_icon);
		mImageView_near = (ImageView) this.findViewById(R.id.index_near_icon);
		mImageView_like = (ImageView) this.findViewById(R.id.index_like_icon);
		mImageView_my = (ImageView) this.findViewById(R.id.index_my_icon);
		mTextView_main = (TextView) this.findViewById(R.id.index_main_txt);
		mTextView_near = (TextView) this.findViewById(R.id.index_near_txt);
		mTextView_my = (TextView) this.findViewById(R.id.index_my_txt);
		mTextView_like = (TextView) this.findViewById(R.id.index_like_txt);
		mUserBusiness = new UserBusinessImpl(this);
		mUpdateBussiness = new UpdateBusinessImpl(this);
		// mLinearLayout_loading = (LinearLayout) this
		// .findViewById(R.id.main_loading);
		mImageData = (List<IndexImage>) getIntent().getSerializableExtra("path");
		sp_city = getSharedPreferences("city_code", Context.MODE_PRIVATE);
		// 商家列表页
		mFragment = new ArrayList<FragmentSellerImg>();
		path = new ArrayList<String>();
		receiver = new DownloadReceiver();
		// 下载完毕要接收通知
		registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		// 点击通知栏要接收通知
		registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
		// 从系统服务中获得DownloadManager对象
		downloadManager = (DownloadManager) getSystemService(Service.DOWNLOAD_SERVICE);

		if (mImageData != null) {
			for (IndexImage img : mImageData) {
				mFragment.add(new FragmentSellerImg(img.getUrl()));
				path.add(img.getUrl());
			}
		}
		// mTextView_location = (TextView) this.findViewById(R.id.index_title);
		// intent_to_person_center = new Intent(MainActivity.this,
		// ActivityPersonCenter.class);
		mImageView_main.setBackgroundResource(R.drawable.index_main_selected);
		mImageView_near.setBackgroundResource(R.drawable.index_near_default);
		mImageView_like.setBackgroundResource(R.drawable.index_like_default);
		mImageView_my.setBackgroundResource(R.drawable.index_my_defalut);
		mTextView_main.setTextColor(getResources().getColor(R.color.bar_bg_txt_color));
	}

	// 点击事件
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id._index_tab_main:
			mImageView_main.setBackgroundResource(R.drawable.index_main_selected);
			mImageView_near.setBackgroundResource(R.drawable.index_near_default);
			mImageView_like.setBackgroundResource(R.drawable.index_like_default);
			mImageView_my.setBackgroundResource(R.drawable.index_my_defalut);
			mTextView_main.setTextColor(getResources().getColor(R.color.bar_bg_txt_color));
			mTextView_near.setTextColor(getResources().getColor(R.color.index_botton_font_color));
			mTextView_like.setTextColor(getResources().getColor(R.color.index_botton_font_color));
			mTextView_my.setTextColor(getResources().getColor(R.color.index_botton_font_color));
			getSupportFragmentManager().beginTransaction().replace(R.id.main_content, mMain_fg).commit();
			break;
		case R.id._index_tab_near:
			if (mNear_fg == null) {
				mNear_fg = new Fragment_near(nowLat, nowLng);
			}
			mImageView_main.setBackgroundResource(R.drawable.index_main_default);
			mImageView_near.setBackgroundResource(R.drawable.index_near_selected);
			mImageView_like.setBackgroundResource(R.drawable.index_like_default);
			mImageView_my.setBackgroundResource(R.drawable.index_my_defalut);
			mTextView_main.setTextColor(getResources().getColor(R.color.index_botton_font_color));
			mTextView_near.setTextColor(getResources().getColor(R.color.bar_bg_txt_color));
			mTextView_like.setTextColor(getResources().getColor(R.color.index_botton_font_color));
			mTextView_my.setTextColor(getResources().getColor(R.color.index_botton_font_color));
			getSupportFragmentManager().beginTransaction().replace(R.id.main_content, mNear_fg).commit();
			break;
		case R.id._index_tab_like:
			if (mLike_fg == null) {
				mLike_fg = new Fragment_like();
			}
			mImageView_main.setBackgroundResource(R.drawable.index_main_default);
			mImageView_near.setBackgroundResource(R.drawable.index_near_default);
			mImageView_like.setBackgroundResource(R.drawable.index_like_selected);
			mImageView_my.setBackgroundResource(R.drawable.index_my_defalut);
			mTextView_main.setTextColor(getResources().getColor(R.color.index_botton_font_color));
			mTextView_near.setTextColor(getResources().getColor(R.color.index_botton_font_color));
			mTextView_like.setTextColor(getResources().getColor(R.color.bar_bg_txt_color));
			mTextView_my.setTextColor(getResources().getColor(R.color.index_botton_font_color));
			getSupportFragmentManager().beginTransaction().replace(R.id.main_content, mLike_fg).commit();
			break;
		case R.id._index_tab_my:
			if (mMy_fg == null) {
				mMy_fg = new Fragment_my();
			}
			mImageView_main.setBackgroundResource(R.drawable.index_main_default);
			mImageView_near.setBackgroundResource(R.drawable.index_near_default);
			mImageView_like.setBackgroundResource(R.drawable.index_like_default);
			mImageView_my.setBackgroundResource(R.drawable.index_my_selected);
			mTextView_main.setTextColor(getResources().getColor(R.color.index_botton_font_color));
			mTextView_near.setTextColor(getResources().getColor(R.color.index_botton_font_color));
			mTextView_like.setTextColor(getResources().getColor(R.color.index_botton_font_color));
			mTextView_my.setTextColor(getResources().getColor(R.color.bar_bg_txt_color));
			// 判断用户是否登录
//			if (StrTools.isNull(sessionId)) {
				getSupportFragmentManager().beginTransaction().replace(R.id.main_content, mMy_fg).commit();
//			} else {
//				// 调用登录
//				Intent intent_login = new Intent(MainActivity.this, ActivityLogin.class);
//				startActivityForResult(intent_login, REQUEST_LOGIN);
//			}
			break;
		default:
			break;
		}
	}

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();

	// 定位
	private void getPosition() {
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 1000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为1000ms
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	String add = "";
	private Double nowLat, nowLng;
	private String nowAdd = "";

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// 此处设置开发者获取到的方向信息，顺时针0-360
			// ll = new LatLng(latitude, longitude);
			if (location == null) {
				Toast.makeText(MainActivity.this, "定位失败！", Toast.LENGTH_SHORT).show();
			} else {
				nowAdd = location.getAddrStr();
				nowLat = location.getLatitude();
				nowLng = location.getLongitude();
				add = location.getCity();
				ConstantData.district = sp_city.getString(add, "510100000000");
				if (!StrTools.isNull(add)) {
					add = add.substring(0, add.indexOf("市"));
				} else {
					Toast.makeText(MainActivity.this, "定位失败！", Toast.LENGTH_SHORT).show();
					add = "成都";
				}
				ConstantData.position = add;
				// mTextView_location.setText(add);
				mMain_fg = new Fragment_main(nowLat, nowLng);
				getSupportFragmentManager().beginTransaction().add(R.id.main_content, mMain_fg).commit();
			}
			mLocClient.stop();
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	// 获取屏幕像素、屏幕密度
	public int getScreenDensityDpi() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
		return densityDpi;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isNetworkConnected()) {
			// new SessionIdTask().execute();
		} else {
			// 没有网的操作
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main_layout);
		startService(new Intent(MainActivity.this, MyPushService.class));
		init();
		update_version();
	}

	@Override
	public void onDestroy() {
		PushDemoReceiver.payloadData.delete(0, PushDemoReceiver.payloadData.length());
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public void onStop() {
		Log.d("GetuiSdkDemo", "onStop()");
		super.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_LOGIN) {
			if (resultCode == ActivityLogin.RESULT_LOGIN_INFO) {
//				getSupportFragmentManager().beginTransaction().replace(R.id.main_content, mMain_fg).commit();
//				mImageView_main.setBackgroundResource(R.drawable.index_main_selected);
//				mImageView_near.setBackgroundResource(R.drawable.index_near_default);
//				mImageView_like.setBackgroundResource(R.drawable.index_like_default);
//				mImageView_my.setBackgroundResource(R.drawable.index_my_defalut);
//				mTextView_main.setTextColor(getResources().getColor(R.color.bar_bg_txt_color));
//				mTextView_near.setTextColor(getResources().getColor(R.color.index_botton_font_color));
//				mTextView_like.setTextColor(getResources().getColor(R.color.index_botton_font_color));
//				mTextView_my.setTextColor(getResources().getColor(R.color.index_botton_font_color));
			}
		}
	}

	private Intent intent_to_person_center, intent_list;

	// 获得sessionId
	private class SessionIdTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			String sessionId = null;
			try {
				sessionId = mUserBusiness.getSessionId();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return sessionId;
		}

		// 得到session后
		protected void onPostExecute(String result) {
			// mLinearLayout_loading.setVisibility(View.GONE);
			if (result != null) {
				sessionId = result;
			}
		}

		// 得到session前
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// mLinearLayout_loading.setVisibility(View.VISIBLE);
		};

	};

	/** * 菜单、返回键响应 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true;
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000);
		} else {
			finish();
		}
	}

	public boolean isNetworkConnected() {
		// 判断网络是否连接
		ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		return mNetworkInfo != null && mNetworkInfo.isAvailable();
	}

	private static String mDistance;

	// 更新版本
	public void update_version() {
		Log.d("MainActivity method", "update_version called...");
		updateAsyncTask = new UpdateAsyncTask();
		updateAsyncTask.execute();
	}

	private class UpdateAsyncTask extends AsyncTask<Void, Void, List<String>> {

		@Override
		protected List<String> doInBackground(Void... arg0) {
			try {
				List<String> versioninfo = mUpdateBussiness.update();
				return versioninfo;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<String> versioninfo) {
			if (versioninfo != null) {
				String code = versioninfo.get(0);
				if (code.equals("0")) {
					String software_version = ConstantData.SOFT_VERSION;
					String version = versioninfo.get(1);
					Log.d("++++++", "software_version = " + software_version);
					Log.d("++++++", "version = " + version);
					if (software_version.equals(version)) {
						Log.d("++++++", "已经是最新版本");
						// Toast.makeText(MainActivity.this, "已经是最新版本",
						// Toast.LENGTH_SHORT).show();
					} else {
						String download_url = versioninfo.get(2);
						Log.d(">>>>>>>>>>", "download_path = " + download_url);
						showUpdatDialog(version, download_url);
					}
				} else {
					// Toast.makeText(MainActivity.this, "更新失败...",
					// Toast.LENGTH_SHORT).show();
					Log.d("更新返回code码", "code = " + code);
				}
			} else {
				Log.d("MainActivity", "服务器无响应。versioninfo为空");
				// Toast.makeText(MainActivity.this, "服务器无响应",
				// Toast.LENGTH_SHORT)
				// .show();
			}
		}
	}

	/**
	 * 更新对话框
	 */
	public void showUpdatDialog(String version, final String download_url) {
		new PromptDialog.Builder(MainActivity.this).setMessage("最新版本：" + version + "\n\n是否更新？", null).setTitle("发现新版本")
				.setButton1("立即更新", new PromptDialog.OnClickListener() {
					@Override
					public void onClick(Dialog dialog, int which) {
						// 开始下载
						try {
							download_file(download_url);
						} catch (Exception e) {
							e.printStackTrace();
						}
						dialog.dismiss();
					}
				}).setButton2("以后再说", new PromptDialog.OnClickListener() {
					@Override
					public void onClick(Dialog dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	// 下载更新文件
	public void download_file(String urlPath) {
		Log.d("method", "download_file called...");
		// 构造下载指定目录
		File downloadDir = new File(Environment.getExternalStorageDirectory() + File.separator + "schytd");
		if (!downloadDir.exists()) {
			downloadDir.mkdirs();
		}
		// 文件名
		String fileName = "update.apk";
		// 构建请求
		Uri uri = Uri.parse(urlPath);
		DownloadManager.Request request = new DownloadManager.Request(uri);
		// 运行在wifi 或者手机网络情况下都下载
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
		// 是否允许在漫游状态下载
		request.setAllowedOverRoaming(false);
		// 设置下载通知栏上的标题名字，如果不写，默认以下载文件名显示
		request.setTitle("下载apk更新文件...");
		// 设置下载通知栏上的描述信息（相当于副标题）
		request.setDescription("惠消费");
		// 指定下载的目录和文件
		// 第一参数只能是目录名字（不能是downloadDir.getAbsolutePath()）
		// 第二参数只能是文件名（downloadFile.getAbsolutePath()）
		request.setDestinationInExternalPublicDir("schytd", fileName);
		// 默认download目录
		// request.setDestinationInExternalPublicDir(
		// Environment.DIRECTORY_DOWNLOADS, fileName);

		// 设置通知栏显示信息
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		// 此次下载的随机id值
		downloadId = downloadManager.enqueue(request);
	}

	// 下载更新文件返回信息
	private class DownloadReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			long completedDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			if (downloadId == completedDownloadId) {
				Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
				// 下载完成自动弹出安装
				Intent intent1 = new Intent(Intent.ACTION_VIEW);
				intent1.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator
						+ "schytd" + File.separator + "update.apk")), "application/vnd.android.package-archive");
				MainActivity.this.startActivity(intent1);
			}
		}
	}

}
