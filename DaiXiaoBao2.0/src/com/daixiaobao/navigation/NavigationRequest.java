package com.daixiaobao.navigation;

import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.daixiaobao.protocol.MyBaseProtocol;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

public class NavigationRequest {

	
	private String id;

	public String getId() {
		return id;
	}
	public void request(Context context, Handler handler, String id) {
		this.id = id;
		CustomLoadingDialog.showProgress(context, null, "正在发送请求中", false, false);
		NavigationProtocol protocol = new NavigationProtocol();
		protocol.invoke(new NavigationBean(id, LoginMessageDataUtils.getToken(context), DeviceTool.getUniqueId(context)), 
				handler);
	}
	
	
	class NavigationProtocol extends MyBaseProtocol {

		@Override
		protected Class getClazz() {
			// TODO Auto-generated method stub
			return NavigationBean.class;
		}

		@Override
		protected String getMethodName() {
			// TODO Auto-generated method stub
			return "/admin/store/sort.htm";
		}
		
	}
	
	public class NavigationBean {
		private String parentId;
		private int isEnd;
		private String token;
		private String deviceId;
		private int errorCode;
		private String message;
		private List<Item> group;
		
		public List<Item> getGroup() {
			return group;
		}
		public void setGroup(List<Item> group) {
			this.group = group;
		}
		public class Item {
			private int isEnd;
			private String description;
			private long sortId;
			private String sortName;
			public int getIsEnd() {
				return isEnd;
			}
			public void setIsEnd(int isEnd) {
				this.isEnd = isEnd;
			}
			public String getDescription() {
				return description;
			}
			public void setDescription(String description) {
				this.description = description;
			}
			public long getSortId() {
				return sortId;
			}
			public void setSortId(long sortId) {
				this.sortId = sortId;
			}
			public String getSortName() {
				return sortName;
			}
			public void setSortName(String sortName) {
				this.sortName = sortName;
			}
			
		}
		public String getParentId() {
			return parentId;
		}
		public void setParentId(String parentId) {
			this.parentId = parentId;
		}
		public int getIsEnd() {
			return isEnd;
		}
		public void setIsEnd(int isEnd) {
			this.isEnd = isEnd;
		}
		public NavigationBean(String parentId, String token, String deviceId) {
			super();
			this.token = token;
			this.deviceId = deviceId;
			this.parentId = parentId;
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
		
		
	}
}
