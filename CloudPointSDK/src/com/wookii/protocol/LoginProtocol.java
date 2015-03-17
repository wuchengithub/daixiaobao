package com.wookii.protocol;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Handler;
import android.os.Message;

import com.wookii.model.LoginRequest;
import com.wookii.model.UserInfo;
import com.wookii.protocollManager.BaseProtocol;
import com.wookii.protocollManager.DataProtocolInterface;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.protocollManager.RequestProtocolTask;

public class LoginProtocol extends BaseProtocol implements
		DataProtocolInterface {

	LoginProtocol loginProtocol = this;
	Handler mHandler;
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	String PARAM_METHOD_NAME = "login.htm";

	public void invokeLogin(String userName, String pwd, Handler handler,
			String deviceId, String first_login, String YZMCode, String tag) {

		this.mHandler = handler;

		params.add(new BasicNameValuePair("tel", userName));
		params.add(new BasicNameValuePair("password", pwd));
		params.add(new BasicNameValuePair("deviceId", deviceId));
		params.add(new BasicNameValuePair("verification_code", YZMCode));
		params.add(new BasicNameValuePair("login_site", tag));
		// first_login 可选 0：非首次登录 1：首次登录 默认值为 0
		params.add(new BasicNameValuePair("first_login", first_login));
		RequestProtocolTask httpRequestTask = new RequestProtocolTask(
				PARAM_METHOD_NAME, params, loginProtocol);
		Thread thread = new Thread(httpRequestTask);
		thread.start();
	}
	public void invokeLogin(LoginRequest loginRequest, Handler mHandler) {
		// TODO Auto-generated method stub
		this.mHandler = mHandler;
		String json = "";
		JSONObject jo = new JSONObject();
		try {
			jo.put("deviceId", loginRequest.getDeviceId());
			jo.put("userName", loginRequest.getUserName());
			jo.put("password", loginRequest.getPassword());
			json = jo.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RequestProtocolTask httpRequestTask = new RequestProtocolTask(
				PARAM_METHOD_NAME, json, this);
		Thread thread = new Thread(httpRequestTask);
		thread.start();
	}
	@Override
	public void onResposeProcotolData(Object object) {
		// TODO Auto-generated method stub

		if (object != null) {

			try {

				Object json = new JSONTokener(object.toString()).nextValue();
				if (json instanceof JSONObject) {
					JSONObject jsonObject = new JSONObject(object.toString());
					String code = jsonObject.getString("errorCode");
					if (Integer.parseInt(code) == ProtocolManager.ERROR_CODE_ZORE) {
						String token = jsonObject.getString("token");
						String uid = jsonObject.getString("uid");
						String storeId = jsonObject.getString("storeId");
						String isVip = jsonObject.getString("isVip");
						UserInfo userInfo = new UserInfo();
						userInfo.setUid(uid);
						userInfo.setIsVip(isVip);
						userInfo.setStoreId(storeId);
						userInfo.setToken(token);
						Message msg = mHandler
								.obtainMessage(ProtocolManager.LOGIN_SUCCESS);
						msg.obj = userInfo;
						mHandler.sendMessage(msg);
					} else {
						Message msg = mHandler.obtainMessage(ProtocolManager.NOTIFICATION_RESPONSE_ERRORINFO);
						msg.obj = jsonObject.getString("message");
						mHandler.sendMessage(msg);
					}

				} else if (json instanceof JSONArray) {
					JSONArray jsonObject = new JSONArray(object.toString());

					for (int i = 0; i < jsonObject.length(); i++) {
						JSONObject jo = (JSONObject) jsonObject.get(i);

					}
					Message msg = mHandler
							.obtainMessage(ProtocolManager.NOTIFICATION);

					mHandler.sendMessage(msg);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Message msg = mHandler.obtainMessage(ProtocolManager.NOTIFICATION_RESPONSE_ERRORINFO);
			mHandler.sendMessage(msg);
		}
	}

	
}
