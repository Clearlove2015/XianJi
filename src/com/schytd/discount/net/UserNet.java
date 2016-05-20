package com.schytd.discount.net;

import java.util.List;

import com.schytd.discount.bean.ConsumHistory;
import com.schytd.discount.bean.ConsumptionTimesAndAmount;
import com.schytd.discount.bean.DiscountCard;
import com.schytd.discount.bean.IntroductionInfo;
import com.schytd.discount.bean.Level;
import com.schytd.discount.bean.ScoreGetHistory;
import com.schytd.discount.bean.UseScoreHistory;
import com.schytd.discount.bean.User;
import com.schytd.discount.bean.UserPay;
import com.schytd.discount.bean.UserPayItem;

public interface UserNet {
	public List<String> toLogin(String... params) throws Exception;

	public String toRegiester(String... params) throws Exception;

	public String isRegiester(String... params) throws Exception;

	public String getCode(String... params) throws Exception;

	// 请求用户信息
	public User getUserInfo(String... params) throws Exception;

	// 修改用户信息
	public String UpdateUserInfo(String... params) throws Exception;

	// 用户忘记密码 重新设置
	public String reSetUserPassword(String... params) throws Exception;

	// 获取用户的消费次数和消费金额
	public ConsumptionTimesAndAmount getConsumforService(String sessionId)
			throws Exception;

	// 获取用户消费历史记录
	public ConsumHistory getConsumHistoryFromService(String... params)
			throws Exception;

	// 注销
	public String toUnRegister(String sessionId) throws Exception;

	// 获取用户的积分和等级
	public Level getLevel(String sessionId) throws Exception;

	// 用户获得积分历史记录
	public ScoreGetHistory getScoreHistory(String... params) throws Exception;

	// 用户积分消费记录
	public UseScoreHistory getUseScoreHistory(String... params)
			throws Exception;

	// 有奖推广
	public IntroductionInfo getIntroductionInfoDetails(String sessionId)
			throws Exception;

	// 用户反馈
	public String userAdvice(String... params) throws Exception;

	// 上传头像
	public List<String> upLoadUserImage(String... params) throws Exception;

	// 用户账单
	public UserPay getUserPaysFormService(String... params) throws Exception;

	// 账单详情 账单编号
	public UserPayItem getUserPayDetailFormService(String... params)
			throws Exception;

	// 用户支付
	public String UserToPayFormService(String... params) throws Exception;

	// 获得现金券
	public DiscountCard getDiscountCard(String... params) throws Exception;
}
