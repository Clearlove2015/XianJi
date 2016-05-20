package com.schytd.discount.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.schytd.discount.bean.ConsumHistory;
import com.schytd.discount.bean.ConsumHistoryItem;
import com.schytd.discount.bean.ConsumptionTimesAndAmount;
import com.schytd.discount.bean.DiscountCard;
import com.schytd.discount.bean.IntroductionInfo;
import com.schytd.discount.bean.Level;
import com.schytd.discount.bean.ScoreGetHistory;
import com.schytd.discount.bean.ScoreGetHistoryItem;
import com.schytd.discount.bean.UseScoreHistory;
import com.schytd.discount.bean.UseScoreHistoryItem;
import com.schytd.discount.bean.User;
import com.schytd.discount.bean.UserPay;
import com.schytd.discount.bean.UserPayItem;
import com.schytd.discount.bean.UserSessionId;
import com.schytd.discount.business.UserBusiness;
import com.schytd.discount.dao.UserDao;
import com.schytd.discount.dao.impl.UserDaoImp;
import com.schytd.discount.net.UserNet;
import com.schytd.discount.net.impl.UserNetImpl;
import com.schytd.discount.tools.ImageTools;
import com.schytd.discount.tools.NetTools;
import com.schytd.discount.tools.StrTools;

public class UserBusinessImpl implements UserBusiness {
	// private String payPassword = "cc.huixiaofei.appserver";
	private String payPassword = "1234567890123456";
	private UserDao mUserDao;
	private UserNet mUserNet;
	private String sessinId = null;
	// 存储用户消费历史，积分等总页数
	private SharedPreferences sp_data_page;

	public UserBusinessImpl() {
		
	}

	public UserBusinessImpl(Context context) {
		mUserDao = new UserDaoImp(context);
		mUserNet = new UserNetImpl(context);
		sp_data_page = context.getSharedPreferences("data_page",
				Context.MODE_PRIVATE);
		mUserNet = new UserNetImpl(context);
	}

	@Override
	public String userRegister(String... params) throws Exception {
		String result = null;
		// 注册
		result = mUserNet.toRegiester("1.0", "user.doreg", "android_app",
				params[0], params[1], params[2], params[3]);
		return result;
	}

	/**
	 * @params 电话号码
	 */
	@Override
	public List<String> userLogin(String... params) throws Exception {
		List<String> result;
		result = mUserNet.toLogin("1.0", "user.dologin", "android_app",
				params[0], params[1]);
		return result;
	}

	@Override
	public Boolean userIsRegister(String params) throws Exception {
		String result = null;
		// 是否注册
		result = mUserNet.isRegiester("1.0", "user.isReged", "android_app",
				params);
		if (StrTools.isNull(result)) {
			return false;
		}
		if (!result.equals("0")) {
			return true;
		}
		return false;
	}

	// 获得验证码
	@Override
	public Boolean getCaptchaCode(String params) throws Exception {
		String result = null;
		// 获取验证码是否成功
		result = mUserNet.getCode("1.0", "user.sendcaptcha", "android_app",
				params);
		if (StrTools.isNull(result)) {
			return false;
		}
		if (result.equals("0")) {
			return true;
		}
		return false;
	}

	@Override
	public void newUserSessionId(UserSessionId user) throws Exception {
		mUserDao.insertUserSessionId(user);
	}

	@Override
	public UserSessionId getUserSessionId() throws Exception {
		UserSessionId sessionId = mUserDao.selectUserSessionId();
		return sessionId;
	}

	@Override
	public void newUser(User user) throws Exception {
		mUserDao.insertUser(user);
	}

	private static User user = null;

	// 获得用户信息
	@Override
	public User getUserinfo(final String sessionId) throws Exception {
		user = mUserDao.selectUser();
		// 没有sessionId
		if (StrTools.isNull(sessionId)) {
			throw new RuntimeException("");
		}
		// 数据没有该用户的缓存 从网络端获取
		if (user.getName() == null || user.getNum() == null
				|| user.getGender() == null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						user = mUserNet.getUserInfo("1.0",
								"user.getUserBaseInfo", "android_app",
								sessionId);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			// 并且再次存入数据库
			mUserDao.delteUser();
			mUserDao.insertUser(user);
		}
		return user;
	}

