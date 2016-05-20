package com.schytd.discount.business;

import java.util.ArrayList;

import com.schytd.discount.bean.IndexImage;
import com.schytd.discount.bean.SellerDetail;
import com.schytd.discount.bean.SellerInfo;
import com.schytd.discount.bean.SellerInfoItem;

public interface SellerBusiness {
//得到商家
	public SellerInfo getSellerInfo(String...params) throws Exception;
//	获得商家详细信息
	public SellerDetail getSellerDetailInfo(String...params) throws Exception;
//	首页轮播图
	public ArrayList<IndexImage> getIndexImageList() throws Exception;
//	优惠资讯
	public SellerInfo getDiscountInfo(String...params) throws Exception;
//	插入浏览历史
	public void addReadHistory(int id,String bid,String time) throws Exception;
//	得到浏览历史
	public ArrayList<SellerInfoItem> getReadHistory() throws Exception;
//	刪除浏览历史
	public void DeleteReadHistoty() throws Exception;
//	得到时间
	public ArrayList<String> getReadTime() throws Exception;
//	删除商家缓存
	public void deleteSellerCache() throws Exception;
}
