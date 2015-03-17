package com.daixiaobao.navigation;

import com.daixiaobao.filter.SearchConfig;


public class RequestNavigationList {
	//private Context mContext;
	private String token;
	private String uid;
	private String deviceId;
	private String currentLength;
	private String count;
	private String order;
	private String sortId;

	public String getSortId() {
		return sortId;
	}
	public void setSortId(String sortId) {
		this.sortId = sortId;
	}

	private SearchConfig searchConfig;
	
	public SearchConfig getSearchConfig() {
		return searchConfig;
	}
	public void setSearchConfig(SearchConfig searchConfig) {
		this.searchConfig = searchConfig;
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

	public RequestNavigationList(String token, String uid, String deviceId,
			String currentLength, String count, String order, SearchConfig searchConfig, String sortId) {
		super();
		this.token = token;
		this.uid = uid;
		this.deviceId = deviceId;
		this.currentLength = currentLength;
		this.count = count;
		this.order = order;
		this.searchConfig = searchConfig;
		this.sortId = sortId;
	}
	
	
	
}
