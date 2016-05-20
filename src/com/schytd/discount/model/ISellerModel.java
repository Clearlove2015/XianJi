package com.schytd.discount.model;

import com.schytd.discount.presenter.OnSellerInternetListener;

public interface ISellerModel {
	// 得到优惠咨询
	public String getSellerDiscountInfo(OnSellerInternetListener internetListener, String... params) throws Exception;

	// 得到附件商家信息
	public String getSellerInfo(OnSellerInternetListener internetListener, String... params) throws Exception;

}
