package com.daixiaobao.proxy.all;

public class RequestProxyAllBean {

	private String token;
	private String uid;
	private String deviceId;
	private String type;
	private String[] productIds;
	private int errorCode;
	private String messge;
	
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessge() {
		return messge;
	}
	public void setMessge(String messge) {
		this.messge = messge;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String[] getProductIds() {
		return productIds;
	}
	public void setProductIds(String[] productIds) {
		this.productIds = productIds;
	}
	public RequestProxyAllBean(String token, String uid, String deviceId,
			String type, String[] productIds) {
		super();
		this.token = token;
		this.uid = uid;
		this.deviceId = deviceId;
		this.type = type;
		this.productIds = productIds;
	}
	
	
}
