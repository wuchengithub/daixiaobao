package com.daixiaobao.filter;

import java.io.Serializable;

public class SearchConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String categoryId = ""; 
    private String brandId = ""; 
    private String productName = ""; 
    private String productCode = "";
    private SearchAttribute[] attribute;
    private String beginTime = "";
	private String endTime = "";
	private String afterSalesId = "";
	
    public String getAfterSalesId() {
		return afterSalesId;
	}

	public void setAfterSalesId(String afterSalesId) {
		this.afterSalesId = afterSalesId;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public SearchAttribute[] getAttribute() {
		return attribute;
	}

	public void setAttribute(SearchAttribute[] attribute) {
		this.attribute = attribute;
	}

	public class SearchAttribute implements Serializable{
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String featureTypeId;
    	private String featureId;
		public String getFeatureTypeId() {
			return featureTypeId;
		}
		public void setFeatureTypeId(String featureTypeId) {
			this.featureTypeId = featureTypeId;
		}
		public String getFeatureId() {
			return featureId;
		}
		public void setFeatureId(String featureId) {
			this.featureId = featureId;
		}
    	
    }

	
}