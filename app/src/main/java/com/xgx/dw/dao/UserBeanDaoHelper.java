package com.xgx.dw.dao;

import android.text.TextUtils;

import com.xgx.dw.StoreBean;
import com.xgx.dw.THDaoHelperInterface;
import com.xgx.dw.THDatabaseLoader;
import com.xgx.dw.UserBean;
import com.xgx.dw.UserBeanDao;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class UserBeanDaoHelper implements THDaoHelperInterface {
    private static UserBeanDaoHelper instance;
    private UserBeanDao userBeanDao;

    private UserBeanDaoHelper() {
        try {
            userBeanDao = THDatabaseLoader.getDaoSession().getUserBeanDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserBeanDaoHelper getInstance() {
        if (instance == null) {
            instance = new UserBeanDaoHelper();
        }

        return instance;
    }

    @Override
    public <T> void addData(T bean) {
        if (userBeanDao != null && bean != null) {
            userBeanDao.insertOrReplace((UserBean) bean);
        }
    }

    @Override
    public void deleteData(String id) {
        if (userBeanDao != null && !TextUtils.isEmpty(id)) {
            userBeanDao.deleteByKey(id);
        }
    }

    @Override
    public UserBean getDataById(String id) {
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

        QueryBuilder<UserBean> qb = userBeanDao.queryBuilder();
        qb.where(UserBeanDao.Properties.UserId.eq(id));
        long count = qb.buildCount().count();
        return count > 0 ? true : false;
    }

    @Override
    public long getTotalCount() {
        if (userBeanDao == null) {
            return 0;
        }

        QueryBuilder<UserBean> qb = userBeanDao.queryBuilder();
        return qb.buildCount().count();
    }

    @Override
    public void deleteAll() {
        if (userBeanDao != null) {
            userBeanDao.deleteAll();
        }
    }

    public List<UserBean> queryByStoreId(String storeid) {
        QueryBuilder localQueryBuilder = this.userBeanDao.queryBuilder();
        localQueryBuilder.where(UserBeanDao.Properties.StoreId.eq(storeid));
        return localQueryBuilder.list();
    }

    public List<UserBean> queryByTransFormId(String transFormId) {
        QueryBuilder localQueryBuilder = this.userBeanDao.queryBuilder();
        localQueryBuilder.where(UserBeanDao.Properties.TransformerId.eq(transFormId));
        return localQueryBuilder.list();
    }

    public UserBean queryByUser(String userId, String ime) {
        QueryBuilder localQueryBuilder = this.userBeanDao.queryBuilder();
        localQueryBuilder.where(UserBeanDao.Properties.UserId.eq(userId), UserBeanDao.Properties.Ime.eq(ime));
        if (localQueryBuilder.list() != null && localQueryBuilder.list().size() > 0) {
            return (UserBean) localQueryBuilder.list().get(0);
        } else {
            return null;
        }
    }

    public boolean queryByTransFormUserId(String userId, String ime) {
        QueryBuilder localQueryBuilder = this.userBeanDao.queryBuilder();
        localQueryBuilder.where(UserBeanDao.Properties.UserId.eq(userId), UserBeanDao.Properties.Ime.eq(ime));
        List<UserBean> list = localQueryBuilder.list();
        if (list != null && list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
