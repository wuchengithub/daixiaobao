package com.example.testpic;

import com.daixiaobao.filter.SearchConfig.SearchAttribute;

public class ChooiceBean {
	private String token;
	private String deviceId;
	private String categoryId;
	private String brandId;
	private String afterSalesId;
	private String content;
	private String price;
	private String prcie2;
	private String address;
	private SearchAttribute[] attrs;
	private boolean syncALJJ;
	
	public boolean getSyncALJJ() {
		return syncALJJ;
	}
	public String getAfterSalesId() {
		return afterSalesId;
	}
	public void setAfterSalesId(String afterSalesId) {
		this.afterSalesId = afterSalesId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPrcie2() {
		return prcie2;
	}
	public void setPrcie2(String prcie2) {
		this.prcie2 = prcie2;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public SearchAttribute[] getAttrs() {
		return attrs;
	}
	public void setAttrs(SearchAttribute[] attrs) {
		this.attrs = attrs;
	}
	public void setSyncALJJ(boolean syncALJJ) {
		// TODO Auto-generated method stub
		this.syncALJJ = syncALJJ;
	}
	
	
}
