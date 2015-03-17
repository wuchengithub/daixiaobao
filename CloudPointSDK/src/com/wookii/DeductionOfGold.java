package com.wookii;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.wookii.alipay.BaseHelper;
import com.wookii.common.Contants;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.CommonUtil;
import com.wookii.utils.LoginMessageDataUtils;
import com.wookii.utils.Str_MD5;

@SuppressLint("HandlerLeak")
public class DeductionOfGold {
	protected static final int USER_COIN = 0x121;
	protected static final int AIL_ORDER_INFO = 0x122;
	protected static final int RESULT_CHECKER = 0x123;
	protected static final int RQF_PAY = 0x124;
	protected int goldNumber;
	protected String customerOrder;
	protected Handler youHandler;
	protected Context context;
	private ProgressDialog mProgress;
	private CommonUtil cu;

	protected int what;
	ProtocolManager dataManager;
	String otherCode = "";
	String icon;
	String gid;
	String uid;
	String token;
	String userNameStr;
	Activity activity;
	private int koufenCount;
	/**
	 * 接收处理
	 */
	private Handler deductionHandler = new Handler() {
		private String tradeStatus;

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case USER_COIN:
				try {
					String icon = (String) msg.obj;
					double iconD = Double.parseDouble(icon);
					if (goldNumber <= iconD) {// 如果获取到的金币 足够支付 直接扣除
						dataManager = new ProtocolManager(context,
								deductionHandler);
						dataManager.getValidationId(Contants.OTHERCODE);// 获取验证码
					} else {
						// 打开支付界面进行支付
						closeProgress();
						openPayView(gid, WookiiSDKManager.PAY_REQUEST_CODE,
								String.valueOf(goldNumber - iconD), goldNumber,
								customerOrder);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				break;
			case ProtocolManager.NOTIFICATION_YANGZHENGCODE:// 获取验证码
				otherCode = msg.obj.toString();
				String tokenToMd5 = Str_MD5.tokenToMD5(uid, token,
						String.valueOf(goldNumber) + otherCode);
				dataManager.spendMoneyNoPassword(tokenToMd5, uid, gid,
						"游戏扣除金币", goldNumber, otherCode, customerOrder);
				break;
			case ProtocolManager.NOTIFICATION:
				closeProgress();
				String toastInfo = msg.obj.toString();// 账户余额
				Message youMsg = youHandler.obtainMessage(what);
				youMsg.obj = "扣除成功";
				youHandler.sendMessage(youMsg);
				break;
			case ProtocolManager.NOTIFICATION_RESPONSE_ERRORINFO:// 获取金币/验证码/扣除失败
				closeProgress();
				Message errorMsg = youHandler.obtainMessage(what);
				errorMsg.obj = msg.obj.toString() + "扣除失败";
				youHandler.sendMessage(errorMsg);
				tipUser();
				break;
			case ProtocolManager.NETWORK_ERROR:// 网络问题
				closeProgress();
				Message networkErrorMsg = youHandler.obtainMessage(what);
				networkErrorMsg.obj = "扣除失败";
				youHandler.sendMessage(networkErrorMsg);
				tipUser();
				break;

			case WookiiSDKManager.PAY_REQUEST_CODE: // 充值结果
				int m = msg.arg1;
				if (m == WookiiSDKManager.SUCCESS) {
					dataManager = new ProtocolManager(context, deductionHandler);
					dataManager.getValidationId(Contants.OTHERCODE);// 获取验证码
					try {
						new Thread().sleep(10000000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (m == WookiiSDKManager.FAIL) {
					Message fail = youHandler.obtainMessage(what);
					fail.obj = "充值失败";
					youHandler.sendMessage(fail);
				}

				break;
			default:
				Message defautMsg = youHandler.obtainMessage(what);
				defautMsg.obj = msg.obj.toString() + "扣除失败";
				youHandler.sendMessage(defautMsg);
				break;
			}
		};
	};

	/**
	 * 
	 * @param context
	 * @param handler
	 * @param what
	 * @param goldNumber
	 *            扣除金币数量
	 * @param customerOrder订单编号
	 * @param password
	 *            密码
	 * @param userNameStr
	 *            用户名
	 * @param gid
	 *            游戏ID
	 */
	protected void deductionGold(Context context, Handler handler, int what,
			int goldNumber, String customerOrder, String userNameStr, String gid) {
		if (goldNumber <= 0) {
			throw new IllegalArgumentException("goldNumber goldNumber <=0 ");
		} else if ("".equals(userNameStr) || userNameStr == null) {
			throw new IllegalArgumentException("userNameStr is null  ");
		} else if ("".equals(gid) || gid == null) {
			throw new IllegalArgumentException("gid is null  ");
		} else {
			uid = LoginMessageDataUtils.getUID(context);
			token = LoginMessageDataUtils.getToken(context);
			if (uid == null || token == null) {
				throw new IllegalArgumentException("please login  ");
			} else {
				this.activity = (Activity) context;
				this.goldNumber = goldNumber;
				this.customerOrder = customerOrder;
				this.context = context;
				this.youHandler = handler;
				this.userNameStr = userNameStr;
				this.gid = gid;
				this.what = what;
				String mdToken = Str_MD5.tokenToMD5(uid, token, "");
				WookiiSDKManager.getUserCoin(context, deductionHandler,
						USER_COIN, uid, mdToken);
			}
		}
		cu = CommonUtil.getInstance();
		closeProgress();
		cu.setHandler(deductionHandler);
		mProgress = BaseHelper.showProgress(activity, null, "加载中...", false,
				true);
	}

	/** 关闭加载框 */
	private void closeProgress() {
		// cu.setHandler(null);
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打开支付界面
	 * 
	 * @param context
	 * @param gameCode
	 */
	private void openPayView(String gameCode, int requestCode, String money,
			int goldNumber, String customerOrder) {
		Intent intent = new Intent();
		intent.putExtra("gid", gameCode);
		intent.putExtra("money", money);
		intent.putExtra("goldNumber", goldNumber);
		intent.putExtra("customerOrder", customerOrder);
		intent.setClass(context, UserPayActivity.class);
		((Activity) context).startActivityForResult(intent, requestCode);
	}

	/**
	 * 扣分失败的时候
	 */
	private void tipUser() {
		new AlertDialog.Builder(context).setMessage("扣分失败，重试三次，没有成功，联系客服")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("重试", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String tokenToMd5 = Str_MD5.tokenToMD5(uid, token,
								String.valueOf(goldNumber) + otherCode);
						dataManager.spendMoneyNoPassword(tokenToMd5, uid, gid,
								"游戏扣除金币", goldNumber, otherCode, customerOrder);
					}
				});
	}
}
