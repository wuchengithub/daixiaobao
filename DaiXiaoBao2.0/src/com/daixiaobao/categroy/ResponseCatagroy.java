package com.daixiaobao.categroy;

public class ResponseCatagroy {
	private int errorCode;
	private String message;
	private Group[] group;
	public class Group{
		private String code;
		private String name;
		private String image;
		
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
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
