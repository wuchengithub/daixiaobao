package com.wookii;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.wookii.protocol.NotRegisterLoginProtocol;
import com.wookii.protocol.UserCoinProtocol;
import com.wookii.utils.CommonUtil;
import com.wookii.utils.LoginMessageDataUtils;

public class WookiiSDKManager {

	public static final int PAY_REQUEST_CODE = 4;
	public static final int LOGIN_REQUEST_CODE = 3;
	public static final int REGISTER_REQUEST_CODE = 5;
	public static final int FIND_PASSWORD_REQUEST_CODE = 2;
	public static final int MODIFY_PASSWORD_REQUEST_CODE = 1;
	public static final int LOGIN_REQUEST_FROM_GAME_CODE = 6;
	public static final int REGISTER_REQUEST_FROM_GAME_CODE = 7;
	/** 扣除金币 */
	public static final int SPEND_MONEY_REQUEST_CODE = 9;

	/** 成功标志 */
	public static int SUCCESS = 10;
	/** 失败标志 */
	public static int FAIL = 11;

	/** 常规提示（提醒Activity已关闭等） */
	public static final int COMMON_SESULT_CODE = 12;
	/** 记录是否是真是用户登录 */

	/** 扣除金币（无需密码确认） */
	public static final int SPEND_MONEY_NO_PASSWORD_REQUEST_CODE = 13;


	/**
	 * 注册设备
	 * 
	 * @param context
	 * @param imei
	 * @param imsi
	 * @param handler
	 * @param what
	 * @param channel
	 *            合作商识别码，不可为空
	 * @return
	 */
	public static boolean registerDevice(Context context, String imei,
			String imsi, String channel, Handler handler, int what) {
		if (imsi == null)
			return false;
		NotRegisterLoginProtocol notRegisterLoginProtocol = new NotRegisterLoginProtocol();
		notRegisterLoginProtocol.invokeNotRegisterLogin(imei, imsi, channel,
				handler, what);
		return true;
	}

	/**
	 * 打开支付界面
	 * 
	 * @param context
	 * @param gameCode
	 */
	public final static void openPayView(Context context, String gameCode,
			int requestCode) {
		CommonUtil.getInstance().setHandler(null);
		Intent intent = new Intent();
		intent.putExtra("gid", gameCode);
		intent.setClass(context, UserPayActivity.class);
		((Activity) context).startActivityForResult(intent, requestCode);
	}

	/**
	 * 判断是否已登录
	 * 
	 * @param context
	 * @return
	 */
	public static final boolean isLogin(Context context) {
		String uid = LoginMessageDataUtils.getUID(context);
		String token = LoginMessageDataUtils.getToken(context);
		if (uid == null || token == null) {
			return false;
		}
		return true;
	}

	/**
	 * 获取用户的金币
	 * 
	 * @param context
	 * @param hanler
	 * @param what
	 * @param uid
	 * @param token
	 */
	public final static void getUserCoin(Context context, Handler hanler,
			int what, String uid, String token) {
		UserCoinProtocol protocol = new UserCoinProtocol(hanler, token, uid,
				what);
	}

	/**
	 * 找回密码
	 * 
	 * @param context
	 * @param requestCode
	 */
	public final static void openFindPasswordView(Context context,
			int requestCode) {
		Intent intent = new Intent();
		intent.setClass(context, ForgetPasswordActiviy.class);
		((Activity) context).startActivityForResult(intent, requestCode);

	}

	/**
	 * 从游戏中登录->找回密码
	 * 
	 * @param context
	 * @param requestCode
	 */
	public final static void openFindPasswordViewFromGame(Context context,
			int requestCode) {
		Intent intent = new Intent();
		intent.setClass(context, ForgetPasswordFromGameActiviy.class);
		((Activity) context).startActivityForResult(intent, requestCode);

	}

	/**
	 * 修改密码
	 * 
	 * @param context
	 * @param requestCode
	 */
	public final static void openModifyPasswordView(Context context,
			int requestCode) {
		Intent intent = new Intent();
		intent.setClass(context, ModifyPasswordActivity.class);
		((Activity) context).startActivityForResult(intent, requestCode);

	}


	/**
	 * 充值扣币
	 * 
	 * @param context
	 * @param handler
	 * @param what
	 * @param goldNumber
	 *            扣除金币数量
	 * @param customerOrder
	 *            订单编号
	 * @param userNameStr
	 *            用户名
	 * @param gid
	 *            游戏编号
	 */
	public final static void deductionOfGold(Context context, Handler handler,
			int what, int goldNumber, String customerOrder, String userNameStr,
			String gid) {
		DeductionOfGold deductionGold = new DeductionOfGold();
		deductionGold.deductionGold(context, handler, what, goldNumber,
				customerOrder, userNameStr, gid);
	}

	/**
	 * 扣币（无需输入密码确认）
	 * 
	 * @param context
	 * @param handler
	 * @param what
	 * @param goldNumber
	 *            扣除金币数量
	 * @param customerOrder
	 *            订单编号
	 * @param userNameStr
	 *            用户名
	 * @param gid
	 *            游戏编号
	 */
	public final static void spendMoneyNoPassword(Context context,
			Handler handler, int what, int goldNumber, String customerOrder,
			String userNameStr, String gid) {
		SpendMoneyNoPassword spendMoneyNoPassword = new SpendMoneyNoPassword();
		spendMoneyNoPassword.spendMoneyNoPassword(context, handler, what,
				goldNumber, customerOrder, userNameStr, gid);
	}
	
	public final static void fristInit(Context context) {
		LoginMessageDataUtils.fristInit(context);
	}
	
	public final static boolean isFristInit(Context context) {
		return LoginMessageDataUtils.getFristInit(context);
	}
}
