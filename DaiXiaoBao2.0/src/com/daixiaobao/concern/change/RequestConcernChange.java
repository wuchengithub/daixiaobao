package com.daixiaobao.concern.change;

public class RequestConcernChange {
	// private Context mContext;
	private String token;
	private String uid;
	private String deviceId;
	private String productIds;
	private String price;
	private String desc;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getProductIds() {
		return productIds;
	}

	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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

	public RequestConcernChange(String token, String uid, String deviceId,
			String productIds, String price, String desc) {
		super();
		this.desc = desc;
		this.token = token;
		this.uid = uid;
		this.deviceId = deviceId;
		this.productIds = productIds;
		this.price = price;
	}

}
