package com.wookii;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.unionpay.UPPayAssistEx;
import com.wookii.alipay.AlixId;
import com.wookii.alipay.BaseHelper;
import com.wookii.alipay.MobileSecurePayHelper;
import com.wookii.alipay.MobileSecurePayer;
import com.wookii.alipay.PartnerConfig;
import com.wookii.alipay.ResultChecker;
import com.wookii.alipay.Rsa;
import com.wookii.protocol.GetYeePayResultProtocol;
import com.wookii.protocol.UserPayAilPotocol;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.CommonUtil;
import com.wookii.utils.LoginMessageDataUtils;
import com.wookii.utils.Str_MD5;

/**
 * 支付
 * 
 * @author DengBin
 * 
 */
public class UserPayActivity extends Activity {
	public static final int UserPay_RESULT_CODE = 4;
	private static final int requestCode = 888;
	private static final int GET_MONEY = 0x123;
	protected static final int USER_PAY_TRATNO = 0x124;
	public static final int ALI_ORDER_INFO = 0x125;
	protected static final int RESULT_CHECKER = 0x126;
	/** 易宝支付请求标志 */
	public static final int YEEPAY_REQUEST_RESULT = 0x127;
	/** 易宝支付结果标志 */
	public static final int YEEPAY_RESULT = 0x131;
	/** 财付通请求标志 */
	public static final int TENPAY_REQUEST_RESULT = 0x128;
	/** 银联支付 */
	public static final int UNIONPAY_REQUEST_RESULT = 0x130;
	/** 用户余额 */
	private static final int USER_COIN = 0x129;
	private Intent intent;
	private String uid;
	/** 输入金额 */
	private EditText rechargeInputNumber;
	/** 显示余额或充值金额 */
	private TextView balance;
	private TextView userName;
	/** 显示为：可用余额或者充值金额 */
	private TextView rechargeTitle;
	/** 支付宝 */
	private ImageButton alipay;
	/** 财付通 */
	private ImageButton tenpay;
	/** 易宝支付 */
	private ImageButton yeepay;
	/** 银联支付 */
	private ImageButton chinaUnionpay;
	/** 关闭按钮 */
	private ImageButton rechargeClose;

	/** 是否已经做了充值操作（包括成功、失败、网络异常等） */
	private boolean isRecharged;

	private String token;
	/** 用户名称（电话号码） */
	private String userNameStr;
	private String amountStr;
	private String gid;
	private ProgressDialog mProgress;
	/** 余额 */
	private String balanceStr;
	private String tradNoStr;
	private Spinner spinnerMoney;
	/** 易宝支付订单 */
	private String yeePayorderId;

	// 按钮资源ID
	private int rechargeCloseId;
	private int alipayId;
	private int yeepayId;
	private int tenpayId;
	private int chinaUnionpayId;

	private LinearLayout ll;
	private FrameLayout l1;
	private LinearLayout l2;
	private LinearLayout l3;
	private LinearLayout l4;

	/**
	 * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	 */
	private String mMode = "00";
	/** 为支付扣费动作返回结果 */
	private Handler commonUtilHandler;
	private static final int PLUGIN_NOT_INSTALLED = -1;
	private static final int PLUGIN_NEED_UPGRADE = 2;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		private String tradeStatus;

