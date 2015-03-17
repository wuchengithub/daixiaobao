package com.daixiaobao;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.wookii.WookiiSDKManager;

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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
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
}
