package com.daixiaobao.center;

public class RequestCenterMain {
	private String token;
	private String uid;
	private String deviceId;
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
	public RequestCenterMain(String token, String uid, String deviceId) {
		super();
		this.token = token;
		this.uid = uid;
		this.deviceId = deviceId;
	}
	
	
}
