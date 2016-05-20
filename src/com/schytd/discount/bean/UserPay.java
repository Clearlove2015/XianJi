package com.schytd.discount.bean;

import java.util.List;

public class UserPay {
	private String code;
	private String message;
	private List<UserPayItem> resultList;
	private String totalCount;// 每页个数
	private String totalPage; // 总页数

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

	public List<UserPayItem> getResultList() {
		return resultList;
	}

	public void setResultList(List<UserPayItem> resultList) {
		this.resultList = resultList;
	}

}
