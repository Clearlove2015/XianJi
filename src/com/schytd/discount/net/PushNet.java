package com.schytd.discount.net;

import java.util.List;

import com.schytd.discount.bean.PushInfo;
import com.schytd.discount.bean.PushItem;

public interface PushNet {
//	得到每日推送
	public List<PushItem> getPushInfoOfDay() throws Exception;
//	得到消息详情
	public PushInfo getPushDetail(String...params)throws Exception;
}
