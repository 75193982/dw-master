package com.xgx.dw.bean;

import com.xgx.dw.app.BaseApplication;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by xgx on 2018/5/3 for dw
 */

public class PurchaseDaoHelper {
    public static Purchase queryById(int id) {
        try {
            QueryBuilder<Purchase> qb = BaseApplication.getDaoSession().getPurchaseDao().queryBuilder();

            qb.where(PurchaseDao.Properties.Id.eq(id));
            qb.buildCount().count();
            return qb.buildCount().count() > 0 ? qb.list().get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Purchase> queryByStatus(int status) {
        try {
            QueryBuilder<Purchase> qb = BaseApplication.getDaoSession().getPurchaseDao().queryBuilder();
            qb.where(PurchaseDao.Properties.Status.eq(status));
            return qb.list();
        } catch (Exception e) {
            return null;
        }
    }
}
