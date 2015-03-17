package com.wookii.protocollManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.wookii.protocol.ForgetPasswordProtocol;
import com.wookii.protocol.GetLastPayLogProtocol;
import com.wookii.protocol.GetMoneyLogProtocol;
import com.wookii.protocol.GetValidationIdProtocol;
import com.wookii.protocol.LoginProtocol;
import com.wookii.protocol.ModifyPasswordProtocol;
import com.wookii.protocol.RegisiterProtocol;
import com.wookii.protocol.SpendMoneyNoPasswordProtocol;
import com.wookii.protocol.SpendMoneyProtocol;
import com.wookii.protocol.UserCoinProtocol;
import com.wookii.protocol.UserPayBFPotocol;
import com.wookii.protocol.UserRegisterTelProtocol;
import com.wookii.utils.DeviceTool;

public class ProtocolManager {
	public static final int NETWORK_ERROR = 0;
	public static final int NOTIFICATION = 0x1211;
	public static final int NOTIFICATION_SUBMIT_SUCCESS = 2;
	public static final int NOTIFICATION_WEIBOKEY = 3;
	public static final int NOTIFICATION_CHECKINFO = 4;
	public static final int NOTIFICATION_RESPONSE_ERRORINFO = 5;
	public static final int TIMEOUT_ERROR = 6;
	public static final int ERROR_CODE_ZORE = 0;
	public static final int ERROR_CODE_ONE = 1;
	public static final int ERROR_CODE_TOW = 2;
	public static final int NOTIFICATION_SUBMIT_GETCODE = 7;
	public static final int NOTIFICATION_NOTREGISTERLOGIN = 8;
	public static final int NOTIFICATION_YANGZHENGCODE = 9;

	// 修改密码成功通知
	public static final int NOTIFICATION_MODIFY_PASSWORD = 10;
	// 忘记密码重置通知
	public static final int NOTIFICATION_FORGET_PASSWORD = 11;

	public static final int NOTIFICATION_PARSE_ERROR = 12;

	public static final int LOGIN_SUCCESS = 13;
	private Context mContext;
	private Handler mHandler;

	public static final String UId = "UId";// sessionUId
	public static final String telNum = "telNum";// sessiontelNum
	public static final String token = "token";// sessiontelToken
	public static final String nickName = "nickName";// sessiontelToken
	public static final String interimToken = "interimToken";// 临时token
	public static final String storeId = "storeId";
	public static final String isVip = "isVip";

	public ProtocolManager(Context mContext, Handler mHandler) {
		super();
		this.mContext = mContext;
		this.mHandler = mHandler;
	}

	/**
	 * 获取验证码
	 * 
	 * @param phoneNumber
	 * @param uid
	 */
	public void userRegisterTel(String phoneNumber, String uid) {
		UserRegisterTelProtocol registerTelProtocol = new UserRegisterTelProtocol();
		registerTelProtocol.invokeRegistertelCode(phoneNumber, uid, mHandler);
	}


	/**
	 * 修改密码
	 * 
	 * @param uid
	 * @param oldPwd
	 * @param newPwd
	 * @param token
	 */
	public void modifyPassword(String uid, String oldPwd, String newPwd,
			String token) {
		ModifyPasswordProtocol modifyPwdProtocol = new ModifyPasswordProtocol();
		String json = "";
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", uid);
			obj.put("token", token);
			obj.put("oldPassword", oldPwd);
			obj.put("newPassword", newPwd);
			obj.put("deviceId", DeviceTool.getUniqueId(mContext));
			json = obj.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		modifyPwdProtocol.invokeModifyPwd(json, mHandler);
	}

	/**
	 * 忘记密码，重置密码
	 * 
	 * @param phone
	 * @param newPwd
	 * @param validateCode
	 */
	public void resetPassword(String phone, String newPwd, String validateCode) {
		ForgetPasswordProtocol protocol = new ForgetPasswordProtocol();
		protocol.invokeResetPwd(phone, newPwd, validateCode, mHandler);
	}

	/**
	 * 获取最后一次充值记录
	 * 
	 * @param token
	 * @param uid
	 */
	public void getLastPayLogProtocol(String token, String uid) {
		GetLastPayLogProtocol getLastPayLogProtocol = new GetLastPayLogProtocol();
		getLastPayLogProtocol.getLastPayLog(token, uid, mHandler);
	}

	/**
	 * 获取充值记录
	 * 
	 * @param token
	 * @param uid
	 * @param mtype
	 */
	public void getMoneyLog(String token, String uid, String mtype) {
		GetMoneyLogProtocol getMoneyLogProtocol = new GetMoneyLogProtocol();
		getMoneyLogProtocol.invokeGetMoneyLog(token, uid, mtype, mHandler);
	}

	/**
	 * 
	 * @param uid
	 * @param amount
	 * @param pay_way
	 * @param gid
	 * @param mobile
	 */
	public void userPay(String uid, String amount, String pay_way, String gid,
			String mobile) {
		UserPayBFPotocol payPotocol = new UserPayBFPotocol();
		payPotocol.sendRequest(uid, amount, pay_way, gid, mobile, mHandler);
	}

	/**
	 * 获取用户金币
	 * 
	 * @param token
	 * @param uid
	 */
	public void getMoney(String token, String uid) {
		UserCoinProtocol getMoneyProtocol = new UserCoinProtocol();
		getMoneyProtocol.invokeGetMoney(token, uid, mHandler);
	}

	/**
	 * 获取支付验证码
	 * 
	 * @param describe
	 */
	public void getValidationId(String describe) {
		GetValidationIdProtocol getValidationIdProtocol = new GetValidationIdProtocol();
		getValidationIdProtocol.invokeRegistertelCode(describe, mHandler);
	}

	/**
	 * 扣除金币（需输入密码确认）
	 * 
	 * @param token
	 * @param uid
	 * @param gid
	 * @param mlmemo
	 * @param money
	 * @param password
	 * @param validation_id
	 * @param other_order_id
	 */
	public void spendMoney(String token, String uid, String gid, String mlmemo,
			int money, String password, String validation_id,
			String other_order_id) {
		SpendMoneyProtocol spendMoneyProtocol = new SpendMoneyProtocol();
		spendMoneyProtocol.invokeSpendMoney(token, uid, gid, mlmemo, money,
				password, validation_id, mHandler, other_order_id);
	}

	/**
	 * 无密码扣除金币
	 * 
	 * @param token
	 * @param uid
	 * @param gid
	 * @param mlmemo
	 * @param money
	 * @param validation_id
	 * @param other_order_id
	 */
	public void spendMoneyNoPassword(String token, String uid, String gid,
			String mlmemo, int money, String validation_id,
			String other_order_id) {
		SpendMoneyNoPasswordProtocol spendMoneyProtocol = new SpendMoneyNoPasswordProtocol();
		spendMoneyProtocol.invokeSpendMoneyNoPassword(token, uid, gid, mlmemo,
				money, validation_id, mHandler, other_order_id);
	}

}
