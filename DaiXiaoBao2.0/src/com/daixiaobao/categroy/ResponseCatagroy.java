package com.daixiaobao.categroy;

import com.daixiaobao.greenrobot.Group;

public class ResponseCatagroy {
	private int errorCode;
	private String message;
	private Group[] group;

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
