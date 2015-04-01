package com.daixiaobao.db;

import java.util.List;

import android.content.Context;

import com.daixiaobao.greenrobot.AfterSales;
import com.daixiaobao.greenrobot.AfterSalesDao.Properties;
import com.daixiaobao.greenrobot.Attrb;
import com.daixiaobao.greenrobot.Brand;
import com.daixiaobao.greenrobot.DaoMaster;
import com.daixiaobao.greenrobot.DaoSession;
import com.daixiaobao.greenrobot.Feature;
import com.daixiaobao.greenrobot.Group;
import com.daixiaobao.search.AttributeBean;
import com.wookii.protocollManager.ProtocolManager;

import de.greenrobot.dao.query.QueryBuilder;

public class DBHelper {

	
	private Context context;
	private DaoSession daoSession;
	
	
	public DBHelper(Context context) {
		this.context = context;
		DaoMaster daoMaster = DBManager.getDaoMaster(context);
		daoSession = DBManager.getDaoSession(daoMaster, context);
	}
	private static DBHelper instance = null;
	
	public synchronized static DBHelper getInstance(Context context){
		if(instance == null) {
			instance = new DBHelper(context);
		}
		return instance;
	}
	
	
	public boolean addGroup(Group entity){
		long l = daoSession.getGroupDao().insert(entity);
		if(l != -1) {
			return true;
		}
		
		return false;
	}

	public boolean addAfterSales(AfterSales entity){
		long l = daoSession.getAfterSalesDao().insert(entity);
		if(l != -1) {
			return true;
		}
		
		return false;
	}
	public boolean addAttrb(Attrb entity){
		long l = daoSession.getAttrbDao().insert(entity);
		if(l != -1) {
			return true;
		}
		
		return false;
	}
	public boolean addBrand(Brand entity){
		long l = daoSession.getBrandDao().insert(entity);
		if(l != -1) {
			return true;
		}
		
		return false;
	}
	public boolean addFeature(Feature entity){
		long l = daoSession.getFeatureDao().insert(entity);
		if(l != -1) {
			return true;
		}
		
		return false;
	}

	public void clearAll() {
		// TODO Auto-generated method stub
		daoSession.getGroupDao().deleteAll();
		daoSession.getAfterSalesDao().deleteAll();
		daoSession.getAttrbDao().deleteAll();
		daoSession.getBrandDao().deleteAll();
		daoSession.getFeatureDao().deleteAll();
	}


	public AttributeBean getAllAttribute(String codeStr) {
		// TODO Auto-generated method stub
		AttributeBean data = new AttributeBean();
		com.daixiaobao.search.AttributeBean.Group group = data.new Group();
		
		List<AfterSales> after = daoSession.getAfterSalesDao().queryBuilder().where(Properties.Categorys_id.eq(codeStr)).list();
		group.setAfterSales(after);
		List<Brand> brand = daoSession.getBrandDao().queryBuilder().where(com.daixiaobao.greenrobot.BrandDao.Properties.Categorys_id.eq(codeStr)).list();
		group.setBrands(brand);
		List<Attrb> attrb = daoSession.getAttrbDao().queryBuilder().where(com.daixiaobao.greenrobot.AttrbDao.Properties.Categorys_id.eq(codeStr)).list();
		
		for (Attrb item : attrb) {
			List<Feature> feature = daoSession.getFeatureDao().queryBuilder().where(com.daixiaobao.greenrobot.FeatureDao.Properties.Attrb_id.eq(item.getFeatureTypeId())).list();
			item.setFeatures(feature);
		}
		group.setAttribute(attrb);
		
		data.setData(group);
		data.setErrorCode(ProtocolManager.ERROR_CODE_ZORE);
		return data;
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
