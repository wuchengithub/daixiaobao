package com.daixiaobao.friend;

public class ResponseBusinessList {

	private int errorCode;
	private String message;
	private Data[] datas;
	
	
	public int getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public Data[] getDatas() {
		return datas;
	}


	public void setDatas(Data[] datas) {
		this.datas = datas;
	}


	public class Data{
		private String userId;
		private String storeId;
		private String userLoginId;
		private String signature;
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getStoreId() {
			return storeId;
		}
		public void setStoreId(String storeId) {
			this.storeId = storeId;
		}
		public String getUserLoginId() {
			return userLoginId;
		}
		public void setUserLoginId(String userLoginId) {
			this.userLoginId = userLoginId;
		}
		public String getSignature() {
			if("null".equals(signature)) return "";
			return signature;
		}
		public void setSignature(String signature) {
			this.signature = signature;
		}
		
		
	}
}
