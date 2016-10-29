package com.xgx.dw.bean;

import com.xgx.dw.PricingBean;
import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.StoreBean;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.UserBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/13 0013.
 */
public class UserAllInfo implements Serializable {
    private UserBean user;
    private StoreBean storeBean;
    private TransformerBean transformerBean;
    private int pricingSize;
    private PricingBean pricings;
    private SpotPricingBean spotBeans;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public StoreBean getStoreBean() {
        return storeBean;
    }

    public void setStoreBean(StoreBean storeBean) {
        this.storeBean = storeBean;
    }

    public TransformerBean getTransformerBean() {
        return transformerBean;
    }

    public void setTransformerBean(TransformerBean transformerBean) {
        this.transformerBean = transformerBean;
    }

    public int getPricingSize() {
        return pricingSize;
    }

    public void setPricingSize(int pricingSize) {
        this.pricingSize = pricingSize;
    }


    public PricingBean getPricings() {
        return pricings == null ? new PricingBean() : pricings;
    }

    public void setPricings(PricingBean pricings) {
        this.pricings = pricings;
    }

    public SpotPricingBean getSpotBeans() {
        return spotBeans == null ? new SpotPricingBean() : spotBeans;
    }

    public void setSpotBeans(SpotPricingBean spotBeans) {
        this.spotBeans = spotBeans;
    }

}
