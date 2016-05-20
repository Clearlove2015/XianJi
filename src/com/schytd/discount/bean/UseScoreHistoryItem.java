package com.schytd.discount.bean;

public class UseScoreHistoryItem {
	private String userBaseId;
	private String score;
	private int useType;
	private String useTime;
	private String page;

	public UseScoreHistoryItem() {
	}

	public UseScoreHistoryItem(String userBaseId, String score, int useType,
			String useTime) {
		this.userBaseId = userBaseId;
		this.score = score;
		this.useType = useType;
		this.useTime = useTime;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getUserBaseId() {
		return userBaseId;
	}

	public void setUserBaseId(String userBaseId) {
		this.userBaseId = userBaseId;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public int getUseType() {
		return useType;
	}

	public void setUseType(int useType) {
		this.useType = useType;
	}

	public String getUseTime() {
		return useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}

}
