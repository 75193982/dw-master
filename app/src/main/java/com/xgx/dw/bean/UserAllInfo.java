package com.xgx.dw.bean;

import com.xgx.dw.PricingBean;
import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.StoreBean;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.UserBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/13 0013.
 */
public class UserAllInfo implements Serializable {
    private UserBean user;
    private StoreBean storeBean;
    private TransformerBean transformerBean;
    private List<PricingBean> pricings;
    private List<SpotPricingBean> spotBeans;

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

    public List<PricingBean> getPricings() {
        return pricings;
    }

    public void setPricings(List<PricingBean> pricings) {
        this.pricings = pricings;
    }

    public List<SpotPricingBean> getSpotBeans() {
        return spotBeans;
    }

    public void setSpotBeans(List<SpotPricingBean> spotBeans) {
        this.spotBeans = spotBeans;
    }
}
