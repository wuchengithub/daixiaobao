package com.daixiaobao.version;


public class RequestVreiosn {
	//private Context mContext;
	private String versionCode;
	private String uid;
	private String deviceId;
	private String type;
	
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public RequestVreiosn(String versionCode, String uid, String deviceId,
			String type) {
		super();
		this.versionCode = versionCode;
		this.uid = uid;
		this.deviceId = deviceId;
		this.type = type;
	}
	
	
	
}
