package com.daixiaobao.confirm;


public class RequestDetailProduct {
	//private Context mContext;
	private String token;
	private String uid;
	private String deviceId;
	private String code;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public RequestDetailProduct(String token, String uid, String deviceId,
			String code) {
		super();
		this.token = token;
		this.uid = uid;
		this.deviceId = deviceId;
		this.code = code;
	}
	
	
}
