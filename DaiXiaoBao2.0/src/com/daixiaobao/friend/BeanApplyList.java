package com.daixiaobao.friend;

import java.util.ArrayList;

class BeanApplyList{
	
	

	public BeanApplyList(String token, String deviceId) {
		super();
		this.token = token;
		this.deviceId = deviceId;
	}

	private String token;
	private String deviceId;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	private int errorCode;
	private String message;
	private ArrayList<Item> group;
	
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

	public ArrayList<Item> getGroup() {
		return group;
	}
	public void setGroup(ArrayList<Item> group) {
		this.group = group;
	}


	public class Item {
		private String applicantId;
		private String userLoginId;
		private String signature;
		private String leaveMessage;
		public String getApplicantId() {
			return applicantId;
		}
		public void setApplicantId(String applicantId) {
			this.applicantId = applicantId;
		}
		public String getUserLoginId() {
			return userLoginId;
		}
		public void setUserLoginId(String userLoginId) {
			this.userLoginId = userLoginId;
		}
		public String getSignature() {
			return signature;
		}
		public void setSignature(String signature) {
			this.signature = signature;
		}
		public String getLeaveMessage() {
			return leaveMessage;
		}
		public void setLeaveMessage(String leaveMessage) {
			this.leaveMessage = leaveMessage;
		}
		
	}
}
