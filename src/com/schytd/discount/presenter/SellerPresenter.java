package com.schytd.discount.presenter;

import com.schytd.discount.model.ISellerModel;
import com.schytd.discount.model.SellerModel;
import com.schytd.discount.ui.iui.ISellerView_FragmentMain;
import com.schytd.discount.ui.iui.ISellerView_FragmentNear;

import android.content.Context;

public class SellerPresenter implements OnSellerInternetListener {
	private ISellerModel iSellerModel;
	private ISellerView_FragmentMain iSellerView_FragmentMain;
	private ISellerView_FragmentNear iSellerView_FragmentNear;

	public SellerPresenter(ISellerView_FragmentMain view, Context c) {
		iSellerView_FragmentMain = view;
		iSellerModel = new SellerModel(c);
	}

	public SellerPresenter(ISellerView_FragmentNear view, Context c) {
		iSellerView_FragmentNear = view;
		iSellerModel = new SellerModel(c);
	}

	// 得到优惠资讯
	public void getDiscountInfo(String... params) {
		try {
			String data = iSellerModel.getSellerDiscountInfo(this, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 解析数据
		iSellerView_FragmentMain.setDiscountInfoData(null);
	}

	// 得到商家列表 请求
	public void getNearSeller(String... params) {
		// 显示进度条
		try {
			String data = iSellerModel.getSellerInfo(this, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iSellerView_FragmentNear.setNearSellerInfoData(null);
	}

	// 请求成功
	@Override
	public void success(String str) {
		// TODO Auto-generated method stub
		// 隐藏进度条
		// 进行界面数据更新

	}

	// 请求失败
	@Override
	public void onFailed() {
		// 隐藏进度条
		// 进行界面数据更新
		// TODO Auto-generated method stub

	}
}
