package com.schytd.discount.net.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.schytd.discount.bean.ConstantData;
import com.schytd.discount.bean.ConsumHistory;
import com.schytd.discount.bean.ConsumHistoryItem;
import com.schytd.discount.bean.ConsumptionTimesAndAmount;
import com.schytd.discount.bean.DiscountCard;
import com.schytd.discount.bean.DiscountCardItem;
import com.schytd.discount.bean.IntroductionInfo;
import com.schytd.discount.bean.Level;
import com.schytd.discount.bean.PayDiscountItem;
import com.schytd.discount.bean.ScoreGetHistory;
import com.schytd.discount.bean.ScoreGetHistoryItem;
import com.schytd.discount.bean.UseScoreHistory;
import com.schytd.discount.bean.UseScoreHistoryItem;
import com.schytd.discount.bean.User;
import com.schytd.discount.bean.UserPay;
import com.schytd.discount.bean.UserPayItem;
import com.schytd.discount.net.UserNet;
import com.schytd.discount.tools.NetTools;
import com.schytd.discount.tools.StrTools;

public class UserNetImpl extends ConnectInternetHelper implements UserNet {
	public UserNetImpl(Context c) {
		super(c);
	}

	@Override
	public String toRegiester(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		NameValuePair parameter_v = new BasicNameValuePair("v", params[0]);
		NameValuePair parameter_method = new BasicNameValuePair("method", params[1]);
		NameValuePair parameter_appkey = new BasicNameValuePair("appKey", params[2]);
		NameValuePair parameter_code = new BasicNameValuePair("captchaCode", params[3]);
		NameValuePair parameter_phonenum = new BasicNameValuePair("phoneNum", params[4]);
		NameValuePair parameter_password = new BasicNameValuePair("pwd", params[5]);
		if (!StrTools.isNull(params[6])) {
			NameValuePair parameter_introductionCode = new BasicNameValuePair("introductionCode", params[6]);
			parameters.add(parameter_introductionCode);
		}
		parameters.add(parameter_v);
		parameters.add(parameter_method);
		parameters.add(parameter_appkey);
		parameters.add(parameter_phonenum);
		parameters.add(parameter_password);
		parameters.add(parameter_code);

		// 生成签名
		String sign = NetTools.sign(parameters, ConstantData.SECRET);
		// 添加
		NameValuePair parameter_sign = new BasicNameValuePair("sign", sign);
		parameters.add(parameter_sign);
		post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("请求服务器错误.");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		JSONObject resultsJSON = new JSONObject(results);
		String resul = resultsJSON.getString("code");
		Log.d("+++++++++++++", "注册" + results);

		return resul;
	}

