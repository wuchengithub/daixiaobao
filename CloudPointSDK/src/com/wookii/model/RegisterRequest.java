package com.wookii.model;

public class RegisterRequest {
	
	private String deviceId;
	private String registerUserName;
	private String registerPassword;
	private String securityCode;
	private String confirmPassword;
	private String mobile;
	private String invitationCode;
	
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getInvitationCode() {
		return invitationCode;
	}
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getRegisterUserName() {
		return registerUserName;
	}
	public void setRegisterUserName(String registerUserName) {
		this.registerUserName = registerUserName;
	}
	public String getRegisterPassword() {
		return registerPassword;
	}
	public void setRegisterPassword(String registerPassword) {
		this.registerPassword = registerPassword;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	public RegisterRequest(String deviceId, String registerUserName,
			String registerPassword, String securityCode,
			String confirmPassword, String mobile, String invitationCode) {
		super();
		this.deviceId = deviceId;
		this.registerUserName = registerUserName;
		this.registerPassword = registerPassword;
		this.securityCode = securityCode;
		this.confirmPassword = confirmPassword;
		this.mobile = mobile;
		this.invitationCode = invitationCode;
	}
	
	

}
