package com.wookii;

import java.util.HashMap;

import com.wookii.alipay.BaseHelper;
import com.wookii.model.LoginRequest;
import com.wookii.model.UserInfo;
import com.wookii.protocol.LoginProtocol;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.CountDownTimerUtil;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment {
	public final static int Login_RESULT_CODE = 3;

	private LoginAndRegActivity mParentActivity;
	private Intent intent;

	private EditText mUserNameET, mPasswordET, mTokenET;
	private Button mLoginButton, mLoginToReg, mButtonGetToken;

	private String toastInfo;
	private ProgressDialog mDialog;
	private ProtocolManager dataManager;

	private String userName;
	private String pwd;
	private RelativeLayout registerTel;
	private CountDownTimerUtil countDownTimer;
	private static final int REGISTER_DEVICE = 0x123;
	private String LOGIN_FROM_CP = "0";

	public static LoginFragment newInstance() {
		return new LoginFragment();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dismissDialog();
			switch (msg.what) {
			case ProtocolManager.LOGIN_SUCCESS:
				//LoginMessageDataUtils.insertInterimToken(mParentActivity, true);
				UserInfo userInfo = (UserInfo) msg.obj;
				LoginMessageDataUtils.insertUID(mParentActivity,
						userInfo.getUid());
				LoginMessageDataUtils.insertToken(mParentActivity,
						userInfo.getToken());
				LoginMessageDataUtils.insertStoreId(mParentActivity, userInfo.getStoreId());
				LoginMessageDataUtils.setIsVip(mParentActivity, userInfo.getIsVip());
				intent.putExtra("JSON", toastInfo);
				mParentActivity.setResult(mParentActivity.RESULT_OK, intent);
				showToast("登录成功");
				mParentActivity.finish();
				break;
			case ProtocolManager.TIMEOUT_ERROR:
				// 帐号异常登录
				registerTel.setVisibility(View.VISIBLE);
//				String[] ret = (String[]) (msg.obj);
//				userName = ret[0];
//				mUserNameET.setText(userName);
				showToast(msg.obj.toString());
				break;
			case ProtocolManager.NETWORK_ERROR:
				showToast("网络错误");
				break;
			case ProtocolManager.NOTIFICATION_RESPONSE_ERRORINFO:
				try {
					showToast(msg.obj.toString());
				} catch (Exception e) {
					showToast("");
				}
				break;
			case REGISTER_DEVICE:
				LoginMessageDataUtils.insertInterimToken(mParentActivity, false);
				HashMap<String, String> dataMap = (HashMap<String, String>) msg.obj;
				String uid = dataMap.get("uid");
//				String token = dataMap.get("token");
				LoginMessageDataUtils.insertUID(mParentActivity, uid);
//				LoginMessageDataUtils.insertToken(mParentActivity, token);

				dataManager.userRegisterTel(userName, "0");
				mDialog = ProgressDialog.show(mParentActivity, "",
						"正在获取验证码...", true, true);
				break;
			case ProtocolManager.NOTIFICATION_SUBMIT_GETCODE:
				if (mDialog != null) {
					if (mDialog.isShowing()) {
						mDialog.dismiss();
					}
				}
				toastInfo = msg.obj.toString();
				// userRegisterTelActivity.insertYZM_CODE(toastInfo);
				mParentActivity.showToast("验证码已经发至您的手机，请查收。");
				// 倒计时
				if (null == countDownTimer) {
					countDownTimer = new CountDownTimerUtil(mHandler,
							60 * 1000, 1000);
				}
				countDownTimer.start();
				break;
			case CountDownTimerUtil.TIMER_FINISH:
				mButtonGetToken.setEnabled(true);
				mButtonGetToken.setText("获取验证码");
				break;
			case CountDownTimerUtil.TIMER_TICK:
				mButtonGetToken.setEnabled(false);
				mButtonGetToken.setText("获取验证码(" + msg.arg1 + ")");
				break;
			default:
				super.handleMessage(msg);
				break;
			}

		}
	};

	private TextView findPassword;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mParentActivity = (LoginAndRegActivity) activity;
		intent = mParentActivity.getIntent();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int layoutId = ResourcesIdFactoryUtils.getLayoutId(
				mParentActivity.getApplicationContext(), "cpsdk_login");
		View view = inflater.inflate(layoutId, container, false);

		mUserNameET = (EditText) view.findViewById(ResourcesIdFactoryUtils
				.getId(mParentActivity.getApplicationContext(),
						"EditId_for_userName"));
		String uid = LoginMessageDataUtils.getUID(getActivity());
		if(uid != null && !"".equals(uid)){
			mUserNameET.setText(uid);
		}
		mPasswordET = (EditText) view.findViewById(ResourcesIdFactoryUtils
				.getId(mParentActivity.getApplicationContext(),
						"EditId_for_pwd"));

		findPassword = (TextView) view.findViewById(ResourcesIdFactoryUtils
				.getId(mParentActivity.getApplicationContext(),
						"btnId_for_find_password"));
		registerTel = (RelativeLayout) view
				.findViewById(ResourcesIdFactoryUtils.getId(
						mParentActivity.getApplicationContext(),
						"register_tel_layout"));
		mTokenET = (EditText) view.findViewById(ResourcesIdFactoryUtils.getId(
				mParentActivity.getApplicationContext(), "input_token"));
		mButtonGetToken = (Button) view.findViewById(ResourcesIdFactoryUtils
				.getId(mParentActivity.getApplicationContext(),
						"get_token_button"));
		mButtonGetToken.setOnClickListener(mOnGetTokenClickListener);

		findPassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WookiiSDKManager.openFindPasswordView(mParentActivity,
						WookiiSDKManager.FIND_PASSWORD_REQUEST_CODE);
			}
		});

		mLoginButton = (Button) view.findViewById(ResourcesIdFactoryUtils
				.getId(mParentActivity.getApplicationContext(),
						"btnId_for_submit_login"));
		mLoginButton.setOnClickListener(mLoginButtonClickListener);
		return view;
	}

	private View.OnClickListener mLoginButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			userName = mUserNameET.getText().toString().trim();
			pwd = mPasswordET.getText().toString().trim();

			if (TextUtils.isEmpty(userName)) {
				mUserNameET.setError("用户名不能为空");
			} else if (TextUtils.isEmpty(pwd)) {
				showToast("密码不能为空");
			} else if (16 < pwd.length() || 5 > pwd.length()) {
				showToast("密码长度不正确，请输入6-16位密码");
			} else {
				
				LoginProtocol loginProtocol = new LoginProtocol();
				mDialog = BaseHelper.showProgress(mParentActivity, null,
						"正在登录", false, true);
				// first_login 可选 0：非首次登录 1：首次登录 默认值为 0
				loginProtocol.invokeLogin(new LoginRequest(DeviceTool.getUniqueId(mParentActivity), userName, pwd), mHandler);
			}
		}
	};
	private OnClickListener mOnGetTokenClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TelephonyManager tm = (TelephonyManager)
			// mParentActivity.getSystemService(mParentActivity.TELEPHONY_SERVICE);
			userName = mUserNameET.getText().toString().trim();

			if (TextUtils.isEmpty(userName)) {
				toastInfo = "手机号码不能为空";
				Toast.makeText(mParentActivity, toastInfo, Toast.LENGTH_LONG)
						.show();
			} else {
				// 先注册设备
				if (DeviceTool.isNetworkAvailable(mParentActivity)) {
					WookiiSDKManager
							.registerDevice(getActivity(), DeviceTool
									.getUniqueId(getActivity()), DeviceTool
									.getSubscriberId(getActivity()), DeviceTool
									.getChannelCode(getActivity(), "CHANNEL"),
									mHandler, REGISTER_DEVICE);
				} else {
					Toast.makeText(mParentActivity, "网络不可用，请检查网络！",
							Toast.LENGTH_LONG).show();
				}

			}

		}
	};

	/*
	 * private void showDialog(String text) { if (mDialog == null) { mDialog =
	 * ProgressDialog .show(mParentActivity, "", text, true, true); } else {
	 * mDialog.show(); } }
	 */

	private void dismissDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	private void showToast(String text) {
		Toast.makeText(mParentActivity, text, Toast.LENGTH_SHORT).show();
	}
	
}
