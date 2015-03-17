package com.wookii.protocol;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.wookii.protocollManager.BaseProtocol;
import com.wookii.protocollManager.DataProtocolInterface;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.protocollManager.RequestProtocolTask;

/**
 * 验证手机号码
 * 
 * @author cloudpoint_android
 * 
 */
public class ProvingPhoneProtocol extends BaseProtocol implements
		DataProtocolInterface {
	ProvingPhoneProtocol receiveGameGiftProtocol = this;
	private int what;
	private Handler mHandler;
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
	private String PARAM_METHOD_NAME = "UserCenter/validatePhone";

	public void ProvingPhoneProtocol(String phone, String code,
			Handler handler, String uid, String token,int what) {
		this.mHandler = handler;
		this.what = what;
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("uid", uid));
		params.add(new BasicNameValuePair("token", token));
		RequestProtocolTask httpRequestTask = new RequestProtocolTask(
				PARAM_METHOD_NAME, params, this);
		Thread thread = new Thread(httpRequestTask);
		thread.start();
	}

	@Override
	public void onResposeProcotolData(Object object) {
		if (object != null) {
			try {
				JSONObject jsonObject = new JSONObject(object.toString());
				String code = jsonObject.getString("code");
				if (code.equals("100000")) {
					Object data = jsonObject.getString("data");
					JSONObject dataObject = new JSONObject(data.toString());
					Message msg = mHandler.obtainMessage(what);
					msg.obj = dataObject.getString("validate_code");
					mHandler.sendMessage(msg);
				} else {
					Message msg = mHandler
							.obtainMessage(ProtocolManager.NOTIFICATION_RESPONSE_ERRORINFO);
					msg.obj = jsonObject.getString("msg");
					mHandler.sendMessage(msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else {
			Message msg = mHandler.obtainMessage(ProtocolManager.NETWORK_ERROR);

			mHandler.sendMessage(msg);
		}
	}

}
