package com.xgx.dw.dao;

import android.text.TextUtils;

import com.xgx.dw.StoreBean;
import com.xgx.dw.StoreBeanDao;
import com.xgx.dw.THDaoHelperInterface;
import com.xgx.dw.THDatabaseLoader;

import de.greenrobot.dao.query.QueryBuilder;

import java.util.List;

public class StoreBeanDaoHelper implements THDaoHelperInterface {
    private static StoreBeanDaoHelper instance;
    private StoreBeanDao userBeanDao;

    private StoreBeanDaoHelper() {
        try {
            userBeanDao = THDatabaseLoader.getDaoSession().getStoreBeanDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static StoreBeanDaoHelper getInstance() {
        if (instance == null) {
            instance = new StoreBeanDaoHelper();
        }

        return instance;
    }

    @Override
    public <T> void addData(T bean) {
        if (userBeanDao != null && bean != null) {
            userBeanDao.insertOrReplace((StoreBean) bean);
        }
    }

    @Override
    public void deleteData(String id) {
        if (userBeanDao != null && !TextUtils.isEmpty(id)) {
            userBeanDao.deleteByKey(id);
        }
    }

    @Override
    public StoreBean getDataById(String id) {
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

        QueryBuilder<StoreBean> qb = userBeanDao.queryBuilder();
        qb.where(StoreBeanDao.Properties.Id.eq(id));
        long count = qb.buildCount().count();
        return count > 0 ? true : false;
    }

    @Override
    public long getTotalCount() {
        if (userBeanDao == null) {
            return 0;
        }

        QueryBuilder<StoreBean> qb = userBeanDao.queryBuilder();
        return qb.buildCount().count();
    }

    @Override
    public void deleteAll() {
        if (userBeanDao != null) {
            userBeanDao.deleteAll();
        }
    }

}
