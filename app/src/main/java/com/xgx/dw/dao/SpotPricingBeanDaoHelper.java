package com.xgx.dw.dao;

import android.text.TextUtils;

import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.SpotPricingBeanDao;
import com.xgx.dw.SpotPricingBeanDao.Properties;
import com.xgx.dw.StoreBean;
import com.xgx.dw.THDaoHelperInterface;
import com.xgx.dw.THDatabaseLoader;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

import java.util.List;

public class SpotPricingBeanDaoHelper implements THDaoHelperInterface {
    private static SpotPricingBeanDaoHelper instance;
    private SpotPricingBeanDao userBeanDao;

    private SpotPricingBeanDaoHelper() {
        try {
            userBeanDao = THDatabaseLoader.getDaoSession().getSpotPricingBeanDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SpotPricingBeanDaoHelper getInstance() {
        if (instance == null) {
            instance = new SpotPricingBeanDaoHelper();
        }

        return instance;
    }

    @Override
    public <T> void addData(T bean) {
        if (userBeanDao != null && bean != null) {
            userBeanDao.insertOrReplace((SpotPricingBean) bean);
        }
    }

    @Override
    public void deleteData(String id) {
        if (userBeanDao != null && !TextUtils.isEmpty(id)) {
            userBeanDao.deleteByKey(id);
        }
    }

    @Override
    public SpotPricingBean getDataById(String id) {
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

        QueryBuilder<SpotPricingBean> qb = userBeanDao.queryBuilder();
        qb.where(SpotPricingBeanDao.Properties.Id.eq(id));
        long count = qb.buildCount().count();
        return count > 0 ? true : false;
    }

    @Override
    public long getTotalCount() {
        if (userBeanDao == null) {
            return 0;
        }

        QueryBuilder<SpotPricingBean> qb = userBeanDao.queryBuilder();
        return qb.buildCount().count();
    }

    @Override
    public void deleteAll() {
        if (userBeanDao != null) {
            userBeanDao.deleteAll();
        }
    }

    public List<SpotPricingBean> testQueryBy(String paramString1, String paramString2) {
        QueryBuilder localQueryBuilder = this.userBeanDao.queryBuilder();
        localQueryBuilder.and(SpotPricingBeanDao.Properties.Name.eq(paramString1), Properties.Store_id.eq(paramString2), new WhereCondition[0]);
        return localQueryBuilder.list();
    }

    public List<SpotPricingBean> testQueryBy(String paramString2) {
        QueryBuilder localQueryBuilder = this.userBeanDao.queryBuilder();
        localQueryBuilder.where(Properties.Store_id.eq(paramString2));
        return localQueryBuilder.list();
    }
}
