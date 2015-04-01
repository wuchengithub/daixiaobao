package com.daixiaobao.search;

import com.daixiaobao.greenrobot.AfterSales;
import com.daixiaobao.greenrobot.Attrb;
import com.daixiaobao.greenrobot.Brand;

public class AttributeBean {

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
	public AttributeBean(String token, String uid, String deviceId,
			String itemId) {
		super();
		this.token = token;
		this.uid = uid;
		this.deviceId = deviceId;
		this.itemId = itemId;
	}
	
	public AttributeBean() {
		
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public Group getData() {
		return data;
	}
	public void setData(Group data) {
		this.data = data;
	}


	private String message;
	private int errorCode;
	private Group data;
	public class Group {
		
		public Attrb[] getAttribute() {
			return attribute;
		}

		public void setAttribute(Attrb[] attribute) {
			this.attribute = attribute;
		}

		public Brand[] getBrands() {
			return brands;
		}

		public void setBrands(Brand[] brands) {
			this.brands = brands;
		}

		private Attrb[] attribute;
		private Brand[] brands;
		private AfterSales[] afterSales;
		
		public AfterSales[] getAfterSales() {
			return afterSales;
		}

		public void setAfterSales(AfterSales[] afterSales) {
			this.afterSales = afterSales;
		}

		
	}

}
