package com.daixiaobao.company;

public class ResponseCompany {
	private int errorCode;
	private String message;
	private Group[] group;

	public class Group {
		private String company;
		private String companyPicUrl;
		private String agentTotal;
		private String shareTotal;
		private String code;
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		public String getCompanyPicUrl() {
			return companyPicUrl;
		}
		public void setCompanyPicUrl(String companyPicUrl) {
			this.companyPicUrl = companyPicUrl;
		}
		public String getAgentTotal() {
			return agentTotal;
		}
		public void setAgentTotal(String agentTotal) {
			this.agentTotal = agentTotal;
		}
		public String getShareTotal() {
			return shareTotal;
		}
		public void setShareTotal(String shareTotal) {
			this.shareTotal = shareTotal;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		
	}

	public Group[] getGroup() {
		return group;
	}

	public void setGroup(Group[] group) {
		this.group = group;
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
