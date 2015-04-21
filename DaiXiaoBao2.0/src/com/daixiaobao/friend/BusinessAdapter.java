package com.daixiaobao.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daixiaobao.R;
import com.daixiaobao.friend.ResponseBusinessList.Data;
import com.daixiaobao.protocol.MyBaseProtocol;
import com.daixiaobao.proxy.FilterAndSearchActivity;
import com.daixiaobao.widget.ChangeMarkConformDialog;
import com.daixiaobao.widget.ConformDialog;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.daixiaobao.widget.SmoothMenu;
import com.daixiaobao.widget.SmoothMenu.OnSmoothMenuItemSelectListener;
import com.daixiaobao.widget.SmoothMenuItem;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

public class BusinessAdapter extends BaseAdapter {

	private static final String TAG = "BusinessAdapter";
	private LayoutInflater layoutInflater;
	private Activity context;
	private ArrayList<Data> data;
	private FriendFragment friendFragment;
	protected int lastPosition = -1;
	private static Map<Integer, String> remarkString;
	public BusinessAdapter(FriendFragment friendFragment, ArrayList<Data> data) {
		this.data = data;
		this.friendFragment = friendFragment;
		this.context = friendFragment.getActivity();
		this.layoutInflater = friendFragment.getActivity().getLayoutInflater();
		remarkString = new HashMap<Integer, String>();
		for (int i = 0; i < data.size() ; i++) {
			String remark = data.get(i).getRemark();
			if(!TextUtils.isEmpty(remark)) {
				remarkString.put(i, remark);
			}
		}
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
			SmoothMenu smoothMenu = new SmoothMenu(context);
			View contentView = layoutInflater.inflate(
					R.layout.friend_supply_list_content, null);
			smoothMenu.setContentView(contentView);
			convertView = smoothMenu;
			holder.id = (TextView) convertView
					.findViewById(R.id.apply_list_content_id);
			holder.remark = (ImageView) convertView
					.findViewById(R.id.apply_list_content_remark);
			holder.edite = (TextView)convertView.findViewById(R.id.apply_list_content_edit_remark);
			holder.smoothMenu = smoothMenu;
			holder.goin = convertView.findViewById(R.id.apply_list_content_goin);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Data item = data.get(position);
		holder.id.setText(item.getUserLoginId());
		String remark = remarkString.get(position);
		if(TextUtils.isEmpty(remark)){
			holder.edite.setText("添加签名");
		} else {
			holder.edite.setText(remark);
		}
		holder.remark.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeMark(context, position, item.getUserId(), item.getRemark());
			}
		});
		holder.goin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int storeId = (int) getItemId(position);
				String businessName = getBusinessName(position);
				Intent intent = new Intent(context, FilterAndSearchActivity.class);
				intent.putExtra("storeId", storeId);
				intent.putExtra("businessName", businessName);
				((Activity) context).startActivity(intent);
			}
		});
		holder.smoothMenu.setOnSmoothMenuItemSelected(new OnSmoothMenuItemSelectListener() {
			
			@Override
			public void onSmoothMenuItemSelected(int type) {
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
										requestDelete(item);
										dialog.dismiss();
									}
								});
				dialog = customBuilder.create();
				dialog.show();
			}
		});
		return convertView;
	}

	final class ViewHolder {

		public View goin;
		public SmoothMenu smoothMenu;
		public TextView edite;
		public ImageView remark;
		TextView id;

	}
	public void changeMark(final Context context, final int position, final String id, final String mark) {
		Dialog dialog = null;
		final ChangeMarkConformDialog.Builder customBuilder = new ChangeMarkConformDialog.Builder(
				context);
		customBuilder.setMessage(mark)
		.setPositiveButton("备注", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case Dialog.BUTTON_NEGATIVE:
							break;
						case Dialog.BUTTON_POSITIVE:
							String remark = customBuilder.getRemark();
							remarkString.put(position, remark); 
							notifyDataSetChanged();
							requestRemark(id, remark);
							break;
						default:
							break;
						}
						dialog.dismiss();
					}

				});
		dialog = customBuilder.create();
		dialog.show();
	}
	
	private void requestRemark(final String id, String remark) {
		RemarkProtocol protocol = new RemarkProtocol();
		protocol.invoke(new RemarkBean(LoginMessageDataUtils.getToken(context), 
				LoginMessageDataUtils.getUID(context), DeviceTool
				.getUniqueId(context), id, remark),
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
			return RemarkBean.class;
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
