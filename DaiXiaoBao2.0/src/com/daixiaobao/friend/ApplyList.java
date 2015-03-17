package com.daixiaobao.friend;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.daixiaobao.R;
import com.daixiaobao.friend.BeanApplyList.Item;
import com.daixiaobao.protocol.MyBaseProtocol;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

public class ApplyList extends SherlockActivity {

	private ListView list;
	private ArrayList<Item> data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_apply_request_list);
		list = (ListView)findViewById(R.id.apply_request_list);
		request();
	}
	
	private void request() {
		CustomLoadingDialog.showProgress(ApplyList.this, null, "正在发送请求中", false, false);
		ApplyProtocol protocol = new ApplyProtocol();
		protocol.invoke(new BeanApplyList(LoginMessageDataUtils.getToken(this), 
				DeviceTool.getUniqueId(this)), new Handler(){
			

			@Override
			public void handleMessage(Message msg) {
				if(msg.obj != null) {
					BeanApplyList obj = (BeanApplyList)msg.obj;
					if(obj.getErrorCode() == 0){
						data = obj.getGroup();
						list.setAdapter(new ApplyListAdapter());
					} else  if (obj.getErrorCode() == 2) {
					}
					Toast.makeText(ApplyList.this, obj.getMessage(), Toast.LENGTH_SHORT).show();
				}
				CustomLoadingDialog.dismissDialog();
			}
		});
	}

	class ApplyProtocol extends MyBaseProtocol{

		@Override
		protected Class getClazz() {
			// TODO Auto-generated method stub
			return BeanApplyList.class;
		}

		@Override
		protected String getMethodName() {
			// TODO Auto-generated method stub
			return "/admin/seller/appls.htm";
		}
		
	}
	
	
	class ApplyListAdapter extends BaseAdapter {

		
		
		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = ApplyList.this.getLayoutInflater().inflate(
						R.layout.apply_list_content, null);
				holder.id = (TextView) convertView
						.findViewById(R.id.apply_list_content_id);
				holder.message = (TextView) convertView
						.findViewById(R.id.apply_list_content_message);
				holder.no = (Button) convertView.findViewById(R.id.apply_list_content_delete);
				holder.yes = (Button) convertView.findViewById(R.id.apply_list_content_yes);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final Item item = data.get(position);
			holder.id.setText("ID：" + item.getUserLoginId());
			holder.message.setText("验证消息：" + item.getLeaveMessage());
			holder.no.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CustomLoadingDialog.showProgress(ApplyList.this, null, "正在发送请求中", false, false);
					requestNo(item.getApplicantId());
				}
			});
			
			holder.yes.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CustomLoadingDialog.showProgress(ApplyList.this, null, "正在发送请求中", false, false);
					requestApply(item.getApplicantId());
				}
			});
			return convertView;
		}
		final class ViewHolder {

			TextView id,message;
			Button no,yes;

		}
		
		class YesProtocol extends MyBaseProtocol {

			@Override
			protected Class getClazz() {
				// TODO Auto-generated method stub
				return BeanRuqest.class;
			}

			@Override
			protected String getMethodName() {
				// TODO Auto-generated method stub
				return "/admin/seller/agree.htm";
			}
			
		}
		class NoProtocol extends MyBaseProtocol {

			@Override
			protected Class getClazz() {
				// TODO Auto-generated method stub
				return BeanRuqest.class;
			}

			@Override
			protected String getMethodName() {
				// TODO Auto-generated method stub
				return "/admin/seller/refusal.htm";
			}
			
		}
		class BeanRuqest {
			private String token;
			private String uid;
			private String deviceId;
			private String applicantId;
			private int errorCode;
			private String message;
			public String getToken() {
				return token;
			}
			public void setToken(String token) {
				this.token = token;
			}
			public String getUid() {
				return uid;
			}
			public void setUid(String uid) {
				this.uid = uid;
			}
			public String getDeviceId() {
				return deviceId;
			}
			public void setDeviceId(String deviceId) {
				this.deviceId = deviceId;
			}
			public String getApplicantId() {
				return applicantId;
			}
			public void setApplicantId(String applicantId) {
				this.applicantId = applicantId;
			}
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
			public BeanRuqest(String token, String uid, String deviceId,
					String applicantId) {
				super();
				this.token = token;
				this.uid = uid;
				this.deviceId = deviceId;
				this.applicantId = applicantId;
			}
			
			
		}
		
		public void requestApply(String applicantId){
			YesProtocol protocl = new YesProtocol();
			protocl.invoke(new BeanRuqest(LoginMessageDataUtils.getToken(ApplyList.this), 
					LoginMessageDataUtils.getUID(ApplyList.this), 
					DeviceTool.getUniqueId(ApplyList.this), 
					applicantId), handler);
		};
		
		public void requestNo(String applicantId){
			NoProtocol protocl = new NoProtocol();
			
			protocl.invoke(new BeanRuqest(LoginMessageDataUtils.getToken(ApplyList.this), 
					LoginMessageDataUtils.getUID(ApplyList.this), 
					DeviceTool.getUniqueId(ApplyList.this), 
					applicantId), handler);
		}
		
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.obj != null) {
					BeanRuqest obj = (BeanRuqest)msg.obj;
					if(obj.getErrorCode() == 0){
						finish();
					} else  if (obj.getErrorCode() == 2) {
					}
					Toast.makeText(ApplyList.this, obj.getMessage(), Toast.LENGTH_SHORT).show();
				}
				CustomLoadingDialog.dismissDialog();
			}
		};
	}
	
	
}
