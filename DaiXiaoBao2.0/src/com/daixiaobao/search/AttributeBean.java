package com.daixiaobao.search;

import java.util.List;

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
		
		private List<Attrb> attribute;
		private List<Brand> brands;
		private List<AfterSales> afterSales;
		public List<Attrb> getAttribute() {
			return attribute;
		}
		public void setAttribute(List<Attrb> attribute) {
			this.attribute = attribute;
		}
		public List<Brand> getBrands() {
			return brands;
		}
		public void setBrands(List<Brand> brands) {
			this.brands = brands;
		}
		public List<AfterSales> getAfterSales() {
			return afterSales;
		}
		public void setAfterSales(List<AfterSales> afterSales) {
			this.afterSales = afterSales;
		}
		
	}

}
