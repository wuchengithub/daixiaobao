package com.daixiaobao.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daixiaobao.R;
import com.daixiaobao.friend.ResponseBusinessList.Data;
import com.daixiaobao.protocol.MyBaseProtocol;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

public class BusinessAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private Activity context;
	private ArrayList<Data> data;
	private FriendFragment friendFragment;
	protected int lastPosition = -1;
	private static Map<Integer, Boolean> remarkStatus;
	public BusinessAdapter(FriendFragment friendFragment, ArrayList<Data> data) {
		this.data = data;
		this.friendFragment = friendFragment;
		this.context = friendFragment.getActivity();
		this.layoutInflater = friendFragment.getActivity().getLayoutInflater();
		remarkStatus = new HashMap<Integer, Boolean>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return Long.parseLong(data.get(arg0).getStoreId());
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(
					R.layout.friend_supply_list_content, null);
			holder.id = (TextView) convertView
					.findViewById(R.id.apply_list_content_id);
			holder.signature = (TextView) convertView
					.findViewById(R.id.apply_list_content_message);
			holder.remark = (ImageView) convertView
					.findViewById(R.id.apply_list_content_remark);
			holder.edite = (EditText)convertView.findViewById(R.id.apply_list_content_edit_remark);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Data item = data.get(position);
		holder.id.setText(item.getUserLoginId());
		String remark = item.getRemark();
		String hintText;
		if(TextUtils.isEmpty(remark)){
			hintText = "添加签名";
			holder.signature.setText(hintText);
		} else {
			hintText = remark;
			holder.signature.setText("签名：" + hintText);
		}
		holder.edite.setHint(hintText);
		holder.edite.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				InputMethodManager inputMethodManager = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if(hasFocus) { 
					inputMethodManager.showSoftInput(v,
							InputMethodManager.SHOW_FORCED);
				} else {
					/*inputMethodManager.hideSoftInputFromWindow(
							v.getWindowToken(), 0);*/
				}
			}
		});
		
		holder.remark.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 将上一个item的状态复原
				if(lastPosition  != -1 && position != lastPosition) {
					remarkStatus.put(lastPosition, false);
				}
				//更改状态:备注修改中
				if(!remarkStatus.get(position)){
					remarkStatus.put(position, true);
					lastPosition = position;
					notifyDataSetChanged();
				}
				
			}
		});
		if(remarkStatus.get(position) == null ){
			remarkStatus.put(position, false);
		}
		//是true，状态:备注修改中
		if(remarkStatus.get(position)) {
			holder.edite.setVisibility(View.VISIBLE);
			holder.signature.setVisibility(View.INVISIBLE);
			holder.remark.setImageResource(R.drawable.ic_confirm_pressed);
			holder.edite.requestFocus();
		} else {
			holder.edite.setVisibility(View.GONE);
			holder.signature.setVisibility(View.VISIBLE);
			holder.remark.setImageResource(R.drawable.edit);
		}
		
		
		
		/*if (item.getStoreId().equals(LoginMessageDataUtils.getStoreId(context))) {
			holder.delete.setVisibility(View.INVISIBLE);
		} else {
			holder.delete.setVisibility(View.VISIBLE);
		}
		holder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Dialog dialog = null;
				ConformDialog.Builder customBuilder = new ConformDialog.Builder(
						context);
				customBuilder
						.setTitle("提示")
						.setMessage("是否要删除好友?")
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								})
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										request(item);
										dialog.dismiss();
									}
								});
				dialog = customBuilder.create();
				dialog.show();
			}
		});*/
		return convertView;
	}

	final class ViewHolder {

		public EditText edite;
		public ImageView remark;
		TextView id, signature;
		//Button delete;

	}

	private void requestRemark(final Data item, String remark) {
		RemarkProtocol protocol = new RemarkProtocol();
		protocol.invoke(new RemarkBean(LoginMessageDataUtils.getToken(context), 
				LoginMessageDataUtils.getUID(context), DeviceTool
				.getUniqueId(context), item.getUserId(), remark),
				new Handler(){
					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						super.handleMessage(msg);
					}
		});
	}
	private void requestDelete(final Data item) {
		CustomLoadingDialog
				.showProgress(context, null, "正在发送请求中", false, false);
		DeleteProtocol protocol = new DeleteProtocol();
		protocol.invoke(
				new Bean(LoginMessageDataUtils.getToken(context),
						LoginMessageDataUtils.getUID(context), DeviceTool
								.getUniqueId(context), item.getUserId()),
				new Handler() {
					@Override
					public void handleMessage(Message msg) {
						if (msg.obj != null) {
							Bean obj = (Bean) msg.obj;
							if (obj.getErrorCode().equals("0")) {
								friendFragment.getDataFromServer();
							} else if (obj.getErrorCode().equals("2")) {
							}
							Toast.makeText(context, obj.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
						CustomLoadingDialog.dismissDialog();
					}

				});
	}

	class RemarkProtocol extends MyBaseProtocol{

		@Override
		protected Class getClazz() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected String getMethodName() {
			// TODO Auto-generated method stub
			return "/admin/party/remark.htm";
		}
		
	}
	class DeleteProtocol extends MyBaseProtocol {

		@Override
		protected Class getClazz() {
			// TODO Auto-generated method stub
			return Bean.class;
		}

		@Override
		protected String getMethodName() {
			// TODO Auto-generated method stub
			return "/admin/seller/delete.htm";
		}

	}

	class RemarkBean {
		private String token;
		private String uid;
		private String deviceId;
		public String userId;
		private String errorCode;
		public String message;
		private String remark;
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
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getErrorCode() {
			return errorCode;
		}
		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public RemarkBean(String token, String uid, String deviceId,
				String userId, String remark) {
			super();
			this.token = token;
			this.uid = uid;
			this.deviceId = deviceId;
			this.userId = userId;
			this.remark = remark;
		}
		
	}
	class Bean {
		private String token;
		private String uid;
		private String deviceId;
		public String userId;
		private String errorCode;
		public String message;

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

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}


		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Bean(String token, String uid, String deviceId, String userId) {
			super();
			this.token = token;
			this.uid = uid;
			this.deviceId = deviceId;
			this.userId = userId;
		}

	}

	public String getBusinessName(int position) {

		return data.get(position).getUserLoginId();
	}
}
