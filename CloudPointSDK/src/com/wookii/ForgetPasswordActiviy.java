package com.wookii;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.CountDownTimerUtil;
import com.wookii.utils.LoginMessageDataUtils;

public class ForgetPasswordActiviy extends Activity {

	private ProtocolManager dataManager;
	private LoginAndRegActivity mParentActivity;

	private ImageView mBackView;

	private EditText mPhoneNumberET;
	private EditText mTokenET;
	private EditText mNewPasswordET;

	private Button mGetTokenButton;
	private Button mSubmitButton;

	private ProgressDialog mDialog;

	private String mPhoneNumber;

	private String toastInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		int layoutId = ResourcesIdFactoryUtils.getLayoutId(
				getApplicationContext(), "cpsdk_forget_password_layout");
		setContentView(layoutId);

		dataManager = new ProtocolManager(this, mHandler);

		mBackView = (ImageView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "back"));
		mBackView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mPhoneNumberET = (EditText) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "input_phone_number"));
		mTokenET = (EditText) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "input_token"));
		mNewPasswordET = (EditText) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "input_new_password"));

		mGetTokenButton = (Button) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "button_get_token"));
		mGetTokenButton.setOnClickListener(mOnGetTokenClickListener);

		mSubmitButton = (Button) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "button_submit"));
		mSubmitButton.setOnClickListener(mOnSubmitClickListener);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dismissDialog();
			switch (msg.what) {

			case ProtocolManager.NOTIFICATION_SUBMIT_GETCODE:
				toastInfo = msg.obj.toString();
				showToast("验证码已经发至您的手机，请查收。");
				break;
			case ProtocolManager.NOTIFICATION_FORGET_PASSWORD:
				showToast("密码设置成功");
				finish();
				break;
			case ProtocolManager.NETWORK_ERROR:
				showToast("网络异常");
				break;
			case ProtocolManager.NOTIFICATION_RESPONSE_ERRORINFO:
				showToast(msg.obj.toString());
				break;
			case CountDownTimerUtil.TIMER_FINISH:
				mGetTokenButton.setEnabled(true);
				mGetTokenButton.setText("获取验证码");
				break;
			case CountDownTimerUtil.TIMER_TICK:
				mGetTokenButton.setEnabled(false);
				mGetTokenButton.setText("获取验证码(" + msg.arg1 + ")");
				break;
			}
		}
	};

	private OnClickListener mOnGetTokenClickListener = new View.OnClickListener() {
		CountDownTimerUtil countDownTimer;

		@Override
		public void onClick(View v) {
			String phoneNumber = mPhoneNumberET.getText().toString().trim();

			if (TextUtils.isEmpty(phoneNumber)) {
				showToast("电话号码不能为空");
			} else if (11 < phoneNumber.length()) {
				showToast("手机号位数大于11位，请确认手机号");
			} else if (11 > phoneNumber.length()) {
				showToast("手机号位数不足11位，请确认手机号");
			}

			else {
				// 倒计时
				if (null == countDownTimer) {
					countDownTimer = new CountDownTimerUtil(mHandler,
							60 * 1000, 1000);
				}
				countDownTimer.start();

				mPhoneNumber = phoneNumber;
				showDialog("正在获取验证码...");
				String uid = LoginMessageDataUtils
						.getUID(ForgetPasswordActiviy.this);
				dataManager.userRegisterTel(phoneNumber, "0");// 服务端要求找回密码传值ID为0
			}
		}
	};

	/**
	 * 倒计时 class CountDownTimerUtil extends CountDownTimer {
	 * 
	 * public CountDownTimerUtil(long millisInFuture, long countDownInterval) {
	 * super(millisInFuture, countDownInterval); }
	 * 
	 * @Override public void onFinish() { // TODO Auto-generated method stub
	 *           mGetTokenButton.setEnabled(true);
	 *           mGetTokenButton.setText("获取验证码"); }
	 * @Override public void onTick(long millisUntilFinished) { // TODO
	 *           Auto-generated method stub mGetTokenButton.setEnabled(false);
	 *           mGetTokenButton .setText("获取验证码(" + millisUntilFinished / 1000
	 *           + ")"); } }
	 */
	private OnClickListener mOnSubmitClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			String validateCode = mTokenET.getText().toString().trim();
			String newpwd = mNewPasswordET.getText().toString().trim();

			if (TextUtils.isEmpty(validateCode)) {
				showToast("验证码不能为空");
			} else if (4 < validateCode.length()) {
				showToast("验证码不能大于4位");
			} else if (TextUtils.isEmpty(newpwd)) {
				showToast("密码不能为空");
			} else if (6 > newpwd.length() || 16 < newpwd.length()) {
				showToast("密码长度不正确，请输入6-16位密码");
			} else {
				showDialog("处理中...");
				dataManager.resetPassword(mPhoneNumber, newpwd, validateCode);
			}
		}
	};

	private void showDialog(String text) {
		if (mDialog == null) {
			mDialog = ProgressDialog.show(ForgetPasswordActiviy.this, "", text,
					true, true);
		} else {
			mDialog.show();
		}
	}

	private void dismissDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	private void showToast(String text) {
		Toast.makeText(ForgetPasswordActiviy.this, text, Toast.LENGTH_SHORT)
				.show();
	}

}
