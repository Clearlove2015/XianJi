package com.schytd.discount.bean;

public class PayDiscountItem {
	private String discount;
	private String discountName;
	private String amount;
	private String afterAmount;
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getDiscountName() {
		return discountName;
	}
	public void setDiscountName(String discountName) {
		this.discountName = discountName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAfterAmount() {
		return afterAmount;
	}
	public void setAfterAmount(String afterAmount) {
		this.afterAmount = afterAmount;
	}
}