		@Override
		public void handleMessage(Message msg) {
			if (mProgress != null) {
				if (mProgress.isShowing()) {
					mProgress.dismiss();
				}
			}
			switch (msg.what) {
			case GET_MONEY:
				closeProgress();
				balanceStr = msg.obj.toString();
				balance.setText(balanceStr);
				// startSdkToPay(toastInfo, suport_c);
				break;
			case ProtocolManager.NETWORK_ERROR:
				if (null != commonUtilHandler) {
					isRecharged = true;
					sentCommonUtilHandlerMsg(WookiiSDKManager.FAIL);
				}
				break;

			case ProtocolManager.TIMEOUT_ERROR:
				isRecharged = true;
				sentCommonUtilHandlerMsg(WookiiSDKManager.FAIL);
				break;
			case ProtocolManager.NOTIFICATION_RESPONSE_ERRORINFO:
				closeProgress();
				balanceStr = msg.obj.toString();
				Toast.makeText(UserPayActivity.this, balanceStr,
						Toast.LENGTH_LONG).show();
				break;

			case USER_PAY_TRATNO:
				closeProgress();
				tradNoStr = msg.obj.toString();
				// startSdkToPay(tradNoStr, payType);
				break;
			case ALI_ORDER_INFO:
				closeProgress();
				String orderInfo = msg.obj.toString();
				startSdkToPayForAil(orderInfo);
				break;
			case YEEPAY_REQUEST_RESULT:// 易宝支付 友游SDK服务器返回
				closeProgress();
				String yeepayInfo = msg.obj.toString();
				try {
					JSONObject jsonObject = new JSONObject(yeepayInfo);
					String uri = jsonObject.getString("url");
					yeePayorderId = jsonObject.getString("order_id");
					Intent intent = new Intent(UserPayActivity.this,
							YeePayActivity.class);
					intent.putExtra("uri", uri);
					startActivityForResult(intent, YEEPAY_REQUEST_RESULT);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				break;
			case YEEPAY_RESULT:// 易宝支付结果
				closeProgress();
				HashMap<String, String> hm = (HashMap<String, String>) msg.obj;
				// 支付状态 0未支付,1支付失败,2支付成功
				String payStatus = hm.get("pay_status");
				String orderId = hm.get("order_id");
				if (null != payStatus && payStatus.equals("2")) {
					// 支付成功
					isRecharged = true;
					sentCommonUtilHandlerMsg(WookiiSDKManager.SUCCESS);
					setResult(WookiiSDKManager.SUCCESS);
				} else {
					// 支付失败
					isRecharged = true;
					sentCommonUtilHandlerMsg(WookiiSDKManager.FAIL);
					setResult(WookiiSDKManager.FAIL);
				}

				break;
			case TENPAY_REQUEST_RESULT: // 财付通支付
				closeProgress();
				// ?
				break;
			case AlixId.RQF_PAY: // 支付宝支付
				closeProgress();
				String ret = (String) msg.obj;
				// 处理交易结果
				try {
					// 获取交易状态码，具体状态代码请参看文档
					tradeStatus = "resultStatus={";
					int imemoStart = ret.indexOf("resultStatus=");
					imemoStart += tradeStatus.length();
					int imemoEnd = ret.indexOf("};memo=");
					if (imemoEnd == -1) {
						Toast.makeText(UserPayActivity.this, "您取消了付款操作",
								Toast.LENGTH_LONG).show();
						isRecharged = true;
						sentCommonUtilHandlerMsg(WookiiSDKManager.FAIL);
						setResult(WookiiSDKManager.FAIL);
					} else {
						tradeStatus = ret.substring(imemoStart, imemoEnd);
						// 先验签通知,这里访问服务器进行验签
						ResultChecker resultChecker = new ResultChecker(ret,
								handler, RESULT_CHECKER);
						resultChecker.checkSignFromServer();
					}
				} catch (Exception e) {
					e.printStackTrace();
					// memo={操作已经取消。};resultStatus={6001};result={}
					int memoStart = ret.indexOf("memo={");
					memoStart += "memo={".length();
					int memoEnd = ret.indexOf("};resultStatus={");
					String memoString = ret.substring(memoStart, memoEnd);
					BaseHelper.showDialog(UserPayActivity.this, "提示",
							memoString, android.R.drawable.ic_dialog_info);

					isRecharged = true;
					sentCommonUtilHandlerMsg(WookiiSDKManager.FAIL);
					setResult(WookiiSDKManager.FAIL);
				}
				break;
			case RESULT_CHECKER: // 支付宝支付验签
				// 验签失败
				if ((Integer) msg.obj == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
					BaseHelper
							.showDialog(UserPayActivity.this, "提示",
									"您的订单信息已被非法篡改。",
									android.R.drawable.ic_dialog_alert);
				} else {
					// 验签成功。验签成功后再判断交易状态码
					if (tradeStatus.equals("9000")) {
						// 判断交易状态码，只有9000表示交易成功
						BaseHelper.showDialog(UserPayActivity.this, "提示",
								"支付成功", android.R.drawable.ic_dialog_info);

						isRecharged = true;
						sentCommonUtilHandlerMsg(WookiiSDKManager.SUCCESS);
						setResult(WookiiSDKManager.SUCCESS);
						// 刷新界面金币
						// getUserCoin();
					} else {
						BaseHelper.showDialog(UserPayActivity.this, "提示",
								"支付失败", android.R.drawable.ic_dialog_info);

						isRecharged = true;
						sentCommonUtilHandlerMsg(WookiiSDKManager.FAIL);
						setResult(WookiiSDKManager.FAIL);
					}
				}
				break;
			case USER_COIN:
				// 余额（金额）
				closeProgress();
				String balanceStr = msg.obj.toString();
				balance.setText(balanceStr);
				break;
			case UNIONPAY_REQUEST_RESULT:
				// 银联支付
				String tn = "";
				closeProgress();
				String unionPay = msg.obj.toString();
				try {
					JSONObject jsonObject = new JSONObject(unionPay);
					tn = jsonObject.getString("tn");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if (null == tn || tn.equals("")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserPayActivity.this);
					builder.setTitle("错误提示");
					builder.setMessage("网络连接失败,请重试!");
					builder.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									isRecharged = true;
									sentCommonUtilHandlerMsg(WookiiSDKManager.FAIL);
									dialog.dismiss();
								}
							});
					builder.create().show();
				} else {
					// 步骤2：通过银联工具类启动支付插件
					int retInt = UPPayAssistEx.startPay(UserPayActivity.this,
							null, null, tn, mMode);
					if (retInt == PLUGIN_NEED_UPGRADE
							|| retInt == PLUGIN_NOT_INSTALLED) {
						// 需要重新安装控件
						AlertDialog.Builder builder = new AlertDialog.Builder(
								UserPayActivity.this);
						builder.setTitle("提示");
						builder.setMessage("完成购买需要安装银联支付控件，是否安装？");
						builder.setNegativeButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// http://mobile.unionpay.com/getclient?platform=android&type=securepayplugin
										Intent intent = new Intent();
										intent.setAction(Intent.ACTION_VIEW);
										intent.setData(Uri
												.parse("http://mobile.unionpay.com/getclient?platform=android&type=securepayplugin"));
										startActivity(intent);
										dialog.dismiss();
									}
								});
						builder.setPositiveButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										isRecharged = true;
										sentCommonUtilHandlerMsg(WookiiSDKManager.FAIL);
										dialog.dismiss();
									}
								});
						builder.create().show();
					}
				}
				break;
			default:
				super.handleMessage(msg);
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		commonUtilHandler = CommonUtil.getInstance().getHandler();
		isRecharged = false;
		try {
			gid = intent.getStringExtra("gid");
		} catch (Exception e) {
			Toast.makeText(this, "请传入游戏Id", Toast.LENGTH_LONG).show();
			try {
				finish();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return;
		}

		int layoutId = ResourcesIdFactoryUtils.getLayoutId(
				getApplicationContext(), "cpsdk_recharge_immediately");
		setContentView(layoutId);
		initViews();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (this.requestCode == requestCode && resultCode == 88888) {
			Toast.makeText(
					this,
					resultCode + "  " + data.getStringExtra("resultMessage")
							+ "retCode" + data.getStringExtra("resultCode"),
					Toast.LENGTH_LONG).show();
			super.onActivityResult(requestCode, resultCode, data);
		} else if (null != data) {
			// 银联支付步骤3：处理银联手机支付控件返回的支付结果
			String msg = "";
			int ret = WookiiSDKManager.FAIL;
			/*
			 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
			 */
			final String str = data.getExtras().getString("pay_result");
			if (str.equalsIgnoreCase("success")) {
				msg = "支付成功！";
				ret = WookiiSDKManager.SUCCESS;
			} else if (str.equalsIgnoreCase("fail")) {
				msg = "支付失败！";
			} else if (str.equalsIgnoreCase("cancel")) {
				msg = "用户取消了支付";
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("支付结果通知");
			builder.setMessage(msg);
			builder.setInverseBackgroundForced(true);
			// builder.setCustomTitle();
			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (str.equalsIgnoreCase("success")) {
								// getUserCoin();
							}
							dialog.dismiss();
						}
					});
			builder.create().show();
			isRecharged = true;
			sentCommonUtilHandlerMsg(ret);
			setResult(ret);
		} else if (requestCode == YEEPAY_REQUEST_RESULT) {
			// 易宝支付界面已关闭，请求订单支付情况
			if (resultCode == WookiiSDKManager.COMMON_SESULT_CODE) {
				getYeePayResult();
			}
		}
	}

	/**
	 * 获取易宝支付结果
	 */
	private void getYeePayResult() {
		GetYeePayResultProtocol gYPRetPro = new GetYeePayResultProtocol();
		gYPRetPro.invokeGetMoney(uid, yeePayorderId, handler, YEEPAY_RESULT);
	}

	/**
	 * goBack()表示返回webView的上一页面
	 */
	public boolean onKeyDown(int keyCoder, KeyEvent event) {
		if (keyCoder == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return true;
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		rechargeCloseId = ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "ib_recharge_close");
		alipayId = ResourcesIdFactoryUtils.getId(getApplicationContext(),
				"ib_recharge_zhifubao");
		yeepayId = ResourcesIdFactoryUtils.getId(getApplicationContext(),
				"ib_recharge_yibao");
		tenpayId = ResourcesIdFactoryUtils.getId(getApplicationContext(),
				"ib_recharge_caifutong");
		chinaUnionpayId = ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "ib_recharge_chinaunionpay");

		ll = (LinearLayout) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "recharge_immediately"));
		l1 = (FrameLayout) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "recharge_layout1"));
		l2 = (LinearLayout) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "recharge_layout2"));
		l3 = (LinearLayout) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "recharge_layout3"));
		l4 = (LinearLayout) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "recharge_layout4"));

		userName = (TextView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "recharge_user_name"));
		rechargeTitle = (TextView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "tv_recharge_left"));
		balance = (TextView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "recharg_balance"));
		alipay = (ImageButton) findViewById(alipayId);
		rechargeInputNumber = (EditText) findViewById(ResourcesIdFactoryUtils
				.getId(getApplicationContext(), "recharge_input_number"));
		rechargeClose = (ImageButton) findViewById(rechargeCloseId);
		spinnerMoney = (Spinner) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "spinner_money"));
		yeepay = (ImageButton) findViewById(yeepayId);
		tenpay = (ImageButton) findViewById(tenpayId);
		chinaUnionpay = (ImageButton) findViewById(chinaUnionpayId);

		spinnerMoney.setOnItemSelectedListener(mSpinnerListener);
		rechargeClose.setOnClickListener(mOnClickListener);
		rechargeInputNumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable edt) {
				String temp = edt.toString();
				int posDot = temp.indexOf(".");
				if (posDot <= 0)
					return;
				if (temp.length() - posDot - 1 > 2) {
					edt.delete(posDot + 3, posDot + 4);
				}
			}
		});
		if (!WookiiSDKManager.isLogin(this)) {
			Toast.makeText(this, "请先登录", Toast.LENGTH_LONG).show();
			try {
				finish();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return;
		}
		uid = LoginMessageDataUtils.getUID(this);
		token = LoginMessageDataUtils.getToken(this);
		userNameStr = LoginMessageDataUtils.getTelNum(this);
		userName.setText("用户名称：" + userNameStr);
		token = Str_MD5.tokenToMD5(uid, token, "");

		alipay.setOnClickListener(mOnClickListener);
		yeepay.setOnClickListener(mOnClickListener);
		tenpay.setOnClickListener(mOnClickListener);
		chinaUnionpay.setOnClickListener(mOnClickListener);
	}

	/**
	 * 友游和游戏充值做不同的展示
	 * 
	 * @param gid
	 */
	private void isGameOrCP(String gid) {
		int bgId = ResourcesIdFactoryUtils.getDrawableId(
				getApplicationContext(), "recharge_bg");
		ll.setBackgroundResource(bgId);
		if (null != gid && gid.equals("cloud_point")) {
			// 当充值方式为友游充值
			getUserCoin();
			l1.setVisibility(View.VISIBLE);
			l2.setVisibility(View.VISIBLE);
			l3.setVisibility(View.VISIBLE);
			l4.setVisibility(View.VISIBLE);
		} else {
			// 游戏进入充值则不能选择充值金额
			rechargeTitle.setText("充值金额：");
			balanceStr = intent.getStringExtra("money");
			balance.setText(balanceStr);
			rechargeInputNumber.setText(balanceStr);
			l1.setVisibility(View.VISIBLE);
			l2.setVisibility(View.VISIBLE);
			l3.setVisibility(View.GONE);
			l4.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 获取用户金币
	 */
	private void getUserCoin() {
		if (null != CommonUtil.getInstance().getHandler()) {
			return;
		}
		mProgress = BaseHelper.showProgress(UserPayActivity.this, null,
				"正在查询余额", false, true);
		String token = LoginMessageDataUtils.getToken(this);
		String uid = LoginMessageDataUtils.getUID(this);
		String tokenToMD5 = Str_MD5.tokenToMD5(uid, token, "");
		WookiiSDKManager.getUserCoin(this, handler, USER_COIN, uid,
				tokenToMD5);
	}

	/**
	 * 处理按钮点击事件，支付按钮、关闭按钮等
	 * 
	 */
	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			// 关闭
			if (id == rechargeCloseId) {
				finish();
			} else if (id == alipayId) {
				// 支付宝支付
				String alipay = "支付宝";
				sendPayRequest(alipay, ALI_ORDER_INFO);
			} else if (id == yeepayId) {
				// 易宝支付接入
				// Toast.makeText(getApplicationContext(), "抱歉，易宝支付暂未接入！",
				// Toast.LENGTH_SHORT).show();
				// return;
				String yeepay = "易宝支付";
				sendPayRequest(yeepay, YEEPAY_REQUEST_RESULT);

			} else if (id == tenpayId) {
				// 财付通接入
				// sendPayRequest("财付通");
				Toast.makeText(getApplicationContext(), "抱歉，财付通支付暂未接入！",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (id == chinaUnionpayId) {
				// 银联支付
				sendPayRequest("银联", UNIONPAY_REQUEST_RESULT);
				// Toast.makeText(getApplicationContext(), "抱歉，银联支付暂未接入！",
				// Toast.LENGTH_SHORT).show();
			}
		}
	};

	/**
	 * 向服务器发送支付请求
	 * 
	 * @param pay_way
	 *            支付类型
	 */
	private void sendPayRequest(String pay_way, int what) {
		// 这里加入获取商品信息为支付宝提供支付的接口
		String phone_type = "android";
		closeProgress();
		mProgress = BaseHelper.showProgress(UserPayActivity.this, null,
				"正在生成订单", false, true);
		amountStr = rechargeInputNumber.getText().toString();
		UserPayAilPotocol potocol = new UserPayAilPotocol(what);
		potocol.sendRequest(uid, amountStr, pay_way, gid, userNameStr, handler,
				phone_type);
	}

	/**
	 * 金额选择下拉列表的监听
	 */
	private OnItemSelectedListener mSpinnerListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			int a = arg0.getCount();
			if (arg2 == (a - 1)) {
				rechargeInputNumber.setVisibility(View.VISIBLE);
			} else {
				rechargeInputNumber.setVisibility(View.GONE);
				String money = (String) (arg0.getSelectedItem());
				money = money.trim().substring(0, money.length() - 1);
				rechargeInputNumber.setText(money);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	};

	/**
	 * 使用支付宝支付
	 * 
	 * @param tradNo
	 *            服务器返回的订单号
	 */
	private void startSdkToPayForAil(String orderInfo) {
		// 检测安全支付服务是否被安装
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		boolean isMobileSpExist = mspHelper.detectMobile_sp();
		if (!isMobileSpExist) {
			return;
		} else {// 这里的sign生成是通过服务返回来的
			// 调用pay方法进行支付
			MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp.pay(orderInfo, handler, AlixId.RQF_PAY, this);
			/*
			 * if (bRet) { // show the progress bar to indicate that we have
			 * started // paying. // 显示“正在支付”进度条 closeProgress(); mProgress =
			 * BaseHelper.showProgress(this, null, "正在支付", false, true); }
			 */
		}

	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param signType
	 *            签名方式
	 * @param content
	 *            待签名订单信息
	 * @return
	 */
	private String sign(String signType, String content) {
		return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
	}

	/**
	 * 获取签名方式
	 * 
	 * @return
	 */
	private String getSignType() {
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}

	/**
	 * 关闭进度框
	 */
	private void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!isRecharged) {
			sentCommonUtilHandlerMsg(WookiiSDKManager.FAIL);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}

	/**
	 * 发送支付结果信息给支付扣费接口
	 * 
	 * @param ret
	 */
	private void sentCommonUtilHandlerMsg(int ret) {
		if (null != commonUtilHandler) {
			Message retMsg = commonUtilHandler
					.obtainMessage(WookiiSDKManager.PAY_REQUEST_CODE);
			retMsg.arg1 = ret;
			commonUtilHandler.sendMessage(retMsg);
			isRecharged = true;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		isGameOrCP(gid);
	}

}
