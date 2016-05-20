package com.schytd.discount.business;

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
import com.schytd.discount.bean.UserSessionId;

public interface UserBusiness {
	/***************** 网络业务层 ********************/
	// 头像上传
	public List<String> upLoadUserImage(String params, String path)
			throws Exception;

	public List<String> userLogin(String... params) throws Exception;

	public String userRegister(String... params) throws Exception;

	public Boolean userIsRegister(String params) throws Exception;

	public Boolean getCaptchaCode(String params) throws Exception;

	// 获取用户信息
	public User getUserinfo(String params) throws Exception;

	public User getUserinfo() throws Exception;

	// 修改用户信息
	public boolean alterUserInfo(String... params) throws Exception;

	// 重置密码
	public boolean forgetUserPassword(String... params) throws Exception;

	// 获取用户消费总次数、总金额
	public ConsumptionTimesAndAmount getConsumInfo() throws Exception;

	// 获取用户获取用户历史消费记录表
	public ConsumHistory getConsumHistory(String... params) throws Exception;

	// 积分中心和个人等级
	public Level getUserLevel() throws Exception;

	// 获取用户积分获得记录
	public ScoreGetHistory getScoreGetHistory(String... params)
			throws Exception;

	// 获得用户积分消费记录
	public UseScoreHistory getScoreUseHistory(String... params)
			throws Exception;

	// 注销
	public String toLogout() throws Exception;

	// 有奖推广
	public IntroductionInfo toIntroductionInfo() throws Exception;

	// 意见反馈
	public Boolean UserAdvice(String... params) throws Exception;

	/*******************************************/

	/***************** SQLite业务层 ****************/

	public void newUserSessionId(UserSessionId user) throws Exception;

	public UserSessionId getUserSessionId(Integer id) throws Exception;

	public void newUser(User user) throws Exception;

	// 得到用户名信息
	public User getUser() throws Exception;

	// 保存用户登陆信息
	public void addUserLoginInfo(User user) throws Exception;

	// 删除用户的登陆信息
	public void delteUserLoginInfo() throws Exception;

	// 当用户注销时 删除用户信息
	public boolean removeUser() throws Exception;

	// 从数据库获得
	public UserSessionId getUserSessionId() throws Exception;

	// 得到sessinId
	public String getSessionId() throws Exception;

	// 删除数据库的sessionID
	public int removeSessionId() throws Exception;

	// 清除用户缓存 清除用户的历史消费记录和积分记录等数据
	public void removeUserData() throws Exception;

	/**********************************************/
	// 用户账单
	public UserPay getUserPays(String... params) throws Exception;

	// 账单详情 账单编号
	public UserPayItem getUserPayDetail(String billId) throws Exception;

	// 用户支付
	public String UserToPay(String... params) throws Exception;

	// 注销-删除信息
	public void removeUserInfo() throws Exception;
 //	刷新缓存
	public void refreshCache()throws Exception;

	// 得到现金券
	public DiscountCard getDiscounTCard(String... params)
			throws Exception;

}
