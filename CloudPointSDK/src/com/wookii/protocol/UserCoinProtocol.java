package com.wookii.protocol;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.wookii.protocollManager.BaseProtocol;
import com.wookii.protocollManager.DataProtocolInterface;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.protocollManager.RequestProtocolTask;

import android.os.Handler;
import android.os.Message;

public class UserCoinProtocol extends BaseProtocol implements
		DataProtocolInterface {

	UserCoinProtocol getMoneyProtocol = this;
	Handler mHandler;
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	String PARAM_METHOD_NAME = "PayLogin/getMoney/";
	private int what;

	public UserCoinProtocol(Handler handler, String token, String uid, int what) {
		// TODO Auto-generated constructor stub
		this.what = what;
		this.mHandler = handler;
		params.add(new BasicNameValuePair("token", token));
		params.add(new BasicNameValuePair("uid", uid));

		RequestProtocolTask httpRequestTask = new RequestProtocolTask(
				PARAM_METHOD_NAME, params, getMoneyProtocol);
		Thread thread = new Thread(httpRequestTask);
		thread.start();
	}

	public UserCoinProtocol() {
		// TODO Auto-generated constructor stub
	}

	public void invokeGetMoney(String token, String uid, Handler handler) {

		this.mHandler = handler;

		params.add(new BasicNameValuePair("token", token));
		params.add(new BasicNameValuePair("uid", uid));

		RequestProtocolTask httpRequestTask = new RequestProtocolTask(
				PARAM_METHOD_NAME, params, getMoneyProtocol);
		Thread thread = new Thread(httpRequestTask);
		thread.start();

	}

	@Override
	public void onResposeProcotolData(Object object) {
		if (object != null) {
			try {
				Object json = new JSONTokener(object.toString()).nextValue();
				if (json instanceof JSONObject) {
					JSONObject jsonObject = new JSONObject(object.toString());
					String code = jsonObject.getString("code");
					if (code.equals("100000")) {
						JSONObject data = jsonObject.getJSONObject("data");
						String amount = data.getString("amount");
						Message msg = mHandler.obtainMessage(what);
						msg.obj = amount;
						mHandler.sendMessage(msg);
					} else {

						Message msg = mHandler
								.obtainMessage(ProtocolManager.NOTIFICATION_RESPONSE_ERRORINFO);
						msg.obj = jsonObject.getString("msg");
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
				e.printStackTrace();
			}

		} else {
			Message msg = mHandler.obtainMessage(ProtocolManager.NETWORK_ERROR);
			mHandler.sendMessage(msg);
		}
	}

}
