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

public class GetValidationIdProtocol extends BaseProtocol implements
		DataProtocolInterface {
	GetValidationIdProtocol getValidationIdProtocol = this;

	Handler mHandler;
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	String PARAM_METHOD_NAME = "PayLogin/getValidationId/";

	public void invokeRegistertelCode(String describe, Handler handler) {

		this.mHandler = handler;

		params.add(new BasicNameValuePair("describe", describe));

		RequestProtocolTask httpRequestTask = new RequestProtocolTask(
				PARAM_METHOD_NAME, params, getValidationIdProtocol);
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
					String code = jsonObject.getString("code");
					if (code.equals("100000")) {
						Object data = jsonObject.getString("data");
						JSONObject jsonObject1 = new JSONObject(data.toString());
						String validation_id = jsonObject1
								.getString("validation_id");
						Message msg = mHandler
								.obtainMessage(ProtocolManager.NOTIFICATION_YANGZHENGCODE);
						msg.obj = validation_id;
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Message msg = mHandler.obtainMessage(ProtocolManager.NETWORK_ERROR);

			mHandler.sendMessage(msg);
		}
	}

}
