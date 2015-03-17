package com.daixiaobao.concern;

import com.daixiaobao.filter.SearchConfig;


public class RequestMineConcern {
	//private Context mContext;
	private String token;
	private String storeId;
	private String deviceId;
	private String currentLength;
	private String count;
	private String order;

	private SearchConfig searchConfig;
	public RequestMineConcern(){
		
	}
	public SearchConfig getConfig() {
		return searchConfig;
	}
	public void setConfig(SearchConfig config) {
		this.searchConfig = config;
	}
	public String getCurrentLength() {
		return currentLength;
	}
	public void setCurrentLength(String currentLength) {
		this.currentLength = currentLength;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public RequestMineConcern(String token, String storeId, String deviceId,
			String currentLength, String count, String order, SearchConfig config) {
		super();
		this.token = token;
		this.storeId = storeId;
		this.deviceId = deviceId;
		this.currentLength = currentLength;
		this.count = count;
		this.order = order;
		this.searchConfig = config;
	}
	
	
	
}
