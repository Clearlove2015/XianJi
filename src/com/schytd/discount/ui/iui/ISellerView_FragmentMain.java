package com.schytd.discount.ui.iui;

import java.util.ArrayList;

import com.schytd.discount.bean.SellerInfoItem;

public interface ISellerView_FragmentMain{
	// 得到轮播图片路径
	public void setImageList(String[] imgurl);

	// 首页baseapter设置list数据源
	public void setDiscountInfoData(ArrayList<SellerInfoItem> mData);
}
