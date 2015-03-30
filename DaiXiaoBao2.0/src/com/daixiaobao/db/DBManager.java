package com.daixiaobao.db;

import android.content.Context;

import com.daixiaobao.greenrobot.DaoMaster;
import com.daixiaobao.greenrobot.DaoMaster.OpenHelper;
import com.daixiaobao.greenrobot.DaoSession;

public class DBManager {

	private static final String DATABASE_NAME = "daixiaobao_db";

	public static DaoMaster getDaoMaster(Context context) {
		OpenHelper helper = new DaoMaster.DevOpenHelper(context,
				DBManager.DATABASE_NAME, null);
		DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
		return daoMaster;
	}

	/**
	 * 取得DaoSession
	 * 
	 * @param context
	 * @return
	 */
	public static DaoSession getDaoSession(DaoMaster daoMaster, Context context) {
		if (daoMaster == null) {
			daoMaster = getDaoMaster(context);
		}
		DaoSession daoSession = daoMaster.newSession();
		return daoSession;
	}
}
