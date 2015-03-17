package com.daixiaobao.proxy.list;

import com.daixiaobao.filter.SearchConfig;


public class RequestProxyList {
	//private Context mContext;
	private String token;
	private String uid;
	private String deviceId;
	private String currentLength;
	private String count;
	private String order;
	private String storeId;


	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	private SearchConfig searchConfig;
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

	public RequestProxyList(String token, String uid, String deviceId,
			String currentLength, String count, String order, SearchConfig searchConfig, String storeId) {
		super();
		this.token = token;
		this.uid = uid;
		this.deviceId = deviceId;
		this.currentLength = currentLength;
		this.count = count;
		this.order = order;
		this.searchConfig = searchConfig;
		this.storeId = storeId;
	}
	
	
	
}
