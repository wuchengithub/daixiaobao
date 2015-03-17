package com.wookii;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.LoginMessageDataUtils;
import com.wookii.utils.Str_MD5;

public class ModifyPasswordActivity extends Activity {

	private ProtocolManager dataManager;

	private RelativeLayout mBackView;

	private EditText mOldPasswordET;
	private EditText mNewPasswordET;
	private EditText mConfirmPasswordET;
	private Button mSubmitButton;

	private ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		int layoutId = ResourcesIdFactoryUtils.getLayoutId(
				getApplicationContext(), "cpsdk_modify_password_layout");
		setContentView(layoutId);

		dataManager = new ProtocolManager(this, mHandler);

		mBackView = (RelativeLayout) findViewById(ResourcesIdFactoryUtils
				.getId(getApplicationContext(), "modify_password_head"));
		mBackView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mOldPasswordET = (EditText) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "input_old_password"));
		mNewPasswordET = (EditText) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "input_new_password"));
		mConfirmPasswordET = (EditText) findViewById(ResourcesIdFactoryUtils
				.getId(getApplicationContext(), "confirm_new_password"));

		mSubmitButton = (Button) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "submit"));
		mSubmitButton.setOnClickListener(mOnSubmitListener);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dismissDialog();
			switch (msg.what) {
			case ProtocolManager.NOTIFICATION_MODIFY_PASSWORD:
				showToast("密码设置成功");
				finish();
				break;
			case ProtocolManager.NETWORK_ERROR:
				showToast("网络成功");
				break;
			case ProtocolManager.NOTIFICATION_RESPONSE_ERRORINFO:
				showToast(msg.obj.toString());
				break;
			}
		}
	};

	private View.OnClickListener mOnSubmitListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			String oldPwd = mOldPasswordET.getText().toString();
			String newPwd = mNewPasswordET.getText().toString();
			String confirmPwd = mConfirmPasswordET.getText().toString();

			// String YZMCode2 = userRegisterTelActivity.getYZM_CODE();
			if (TextUtils.isEmpty(oldPwd)) {
				showToast("旧密码不能为空");
			} else if (16 < oldPwd.length() || 6 > oldPwd.length()) {
				showToast("旧密码不能小于6位或大于16位");
			} else if (TextUtils.isEmpty(newPwd)) {
				showToast("新密码不能为空");
			} else if (16 < newPwd.length() || 6 > newPwd.length()) {
				showToast("新密码不能小于6位或大于16位");
			} else if (TextUtils.isEmpty(confirmPwd)) {
				showToast("再次输入新密码不能为空");
			} else if (!newPwd.equals(confirmPwd)) {
				showToast("两次输入密码不一致");
			} else if (newPwd.equals(oldPwd)) {
				showToast("请设置跟旧密码不同的新密码");
			} else {
				String uid = LoginMessageDataUtils
						.getUID(ModifyPasswordActivity.this);
				String token = LoginMessageDataUtils
						.getToken(ModifyPasswordActivity.this);
				dataManager.modifyPassword(uid, oldPwd, newPwd, token);
				showDialog("处理中...");
			}

		}
	};

	private void showDialog(String text) {
		if (mDialog == null) {
			mDialog = ProgressDialog.show(ModifyPasswordActivity.this, "",
					text, true, true);
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
		Toast.makeText(ModifyPasswordActivity.this, text, Toast.LENGTH_LONG)
				.show();
	}

}
