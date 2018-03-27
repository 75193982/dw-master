package com.xgx.dw.bean;

import com.xgx.dw.app.BaseApplication;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;


/**
 * Created by CWJ on 2017/3/20.
 */

public class UserDao {
    /**
     * 添加数据，如果有重复则覆盖
     */
    public static void insertFile(SysUser groupInfo) {
        BaseApplication.getDaoInstant().getSysUserDao().insertOrReplace(groupInfo);
    }

    /**
     * 删除数据
     */
    public static void deleteFile(SysUser groupInfo) {
        BaseApplication.getDaoInstant().getSysUserDao().delete(groupInfo);
    }

    /**
     * 更新数据
     */
    public static void updateFile(SysUser groupInfo) {
        BaseApplication.getDaoInstant().getSysUserDao().update(groupInfo);
    }


    /**
     * 查询全部数据
     */
    public static List<SysUser> queryAll() {
        return BaseApplication.getDaoInstant().getSysUserDao().loadAll();

    }

    /**
     * 删除全部数据
     */
    public static void deleteAll1() {
        BaseApplication.getDaoInstant().getSysUserDao().deleteAll();
    }

    public static boolean isContain(String id) {
        QueryBuilder<SysUser> qb = BaseApplication.getDaoInstant().getSysUserDao().queryBuilder();

        qb.where(SysUserDao.Properties.Id.eq(id));
        qb.buildCount().count();
        return qb.buildCount().count() > 0 ? true : false;
    }

    public static SysUser queryGroup(String username) {
        try {
            QueryBuilder<SysUser> qb = BaseApplication.getDaoInstant().getSysUserDao().queryBuilder();
            qb.where(SysUserDao.Properties.Account.eq(username));
            qb.buildCount().count();
            return qb.buildCount().count() > 0 ? qb.list().get(0) : null;
        } catch (Exception e) {
            return null;
        }

    }

}
