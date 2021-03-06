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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.schytd.discount.bean.ConstantData;
import com.schytd.discount.net.UpdateVersion;
import com.schytd.discount.tools.NetTools;

import android.util.Log;

public class UpdateVersionImpl implements UpdateVersion {
	@Override
	public List<String> updateversion(String... params) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(ConstantData.URI);

		NameValuePair parameter_v = new BasicNameValuePair("v", params[0]);
		NameValuePair parameter_method = new BasicNameValuePair("method",
				params[1]);
		NameValuePair parameter_appkey = new BasicNameValuePair("appKey",
				params[2]);
		NameValuePair parameter_appType = new BasicNameValuePair("appType",
				params[3]);

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(parameter_v);
		parameters.add(parameter_method);
		parameters.add(parameter_appkey);
		parameters.add(parameter_appType);

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
		// 解析返回值
		String code = resultsJSON.getString("code");
		Log.d("++UpdateVersionImpl++", "更新" + results);
		if (code.equals("0")) {
			List<String> versioninfo = new ArrayList<String>();
			// 解析json
			String version = resultsJSON.getString("version");
			String download_url = resultsJSON.getString("downloadUrl");
			versioninfo.add(code);
			versioninfo.add(version);
			versioninfo.add(download_url);
			return versioninfo;
		} else {
			List<String> versioninfo = new ArrayList<String>();
			versioninfo.add(code);
			return versioninfo;
		}
		
	}

}
