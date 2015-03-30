package com.daixiaobao;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;

import com.daixiaobao.categroy.ResponseCatagroy;
import com.daixiaobao.db.DBHelper;
import com.daixiaobao.greenrobot.Group;
import com.daixiaobao.other.CatagroyHelper;
import com.daixiaobao.other.CatagroyHelper.OnCategroyHandleListener;
import com.daixiaobao.search.AttributeProtocol;
import com.daixiaobao.search.AttributeRequestBean;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

public class WelcomeActivity extends Activity {

	
	
	protected static final int OPEN_LOGIN = 0x123;
	private static final String TAG = "WelcomeActivity";
	private static final int OPEN_MAIN = 0x124;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OPEN_LOGIN:
				CommonUtil.openLoginView(WelcomeActivity.this, CommonUtil.LOGIN_REQUEST_CODE);
				break;
			case OPEN_MAIN:
				CommonUtil.openMainView(WelcomeActivity.this);
				/*Intent intent = new Intent(WelcomeActivity.this, com.daixiaobao.friend.FriendActivity.class);
				startActivity(intent);*/
				finish();
				break;
			default:
				break;
			}
		};
	};
	private AnimationDrawable animationDrawable;
	private AttributeProtocol protocol;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		requestData();
		//检测登陆,三秒后检测
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				openActivity();
			}

		}, 3000);
		
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if( CommonUtil.LOGIN_REQUEST_CODE == requestCode){
			if(resultCode == Activity.RESULT_CANCELED){
				
			} else {
				
			}
			//openActivity();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void openActivity() {
		// TODO Auto-generated method stub
		//if(WookiiSDKManager.isLogin(WelcomeActivity.this)){
		handler.sendEmptyMessage(OPEN_MAIN);
		/*} else {
			handler.sendEmptyMessage(OPEN_LOGIN);
			
		}*/
	}
	
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		super.onDestroy();
	}
	
	private void requestData() {
		
		CatagroyHelper.getInstance(this, new OnCategroyHandleListener() {
			
			@Override
			public void onHandle(ResponseCatagroy obj) {
				// TODO Auto-generated method stub
				Group[] group = obj.getGroup();
				if(group != null && group.length != 0) {
					DBHelper instance = DBHelper.getInstance(WelcomeActivity.this);
					//save in local data
					for (Group item : group) {
						boolean addGroup = instance.addGroup(item);
						if(addGroup) {
							getAttrbFromService(WelcomeActivity.this, item.getCode());
						}
					}
					
				}
			}
		});
	}
	protected void getAttrbFromService(Context context, String code) {
		protocol = new AttributeProtocol();
		protocol.invoke(new AttributeRequestBean(LoginMessageDataUtils.getToken(context), LoginMessageDataUtils.getUID(context), 
				DeviceTool.getDeviceId(context), code), handler);
	}
}
