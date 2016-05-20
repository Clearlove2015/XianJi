package com.schytd.discount.bean;

import java.io.Serializable;

public class DiscountCardItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;// 现金券编号
	private String amount;// 现金券金额
	private String businessName;// 适用商家
	private String baseInfoId;// 适用商家基本信息ID
	private String isPay;
	private String createTime;// 创建时间
	private String endDate;// 过期时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
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

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
