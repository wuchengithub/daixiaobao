package com.daixiaobao;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.daixiaobao.categroy.RequestCategroy;
import com.daixiaobao.categroy.ResponseCatagroy;
import com.daixiaobao.db.DBHelper;
import com.daixiaobao.greenrobot.AfterSales;
import com.daixiaobao.greenrobot.Attrb;
import com.daixiaobao.greenrobot.Brand;
import com.daixiaobao.greenrobot.Feature;
import com.daixiaobao.greenrobot.Group;
import com.daixiaobao.other.CatagroyHelper;
import com.daixiaobao.other.CatagroyHelper.OnCategroyHandleListener;
import com.daixiaobao.search.AttributeProtocol;
import com.daixiaobao.search.AttributeBean;
import com.wookii.WookiiSDKManager;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

public class WelcomeActivity extends Activity {

	
	
	protected static final int OPEN_LOGIN = 0x123;
	private static final String TAG = "WelcomeActivity";
	private static final int OPEN_MAIN = 0x124;
	private static final String TOP_LEVEL = "-1";
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OPEN_LOGIN:
				CommonUtil.openLoginView(WelcomeActivity.this, CommonUtil.LOGIN_REQUEST_CODE);
				break;
			case OPEN_MAIN:
				CommonUtil.openMainView(WelcomeActivity.this);
				finish();
				break;
			default:
				break;
			}
		};
	};
	private AnimationDrawable animationDrawable;
	private AttributeProtocol protocol;
	private DBHelper dbHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (WookiiSDKManager.isLogin(WelcomeActivity.this)) {
			initData();
		} else {
			handler.sendEmptyMessage(OPEN_LOGIN);
		}
	}

	private void initData() {
		dbHelper = DBHelper.getInstance(WelcomeActivity.this);
		requestData(TOP_LEVEL);
	}
	

	
	private void openActivity() {
		handler.sendEmptyMessage(OPEN_MAIN);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == CommonUtil.LOGIN_REQUEST_CODE) {
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void requestData(String itemId) {
		dbHelper.clearAll();
		CatagroyHelper instance = CatagroyHelper.getInstance(this, new OnCategroyHandleListener() {
			
			@Override
			public void onHandle(ResponseCatagroy obj) {
				// TODO Auto-generated method stub
				Group[] group = obj.getGroup();
				if(group != null && group.length != 0) {
					
					//save in local data
					for (Group item : group) {
						boolean addGroup = dbHelper.addGroup(item);
						if(addGroup) {
							getAttrbFromService(WelcomeActivity.this, item.getCode());
						}
					}
					
				}
			}
		});
		
		instance.invoke(new RequestCategroy(LoginMessageDataUtils.getToken(WelcomeActivity.this), LoginMessageDataUtils.getUID(WelcomeActivity.this), 
				DeviceTool.getUniqueId(WelcomeActivity.this), itemId));
	}
	
	
	protected void getAttrbFromService(Context context, final String code) {
		protocol = new AttributeProtocol();
		protocol.invoke(new AttributeBean(LoginMessageDataUtils.getToken(context), LoginMessageDataUtils.getUID(context), 
				DeviceTool.getDeviceId(context), code), new Handler(){
			@Override
			public void handleMessage(Message msg) {
				
				switch (msg.what) {
				case ProtocolManager.NOTIFICATION:
					AttributeBean mObj = (AttributeBean)msg.obj;
					if(mObj != null && mObj.getErrorCode() == ProtocolManager.ERROR_CODE_ZORE){
						com.daixiaobao.search.AttributeBean.Group data = mObj.getData();
						AfterSales[] afterSales = data.getAfterSales();
						if(afterSales != null && afterSales.length != 0) {
							for (AfterSales item : afterSales) {
								item.setCategorys_id(code);
								dbHelper.addAfterSales(item);
							}
						}
						
						Attrb[] attribute = data.getAttribute();
						if(attribute != null && attribute.length != 0) {
							for (Attrb item : attribute) {
								item.setCategorys_id(code);
								dbHelper.addAttrb(item);
								Feature[] features = item.getFeatures();
								if(features != null && features.length != 0) {
									for (Feature feature : features) {
										feature.setAttrb_id(item.getFeatureTypeId());
										dbHelper.addFeature(feature);
									}
								}
							}
						}
						
						Brand[] brands = data.getBrands();
						if(brands != null && brands.length != 0) {
							for (Brand item : brands) {
								item.setCategorys_id(code);
								dbHelper.addBrand(item);
							}
						}
					}
					break;

				default:
					break;
				}
				
				openActivity();
			}
		});
	}
}
