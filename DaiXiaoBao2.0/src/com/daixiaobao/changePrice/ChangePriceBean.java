package com.daixiaobao.changePrice;

public class ChangePriceBean {
	private String token;
	private String uid;
	private String deviceId;
	private String returns;
	private String[] productIds;
	private String transformType;// 加：plus，减：minus，乘：multiply，除：except）
	private String price;

	
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

	public String getReturns() {
		return returns;
	}

	public void setReturns(String returns) {
		this.returns = returns;
	}

	public String[] getProductIds() {
		return productIds;
	}

	public void setProductIds(String[] productIds) {
		this.productIds = productIds;
	}

	public String getTransformType() {
		return transformType;
	}

	public void setTransformType(String transformType) {
		this.transformType = transformType;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	private String errorCode;
	private String message;

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

	public ChangePriceBean(String token, String uid, String deviceId,
			String returns, String[] productIds, String transformType,
			String price) {
		super();
		this.token = token;
		this.uid = uid;
		this.deviceId = deviceId;
		this.returns = returns;
		this.productIds = productIds;
		this.transformType = transformType;
		this.price = price;
	}

}
