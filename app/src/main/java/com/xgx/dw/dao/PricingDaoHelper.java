package com.xgx.dw.dao;

import android.text.TextUtils;

import com.xgx.dw.PricingBean;
import com.xgx.dw.PricingBeanDao;
import com.xgx.dw.SpotPricingBeanDao;
import com.xgx.dw.StoreBean;
import com.xgx.dw.THDaoHelperInterface;
import com.xgx.dw.THDatabaseLoader;
import com.xgx.dw.utils.MyStringUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class PricingDaoHelper implements THDaoHelperInterface {
    private static PricingDaoHelper instance;
    private PricingBeanDao userBeanDao;

    private PricingDaoHelper() {
        try {
            userBeanDao = THDatabaseLoader.getDaoSession().getPricingBeanDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PricingDaoHelper getInstance() {
        if (instance == null) {
            instance = new PricingDaoHelper();
        }

        return instance;
    }

    @Override
    public <T> void addData(T bean) {
        if (userBeanDao != null && bean != null) {
            userBeanDao.insertOrReplace((PricingBean) bean);
        }
    }

    @Override
    public void deleteData(String id) {
        if (userBeanDao != null && !TextUtils.isEmpty(id)) {
            userBeanDao.deleteByKey(id);
        }
    }

    @Override
    public PricingBean getDataById(String id) {
        if (userBeanDao != null && !TextUtils.isEmpty(id)) {
            return userBeanDao.load(id);
        }
        return null;
    }

    @Override
    public List getAllData() {
        if (userBeanDao != null) {
            return userBeanDao.loadAll();
        }
        return null;
    }

    @Override
    public boolean hasKey(String id) {
        if (userBeanDao == null || TextUtils.isEmpty(id)) {
            return false;
        }

        QueryBuilder<PricingBean> qb = userBeanDao.queryBuilder();
        qb.where(PricingBeanDao.Properties.Id.eq(id));
        long count = qb.buildCount().count();
        return count > 0 ? true : false;
    }

    @Override
    public long getTotalCount() {
        if (userBeanDao == null) {
            return 0;
        }

        QueryBuilder<PricingBean> qb = userBeanDao.queryBuilder();
        return qb.buildCount().count();
    }

    @Override
    public void deleteAll() {
        if (userBeanDao != null) {
            userBeanDao.deleteAll();
        }
    }

    public List<PricingBean> queryByUserId(String id) {
        try {
            QueryBuilder localQueryBuilder = this.userBeanDao.queryBuilder();
            localQueryBuilder.where(PricingBeanDao.Properties.UserPrimaryid.eq(id));
            localQueryBuilder.orderDesc(PricingBeanDao.Properties.CreateTime);
            return localQueryBuilder.list();
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    public List<PricingBean> queryByUserDeviceId(String id) {
        try {
            QueryBuilder localQueryBuilder = this.userBeanDao.queryBuilder();
            localQueryBuilder.where(PricingBeanDao.Properties.UserId.eq(id));
            localQueryBuilder.orderDesc(PricingBeanDao.Properties.CreateTime);
            return localQueryBuilder.list();
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    public List<PricingBean> queryByUserId(String userId, String ime, String id) {
        try {
            QueryBuilder localQueryBuilder = this.userBeanDao.queryBuilder();
            localQueryBuilder.where(PricingBeanDao.Properties.UserId.eq(userId), PricingBeanDao.Properties.Ime.eq(ime), PricingBeanDao.Properties.Id.eq(id));
            localQueryBuilder.orderDesc(PricingBeanDao.Properties.CreateTime);
            return localQueryBuilder.list();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
