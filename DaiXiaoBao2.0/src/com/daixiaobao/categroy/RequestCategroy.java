package com.daixiaobao.categroy;


public class RequestCategroy {
	private String token;
	private String uid;
	private String deviceId;
	private String itemId;
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
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public RequestCategroy(String token, String uid, String deviceId,
			String itemId) {
		super();
		this.token = token;
		this.uid = uid;
		this.deviceId = deviceId;
		this.itemId = itemId;
	}
	
	
}
