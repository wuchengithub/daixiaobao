package com.daixiaobao.friend;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.daixiaobao.CommonUtil;
import com.daixiaobao.R;
import com.daixiaobao.friend.ResponseBusinessList.Data;
import com.daixiaobao.protocol.MyBaseProtocol;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SupplierActivity extends SherlockFragmentActivity {

	private TextView name;
	private TextView message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_supplier_activity);
		name = (TextView)findViewById(R.id.add_supplier_name);
		message = (TextView)findViewById(R.id.add_supplier_message);
		findViewById(R.id.add_supplier_send).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				if(TextUtils.isEmpty(name.getText().toString())){
					name.setFocusable(true);
					name.setError("供应商名不能为空");
					return;
				}
				CustomLoadingDialog.showProgress(SupplierActivity.this, null, "正在发送请求中", false, false);
				BeanAddSuplier bean = new BeanAddSuplier(LoginMessageDataUtils.getToken(SupplierActivity.this), 
						DeviceTool.getDeviceId(SupplierActivity.this), name.getText().toString(), 
						message.getText().toString());
				AddSuplierProtocol protocol = new AddSuplierProtocol();
				protocol.invoke(bean, new Handler(){
					@Override
					public void handleMessage(Message msg) {
						if(msg.obj != null) {
							BeanAddSuplier obj = (BeanAddSuplier)msg.obj;
							if(obj.getErrorCode() == 0){
								finish();
							} else  if (obj.getErrorCode() == 2) {
							}
							Toast.makeText(SupplierActivity.this, obj.getMessage(), Toast.LENGTH_SHORT).show();
						}
						CustomLoadingDialog.dismissDialog();
					}
				});
			}
		});
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	class AddSuplierProtocol extends MyBaseProtocol{

		@Override
		protected Class getClazz() {
			// TODO Auto-generated method stub
			return BeanAddSuplier.class;
		}

		@Override
		protected String getMethodName() {
			// TODO Auto-generated method stub
			return "/admin/seller/apply.htm";
		}
		
	}
	
	class BeanAddSuplier {
		private String token;
		private String deviceId;
		private String userLoginId;
		private String leaveMessage;
		
		private int errorCode;
		private String message;
		
		
		public int getErrorCode() {
			return errorCode;
		}
		public void setErrorCode(int errorCode) {
			this.errorCode = errorCode;
		}
		
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getDeviceId() {
			return deviceId;
		}
		public void setDeviceId(String deviceId) {
			this.deviceId = deviceId;
		}
		public String getUserLoginId() {
			return userLoginId;
		}
		public void setUserLoginId(String userLoginId) {
			this.userLoginId = userLoginId;
		}
		public String getLeaveMessage() {
			return leaveMessage;
		}
		public void setLeaveMessage(String leaveMessage) {
			this.leaveMessage = leaveMessage;
		}
		public BeanAddSuplier(String token, String deviceId,
				String userLoginId, String leaveMessage) {
			super();
			this.token = token;
			this.deviceId = deviceId;
			this.userLoginId = userLoginId;
			this.leaveMessage = leaveMessage;
		}
		
		
	}
	
}
