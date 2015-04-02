package com.daixiaobao.categroy;

import java.util.List;

import com.daixiaobao.greenrobot.Group;

public class ResponseCatagroy {
	private int errorCode;
	private String message;
	private List<Group> group;

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

	public List<Group> getGroup() {
		return group;
	}

	public void setGroup(List<Group> group) {
		this.group = group;
	}

	
}
