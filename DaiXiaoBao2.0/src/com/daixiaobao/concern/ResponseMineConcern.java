package com.daixiaobao.concern;

import java.util.List;
import java.util.Map;

public class ResponseMineConcern {
	private int errorCode;
	private String message;
	private Group[] group;
	public class Group{
		private String imageUrl;
		private String imageCount;
		private String price;
		private String description;
		private String code;
		private String status;
		private String productCode;
		private String companyName;
		private String afterSales;
		private String createTime;
		private String isShare;
		private String[] images;
		private String sellPrice;
		private String releaseDate;
		private String aftermarket;
		private String weixinId;
		private String qq;
		private String email;
		private String tel;
		private String agentPrice;
		private String address;
		private Map<String,String> attrs;
		private String brandName;
		
		
		
		public String getBrandName() {
			return brandName;
		}
		public void setBrandName(String brandName) {
			this.brandName = brandName;
		}
		public Map<String,String> getAttrs() {
			return attrs;
		}
		public void setAttrs(Map<String,String> attrs) {
			this.attrs = attrs;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getAgentPrice() {
			return agentPrice;
		}
		public void setAgentPrice(String agentPrice) {
			this.agentPrice = agentPrice;
		}
		public String getSellPrice() {
			return sellPrice;
		}
		public void setSellPrice(String sellPrice) {
			this.sellPrice = sellPrice;
		}
		public String getReleaseDate() {
			return releaseDate;
		}
		public void setReleaseDate(String releaseDate) {
			this.releaseDate = releaseDate;
		}
		public String getAftermarket() {
			return aftermarket;
		}
		public void setAftermarket(String aftermarket) {
			this.aftermarket = aftermarket;
		}
		public String getWeixinId() {
			return weixinId;
		}
		public void setWeixinId(String weixinId) {
			this.weixinId = weixinId;
		}
		public String getQq() {
			return qq;
		}
		public void setQq(String qq) {
			this.qq = qq;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getTel() {
			return tel;
		}
		public void setTel(String tel) {
			this.tel = tel;
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
		public void setImages(String[] images) {
			this.images = images;
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
		public String getAfterSales() {
			return afterSales;
		}
		public void setAfterSales(String afterSales) {
			this.afterSales = afterSales;
		}
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
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
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
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
		public String[] getImages() {
			// TODO Auto-generated method stub
			return images;
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
