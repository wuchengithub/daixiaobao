package com.daixiaobao.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.daixiaobao.greenrobot.DaoMaster;
import com.daixiaobao.greenrobot.DaoSession;
import com.daixiaobao.greenrobot.Group;
import com.daixiaobao.greenrobot.GroupDao;
import com.daixiaobao.greenrobot.GroupDao.Properties;

import de.greenrobot.dao.query.Query;

public class DBHelper {

	
	private Context context;
	private GroupDao groupDao;
	
	
	public DBHelper(Context context) {
		this.context = context;
		DaoMaster daoMaster = DBManager.getDaoMaster(context);
		DaoSession daoSession = DBManager.getDaoSession(daoMaster, context);
		groupDao = daoSession.getGroupDao();
	}
	private static DBHelper instance = null;
	
	public synchronized static DBHelper getInstance(Context context){
		if(instance == null) {
			instance = new DBHelper(context);
		}
		return instance;
	}
	
	
	public boolean addGroup(Group entity){
		long l = groupDao.insert(entity);
		if(l != -1) {
			return true;
		}
		
		return false;
	}


	public void clearAll() {
		// TODO Auto-generated method stub
		groupDao.deleteAll();
	}
	
	/*public HashMap<String, Object> reload(){
		ArrayList<HotLine> groupList = new ArrayList<HotLine>();
		ArrayList<List<HotLine>> childList = new ArrayList<List<HotLine>>();
		Query<HotLine> qb = groupDao.queryRawCreate(" GROUP BY GROUP_ID ORDER BY _ID");
		for (HotLine item : qb.list()) {
			groupList.add(new HotLine("0", null, null, null, null, item.getGroup_id(), item.getGroup_name()));
			List<HotLine> list = groupDao.queryBuilder().where(Properties.Group_id.eq(item.getGroup_id())).orderAsc(Properties.Id).list();
			childList.add(list);
		}
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("group", groupList);
		data.put("child", childList);
		return data;
	}*/
}
