package com.daixiaobao.greenrobot;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table daixiaobao_attrb.
 */
public class Attrb {

    private Long id;
    /** Not-null value. */
    private String featureTypeId;
    private String featureTypeName;
    private String categorys_id;
    private Feature[] features;

    public Feature[] getFeatures() {
		return features;
	}

	public void setFeatures(Feature[] features) {
		this.features = features;
	}

	public Attrb() {
    }

    public Attrb(Long id) {
        this.id = id;
    }

    public Attrb(Long id, String featureTypeId, String featureTypeName, String categorys_id) {
        this.id = id;
        this.featureTypeId = featureTypeId;
        this.featureTypeName = featureTypeName;
        this.categorys_id = categorys_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getFeatureTypeId() {
        return featureTypeId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFeatureTypeId(String featureTypeId) {
        this.featureTypeId = featureTypeId;
    }

    public String getFeatureTypeName() {
        return featureTypeName;
    }

    public void setFeatureTypeName(String featureTypeName) {
        this.featureTypeName = featureTypeName;
    }

    public String getCategorys_id() {
        return categorys_id;
    }

    public void setCategorys_id(String categorys_id) {
        this.categorys_id = categorys_id;
    }

}
