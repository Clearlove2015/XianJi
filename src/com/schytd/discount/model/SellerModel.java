package com.schytd.discount.model;

import com.schytd.discount.presenter.OnSellerInternetListener;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;
import com.zhy.http.okhttp.request.OkHttpRequest.Builder;

import android.content.Context;
import android.util.Log;
import android.view.View;

public class SellerModel implements ISellerModel {
	private Builder okHttpBuilder = new OkHttpRequest.Builder();
	// 网络请求

	public SellerModel() {
	}

	public SellerModel(Context c) {
	}

	public abstract class MyResultCallback<T> extends ResultCallback<T> {

		@Override
		public void onBefore(Request request) {
			super.onBefore(request);
		}

		@Override
		public void onAfter() {
			super.onAfter();
		}

		@Override
		public void inProgress(float progress) {
			// TODO Auto-generated method stub
			super.inProgress(progress);
		}

		@Override
		public void onError(Request arg0, Exception arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onResponse(T arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	private ResultCallback<String> stringResultCallback = new MyResultCallback<String>() {
		@Override
		public void onError(Request request, Exception e) {
			Log.e("TAG", "onError , e = " + e.getMessage());
		}

		@Override
		public void onResponse(String response) {
			Log.e("TAG", "onResponse , response = " + response);
		}
	};

	@Override
	public String getSellerDiscountInfo(final OnSellerInternetListener internetListener, String... params) {
		// 获取缓存 有缓冲不进行网络请求
		// 网络请求
		String url = "https://192.168.1.100:443/discountserver/api";
		okHttpBuilder.url(url).post(new MyResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				Log.e("TAG", "onError , e = " + e.getMessage());
				internetListener.onFailed();
			}

			@Override
			public void onResponse(String response) {
				internetListener.success(response);
			}
		});

		return null;
	}

	@Override
	public String getSellerInfo(OnSellerInternetListener internetListener, String... params) throws Exception {
		// 获取缓存
		// 网络端获取
		return null;
	}

	public void getSimpleString(View view) {
		String url = "https://192.168.1.100:443/discountserver/api";
		// String url = "https://kyfw.12306.cn/otn/";

		new OkHttpRequest.Builder().url(url).get(new MyResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				Log.e("TAG", "onError , e = " + e.getMessage());
			}

			@Override
			public void onResponse(String response) {
			}
		});

	}
}
