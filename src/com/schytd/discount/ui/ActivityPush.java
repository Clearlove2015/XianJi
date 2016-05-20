package com.schytd.discount.ui;

import java.util.Locale;

import com.google.zxing.WriterException;
import com.schytd.discount.bean.ConstantData;
import com.schytd.discount.bean.IntroductionInfo;
import com.schytd.discount.business.UserBusiness;
import com.schytd.discount.business.impl.UserBusinessImpl;
import com.schytd.discount.tools.AESUtils;
import com.schytd.discount.tools.StrTools;
import com.schytd.xianji.R;
import com.umeng.socialize.bean.SHARE_MEDIA;

import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityPush extends ActivityShare implements OnClickListener {
	private UserBusiness mUserBussiness;
	// 总推广人数
	private TextView mTextView_All;
	// 有效退广人数
	private TextView mTextView_Effect;
	// 推广码
	private TextView mTextView_Code;
	// 二维码图片
	private ImageView mImageView_QrCode;
	String path = "http://192.168.1.122:8080/discountweb/introduction/getBusinessInfo/446";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push);
		init();
		setShareContent(path);
		configPlatforms(path);
		new GetIntroductionInfo().execute();
	}

	private void init() {
		mUserBussiness = new UserBusinessImpl(this);
		mTextView_All = (TextView) this.findViewById(R.id.all_introduction_num);
		mTextView_Effect = (TextView) this
				.findViewById(R.id.effect_introduction_num);
		mTextView_Code = (TextView) this.findViewById(R.id.introduction_code);
		mImageView_QrCode = (ImageView) this.findViewById(R.id.iv_qrcode);
	}
	// 获取用户的退广信息
	private class GetIntroductionInfo extends
			AsyncTask<Void, Void, IntroductionInfo> {
		@Override
		protected IntroductionInfo doInBackground(Void... param) {
			IntroductionInfo introductionInfo = null;
			try {
				introductionInfo = mUserBussiness.toIntroductionInfo();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return introductionInfo;
		}

		@Override
		protected void onPostExecute(IntroductionInfo result) {
			if (result != null) {
				mTextView_All.setText(result.getAllIntroductionCount() + " 个");
				mTextView_Effect.setText(result.getEffectiveIntroductionCount()
						+ " 个");
				String codeStr = result.getIntroductionCode();
				mTextView_Code.setText(codeStr.toUpperCase(Locale.ENGLISH));
				// AES加密
				Log.d("++++++++", "qrcode_password = "
						+ ConstantData.QRCODE_PASSWORD);
				byte[] b = AESUtils.encrypt(codeStr,
						ConstantData.QRCODE_PASSWORD);
				Log.d("++++++++", "byte[] b = " + b);
				String result1 = AESUtils.parseByte2HexStr(b);
				Log.d("++++++++", "result = " + result1);
				Bitmap mBitmap;
				try {
					mBitmap = StrTools.cretaeQrCodeBitmap(result1,
							getResources().getDrawable(R.drawable.qcode));
					mImageView_QrCode.setImageBitmap(mBitmap);
				} catch (NotFoundException e) {
					e.printStackTrace();
				} catch (WriterException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getApplicationContext(), "数据请求错误！",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_back:
			finish();
			break;

		case R.id.goto_share_push:
			// 是否只有已登录用户才能打开分享选择页
			mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
					SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
					SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA,
					SHARE_MEDIA.TENCENT, SHARE_MEDIA.DOUBAN,
					SHARE_MEDIA.RENREN);
			mController.openShare(ActivityPush.this, false);
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

}
