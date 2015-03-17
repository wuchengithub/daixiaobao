package com.wookii.protocol;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Handler;
import android.os.Message;

import com.wookii.alipay.ResultChecker;
import com.wookii.protocollManager.BaseProtocol;
import com.wookii.protocollManager.DataProtocolInterface;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.protocollManager.RequestProtocolTask;

public class SignCheckerProtocol extends BaseProtocol implements
		DataProtocolInterface {

	private SignCheckerProtocol loginProtocol = this;
	private Handler mHandler;
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
	private String PARAM_METHOD_NAME = "PayLogin/userPayByAilChecker/";
	private int what;

	public SignCheckerProtocol(List<NameValuePair> params) {
		this.params = params;
	}

	public void invokeSign(Handler handler, int what) {

		this.what = what;
		this.mHandler = handler;
		RequestProtocolTask httpRequestTask = new RequestProtocolTask(
				PARAM_METHOD_NAME, params, this);
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
						Message msg = mHandler.obtainMessage(what);
						msg.obj = ResultChecker.RESULT_CHECK_SIGN_SUCCEED;
						mHandler.sendMessage(msg);
					} else {

						Message msg = mHandler.obtainMessage(what);
						msg.obj = ResultChecker.RESULT_CHECK_SIGN_FAILED;
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
