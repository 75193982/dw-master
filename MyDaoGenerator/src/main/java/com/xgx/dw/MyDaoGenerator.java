package com.xgx.dw;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;


public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.xgx.dw");
        initUserBean(schema);
        initTransformerBean(schema);
        initSpotPricingBean(schema);
        initStoreBean(schema);
        new DaoGenerator().generateAll(schema, args[0]);
    }


    private static void initUserBean(Schema schema) {
        Entity userBean = schema.addEntity("UserBean");
        userBean.setTableName("user");
        userBean.addStringProperty("userId").primaryKey();
        userBean.addStringProperty("userName");
        userBean.addStringProperty("password");
        userBean.addStringProperty("type");
        userBean.addStringProperty("storeId");
        userBean.addStringProperty("storeName");
        userBean.addStringProperty("isBuy");
        userBean.addStringProperty("isTest");
        userBean.addStringProperty("transformerId");
        userBean.addStringProperty("transformerName");
        userBean.addStringProperty("voltageRatio");
        userBean.addStringProperty("currentRatio");
        userBean.addStringProperty("meterNum");
        userBean.addStringProperty("price");
        userBean.addStringProperty("phone");
        userBean.addStringProperty("createTime");
        userBean.addStringProperty("remark");
    }


    private static void initTransformerBean(Schema schema) {
        Entity userBean = schema.addEntity("TransformerBean");
        userBean.setTableName("Transformer");
        userBean.addStringProperty("id").primaryKey();
        userBean.addStringProperty("name");
        userBean.addStringProperty("store_id");
        userBean.addStringProperty("store_name");
    }


    private static void initStoreBean(Schema schema) {
        Entity userBean = schema.addEntity("StoreBean");
        userBean.setTableName("Store");
        userBean.addStringProperty("id").primaryKey();
        userBean.addStringProperty("name");
        userBean.addStringProperty("address");
        userBean.addStringProperty("linkman");
        userBean.addStringProperty("contact_way");
    }


    private static void initSpotPricingBean(Schema schema) {
        Entity userBean = schema.addEntity("SpotPricingBean");
        userBean.setTableName("SpotPricing");
        userBean.addStringProperty("id").primaryKey();
        userBean.addStringProperty("name");
        userBean.addStringProperty("store_id");
        userBean.addStringProperty("storename");
        userBean.addStringProperty("type");
        userBean.addStringProperty("price_count");
        userBean.addStringProperty("pointed_price");
        userBean.addStringProperty("peek_price");
        userBean.addStringProperty("flat_price");
        userBean.addStringProperty("valley_price");
    }
}