	@Override
	public List<String> toLogin(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		NameValuePair parameter_v = new BasicNameValuePair("v", params[0]);
		NameValuePair parameter_method = new BasicNameValuePair("method", params[1]);
		NameValuePair parameter_appkey = new BasicNameValuePair("appKey", params[2]);
		NameValuePair parameter_phonenum = new BasicNameValuePair("phoneNum", params[3]);
		NameValuePair parameter_pwd = new BasicNameValuePair("pwd", params[4]);

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(parameter_v);
		parameters.add(parameter_appkey);
		parameters.add(parameter_method);
		parameters.add(parameter_phonenum);
		parameters.add(parameter_pwd);

		String sign = NetTools.sign(parameters, ConstantData.SECRET);
		NameValuePair parameter_sign = new BasicNameValuePair("sign", sign);
		parameters.add(parameter_sign);

		post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
		HttpResponse response = client.execute(post);

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("请求服务器错误。");
		}
		List<String> result = new ArrayList<String>();
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.has("code") ? resultsJSON.getString("code") : null;
		result.add(code);
		if (code.equals("0")) {
			String sessionId = resultsJSON.getString("sessionId");
			result.add(sessionId);
		}
		return result;
	}

	// 判断用户是否已经注册
	@Override
	public String isRegiester(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);

		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", params[0]));
		strParams.add(new BasicNameValuePair("method", params[1]));
		strParams.add(new BasicNameValuePair("appKey", params[2]));
		strParams.add(new BasicNameValuePair("phoneNum", params[3]));
		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.getString("code");
		// Log.d("+++++++++++++", "++++++" + results);
		return code;
	}

	// 获得验证码
	@Override
	public String getCode(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", params[0]));
		strParams.add(new BasicNameValuePair("method", params[1]));
		strParams.add(new BasicNameValuePair("appKey", params[2]));
		strParams.add(new BasicNameValuePair("phoneNum", params[3]));
		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.getString("code");
		Log.d("+++++++++++++", "Code" + results);
		return code;

	}

	// 获取用户信息
	@Override
	public User getUserInfo(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", params[0]));
		strParams.add(new BasicNameValuePair("method", params[1]));
		strParams.add(new BasicNameValuePair("appKey", params[2]));
		strParams.add(new BasicNameValuePair("sessionId", params[3]));

		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		User user = new User();
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.getString("code");
		if (!code.equals("0")) {
			return null;
		}
		String nickname = resultsJSON.getString("nickName");
		String gender_ = resultsJSON.getString("gender");
		String phoneNum = resultsJSON.getString("phoneNum");
		String iconPath = resultsJSON.getString("qrcodeUrl");
		String gender = null;
		if (gender_.equals("1")) {
			gender = "男";
		} else if (gender_.equals("2")) {
			gender = "女";
		} else if (gender_.equals("-1")) {
			gender = "未知";
		}
		if (StrTools.isNull(phoneNum) || StrTools.isNull(gender) || StrTools.isNull(nickname)) {
			return null;
		}
		user.setName(nickname);
		user.setGender(gender);
		user.setNum(phoneNum);
		user.setUserIcon(iconPath);
		Log.d("++++++++++++", results);
		return user;
	}

	// 更改用户信息
	@Override
	public String UpdateUserInfo(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", params[0]));
		strParams.add(new BasicNameValuePair("method", params[1]));
		strParams.add(new BasicNameValuePair("appKey", params[2]));
		strParams.add(new BasicNameValuePair("type", params[3]));
		String type = null;
		type = params[3];
		if (type == null) {
			return "错误";
		} else {
			if (type.equals("1")) {
				strParams.add(new BasicNameValuePair("nickName", params[4]));
				strParams.add(new BasicNameValuePair("sessionId", params[5]));
			} else if (type.equals("2")) {
				strParams.add(new BasicNameValuePair("gender", params[4]));
				strParams.add(new BasicNameValuePair("sessionId", params[5]));
				strParams.add(new BasicNameValuePair("nickName", params[6]));
			} else if (type.equals("3")) {
				strParams.add(new BasicNameValuePair("pwd", params[4]));
				strParams.add(new BasicNameValuePair("newPwd", params[5]));
				strParams.add(new BasicNameValuePair("sessionId", params[6]));
			}
		}
		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.getString("code");
		Log.d("+++++++++++++", "更改：Code = " + results);
		return code;
	}

	// 重置密码
	@Override
	public String reSetUserPassword(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", params[0]));
		strParams.add(new BasicNameValuePair("method", params[1]));
		strParams.add(new BasicNameValuePair("appKey", params[2]));
		strParams.add(new BasicNameValuePair("phoneNum", params[3]));
		strParams.add(new BasicNameValuePair("captchaCode", params[4]));
		strParams.add(new BasicNameValuePair("newPwd", params[5]));
		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.getString("code");
		// Log.d("+++++++++++++", "++++++" + results);
		return code;
	}

	@Override
	public ConsumptionTimesAndAmount getConsumforService(String sessionId) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", "1.0"));
		strParams.add(new BasicNameValuePair("method", "user.getConsumptionTimesAndAmount"));
		strParams.add(new BasicNameValuePair("appKey", "android_app"));
		strParams.add(new BasicNameValuePair("sessionId", sessionId));

		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		ConsumptionTimesAndAmount andAmount = new ConsumptionTimesAndAmount();
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.getString("code");
		if (!code.equals("0")) {
			return null;
		}
		String times = resultsJSON.getString("times");
		String total = resultsJSON.getString("amount");
		andAmount.setTimes(times);
		andAmount.setTotal(total);
		Log.d("++++++++++++", results);
		return andAmount;
	}

	@Override
	public ConsumHistory getConsumHistoryFromService(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", "1.0"));
		strParams.add(new BasicNameValuePair("method", "user.getUserConsumptionhistoryList"));
		strParams.add(new BasicNameValuePair("appKey", "android_app"));
		strParams.add(new BasicNameValuePair("sessionId", params[0]));
		strParams.add(new BasicNameValuePair("pageSize", params[1]));
		strParams.add(new BasicNameValuePair("currentPage", params[2]));
		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.getString("code");
		if (!code.equals("0")) {
			return null;
		}
		// 解析json
		ConsumHistory consumHistory = new ConsumHistory();
		int totalCount = resultsJSON.getInt("totalCount");
		ArrayList<ConsumHistoryItem> itemList = new ArrayList<ConsumHistoryItem>();
		if (totalCount == 0) {
			return null;
		}
		int pageNum = resultsJSON.getInt("totalPage");
		JSONArray resultList = resultsJSON.getJSONArray("resultList");
		for (int i = 0; i < resultList.length(); i++) {
			JSONObject item = resultList.getJSONObject(i);
			ConsumHistoryItem historyItem = new ConsumHistoryItem();
			historyItem.setId(item.getString("id"));
			historyItem.setBussinessName(item.getString("businessName"));
			historyItem.setAmount(item.getString("amount"));
			historyItem.setConsumptionTime(item.getString("consumptionTime"));
			itemList.add(historyItem);
		}

		consumHistory.setTotalCount(totalCount);
		consumHistory.setResultList(itemList);
		consumHistory.setPageNum(pageNum);
		Log.d("消费历史记录", results);
		return consumHistory;
	}

	@Override
	public String toUnRegister(String sessionId) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", "1.0"));
		strParams.add(new BasicNameValuePair("method", "user.dologout"));
		strParams.add(new BasicNameValuePair("appKey", "android_app"));
		strParams.add(new BasicNameValuePair("sessionId", sessionId));
		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.getString("code");
		Log.d("++++++++++++", results);

		return code;
	}

	@Override
	public Level getLevel(String sessionId) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", "1.0"));
		strParams.add(new BasicNameValuePair("method", "user.getUserScoreAndLevel"));
		strParams.add(new BasicNameValuePair("appKey", "android_app"));
		strParams.add(new BasicNameValuePair("sessionId", sessionId));

		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		Level userLevel = new Level();
		JSONObject resultsJSON = new JSONObject(results);
		// String code = resultsJSON.getString("code");
		// if (!code.equals("0")) {
		// return null;
		// }
		String score = resultsJSON.getString("score");
		String level = resultsJSON.getString("level");
		userLevel.setScore(score);
		userLevel.setLevel(level);
		Log.d("++++++++++++", results);
		return userLevel;
	}

	// 积分获得记录
	@Override
	public ScoreGetHistory getScoreHistory(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", "1.0"));
		strParams.add(new BasicNameValuePair("method", "user.getGetScoreHistory4Page"));
		strParams.add(new BasicNameValuePair("appKey", "android_app"));
		strParams.add(new BasicNameValuePair("sessionId", params[0]));
		strParams.add(new BasicNameValuePair("pageSize", params[1]));
		strParams.add(new BasicNameValuePair("currentPage", params[2]));
		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.getString("code");
		if (!code.equals("0")) {
			return null;
		}
		// 解析json
		ScoreGetHistory scoreHistory = new ScoreGetHistory();
		int totalCount = resultsJSON.getInt("totalCount");
		if (totalCount < 1) {
			return null;
		}
		int pageNum = resultsJSON.getInt("totalPage");
		ArrayList<ScoreGetHistoryItem> itemList = new ArrayList<ScoreGetHistoryItem>();
		JSONArray resultList = resultsJSON.getJSONArray("resultList");
		for (int i = 0; i < resultList.length(); i++) {
			JSONObject item = resultList.getJSONObject(i);
			ScoreGetHistoryItem historyItem = new ScoreGetHistoryItem();
			historyItem.setBaseInfoId(item.getString("baseInfoId"));
			historyItem.setBusinessName(item.getString("businessName"));
			historyItem.setScore(item.getString("score"));
			historyItem.setTheTime(item.getString("theTime"));
			historyItem.setAmount(item.getString("amount"));
			itemList.add(historyItem);
		}
		scoreHistory.setPageNum(pageNum);
		scoreHistory.setTotalCount(totalCount);
		scoreHistory.setResultList(itemList);
		Log.d("积分获得记录", results);
		return scoreHistory;
	}

	@Override
	public UseScoreHistory getUseScoreHistory(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", "1.0"));
		strParams.add(new BasicNameValuePair("method", "user.getUseScoreHistory4Page"));
		strParams.add(new BasicNameValuePair("appKey", "android_app"));
		strParams.add(new BasicNameValuePair("sessionId", params[0]));
		strParams.add(new BasicNameValuePair("pageSize", params[1]));
		strParams.add(new BasicNameValuePair("currentPage", params[2]));
		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.getString("code");
		if (!code.equals("0")) {
			return null;
		}
		// 解析json
		UseScoreHistory scoreHistory = new UseScoreHistory();
		int totalCount = resultsJSON.getInt("totalCount");
		Log.d("积分消费记录", results);
		ArrayList<UseScoreHistoryItem> itemList = new ArrayList<UseScoreHistoryItem>();
		if (totalCount < 1) {
			return null;
			// itemList.add(new UseScoreHistoryItem("1", "100", 1,
			// "1432548424704"));
			// scoreHistory.setPageNum(1);
			// scoreHistory.setResultList(itemList);
			// scoreHistory.setTotalCount(1);
			// return scoreHistory;
		}
		JSONArray resultList = resultsJSON.getJSONArray("resultList");
		for (int i = 0; i < resultList.length(); i++) {
			JSONObject item = resultList.getJSONObject(i);
			UseScoreHistoryItem historyItem = new UseScoreHistoryItem();
			historyItem.setUserBaseId(item.getString("userBaseId"));
			historyItem.setScore(item.getString("score"));
			historyItem.setUseType(item.getInt("useType"));
			historyItem.setUseTime(item.getString("UseTime"));
			itemList.add(historyItem);
		}

		scoreHistory.setTotalCount(totalCount);
		scoreHistory.setResultList(itemList);
		return scoreHistory;
	}

	// 有奖推广
	@Override
	public IntroductionInfo getIntroductionInfoDetails(String sessionId) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", "1.0"));
		strParams.add(new BasicNameValuePair("method", "user.getIntroductionInfoDetails"));
		strParams.add(new BasicNameValuePair("appKey", "android_app"));
		strParams.add(new BasicNameValuePair("sessionId", sessionId));
		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		IntroductionInfo introductionInfo = new IntroductionInfo();
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.getString("code");
		if (!code.equals("0")) {
			return null;
		}
		introductionInfo.setUserBaseId(resultsJSON.getString("userBaseId"));
		introductionInfo.setIntroductionCode(resultsJSON.getString("introductionCode"));
		introductionInfo.setEffectiveIntroductionCount(resultsJSON.getString("effectiveIntroductionCount"));
		introductionInfo.setAllIntroductionCount(resultsJSON.getString("allIntroductionCount"));
		Log.d("有奖推广", results);
		return introductionInfo;
	}

	@Override
	public String userAdvice(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", "1.0"));
		strParams.add(new BasicNameValuePair("method", "common.feedback"));
		strParams.add(new BasicNameValuePair("appKey", "android_app"));
		strParams.add(new BasicNameValuePair("phoneNum", params[0]));
		// 反馈意见文本
		strParams.add(new BasicNameValuePair("content", params[1]));
		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.getString("code");
		Log.d("用户反馈", results);
		return code;
	}

	@Override
	public List<String> upLoadUserImage(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", "1.0"));
		strParams.add(new BasicNameValuePair("method", "user.updatePortrait"));
		strParams.add(new BasicNameValuePair("appKey", "android_app"));
		strParams.add(new BasicNameValuePair("sessionId", params[0]));
		strParams.add(new BasicNameValuePair("portrait", params[1]));
		Log.d("图片", params[1]);
		Log.d("sessionId", params[0]);
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		List<String> reData = new ArrayList<String>();
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.getString("code");
		String path = resultsJSON.getString("qrCodeUrl");
		reData.add(code);
		reData.add(path);
		return reData;
	}

	// 得到用户账单
	@Override
	public UserPay getUserPaysFormService(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", "1.0"));
		strParams.add(new BasicNameValuePair("method", "bill.getBillListByCondition"));
		strParams.add(new BasicNameValuePair("appKey", "android_app"));
		strParams.add(new BasicNameValuePair("sessionId", params[0]));
		strParams.add(new BasicNameValuePair("userBaseId", params[1]));
		strParams.add(new BasicNameValuePair("isPay", params[2]));
		strParams.add(new BasicNameValuePair("pageSize", params[3]));
		strParams.add(new BasicNameValuePair("currentPage", params[4]));
		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.has("code") ? resultsJSON.getString("code") : null;
		String message = resultsJSON.has("message") ? resultsJSON.getString("message") : null;
		JSONArray resultList = resultsJSON.has("resultList") ? resultsJSON.getJSONArray("resultList") : null;
		String totalCount = resultsJSON.has("totalCount") ? resultsJSON.getString("totalCount") : null;
		String totalPage = resultsJSON.has("totalPage") ? resultsJSON.getString("totalPage") : null;
		List<UserPayItem> list = new ArrayList<UserPayItem>();
		UserPay userPay = new UserPay();
		userPay.setCode(code);
		userPay.setMessage(message);
		userPay.setTotalCount(totalCount);
		userPay.setTotalPage(totalPage);
		if (resultList == null) {
			userPay.setCode("1234");// 1234
			userPay.setMessage("没有结果集");
			return userPay;
		}
		for (int i = 0; i < resultList.length(); i++) {
			JSONObject item = resultList.getJSONObject(i);
			UserPayItem userPayItem = new UserPayItem();
			userPayItem.setId(item.getString("id"));
			userPayItem.setTotalAmount(item.getString("totalAmount"));
			userPayItem.setAfterDiscountAmount(item.getString("afterDiscountAmount"));
			userPayItem.setBussinessName(item.getString("businessName"));
			userPayItem.setUserBaseId(item.getString("userBaseId"));
			userPayItem.setBaseInfoId(item.getString("baseInfoId"));
			userPayItem.setIsPay(item.getString("isPay"));
			userPayItem.setCreateTime(item.getString("createTime"));
			userPayItem.setNoDiscountAmount(item.getString("noDiscountAmount"));
			list.add(userPayItem);
		}
		userPay.setResultList(list);
		return userPay;
	}

	// 账单详情
	@Override
	public UserPayItem getUserPayDetailFormService(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 6000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", "1.0"));
		strParams.add(new BasicNameValuePair("method", "bill.getBillDetailByBillId"));
		strParams.add(new BasicNameValuePair("appKey", "android_app"));
		strParams.add(new BasicNameValuePair("sessionId", params[0]));
		strParams.add(new BasicNameValuePair("billId", params[1]));
		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.has("code") ? resultsJSON.getString("code") : null;
		String message = resultsJSON.has("message") ? resultsJSON.getString("message") : null;
		String id = resultsJSON.has("id") ? resultsJSON.getString("id") : null;
		String totalAmount = resultsJSON.has("totalAmount") ? resultsJSON.getString("totalAmount") : null;
		String afterDiscountAmount = resultsJSON.has("afterDiscountAmount")
				? resultsJSON.getString("afterDiscountAmount") : null;
		String userBaseId = resultsJSON.has("userBaseId") ? resultsJSON.getString("userBaseId") : null;
		String baseInfoId = resultsJSON.has("baseInfoId") ? resultsJSON.getString("baseInfoId") : null;
		String isPay = resultsJSON.has("isPay") ? resultsJSON.getString("isPay") : null;
		String createTime = resultsJSON.has("createTime") ? resultsJSON.getString("createTime") : null;
		String noDiscount = resultsJSON.has("noDiscountAmount") ? resultsJSON.getString("noDiscountAmount") : null;
		UserPayItem userPayItem = new UserPayItem();
		userPayItem.setCode(code);
		userPayItem.setMessage(message);
		userPayItem.setId(id);
		userPayItem.setTotalAmount(totalAmount);
		userPayItem.setAfterDiscountAmount(afterDiscountAmount);
		userPayItem.setUserBaseId(userBaseId);
		userPayItem.setBaseInfoId(baseInfoId);
		userPayItem.setIsPay(isPay);
		userPayItem.setCreateTime(createTime);
		userPayItem.setNoDiscountAmount(noDiscount);
		// 折扣项目
		JSONArray resultList = resultsJSON.has("list") ? resultsJSON.getJSONArray("list") : null;
		// 优惠券
		JSONArray resultCard = resultsJSON.has("useVoucherList") ? resultsJSON.getJSONArray("useVoucherList") : null;
		if (resultCard != null) {
			JSONObject json = resultCard.getJSONObject(0);
			userPayItem.setCardItem(json.getString("amount"));
		}
		ArrayList<PayDiscountItem> result = new ArrayList<PayDiscountItem>();
		// discountName":"菜品折扣","discount":5.5,"amount":2000,"createTime":1443604874000}]}
		for (int i = 0; i < resultList.length(); i++) {
			PayDiscountItem item = new PayDiscountItem();
			JSONObject jsonObject = resultList.getJSONObject(i);
			item.setDiscount(jsonObject.getString("discount"));
			item.setDiscountName(jsonObject.getString("discountName"));
			item.setAmount(jsonObject.getString("amount"));
			item.setAfterAmount(jsonObject.getString("afterDiscountAmount"));
			result.add(item);
		}
		userPayItem.setResultList(result);
		return userPayItem;
	}

	// 用户支付
	@Override
	public String UserToPayFormService(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", "1.0"));
		strParams.add(new BasicNameValuePair("method", "pay.pay2Bill"));
		strParams.add(new BasicNameValuePair("appKey", "android_app"));
		if (!StrTools.isNull(params[0])) {
			strParams.add(new BasicNameValuePair("sessionId", params[0]));
		}
		if (!StrTools.isNull(params[1])) {
			strParams.add(new BasicNameValuePair("billId", params[1]));
		}
		if (!StrTools.isNull(params[2])) {
			strParams.add(new BasicNameValuePair("payPwd", params[2]));
		}
		if (!StrTools.isNull(params[3])) {
			strParams.add(new BasicNameValuePair("payInfoList", params[3]));
		}
		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.has("code") ? resultsJSON.getString("code") : "未知错误";
		return code;
	}

	// 获得现金券
	@Override
	public DiscountCard getDiscountCard(String... params) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(ConstantData.URI);
		// 封装数据
		List<NameValuePair> strParams = new ArrayList<NameValuePair>();
		strParams.add(new BasicNameValuePair("v", "1.0"));
		strParams.add(new BasicNameValuePair("method", "voucher.getAllVouchersByUser"));
		strParams.add(new BasicNameValuePair("appKey", "android_app"));
		strParams.add(new BasicNameValuePair("sessionId", params[0]));
		strParams.add(new BasicNameValuePair("userBaseId", params[1]));
		strParams.add(new BasicNameValuePair("status", params[2]));
		strParams.add(new BasicNameValuePair("pageSize", params[3]));
		strParams.add(new BasicNameValuePair("currentPage", params[4]));
		// 生成签名
		String sign = NetTools.sign(strParams, ConstantData.SECRET);
		strParams.add(new BasicNameValuePair("sign", sign));
		post.setEntity(new UrlEncodedFormEntity(strParams, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("服务器忙！");
		}
		String results = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		// 解析返回值
		JSONObject resultsJSON = new JSONObject(results);
		String code = resultsJSON.has("code") ? resultsJSON.getString("code") : null;
		String message = resultsJSON.has("message") ? resultsJSON.getString("message") : null;
		JSONArray resultList = resultsJSON.has("resultList") ? resultsJSON.getJSONArray("resultList") : null;
		String totalCount = resultsJSON.has("totalCount") ? resultsJSON.getString("totalCount") : null;
		String totalPage = resultsJSON.has("totalPage") ? resultsJSON.getString("totalPage") : null;
		ArrayList<DiscountCardItem> list = new ArrayList<DiscountCardItem>();
		DiscountCard discountCard = new DiscountCard();
		discountCard.setCode(code);
		discountCard.setMesaage(message);
		discountCard.setTotalCount(totalCount);
		discountCard.setTotalPage(totalPage);
		if (resultList == null) {
			discountCard.setCode("1234");// 1234
			discountCard.setMesaage("没有结果集");
			return discountCard;
		}
		for (int i = 0; i < resultList.length(); i++) {
			JSONObject item = resultList.getJSONObject(i);
			DiscountCardItem discountCardItem = new DiscountCardItem();
			String id = item.has("id") ? item.getString("id") : null;
			String amount = item.has("amount") ? item.getString("amount") : null;
			String businessName = item.has("businessName") ? item.getString("businessName") : null;
			String baseInfoId = item.has("baseInfoId") ? item.getString("baseInfoId") : null;
			String createTime = item.has("createTime") ? item.getString("createTime") : null;
			String endDate = item.has("endDate") ? item.getString("endDate") : null;
			discountCardItem.setId(id);
			discountCardItem.setAmount(amount);
			discountCardItem.setBusinessName(businessName);
			discountCardItem.setBaseInfoId(baseInfoId);
			discountCardItem.setCreateTime(createTime);
			discountCardItem.setEndDate(endDate);
			list.add(discountCardItem);
		}
		discountCard.setResultList(list);
		return discountCard;
	}
}
