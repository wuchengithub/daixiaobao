package com.daixiaobao.center;

public class ResponseCenterMain {
	private int errorCode;
	private String message;
	
	private Detail detail;
	
	class Detail{
		private String nickName;
		private String valid;
		private String productLimit;
		private String proxyedCount;
		private String friendsLimit;
		private String userLoginId;
		private String imageUrl;
		public String getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		public String getNickName() {
			return nickName;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		
		public String getProductLimit() {
			return productLimit;
		}
		public void setProductLimit(String productLimit) {
			this.productLimit = productLimit;
		}
		public String getValid() {
			return valid;
		}
		public void setValid(String valid) {
			this.valid = valid;
		}
		public String getProxyedCount() {
			return proxyedCount;
		}
		public void setProxyedCount(String proxyedCount) {
			this.proxyedCount = proxyedCount;
		}
		
		
		public String getFriendsLimit() {
			return friendsLimit;
		}
		public void setFriendsLimit(String friendsLimit) {
			this.friendsLimit = friendsLimit;
		}
		public String getUserLoginId() {
			return userLoginId;
		}
		public void setUserLoginId(String userLoginId) {
			this.userLoginId = userLoginId;
		}
		
	}

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

	public Detail getDetail() {
		return detail;
	}

	public void setDetail(Detail detail) {
		this.detail = detail;
	}
	
	
}
