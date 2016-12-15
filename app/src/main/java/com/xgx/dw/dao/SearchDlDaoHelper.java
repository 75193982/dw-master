package com.xgx.dw.dao;

import android.text.TextUtils;

import com.xgx.dw.PricingBean;
import com.xgx.dw.PricingBeanDao;
import com.xgx.dw.SearchDlLog;
import com.xgx.dw.SearchDlLogDao;
import com.xgx.dw.THDaoHelperInterface;
import com.xgx.dw.THDatabaseLoader;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class SearchDlDaoHelper implements THDaoHelperInterface {
    private static SearchDlDaoHelper instance;
    private SearchDlLogDao userBeanDao;

    private SearchDlDaoHelper() {
        try {
            userBeanDao = THDatabaseLoader.getDaoSession().getSearchDlLogDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SearchDlDaoHelper getInstance() {
        if (instance == null) {
            instance = new SearchDlDaoHelper();
        }

        return instance;
    }

    @Override
    public <T> void addData(T bean) {
        if (userBeanDao != null && bean != null) {
            userBeanDao.insertOrReplace((SearchDlLog) bean);
        }
    }

    @Override
    public void deleteData(String id) {
        if (userBeanDao != null && !TextUtils.isEmpty(id)) {
            userBeanDao.deleteByKey(id);
        }
    }

    @Override
    public SearchDlLog getDataById(String id) {
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

        QueryBuilder<SearchDlLog> qb = userBeanDao.queryBuilder();
        qb.where(SearchDlLogDao.Properties.Id.eq(id));
        long count = qb.buildCount().count();
        return count > 0 ? true : false;
    }

    @Override
    public long getTotalCount() {
        if (userBeanDao == null) {
            return 0;
        }

        QueryBuilder<SearchDlLog> qb = userBeanDao.queryBuilder();
        return qb.buildCount().count();
    }

    @Override
    public void deleteAll() {
        if (userBeanDao != null) {
            userBeanDao.deleteAll();
        }
    }

    public List<SearchDlLog> queryByUserId(String type, String userId) {
        QueryBuilder localQueryBuilder = this.userBeanDao.queryBuilder();
        localQueryBuilder.where(SearchDlLogDao.Properties.Type.eq(type), SearchDlLogDao.Properties.UserId.eq(userId));
        localQueryBuilder.orderDesc(SearchDlLogDao.Properties.CreateTime);

        return localQueryBuilder.list();
    }

}
