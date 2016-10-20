package com.xgx.dw;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import java.io.Serializable;

/**
 * Entity mapped to table Pricing.
 */
public class PricingBean implements Serializable {

    private String id;
    private String pid;
    private String price;
    private String userId;
    private String userName;
    private String adminName;
    private String storeId;
    private String storeName;
    private String transformerId;
    private String transformerName;
    private String createTime;
    private String type;

    public PricingBean() {
    }

    public PricingBean(String id) {
        this.id = id;
    }

    public PricingBean(String id, String pid, String price, String userId, String userName, String adminName, String storeId, String storeName, String transformerId, String transformerName, String createTime, String type) {
        this.id = id;
        this.pid = pid;
        this.price = price;
        this.userId = userId;
        this.userName = userName;
        this.adminName = adminName;
        this.storeId = storeId;
        this.storeName = storeName;
        this.transformerId = transformerId;
        this.transformerName = transformerName;
        this.createTime = createTime;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getTransformerId() {
        return transformerId;
    }

    public void setTransformerId(String transformerId) {
        this.transformerId = transformerId;
    }

    public String getTransformerName() {
        return transformerName;
    }

    public void setTransformerName(String transformerName) {
        this.transformerName = transformerName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
