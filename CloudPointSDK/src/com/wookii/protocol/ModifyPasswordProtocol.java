package com.wookii.protocol;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.wookii.model.UserInfo;
import com.wookii.protocollManager.BaseProtocol;
import com.wookii.protocollManager.DataProtocolInterface;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.protocollManager.RequestProtocolTask;

import android.os.Handler;
import android.os.Message;

public class ModifyPasswordProtocol extends BaseProtocol implements
		DataProtocolInterface {

	private Handler mHandler;
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
	private String PARAM_METHOD_NAME = "admin/party/modifyPassword.htm";

	public void invokeModifyPwd(String uid, String oldPwd, String newPwd,
			String token, Handler handler) {
		mHandler = handler;

		params.add(new BasicNameValuePair("uid", uid));
		params.add(new BasicNameValuePair("oldpassword", oldPwd));
		params.add(new BasicNameValuePair("password", newPwd));
		params.add(new BasicNameValuePair("token", token));

		RequestProtocolTask httpRequestTask = new RequestProtocolTask(
				PARAM_METHOD_NAME, params, this);
		Thread thread = new Thread(httpRequestTask);
		thread.start();

	}

	// 接口返回数据没有说明
	@Override
	public void onResposeProcotolData(Object object) {
		if (object != null) {
			try {
				Object json = new JSONTokener(object.toString()).nextValue();
				if (json instanceof JSONObject) {
					JSONObject jsonObject = new JSONObject(object.toString());
					String code = jsonObject.getString("errorCode");
					if (code .equals(ProtocolManager.ERROR_CODE_ZORE)) {
						Message msg = mHandler
								.obtainMessage(ProtocolManager.NOTIFICATION_MODIFY_PASSWORD);
						msg.obj = null;
						mHandler.sendMessage(msg);
					} else {
						Message msg = mHandler
								.obtainMessage(ProtocolManager.NOTIFICATION_RESPONSE_ERRORINFO);
						msg.obj = jsonObject.getString("message");
						mHandler.sendMessage(msg);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Message msg = mHandler.obtainMessage(ProtocolManager.NETWORK_ERROR);
			mHandler.sendMessage(msg);
		}
	}

	public void invokeModifyPwd(String json, Handler mHandler) {
		// TODO Auto-generated method stub
		this.mHandler = mHandler;

		RequestProtocolTask httpRequestTask = new RequestProtocolTask(
				PARAM_METHOD_NAME, json, this);
		Thread thread = new Thread(httpRequestTask);
		thread.start();
	}

}
