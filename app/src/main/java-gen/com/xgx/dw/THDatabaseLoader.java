package com.xgx.dw;

import com.xgx.dw.app.BaseApplication;

/**
 * Created by Administrator on 2016/8/13.
 */
public class THDatabaseLoader
{
    private static final String DATABASE_NAME = "dw-db";
    private static DaoSession daoSession;

    public static DaoSession getDaoSession()
    {
        return daoSession;
    }

    public static void init()
    {
        daoSession = new DaoMaster(new THDevOpenHelper(BaseApplication.getInstance(), "dw-db", null).getWritableDatabase()).newSession();
    }
}

