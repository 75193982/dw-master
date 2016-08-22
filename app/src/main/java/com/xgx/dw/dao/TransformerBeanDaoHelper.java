package com.xgx.dw.dao;

import android.text.TextUtils;

import com.xgx.dw.StoreBean;
import com.xgx.dw.StoreBeanDao;
import com.xgx.dw.THDaoHelperInterface;
import com.xgx.dw.THDatabaseLoader;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.TransformerBeanDao;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class TransformerBeanDaoHelper implements THDaoHelperInterface {
    private static TransformerBeanDaoHelper instance;
    private TransformerBeanDao userBeanDao;

    private TransformerBeanDaoHelper() {
        try {
            userBeanDao = THDatabaseLoader.getDaoSession().getTransformerBeanDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TransformerBeanDaoHelper getInstance() {
        if (instance == null) {
            instance = new TransformerBeanDaoHelper();
        }

        return instance;
    }

    @Override
    public <T> void addData(T bean) {
        if (userBeanDao != null && bean != null) {
            userBeanDao.insertOrReplace((TransformerBean) bean);
        }
    }

    @Override
    public void deleteData(String id) {
        if (userBeanDao != null && !TextUtils.isEmpty(id)) {
            userBeanDao.deleteByKey(id);
        }
    }

    @Override
    public TransformerBean getDataById(String id) {
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

        QueryBuilder<TransformerBean> qb = userBeanDao.queryBuilder();
        qb.where(TransformerBeanDao.Properties.Id.eq(id));
        long count = qb.buildCount().count();
        return count > 0 ? true : false;
    }

    @Override
    public long getTotalCount() {
        if (userBeanDao == null) {
            return 0;
        }

        QueryBuilder<TransformerBean> qb = userBeanDao.queryBuilder();
        return qb.buildCount().count();
    }

    @Override
    public void deleteAll() {
        if (userBeanDao != null) {
            userBeanDao.deleteAll();
        }
    }

}
