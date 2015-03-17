package com.daixiaobao.friend;

import com.daixiaobao.filter.SearchConfig;


public class RequestBusinessList {
	//private Context mContext;
	private String token;
	private String deviceId;
	private String userloginId;
	private String currentLength;
	private String count;

	public String getCurrentLength() {
		return currentLength;
	}
	public void setCurrentLength(String currentLength) {
		this.currentLength = currentLength;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getUserloginId() {
		return userloginId;
	}
	public void setUserloginId(String userloginId) {
		this.userloginId = userloginId;
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
	public RequestBusinessList(String token, String deviceId,
			String userloginId, String currentLength, String count) {
		super();
		this.token = token;
		this.deviceId = deviceId;
		this.userloginId = userloginId;
		this.currentLength = currentLength;
		this.count = count;
	}

	
	
	
	
}
