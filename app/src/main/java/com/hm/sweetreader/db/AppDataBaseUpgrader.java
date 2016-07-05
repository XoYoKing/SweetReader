package com.hm.sweetreader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * apps_data.db数据库升级接口
 * @author 杨俊
 * @date 2015-5-14 下午04:41:30
 */
public class AppDataBaseUpgrader {
	
	@SuppressWarnings("unused")
	private Context ctx;
	
	public AppDataBaseUpgrader(Context ctx) {
		this.ctx = ctx;
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
