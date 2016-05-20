package com.schytd.discount.bean;

import java.util.ArrayList;
import java.util.List;

public class UserPayItem {
	private String code;
	private String message;
	private String id;// 账单id
	private String bussinessName;
	private String totalAmount;// 原总价
	private String afterDiscountAmount;// 折后总价
	private String userBaseId;// 待付款用户基本信息ID
	private String baseInfoId;// 待收款商家基本信息ID
	private String isPay;// 是否支付（1：已支付、-1：作废、0：未支付）
	private String createTime;// 创建时间
	private ArrayList<PayDiscountItem> resultList;// 折扣结果集
	private String noDiscountAmount;// 无折扣项目
    private String cardItem;//使用的优惠券
	public String getCardItem() {
		return cardItem;
	}

	public void setCardItem(String cardItem) {
		this.cardItem = cardItem;
	}

	public String getNoDiscountAmount() {
		return noDiscountAmount;
	}

	public void setNoDiscountAmount(String noDiscountAmount) {
		this.noDiscountAmount = noDiscountAmount;
	}

	public ArrayList<PayDiscountItem> getResultList() {
		return resultList;
	}

	public void setResultList(ArrayList<PayDiscountItem> resultList) {
		this.resultList = resultList;
	}

	public String getBussinessName() {
		return bussinessName;
	}

	public void setBussinessName(String bussinessName) {
		this.bussinessName = bussinessName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getAfterDiscountAmount() {
		return afterDiscountAmount;
	}

	public void setAfterDiscountAmount(String afterDiscountAmount) {
		this.afterDiscountAmount = afterDiscountAmount;
	}

	public String getUserBaseId() {
		return userBaseId;
	}

	public void setUserBaseId(String userBaseId) {
		this.userBaseId = userBaseId;
	}

	public String getBaseInfoId() {
		return baseInfoId;
	}

	public void setBaseInfoId(String baseInfoId) {
		this.baseInfoId = baseInfoId;
	}

	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
