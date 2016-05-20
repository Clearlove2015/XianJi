package com.schytd.discount.business.impl;

import java.util.List;

import com.schytd.discount.business.UpdateBusiness;
import com.schytd.discount.net.UpdateVersion;
import com.schytd.discount.net.impl.UpdateVersionImpl;

import android.content.Context;

public class UpdateBusinessImpl implements UpdateBusiness {
	UpdateVersion updateVersion;

	public UpdateBusinessImpl(Context c) {

		updateVersion = new UpdateVersionImpl();
	}

	@Override
	public List<String> update(String... params) throws Exception {
		List<String> versioninfo = updateVersion.updateversion("1.0", "common.getLastAppVersion", "android_app", "1");
		return versioninfo;
	}

}
