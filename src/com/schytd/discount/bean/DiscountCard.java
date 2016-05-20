package com.schytd.discount.bean;

import java.util.ArrayList;
import java.util.List;

public class DiscountCard {
	private String code;
	private String mesaage;
	private String totalCount;
	private String totalPage;
	private ArrayList<DiscountCardItem> resultList;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMesaage() {
		return mesaage;
	}
	public void setMesaage(String mesaage) {
		this.mesaage = mesaage;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public String getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}
	public ArrayList<DiscountCardItem> getResultList() {
		return resultList;
	}
	public void setResultList(ArrayList<DiscountCardItem> resultList) {
		this.resultList = resultList;
	}
	

}
