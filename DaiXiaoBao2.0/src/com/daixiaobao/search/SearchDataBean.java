package com.daixiaobao.search;

public class SearchDataBean {

	private String token;
	private String uid;
	private String deviceId;
	private String itemId;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public SearchDataBean(String token, String uid, String deviceId,
			String itemId) {
		super();
		this.token = token;
		this.uid = uid;
		this.deviceId = deviceId;
		this.itemId = itemId;
	}
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public Group getData() {
		return data;
	}
	public void setData(Group data) {
		this.data = data;
	}


	private String message;
	private int errorCode;
	private Group data;
	public class Group {
		
		public Attrb[] getAttribute() {
			return attribute;
		}

		public void setAttribute(Attrb[] attribute) {
			this.attribute = attribute;
		}

		public Brand[] getBrands() {
			return brands;
		}

		public void setBrands(Brand[] brands) {
			this.brands = brands;
		}

		private Attrb[] attribute;
		private Brand[] brands;
		private AfterSales[] afterSales;
		
		public AfterSales[] getAfterSales() {
			return afterSales;
		}

		public void setAfterSales(AfterSales[] afterSales) {
			this.afterSales = afterSales;
		}

		public class AfterSales{
			private String name;
			private String afterSalesId;
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getAfterSalesId() {
				return afterSalesId;
			}
			public void setAfterSalesId(String afterSalesId) {
				this.afterSalesId = afterSalesId;
			}
			
		}
		public class Brand{
			private String name;
			private String brandId;
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getBrandId() {
				return brandId;
			}
			public void setBrandId(String brandId) {
				this.brandId = brandId;
			}
			
			
		}
		
		public class Attrb {
			private String featureTypeName;
			private String featureTypeId;
			private Feature[] features;
			
			public String getFeatureTypeName() {
				return featureTypeName;
			}

			public void setFeatureTypeName(String featureTypeName) {
				this.featureTypeName = featureTypeName;
			}

			public String getFeatureTypeId() {
				return featureTypeId;
			}

			public void setFeatureTypeId(String featureTypeId) {
				this.featureTypeId = featureTypeId;
			}

			public Feature[] getFeatures() {
				return features;
			}

			public void setFeatures(Feature[] features) {
				this.features = features;
			}

			public class Feature{
				private String featureId;
				private String featureName;
				public String getFeatureId() {
					return featureId;
				}
				public void setFeatureId(String featureId) {
					this.featureId = featureId;
				}
				public String getFeatureName() {
					return featureName;
				}
				public void setFeatureName(String featureName) {
					this.featureName = featureName;
				}
				
			}
		}
	}

}