	// 获得用户信息
	@Override
	public User getUserinfo() throws Exception {
		user = mUserDao.selectUser();
		// 没有sessionId
		sessinId = getSessionId();
		if (sessinId == null) {
			return null;
		}
		// 数据没有该用户的缓存 从网络端获取
		if (user == null || user.getName() == null || user.getNum() == null
				|| user.getGender() == null) {
			user = mUserNet.getUserInfo("1.0", "user.getUserBaseInfo",
					"android_app", sessinId);
			// 并且再次存入数据库
			mUserDao.delteUser();
			mUserDao.insertUser(user);
		}
		return user;
	}

	// 删除用户
	@Override
	public boolean removeUser() throws Exception {
		int i = -1;
		i = mUserDao.delteUser();
		if (i < 0) {
			return true;
		}
		return false;
	}

	// 传入三个参数 修改用户信息
	@Override
	public boolean alterUserInfo(String... params) throws Exception {
		String result = null;
		String gender = null;
		sessinId = getSessionId();
		String type = params[0];
		if (type.equals("1")) {
			// 修改昵称
			result = mUserNet.UpdateUserInfo("1.0", "user.updateUserBaseInfo",
					"android_app", "1", params[1], sessinId);
			// 存入数据库
			if (result.equals("0")) {
				mUserDao.updateUserInfo(1, "name", params[1]);
			}

		} else if (type.equals("2")) {
			// 修改性别
			result = mUserNet.UpdateUserInfo("1.0", "user.updateUserBaseInfo",
					"android_app", "2", params[1], sessinId, params[2]);
			// 存入数据库
			if (params[1].equals("1")) {
				gender = "男";
			} else if (params[1].equals("2")) {
				gender = "女";
			}
			if (result.equals("0")) {
				mUserDao.updateUserInfo(1, "gender", gender);
			}
		} else if (type.equals("3")) {
			// 修改密码
			result = mUserNet.UpdateUserInfo("1.0", "user.updateUserBaseInfo",
					"android_app", "3", params[1], params[2], sessinId);
			// 存入数据库
			if (result.equals("0")) {
				mUserDao.updateUserLoginInfo("password", params[2]);
			}
		}
		if (!StrTools.isNull(result)) {
			if (result.equals("0")) {
				return true;
			}
		}
		return false;
	}

