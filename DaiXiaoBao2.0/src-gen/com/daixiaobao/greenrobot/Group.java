package com.daixiaobao.greenrobot;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table daixiaobao_categorys.
 */
public class Group {

    private Long id;
    /** Not-null value. */
    private String code;
    private String name;
    private String image;

    public Group() {
    }

    public Group(Long id) {
        this.id = id;
    }

    public Group(Long id, String code, String name, String image) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getCode() {
        return code;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}