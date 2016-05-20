package com.schytd.discount.dao.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDataBase extends SQLiteOpenHelper {
	// 定义一个数据库名字
	private static final String DB_NAME = "user_data.db";
	// 定义此次数据库的版本号
	private static final int DB_VERSION = 1;
	// 定义表名
	public static final String TABLE_SESSIONID = "tab_sessionid";
	// 登陆表
	public static final String TABLE_LOGIN = "login_info";

	public UserDataBase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建tab_sessionid表
		String sql1 = "DROP TABLE IF EXISTS " + TABLE_SESSIONID;
		db.execSQL(sql1);
		String sql2 = "CREATE TABLE " + TABLE_SESSIONID + " ("
				+ " id INTEGER PRIMARY KEY,"
				+ " sessionid VARCHAR(40) NOT NULL, "
				+ " time VARCHAR(20) NOT NULL " + ") ";
		db.execSQL(sql2);
		// 创建登陆表
		String sql11 = "DROP TABLE IF EXISTS " + TABLE_LOGIN;
		db.execSQL(sql11);
		String sql12 = "CREATE TABLE " + TABLE_LOGIN + " ("
				+ " id INTEGER PRIMARY KEY," + " number VARCHAR(20),"
				+ " password VARCHAR(20)" + ")";
		db.execSQL(sql12);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