	// 忘记密码 修改
	@Override
	public boolean forgetUserPassword(String... params) throws Exception {
		String result = null;
		String newPassword = NetTools.getMD5Code(params[0].concat(params[2])
				.trim().getBytes());
		// 传向网络端修改密码
		result = mUserNet.reSetUserPassword("1.0", "user.resetPassword",
				"android_app", params[0], params[1], newPassword);
		if (result.equals("0")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public UserSessionId getUserSessionId(Integer id) throws Exception {
		UserSessionId userSessionId = null;
		userSessionId = mUserDao.selectUserSessionId();
		if (userSessionId != null) {
			return userSessionId;
		}
		return null;
	}

	public String getSessionId() throws Exception {
		UserSessionId userSessionId = null;
		String sessionId = null;
		userSessionId = getUserSessionId();
		long result = 0;
		if (userSessionId != null) {
			sessionId = userSessionId.getSessionid();
			long time = userSessionId.getTime();
			result = System.currentTimeMillis() - time;
			if (result >= 3600000 || sessionId.equals("0")) {
				removeSessionId();
				User user = mUserDao.selectUserLoginInfo();
				String num = user.getNum();
				String password = user.getPassword();
				sessionId = userLogin(num, password).get(1);
				// 封装newSessionId对象
				UserSessionId newSessionId = new UserSessionId();
				newSessionId.setId(1);
				newSessionId.setSessionid(sessionId);
				newSessionId.setTime(System.currentTimeMillis());
				newUserSessionId(newSessionId);
				return sessionId;
			} else {
				return sessionId;
			}
		} else {
			return sessionId;
		}
	}

	@Override
	public int removeSessionId() throws Exception {
		int i = 0;
		i = mUserDao.delteUserSessionId();
		return i;
	}

	@Override
	public User getUser() throws Exception {
		User user = null;
		user = mUserDao.selectUser();
		return user;
	}

	// 获取消费总金额和总次数
	@Override
	public ConsumptionTimesAndAmount getConsumInfo() throws Exception {
		String sessionId = null;
		sessionId = getSessionId();
		return mUserNet.getConsumforService(sessionId);
	}

	// 历史消费记录
	@Override
	public ConsumHistory getConsumHistory(String... params) throws Exception {
		// 到数据库获取
		ConsumHistory history = new ConsumHistory();
		ArrayList<ConsumHistoryItem> resultList = null;
		try {
			resultList = mUserDao.selectConsumHistory(params[1]);
			if (resultList != null) {
				history.setResultList(resultList);
			}
		} finally {
			if (resultList == null || resultList.size() == 0) {
				// 从网络获取
				history = mUserNet.getConsumHistoryFromService(getSessionId(),
						params[0], params[1]);
				if (history != null) {
					// 储存消费总页数
					Editor editor = sp_data_page.edit();
					editor.putInt("consum_total_page", history.getPageNum());
					editor.commit();
					// 判断数据库是否有
					ArrayList<ConsumHistoryItem> result = history
							.getResultList();
					for (ConsumHistoryItem consumHistoryItem : result) {
						consumHistoryItem.setPage(params[1]);
					}
					// 并写入数据库
					mUserDao.insertConsumHistory(result);
				}
			}
		}
		return history;
	}

	// 积分和用户等级
	@Override
	public Level getUserLevel() throws Exception {
		return mUserNet.getLevel(getSessionId());
	}

	// 积分获得记录
	@Override
	public ScoreGetHistory getScoreGetHistory(String... params)
			throws Exception {
		// 到数据库获取
		ScoreGetHistory scoreGetHistory = new ScoreGetHistory();
		ArrayList<ScoreGetHistoryItem> resultList = null;
		try {
			resultList = mUserDao.selectScoreHistory(params[1]);
			if (resultList != null) {
				scoreGetHistory.setResultList(resultList);
			}
		} finally {
			if (resultList == null || resultList.size() == 0) {
				scoreGetHistory = mUserNet.getScoreHistory(getSessionId(),
						params[0], params[1]);
				// 储存积分获得总页数
				Editor editor = sp_data_page.edit();
				editor.putInt("score_total_page", scoreGetHistory.getPageNum());
				editor.commit();
				ArrayList<ScoreGetHistoryItem> result = scoreGetHistory
						.getResultList();
				for (ScoreGetHistoryItem scoreGetHistoryItem : result) {
					scoreGetHistoryItem.setPage(params[1]);
				}
				// 存入数据库
				mUserDao.insertScoreHistory(result);
			}
		}
		return scoreGetHistory;
	}

	// 积分消费记录
	@Override
	public UseScoreHistory getScoreUseHistory(String... params)
			throws Exception {
		// 到数据库获取
		UseScoreHistory scoreUseHistory = new UseScoreHistory();
		ArrayList<UseScoreHistoryItem> resultList = null;
		try {
			resultList = mUserDao.selectUseScoreHistory(params[1]);
			if (resultList != null) {
				scoreUseHistory.setResultList(resultList);
			}
		} finally {
			if (resultList == null || resultList.size() == 0) {
				scoreUseHistory = mUserNet.getUseScoreHistory(getSessionId(),
						params[0], params[1]);
				if (scoreUseHistory != null) {
					// 储存积分消费总页数
					Editor editor = sp_data_page.edit();
					editor.putInt("score_use_total_page",
							scoreUseHistory.getPageNum());
					editor.commit();
					ArrayList<UseScoreHistoryItem> result = scoreUseHistory
							.getResultList();
					for (UseScoreHistoryItem useScoreHistoryItem : result) {
						useScoreHistoryItem.setPage(params[1]);
					}
					// 存入数据库
					mUserDao.insertUseScoreHistory(result);
				}
			}
		}
		return scoreUseHistory;
	}

	// 注销
	@Override
	public String toLogout() throws Exception {
		String sessionId = null;
		sessionId = getSessionId();
		removeUserData();
		return mUserNet.toUnRegister(sessionId);
	}

	// 有奖推广
	@Override
	public IntroductionInfo toIntroductionInfo() throws Exception {
		return mUserNet.getIntroductionInfoDetails(getSessionId());
	}

	// 保存用户登陆信息
	@Override
	public void addUserLoginInfo(User user) throws Exception {
		mUserDao.deleteUserLoginInfo();
		mUserDao.insertUserLoginInfo(user);
	}

	@Override
	public void delteUserLoginInfo() throws Exception {
		mUserDao.deleteUserLoginInfo();
	}

	@Override
	public Boolean UserAdvice(String... params) throws Exception {
		String code = mUserNet.userAdvice(params[0], params[1]);
		if (StrTools.isNull(code)) {
			return false;
		}
		if (code.equals("0")) {
			return true;
		}
		return false;
	}

	// 清除用户缓存的信息
	@Override
	public void removeUserData() throws Exception {
		mUserDao.deleteConsumHistory();
		mUserDao.deleteScoreHistory();
		mUserDao.deleteUseScoreHistory();
		mUserDao.deleteUserLoginInfo();
		mUserDao.delteUser();
	}
	

	// 删除用户登陆信息 注销时
	public void removeUserInfo() throws Exception {
		delteUserLoginInfo();
		removeSessionId();
		removeUserData();
	}
	@Override
	public void refreshCache() throws Exception {
		mUserDao.deleteConsumHistory();
		mUserDao.deleteScoreHistory();
	}

	// 上次头像
	@Override
	public List<String> upLoadUserImage(String params, String path)
			throws Exception {
		// sessionid
		List<String> result = null;
		String sessionId = null;
		sessionId = getSessionId();
		// 对图片精选base64 编码处理
		String imageFile = ImageTools.getFileToByte(path);
		if (imageFile != null && !StrTools.isNull(params)
				&& !StrTools.isNull(sessionId)) {
			// Log.d("加密后：", params + "@" + imageFile);
			result = mUserNet.upLoadUserImage(sessionId, params + "@"
					+ imageFile);
			if (result.get(0).equals("0")) {
				mUserDao.updateUserInfo(1, "path", result.get(1));
			}
			return result;
		}
		return null;
	}

	// 得到账单
	/*
	 * 
	 * @see
	 * com.schytd.discount.business.UserBusiness#getUserPays(java.lang.String[])
	 * 
	 * @params userBaseId(num) isPay pageSize currentPage
	 */
	@Override
	public UserPay getUserPays(String... params) throws Exception {
		String sessionId = getSessionId();
		User user = getUserinfo(sessionId);
		UserPay userPay = mUserNet.getUserPaysFormService(sessionId,
				user.getNum(), params[0], params[1], params[2]);
		if (userPay != null) {
			// 储存积分获得总页数
			Editor editor = sp_data_page.edit();
			editor.putInt("pay_total_page",
					Integer.valueOf(userPay.getTotalPage()));
			editor.commit();
		}
		return userPay;
	}

	// 账单详情 @params 账单id
	@Override
	public UserPayItem getUserPayDetail(String params) throws Exception {
		return mUserNet.getUserPayDetailFormService(getSessionId(), params);
	}

	// 用户支付 @params 账单 id,支付密码，
	// payInfoList
	// [{payType:1,payAmount:320},{payType:2,payAmount:600,voucherId:32131232323231}]
	// 此数据需要用AESUtils进行加密；密钥:cc.huixiaofei.appserver
	@Override
	public String UserToPay(String... params) throws Exception {
		if (params.length < 1) {
			return "参数传递错误";
		}
		// [{"payAmount":"35.0","payType":"1"},{"payAmount":"200","payType":"2","voucherId":"3"},{"payAmount":"200","payType":"2","voucherId":"20000003"}]
		// [null,{"payAmount":"100","payType":"2","voucherId":"22220001"},{"payAmount":"200","payType":"2","voucherId":"3"},{"payAmount":135,"payType":"2","voucherId":"20000003"}]
		// [null,{"payAmount":"50","payType":"2","voucherId":"4"},{"payAmount":"100","payType":"2","voucherId":"22220001"},{"payAmount":"100","payType":"2","voucherId":"5"},{"payAmount":"200","payType":"2","voucherId":"3"},{"payAmount":"200","payType":"2","voucherId":"20000003"}]
		// [{"payAmount":"435.0","payType":"1"}]
		// [{"payAmount":"50","payType":"2","voucherId":"4"},{"payAmount":"100","payType":"2","voucherId":"22220001"},{"payAmount":"200","payType":"2","voucherId":"3"},null,{"payAmount":85,"payType":"2","voucherId":"20000003"}]
//		[{"payAmount":"50","payType":"2","voucherId":"4"},{"payAmount":"100","payType":"2","voucherId":"22220001"},{"payAmount":"200","payType":"2","voucherId":"3"},{"payAmount":85,"payType":"2","voucherId":"20000003"}]
		return mUserNet.UserToPayFormService(getSessionId(), params[0],
				params[1], params[2]);
	}

	@Override
	public DiscountCard getDiscounTCard(String... params) throws Exception {
		String sessionId = getSessionId();
		User user = getUserinfo(sessionId);
		return mUserNet.getDiscountCard(sessionId, user.getNum(), "1",
				params[0], params[1]);
	}
}
