package com.daixiaobao.navigation;

import java.util.Map;

public class ResponseNavigationList {
	private int errorCode;
	private String message;
	private Group[] group;
	public class Group{
		private String imageUrl;
		private String imageCount;
		private String sellPrice;
		private String tel;
		private String siteUrl;
		private String weixinId;
		private String description;
		private String code;
		private String status;
		private String productCode;
		private String companyName;
		private String aftermarket;
		private String releaseDate;
		private String email;
		private String shareTotal;
		private String qq;
		private String isShare;
		private String[] images;
		private String brandName;
		private Map<String, String> attrs;
		private String address;
		
		
		
		public String getBrandName() {
			return brandName;
		}
		public void setBrandName(String brandName) {
			this.brandName = brandName;
		}
		public Map<String, String> getAttrs() {
			return attrs;
		}
		public void setAttrs(Map<String, String> attrs) {
			this.attrs = attrs;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String[] getImages() {
			return images;
		}
		public void setImages(String[] images) {
			this.images = images;
		}
		public String toMessageGroupString(){
			StringBuilder sb = new StringBuilder();
			sb/*.append("产品编号：").append(productCode)*/
			/*.append("\n商家名称：").append(company)*/
			.append(" \n微信号：").append(weixinId)
			.append(" \nQQ：").append(qq)
			.append(" \n电话：").append(tel)
			.append(" \nEmail：").append(email)
			/*.append(" \n地址：").append(address)*/;
			return sb.toString();
		}
		public String getAftermarket() {
			return aftermarket;
		}
		public void setAftermarket(String aftermarket) {
			this.aftermarket = aftermarket;
		}
	
		public String getSellPrice() {
			return sellPrice;
		}
		public void setSellPrice(String sellPrice) {
			this.sellPrice = sellPrice;
		}
		public String getTel() {
			return tel;
		}
		public void setTel(String tel) {
			this.tel = tel;
		}
		public String getSiteUrl() {
			return siteUrl;
		}
		public void setSiteUrl(String siteUrl) {
			this.siteUrl = siteUrl;
		}
		public String getWeixinId() {
			return weixinId;
		}
		public void setWeixinId(String weixinId) {
			this.weixinId = weixinId;
		}
		public String getReleaseDate() {
			return releaseDate;
		}
		public void setReleaseDate(String releaseDate) {
			this.releaseDate = releaseDate;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getShareTotal() {
			return shareTotal;
		}
		public void setShareTotal(String shareTotal) {
			this.shareTotal = shareTotal;
		}
		public String getQq() {
			return qq;
		}
		public void setQq(String qq) {
			this.qq = qq;
		}
		public String getIsShare() {
			return isShare;
		}
		public void setIsShare(String isShare) {
			this.isShare = isShare;
		}
		public String getCompanyName() {
			return companyName;
		}
		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}
		public String getProductCode() {
			return productCode;
		}
		public void setProductCode(String productCode) {
			this.productCode = productCode;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		public String getImageCount() {
			return imageCount;
		}
		public void setImageCount(String imageCount) {
			this.imageCount = imageCount;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
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

	public Group[] getGroup() {
		return group;
	}

	public void setGroup(Group[] group) {
		this.group = group;
	}

	
}
