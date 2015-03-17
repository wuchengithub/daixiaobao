package com.wookii;

import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.LoginMessageDataUtils;
import com.wookii.utils.Str_MD5;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GetMoneyActivity extends Activity {

	public final static int GetMoney_RESULT_CODE = 8;
	String toastInfo;
	private ProgressDialog pd;
	ProtocolManager dataManager;

	LinearLayout getMoneyLay;
	Button btnId_for_submit_money;

	String otherCode = "";

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ProtocolManager.NOTIFICATION:
				if (pd != null) {
					if (pd.isShowing()) {
						pd.dismiss();
					}
				}
				toastInfo = msg.obj.toString();

				Intent intent = new Intent();
				intent.putExtra("JSON", toastInfo);
				setResult(GetMoney_RESULT_CODE, intent);
				Toast.makeText(GetMoneyActivity.this, toastInfo,
						Toast.LENGTH_LONG).show();
				finish();
				break;
			case ProtocolManager.NETWORK_ERROR:
				if (pd != null) {
					if (pd.isShowing()) {
						pd.dismiss();
					}
				}
				break;

			case ProtocolManager.TIMEOUT_ERROR:

				break;
			case ProtocolManager.NOTIFICATION_RESPONSE_ERRORINFO:
				if (pd != null) {
					if (pd.isShowing()) {
						pd.dismiss();
					}
				}

				break;
			case ProtocolManager.NOTIFICATION_NOTREGISTERLOGIN:
				if (pd != null) {
					if (pd.isShowing()) {
						pd.dismiss();
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setContentView(R.layout.getmoney);
		findView();
		setContentView(getMoneyLay);
		dataManager = new ProtocolManager(GetMoneyActivity.this, mHandler);
		btnId_for_submit_money
				.setOnClickListener(new BtnId_for_submit_moneyListener());
	}

	private void findView() {
		// btnId_for_submit_money = (Button)
		// this.findViewById(R.id.btnId_for_submit_money);

		getMoneyLay = new LinearLayout(this);

		btnId_for_submit_money = new Button(this);
		btnId_for_submit_money.setText("��ȡ���");
		getMoneyLay.addView(btnId_for_submit_money);
	}

	class BtnId_for_submit_moneyListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String uid = LoginMessageDataUtils.getUID(GetMoneyActivity.this);
			String token = LoginMessageDataUtils
					.getToken(GetMoneyActivity.this);
			token = Str_MD5.tokenToMD5(uid, token, otherCode);

			dataManager.getMoney(token, uid);
			if (pd == null) {
				pd = ProgressDialog.show(GetMoneyActivity.this, "", "���Ե�...",
						true, true);
			} else {
				pd.show();
			}
		}
	}
}
