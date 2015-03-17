package com.wookii.protocol;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.wookii.UserPayActivity;
import com.wookii.protocollManager.BaseProtocol;
import com.wookii.protocollManager.DataProtocolInterface;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.protocollManager.RequestProtocolTask;

public class UserPayAilPotocol extends BaseProtocol implements
		DataProtocolInterface {

	private static final String PARAM_METHOD_NAME = "PayLogin/userPay/";
	private Handler mHandler;
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
	private int what;
	private JSONObject jsonObject;

	public UserPayAilPotocol(int what) {
		this.what = what;
	}

	@Override
	public void onResposeProcotolData(Object object) {
		if (object != null) {

			try {

				Object json = new JSONTokener(object.toString()).nextValue();
				if (json instanceof JSONObject) {
					jsonObject = new JSONObject(object.toString());
					String code = jsonObject.getString("code");
					if (code.equals("100000")) {

						Object sb = jsonObject.getString("data");
						Message msg = mHandler.obtainMessage(what);
						/*if (what == UserPayActivity.ALI_ORDER_INFO) {
							msg.obj = sb.toString();
						} else if (what == UserPayActivity.YEEPAY_REQUEST_RESULT) {
							msg.obj = url.toString();
						} else if (what == UserPayActivity.TENPAY_REQUEST_RESULT) {
							//
						}*/
						msg.obj = sb.toString();
						mHandler.sendMessage(msg);

						// JSONObject data = new JSONObject(obj.toString());
						/*
						 * StringBuilder sb = new StringBuilder();
						 * sb.append("partner="); String partner =
						 * data.getString("partner");//合作者身份id sb.append("\"" +
						 * partner + "\"&");
						 * 
						 * sb.append("sign_type="); String sign_type =
						 * data.getString("sign_type"); sb.append("\"" +
						 * sign_type + "\"&");
						 * 
						 * sb.append("sign="); String sign =
						 * data.getString("sign"); sb.append("\"" + sign +
						 * "\"&");
						 * 
						 * sb.append("notify_url="); String notify_url =
						 * data.getString("notify_url"); sb.append("\"" +
						 * notify_url + "\"&");
						 * 
						 * sb.append("seller="); String seller =
						 * data.getString("seller"); sb.append("\"" + seller +
						 * "\"&");
						 * 
						 * sb.append("out_trade_no="); String out_trade_no =
						 * data.getString("out_trade_no"); sb.append("\"" +
						 * out_trade_no + "\"&");
						 * 
						 * sb.append("subject="); String subject =
						 * data.getString("subject"); sb.append("\"" + subject +
						 * "\"&");
						 * 
						 * sb.append("body="); String body =
						 * data.getString("body"); sb.append("\"" + body +
						 * "\"&");
						 * 
						 * sb.append("total_fee="); String total_fee =
						 * data.getString("total_fee"); sb.append("\"" +
						 * total_fee + "\"&");
						 * 
						 * sb.append("extern_token="); String extern_token =
						 * data.getString("extern_token"); sb.append("\"" +
						 * extern_token + "\"");
						 */
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
				// TODO Auto-generated catch block
				Message msg = mHandler
						.obtainMessage(ProtocolManager.NOTIFICATION_RESPONSE_ERRORINFO);
				try {
					msg.obj = jsonObject.getString("msg");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mHandler.sendMessage(msg);
			}

		} else {
			Message msg = mHandler.obtainMessage(ProtocolManager.NETWORK_ERROR);

			mHandler.sendMessage(msg);
		}
	}

	public void sendRequest(String uid, String amountStr, String pay_way,
			String gid, String userNameStr, Handler handler, String phone_type) {
		this.mHandler = handler;

		params.add(new BasicNameValuePair("uid", uid));
		params.add(new BasicNameValuePair("amount", amountStr));
		params.add(new BasicNameValuePair("pay_way", pay_way));
		params.add(new BasicNameValuePair("game_code", gid));
		params.add(new BasicNameValuePair("mobile", userNameStr));
		params.add(new BasicNameValuePair("phone_type", phone_type));

		RequestProtocolTask httpRequestTask = new RequestProtocolTask(
				PARAM_METHOD_NAME, params, this);
		Thread thread = new Thread(httpRequestTask);
		thread.start();
	}

}
