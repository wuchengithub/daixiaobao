package com.wookii.protocol;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.wookii.model.RegisterRequest;
import com.wookii.protocollManager.BaseProtocol;
import com.wookii.protocollManager.DataProtocolInterface;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.protocollManager.RequestProtocolTask;

import android.os.Handler;
import android.os.Message;

public class RegisiterProtocol extends BaseProtocol implements
		DataProtocolInterface {

	RegisiterProtocol regisiterProtocol = this;

	Handler mHandler;
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	String PARAM_METHOD_NAME = "register.htm";

	public void invokeRegister(String tel, String nickname, String uid,
			String deviceId, String validate_code, String codeFromPic,
			String password, Handler handler) {

		this.mHandler = handler;

		params.add(new BasicNameValuePair("tel", tel));
		params.add(new BasicNameValuePair("nickname", nickname));
		params.add(new BasicNameValuePair("uid", uid));
		params.add(new BasicNameValuePair("deviceId", deviceId));
		params.add(new BasicNameValuePair("validate_code", validate_code));
		params.add(new BasicNameValuePair("web_validate_code", codeFromPic));
		params.add(new BasicNameValuePair("password", password));
		RequestProtocolTask httpRequestTask = new RequestProtocolTask(
				PARAM_METHOD_NAME, params, regisiterProtocol);
		Thread thread = new Thread(httpRequestTask);
		thread.start();

	}

	public void invokeRegister(RegisterRequest model,Handler handler) {

		this.mHandler = handler;
		//model to json
		String json = "";
		JSONObject jo = new JSONObject();
		try {
			jo.put("deviceId", model.getDeviceId());
			jo.put("registerPassword", model.getRegisterPassword());
			jo.put("registerUserName", model.getRegisterUserName());
			jo.put("securityCode", model.getSecurityCode());
			jo.put("confirmPassword", model.getConfirmPassword());
			jo.put("mobile", model.getMobile());
			jo.put("invitationCode", model.getInvitationCode());
			json = jo.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RequestProtocolTask httpRequestTask = new RequestProtocolTask(
				PARAM_METHOD_NAME, json, regisiterProtocol);
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
						Object data = jsonObject.getString("message");
						Message msg = mHandler
								.obtainMessage(ProtocolManager.NOTIFICATION);
						msg.obj = data;
						mHandler.sendMessage(msg);
					} else {

						Message msg = mHandler
								.obtainMessage(ProtocolManager.NOTIFICATION_RESPONSE_ERRORINFO);
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
			Message msg = mHandler.obtainMessage(ProtocolManager.NETWORK_ERROR);

			mHandler.sendMessage(msg);
		}
	}

}
