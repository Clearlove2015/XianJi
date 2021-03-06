package com.schytd.discount.tools;

import java.io.File;
import java.math.BigDecimal;

import android.content.Context;
import android.os.Environment;

import com.schytd.discount.business.SellerBusiness;
import com.schytd.discount.business.UserBusiness;
import com.schytd.discount.business.impl.SellerBusinessImp;
import com.schytd.discount.business.impl.UserBusinessImpl;

public class DataCleanManager {
	// 数据库
	private static SellerBusiness mSellerBussiness;
	private static UserBusiness mUserBussiness;

	public static String getTotalCacheSize(Context context) throws Exception {
		mSellerBussiness = new SellerBusinessImp(context);
		mUserBussiness = new UserBusinessImpl(context);
		long cacheSize = getFolderSize(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			cacheSize += getFolderSize(context.getExternalCacheDir());
		}
		cacheSize += new File(
				"data/data/com.schytd.discount_android/databases/cache_database.db")
				.length();
		// Toast.makeText(
		// context,
		// new File(
		// "data/data/com.schytd.discount_android/databases/cache_database.db").length()
		// + "", Toast.LENGTH_SHORT).show();
		return getFormatSize(cacheSize);
	}

	public static void clearAllCache(Context context) {
		deleteDir(context.getCacheDir());
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				deleteDir(context.getExternalCacheDir());
			}
			mSellerBussiness.deleteSellerCache();
			mUserBussiness.removeUserData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	// 获取文件
	// Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
	// 目录，一般放一些长时间保存的数据
	// Context.getExternalCacheDir() -->
	// SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			if (fileList == null) {
				return 0;
			}
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 格式化单位
	 * 
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte <70){
			return "0KB";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}
}