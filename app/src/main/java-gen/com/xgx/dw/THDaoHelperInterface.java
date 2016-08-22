package com.xgx.dw;

import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public abstract interface THDaoHelperInterface {
    public abstract <T> void addData(T paramT);

    public abstract void deleteAll();

    public abstract void deleteData(String paramString);

    public abstract List getAllData();

    public abstract <T> T getDataById(String paramString);

    public abstract long getTotalCount();

    public abstract boolean hasKey(String paramString);
}