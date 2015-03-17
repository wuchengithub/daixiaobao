package com.daixiaobao.detailCompany;

public class ResponseDetailCompany {
	private int errorCode;
	private String message;
	private String company;
	private String companyId;
	private String contact;
	private String mobile;
	private String qq;
	private String tel;
	private String email;
	private String companyIntroduce;
	private String website;
	private String weixin;

	public String toMessageGroupString(){
		StringBuilder sb = new StringBuilder();
		sb.append(companyIntroduce + "\r\n")
		.append(" \n联系人：").append(contact)
		.append(" \n电话：").append(tel)
		.append(" \n手机：").append(mobile)
		.append(" \n微信号：").append(weixin)
		.append(" \nQQ:").append(qq)
		.append(" \nEmail:").append(email)
		.append(" \n网址：").append(website);
		return sb.toString();
	}
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCompanyIntroduce() {
		return companyIntroduce;
	}

	public void setCompanyIntroduce(String companyIntroduce) {
		this.companyIntroduce = companyIntroduce;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
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
	

	
}
