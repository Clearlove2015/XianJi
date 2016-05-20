package com.schytd.discount.net.impl;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import com.schytd.discount.bean.ConstantData;

import android.content.Context;

public class ConnectInternetHelper {
	HttpClient client;
	HttpPost post;

	public ConnectInternetHelper(Context c) {
		client = HttpClientSslHelper.getSslSocketFactoryHttp(c.getApplicationContext(), 443);
		post = new HttpPost(ConstantData.URI);
	}

}
