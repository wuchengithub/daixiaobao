package com.daixiaobao.confirm;

public class ResponseDetailProduct {
	private int errorCode;
	private String message;
	private String company;
	private String qq;
	private String tel;
	private String email;
	private String address;
	private String price;
	private String description;
	private String[] imageUrls;
	private String proxyStatus;
	private String wx;

	public String toMessageGroupString(){
		StringBuilder sb = new StringBuilder();
		sb.append("商家名称：").append(company)
		.append(" \n微信号：").append(wx)
		.append(" \nQQ:").append(qq)
		.append(" \n电话：").append(tel)
		.append(" \nEmail:").append(email)
		.append(" \n地址：").append(address);
		return sb.toString();
	}
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String[] getImageUrls() {
		return imageUrls;
	}
	public void setImageUrls(String[] imageUrls) {
		this.imageUrls = imageUrls;
	}
	public String getProxyStatus() {
		return proxyStatus;
	}
	public void setProxyStatus(String proxyStatus) {
		this.proxyStatus = proxyStatus;
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

	public String getWx() {
		return wx;
	}

	public void setWx(String wx) {
		this.wx = wx;
	}
	

	
}
