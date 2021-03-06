package com.wookii.alipay;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.Handler;

import com.wookii.protocol.SignCheckerProtocol;

/**
 * 对签名进行验签
 * 
 */
public class ResultChecker {

	public static final int RESULT_INVALID_PARAM = 0;
	public static final int RESULT_CHECK_SIGN_FAILED = 1;
	public static final int RESULT_CHECK_SIGN_SUCCEED = 2;

	String mContent;
	private Handler handler;
	private int what;

	public ResultChecker(String content) {
		this.mContent = content;
	}

	// /**
	// * 从验签内容中获取成功状态
	// *
	// * @return
	// */
	// private String getSuccess() {
	// String success = null;
	//
	// try {
	// JSONObject objContent = BaseHelper.string2JSON(this.mContent, ";");
	// String result = objContent.getString("result");
	// result = result.substring(1, result.length() - 1);
	//
	// JSONObject objResult = BaseHelper.string2JSON(result, "&");
	// success = objResult.getString("success");
	// success = success.replace("\"", "");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return success;
	// }

	public ResultChecker(String content, Handler handler, int what) {
		// TODO Auto-generated constructor stub
		this.mContent = content;
		this.handler = handler;
		this.what = what;
	}

	/**
	 * ]
	 * 
	 * @return
	 */
	public int checkSignFromServer() {
		int retVal = RESULT_CHECK_SIGN_SUCCEED;

		try {
			JSONObject objContent = BaseHelper.string2JSON(this.mContent, ";");
			String result = objContent.getString("result");
			result = result.substring(1, result.length() - 1);
			// 获取待签名数据
			int iSignContentEnd = result.indexOf("&sign_type=");
			String signContent = result.substring(0, iSignContentEnd);
			// 获取签名
			JSONObject objResult = BaseHelper.string2JSON(result, "&");
			String signType = objResult.getString("sign_type");
			signType = signType.replace("\"", "");

			String sign = objResult.getString("sign");
			sign = sign.replace("\"", "");

			// 进行验签 返回验签结果
			if (signType.equalsIgnoreCase("RSA")) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("sign", sign));
				params.add(new BasicNameValuePair("signContent", signContent));
				params.add(new BasicNameValuePair("signType", "RSA"));
				SignCheckerProtocol signCheckerProtocol = new SignCheckerProtocol(
						params);
				signCheckerProtocol.invokeSign(handler, what);
				/*
				 * if (!Rsa.doCheck(signContent, sign,
				 * PartnerConfig.RSA_ALIPAY_PUBLIC)) retVal =
				 * RESULT_CHECK_SIGN_FAILED;
				 */
			}
		} catch (Exception e) {
			retVal = RESULT_INVALID_PARAM;
			e.printStackTrace();
		}

		return retVal;
	}

	/**
	 * 对签名进行验签
	 * 
	 * @return
	 */
	public int checkSign() {
		int retVal = RESULT_CHECK_SIGN_SUCCEED;

		try {
			JSONObject objContent = BaseHelper.string2JSON(this.mContent, ";");
			String result = objContent.getString("result");
			result = result.substring(1, result.length() - 1);
			// 获取待签名数据
			int iSignContentEnd = result.indexOf("&sign_type=");
			String signContent = result.substring(0, iSignContentEnd);
			// 获取签名
			JSONObject objResult = BaseHelper.string2JSON(result, "&");
			String signType = objResult.getString("sign_type");
			signType = signType.replace("\"", "");

			String sign = objResult.getString("sign");
			sign = sign.replace("\"", "");
			// 进行验签 返回验签结果
			if (signType.equalsIgnoreCase("RSA")) {
				if (!Rsa.doCheck(signContent, sign,
						PartnerConfig.RSA_ALIPAY_PUBLIC))
					retVal = RESULT_CHECK_SIGN_FAILED;
			}
		} catch (Exception e) {
			retVal = RESULT_INVALID_PARAM;
			e.printStackTrace();
		}

		return retVal;
	}

	// boolean isPayOk() {
	// boolean isPayOk = false;
	//
	// String success = getSuccess();
	// if (success.equalsIgnoreCase("true")
	// && checkSign() == RESULT_CHECK_SIGN_SUCCEED)
	// isPayOk = true;
	//
	// return isPayOk;
	// }
}