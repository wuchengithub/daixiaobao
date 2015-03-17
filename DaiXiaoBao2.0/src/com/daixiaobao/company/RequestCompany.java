package com.daixiaobao.company;


public class RequestCompany {
	//private Context mContext;
	private String token;
	private String uid;
	private String deviceId;
	private String currentLength;
	private String count;
	private String order;
	
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
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
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
	public RequestCompany(String token, String uid, String deviceId,
			String currentLength, String count, String order) {
		super();
		this.token = token;
		this.uid = uid;
		this.deviceId = deviceId;
		this.currentLength = currentLength;
		this.count = count;
		this.order = order;
	}
	
	
	
	
}
